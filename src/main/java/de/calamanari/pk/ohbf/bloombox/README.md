#### [Project Overview](../../../../../../../../README.md)
----

# The BloomBox

_A probabilistic "data-free" counting engine._&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![Test](../../../../../../../../doc/patterns/images/bloombox.svg)

## Introduction

Data files with a couple of thousand columns are a challenge to most database systems. There are of course specialized databases that can deal with this problem, but especially if you
just need a way to quickly get some insights about the contained data it can be clumsy or expensive to prepare the database for your queries.

After finishing my experiments on [Generic Bloom Filters](../README.md) I thought it might be possible to leverage this technology for faster counting. Ok, there is a risk of false-positives, but it is small and it should not matter if we are just interested in some overview numbers (maybe percentages) whether the one or the other wrong row gets counted. So, the idea was born to represent each row with a bloom filter resp. its vector and built a _counting engine_.

An interesting aspect of this approach is that it would be "data-free". After feeding has completed, a BloomBox neither knows any source data nor any field meta data. The BloomBox can return result counts based on arbitrary queries, but it cannot tell you what to query. This makes the technique interesting whenever privacy concerns are a problem. A BloomBox cannot be used to reconstruct the rows it originally has been fed with!

## Concept

* For each row of the input data create a bloom filter.
* Feed the row's filter with the key-value pairs (create a hash-key ([MUHAI](../../../../../../../test/java/de/calamanari/pk/muhai/README.md)) for each pair).
* Consequently, the row store becomes a large number of bit vectors, one per row.
* For any query create a query bloom filter with the filter conditions.
* Scan the store and count any row bloom filter that matches (AND) the query bloom filter.

## Implementation

![Test](../../../../../../../../doc/patterns/images/BloomBox-classes.svg)

Constructing a BloomBox as described above is quite simple as it is just a large array of long values for representing the bit vectors. The main challenge was providing a convenient and performant way to run queries.

The diagram above shows the main levels of abstraction. The data store and its feeding have been separated first. Today, there is just an in-memory data store and (for some experiments) a _slow_ file-based implementation. Feeding happens single-threaded. However, you can easily provide custom data stores or feeding mechanisms. Besides support for feeding, the store just needs to _dispatch_ the query ([QueryDelegate](QueryDelegate.java)) to the rows, that's it. Results could be summed-up if you plan to distribute the work.

To simplify integration, the interface of the [BloomBoxQueryRunner](BloomBoxQueryRunner.java), which parses the query and coordinates the execution is very simple. You pass-in a [QueryBundle](QueryBundle.java) and get back a [QueryBundleResult](QueryBundleResult.java). All counts, warnings and error messages are contained in this result. I decided to use query _bundles_ because the scan is the most expensive part. By allowing an arbitrary number of queries to be executed during a single scan we get a performance advantage compared to many queries, each running a separate scan.

Not depicted above is the [ANTLR](https://www.antlr.org/)-based transformation of the textual queries into the binary [BbqExpression](bbq/BbqExpression.java) tree. Besides parsing, some basic validations and [optimizations](bbq/IntermediateExpressionOptimizer.java) lead to a unique representation for fast processing of the query bundle.

Still experimental is the support for upscaling. This is a useful feature if you only want to put a sufficient (and considerably smaller) sample into the box but report numbers extrapolated to the real dataset (called _target population_). While linear upscaling based on a representative sample is trivial (apply a fixed multiplier), it gets tricky if you want to correct a bias in the target population. I quickly realized that this needs more research, so I introducted the [UpScaler](UpScaler.java) interface as a further abstraction. The [DefaultUpScaler](DefaultUpScaler.java) is a first attempt. It uses supplementary queries in addition to the current user query to improve upscaling results. However, you can implement your own upscaler.

### The Finite Probability Overdrive (FBO)

Upscaling can get quite complicated, and I was thinking about alternative ways to deal with the principle challenge that DPAVs (**d**ata **p**oint **a**ttribute **v**alue, e.g. `color=blue`) may have assigned probabilities.

This idea is kind of ambitious because for many rows with many columns it needs an enormous amount of space to store a probability in range `[0.0 .. 1.0]` as a double (64 bits) or even as a float value (32 bits). However, I decided to implement the [PbInMemoryDataStore](PbInMemoryDataStore.java) that does exactly this in addition to the bloom filter vector. In conjunction with the [PbDataStoreFeeder](PbDataStoreFeeder.java) you can feed a BloomBox with probabilities and use the same query syntax as for a regular BloomBox.

To save space, I reduced the precision from floating point to fixed-point precision with 8 decimals, and I compress the probability vector.

Counting works different than for a normal BloomBox. The probabilities get computed to be summed-up. Interestingly, the precision profits from the FBO because due to the lookup per DPAV (see [PbDpavProbabilityManager](PbDpavProbabilityManager.java) false-positives get practically eliminated.

Besides the still very high memory consumption there is another caveat related to this concept: DPAV multi-references. If a query references the same DPAV (and thus its probability value) twice or more often, the easy way of computation becomes wrong.

The computation is entirely based on two simple rules, which can be applied recursively:
`P(A AND B) := P(A) * P(B)` 
`P(A OR B) := 1 - ( (1-P(A)) * (1-P(B)) )` 

So, for `color=blue~0.75 AND motor=big~0.5`, we get a total probability of `0.75 * 0.5 = 0.375`

You can sum-up these probabilities taken from all rows of your sample, turn the sum into an integer, and you get a precise prediction of the count.

Unfortunately, this does not hold true, if the same DPAV occurs multiple times.

Here are a few examples:

`(1) P(A AND A) != P(A) * P(A)` :x:
`(2) P( (A AND B) OR (A AND C) ) != 1 - ((1-(P(A) * P(B))) * (1-(P(A) * P(C))))` :x:
`(3) P( (A AND B) OR (NOT(A) AND B) ) != 1 - ((1-(P(A) * P(B))) * (1-((1-P(A)) * P(B))))` :x:
`(4) P( (A AND B) OR (NOT(A) AND C) ) != 1 - ((1-(P(A) * P(B))) * (1-((1-P(A)) * P(C))))` :x:

Many of these issues (like (1), (2) and (3)) you can avoid with predicate logic optimization, so that each DPAV only appears once:

`(1) P(A AND A) = P(A)` :white_check_mark:
`(2) P( (A AND B) OR (A AND C) ) = P( A AND (B OR C) ) = P(A) * (1-((1-P(B)) * (1-P(C))))` :white_check_mark:
`(3) P( (A AND B) OR (NOT(A) AND B) ) = P(B)` :white_check_mark:

See also [De Morgan's Law](https://en.wikipedia.org/wiki/De_Morgan%27s_laws).

But in cases like (4) you are lost. The computed probability for the entire expression will be wrong, because some conditions are *dependent*.

You could now do fancy things like estimating (and correcting such effects) leveraging a [covariance matrix](https://en.wikipedia.org/wiki/Covariance_matrix), but this is complex and insanely computation/memory intensive.

Instead of trying to solve this issue, I decided to optimize each expression as far as possible (see [QuantumOptimizer](./bbq/QuantumOptimizer.java)) and detect cases where this does not help. Then I raise a warning. Additionally, to reduce the impact, I replace the probability of a DPAV being referenced multiple times by its square-root. Often, this considerably reduces the error (deviation from the correct value).

**Conclusion:** The [PbInMemoryDataStore](PbInMemoryDataStore.java) is a way to deal with DPAVs with attached probabilities. It can deliver insights with less effort and much quicker than a solution backed by a database.

### Random Thresholding as an alternative to FBO

Sometimes the probability overdrive approach is not an option. The high memory consumption might be an issue or its overall performance. Especially, queries with many matches lead to long-running execution because the probability vector has to be uncompressed and evaluated for a high number of rows.

However, the bigger issue comes with *natural dependencies* among attributes. E.g. if you have one attribute `vendor` and another one `model`, of course there is a natural dependency that may have a strange impact on your results. This can create massive headaches.

If you have enough time, problems like this can be addressed query by query with a dependency analysis on the attributes which are referenced in the query to compute estimators for correcting the result. However, for a tool like the BloomBox, made for quickly delivering insights, this is not acceptable - not to say infeasible.

The trick is not avoiding the unavoidable, but replacing an unpredictable error with a kind of error you can get under control or at least explain to the users.

Therefore, the [PbThresholdBinaryFeeder](PbThresholdBinaryFeeder.java) feeds a regular BloomBox (no attached probabilities) based on probabilities.

*Huh?*

To put it simple: We throw dices. For every incoming DPAV with attached probability `P`, the feeder creates a random number `R` in range `[0.0 .. 1.0]` and uses `P` as a threshold to decide (`R <= P`) whether or not to put this DPAV into the row's bloom filter. 

The resulting BloomBox is fast and light-weight, and most important: It will provide the user with acceptable results *on average* instead of returning super-precise results in some cases while occasionally being completely wrong.

At the end it depends on the use case which approach works best.

## The query language BBQ ("Barbecue")

I needed an easy-to-use query language and I wanted to make the best out of the query bundle concept. The result is a dual-grammar query language with optional dependencies among queries.

* Basic Queries cover the typical field matches. Supported are `=`, `!=`, `IN(...)`, `NOT IN(...)`, `AND` and `OR` plus parenthesis to group expressions ([ANTLR Grammar](../../../../../../../../src/main/antlr4/de/calamanari/pk/ohbf/bloombox/bbq/Bbq.g4)).
* Post Queries allow the user to combine queries using `INTERSECT`, `UNION` and `MINUS` ([ANTLR Grammar](../../../../../../../../src/main/antlr4/de/calamanari/pk/ohbf/bloombox/bbq/PostBbq.g4)).
* Every query can have sub-queries (single level). This is useful for the common case that you want to know the numbers split by another criterion (e.g. "color").
* In BBQ you only need to put a name or value in quotes when necessary (e.g. a space character contained). For simplicity, both, double and single quotes are supported.
* If you need to escape a character inside quotes then use the backslash. The backslash itself can be escaped by doubling it.

A clear separation of the two grammars made the implementation much simpler and hopefully will lead to less usage problems. 

### Basic query examples

```
vendor=Audi and model=A3

(color=red or color="ice blue") and (electro=yes)

color NOT IN (red, blue, black)

vendor=Ford and model=Mustang

type!=sedan

```

### Post query examples

```

${query1} INTERSECT ${query2}

${query1} UNION ${query2}

(${query1} UNION ${query2}) MINUS ${query3}

```


### Errors and Warnings

To keep the interface easy and clean I decided to include error and warning codes in the messages returned by the engine. This way a client may either display the messages as is or pick the codes and display custom messages.

All codes are defined in [BbxMessage](BbxMessage.java).

## Storage format

The core format is just an array of long-values. Whenever written to disk these longs get [big-endian](https://en.wikipedia.org/wiki/Endianness)-encoded into 8 bytes per long. This raw format (just the bytes, no meta-information) is called BBS-format.

The [BloomBox](BloomBox.java)'s storage format puts some meta data headers in front of the store and BBS-section. This format is called BBX. Only a BBX-file can be restored to a valid BloomBox. If you want to inspect the metadata of a BBX-file on UNIX, write `head -n 1 <my-bloom-box-file>.bbx`. It will show you the single-line json that describes the box. The box header is followed by the data store header, usually followed by the BBS-section.
This way, any BloomBox using the [FileDataStore](FileDataStore.java) can run on the BBX-file without having to copy the BBS-store before start.

See also [BloomBoxHeader](BloomBoxHeader.java) and [DataStoreHeader](DataStoreHeader.java).

## Known Limitations

* No support for quantitative data.
 * You can mitigate this restriction by _binning_ (create clases like 0-99, 100-199, 200-299, etc.)
* No fuzzy queries, uppercase/lowercase, spelling etc.
 * "Aliased feeding" may help, use a generic representation for multiple variations of an input value
* The size of the box still grows linearly with the number of rows.
 * You may feed the box with a representative sample and perform upscaling.
 


## The Bird Strikes Example

For making your own experiments with the BloomBox I have prepared some data. 

I originally obtained this record set from [Wisdom Axis](https://www.wisdomaxis.com/technology/software/data/for-reports/bird-strikes-data-for-reports.php) who provided the sample for free. 

This data is about aircraft incidents. I have chosen this example because we have a certain data volume (99,404 rows, 17 columns) that can still be handled by any ordinary RDBMS like MySQL/MariaDB, so that we can compare results.

### Preparation

* Unzip [birdstrikes.csv.zip](../../../../../../../../src/test/resources/birdstrikes.csv.zip) to some folder of your choice.
* Install a MySQL or MariaDB database.
* Create a table   

```
CREATE TABLE BIRD_STRIKES (
    id INT NOT NULL AUTO_INCREMENT,
	aircraft_type VARCHAR(255) NULL,
	airport VARCHAR(255) NULL,
	altitude_bin VARCHAR(255) NULL,
	aircraft_model VARCHAR(255) NULL,
	num_of_strikes VARCHAR(255) NULL,
	impact_to_flight VARCHAR(255) NULL,
	effect VARCHAR(255) NULL,
	num_of_engines INT NULL,
	airline VARCHAR(255) NULL,
	state VARCHAR(255) NULL,
	phase_of_flight VARCHAR(255) NULL,
	special_conditions VARCHAR(255) NULL,
	bird_size VARCHAR(255) NULL,
	sky_conditions VARCHAR(255) NULL,
	species VARCHAR(255) NULL,
	time_of_day VARCHAR(255) NULL,
	prewarned VARCHAR(1) NULL,
    PRIMARY KEY (id)
);
``` 
* Import the CSV-data

```
LOAD DATA INFILE '/mytemp/birdstrikes.csv' 
INTO TABLE BIRD_STRIKES
FIELDS TERMINATED BY ';' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(aircraft_type, airport, altitude_bin, aircraft_model, num_of_strikes, impact_to_flight, effect, @vnum_of_engines, airline, state, phase_of_flight, special_conditions, bird_size, sky_conditions, species, time_of_day, prewarned)
SET num_of_engines = NULLIF(@vnum_of_engines,'')
; 
```
* This is your stable basis for deterministic counting and comparison.
* Now open [BloomBoxTest.java](../../../../../../../test/java/de/calamanari/pk/ohbf/bloombox/BloomBoxTest.java)
* Adjust (file names) and run the test case `testCreateBirdStrikesBloomBox` which will create a light-weight BloomBox (5.5MB) that reflects the data you have just imported into your local database.
* This test data is not impressive, yet (just 17 columns). Please adjust and run the case `testCreateEnrichedBirdStrikesBloomBox`. It will use the same input but add 10,000 extra columns (see description in the test case). The new box size is around 2.9 GB, it reflects a table with 99,404 rows, each 10017 columns, too big for most DBs. Here lies the strength of the BloomBox.


### Querying the BloomBox

* Adjust the test case `testBloomBoxDemoUI` to run the little demo application (see screenshot  below).
* Please first use the small box (5.5MB). 
* Now you can run the same queries against your local database and compare the results to what you get from the BloomBox.
* The results should match very well, because of the settings provided for the false-positive rate epsilon.
* But you can also show that it is a probabilistic store. Enter this query: `wrong: i=9`. You would expect `0` but you will see `1` for the small box!
* You can now make experiments with an increased epsilon. Then query results will deviate more and more from the deterministic counts returned by your database.
* Finally, run the demo again with the large box. You should see that the queries based on the first 17 columns still match the database results. Then you can play with the 10,000 special columns. E.g. if you add an AND-condition `occ_9940=1` (true for 10% of all rows) then your query results should approx. go down to 10% of the original counts.
* **Have fun!** 

![Test](../../../../../../../../doc/patterns/images/BloomBoxDemoUI.png)