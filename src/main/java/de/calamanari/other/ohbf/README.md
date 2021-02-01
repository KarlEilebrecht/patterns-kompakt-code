#### [Project Overview](../../../../../../../README.md)
----

# Generic One-Hashing Bloom Filter (OHBF)

## Introduction

I always found [Bloom filters](https://en.wikipedia.org/wiki/Bloom_filter) interesting. In the first place it is a memory-efficient way to tell for an unknown huge number of keys whether it is guaranteed that they are _not_ contained in a predefined set of keys or (with configurable false-positive rate) that they _might be_ contained in the set. Although powerful, the concept is simple enough to play with it. There are some handy implementations in Java available like [Guava](https://guava.dev/releases/21.0/api/docs/com/google/common/hash/BloomFilter.html), see also this [tutorial](http://llimllib.github.io/bloomfilter-tutorial/). While for most developers not being "everydayish", Bloom filters are often used in combination with hash maps, and many modern databases leverage them to improve performance (e.g. [Cassandra](https://stackoverflow.com/questions/39327427/what-is-role-of-bloom-filter-in-cassandra)). But this is only a small piece of the success story of Bloom filters and more specialized related data structures. For example, Microsoft's [BitFunnel](https://danluu.com/bitfunnel-sigir.pdf) backing the [Bing](https://www.bing.com) search engine is conceptually a successor of Bloom Filters. The only thing that feels uncomfortable is the need to select _k_ "good" independent hash-functions. 

After playing with cryptographic hashes in 2020 to create keys (see [MUHAI](../../../../../../test/java/de/calamanari/pk/muhai/README.md)) and being fascinated about the _randomness_ of these hashes, I wondered if it was possible to leverage the output of a single cryptographic hash to "simulate" multiple hash-functions required for a Bloom Filter. The point is: a cryptographic hash computation may be expensive, but iterating over the input _k_-times (for computing _k_ _cheap_ hash-functions) isn't for free either. Furthermore, selecting _k_ independent "good" hash-functions can be a headache, sometimes highly depending on the use-case and its data types. So, why not spending more effort for computing a single well-investigated cryptographic hash? No need to "guess" any hash functions anymore, and with a large _k_ most likely comparable speed.

Soon, I realized that I was obviously not the first one reasoning about this :smirk: - and found the conference paper _[One-Hashing Bloom Filter](https://www.researchgate.net/publication/284283336_One-Hashing_Bloom_Filter)_ published in 2015 by Jianyuan Lu, Tong Yang, Yi Wang, Huichen Dai, Linxiao Jin, Haoyu Song and Bin Liu.
The authors have successfully demonstrated that the _k_ independent hash-functions a bloom filter requires can be replaced by leveraging the output of a single cryptographic hash.
Therefore, they suggest a multi-stage approach, where the first stage is computing a single hash followed by the application to _k_ partitions of the filter (forming together _m* >= m_ bits). Their partition mechanism creates _k_ partitions to fit _m_ as good as possible, where the individual partition sizes are unique, prime and close to each other. This way they can "simulate _k_ hash-functions" using modulo(partition size _m<sub>i</sub>_) to find the bit to set in each partition for a particular hashed input. Finding a good partition setup with minimal waste _(m* - m)_ based on primes is of course not trivial. However, the authors have demonstrated that the waste is acceptable and the resulting bloom filter outperforms certain other techniques.

While I find the modulo-approach with prime-aligned partitions clever, I wondered if we were able to avoid that kind of complexity. The point is: We assume the hash bits to be random and not to contain any internal patterns (otherwise the cryptographic hash would be vulnerable). So, for uniform (random-like) distribution we should not need any additional sophisticated shuffling. The challenge should be finding an efficient mechanism to derive _k_ pseudo-hash-values to set bits in the k partitions, so that these partitions are going to fill up uniformly.

## Goals

* Implement a Bloom filter (fun :smirk:)
* Create a convenient implementation that lets me specify the number of elements I plan to insert _n_ or the number of bits I want to use _m_ and the false-positive rate I want to achieve. All other settings should be left to automatic estimation, because there are formulas.
* My Bloom filter shall use a single cryptographic hash.
* I want to avoid complicated partitioning and especially the modulo-stage.
* Performance is relevant but not a major concern. It is more important to create an implementation that is easy to use and to understand.



## Concept

My idea was to use hash-bits as-is. Assumption: any _b_ bits taken from the hash can be considered random, thus every integer number created of _b_ bits from left to right should be random in range _[ 0 .. 2^b )_. Thus, we can create _k_ partitions and derive _k_ random numbers from the hash. The _k_ numbers are the results from _k_ _virtual hash functions_ and can be used directly to set bits in the k partitions. Given these numbers are random we do not need any modulo, we just need enough hash bits to read for computing indexes. Modern hash functions like SHA-family provided by the JDK provide you with 512 bits per hash run out of the box, and the optimal _k_ is often not too high. So, a single hash run should provide us with the desired input to derive all the indexes for the _k_ partitions.

## Measuring success
Luckily, a Bloom filter based on cryptographic hashing is easy to test, because you don't need to care about the input's nature, it just has to be unique. Thus, for feeding the filter and testing you can just employ an upcounting sequence. The first _n_ element you test and insert, followed by an arbitrary number of subsequent elements you just test. 

If _t_ is the total number of operations (inserts and test), I set _t := n * 100_. The limit _100_ has been chosen after some experiments. 

:warning: I am not sure if this _"100 times is enough"-assumption_ can be trusted. My gut feeling is that more systematic testing is required.

### Metrics

* The measured false-positive rate can be compared to the configured one. The deviation should be small.
* There is a well-investigated formula by [Swamidass & Baldi (2007)](https://en.wikipedia.org/wiki/Bloom_filter#Approximating_the_number_of_items_in_a_Bloom_filter) for approximating the number of elements contained in a Bloom Filter based on the number of bits set to 1. This can be turned into a metric by comparing the number of successfully inserted elements _n'_ (after _n_ puts) to the approximated number of elements _n<sub>e</sub>_. These numbers should be close to each other. 


## Implementation

### Configuration

Independent from any specific Bloom filter implementation I took the [formulas](https://en.wikipedia.org/wiki/Bloom_filter) to implement the class [BloomFilterConfig](BloomFilterConfig.java). 

It has the following properties:
* Number of elements to be inserted into the filter _n_
* Number of bits to spend (size of the filter's bit vector) _m_
* Expected false-positive rate _epsilon_
* Number of hash-functions _k_

There are by intention only 3 ways to create a configuration:

1. Specify the number of elements you want to insert into the filter _n_ and the number of bits you want to spend _m_.
2. Specify the number of elements you want to insert _n_ and the false-positive-rate _epsilon_ you would like to achieve.
3. Specifiy the number of bits you want to spend _m_ and the false-positive-rate _epsilon_ you would like to achieve.

The remaining settings will be calculated. It makes experiments less complicated (the more dependent settings you configure manually the more likely you will create broken configurations that lead to unexpected results).

Thus, besides obviously impossible setting combinations, you don't have to worry about the consistency of the configuration.

### GenericOHBF
To concentrate on the Bloom filter and not on fancy optimized input-hashing, I decided to repurpose my implementation of [MuhaiGenerator](../../pk/muhai/MuhaiGenerator.java) to do the hashing. The class [HashGenerators](HashGenerators.java) auto-selects the hash algorithm based on the required number of bits. This is just supplementary stuff for the Bloom filter implementation. The decision to use the [AtomicFixedLengthBitVector](../../pk/util/AtomicFixedLengthBitVector.java) rather than a Java `BitSet` I made at the beginning to make the filter thread-safe. I agree, this was definitely a little playful. Next time, I would probably go with a `BitSet` (less coding and testing) and synchronize access if I needed concurrency at all.

In the first attempt to implement the OHBF I tried to create an optimal partition setup based on partition sizes alligned to _2^x_. This alignment was necessary because the random numbers to be read from the hash bits are always in a range _[ 0 .. 2^x )_, and modulo I wanted to avoid. If _m<sub>i</sub>_ is the size of a single partition, then the ultimate goal is to minimize _m* = sum<sub>i=1..k</sub>(m<sub>i</sub>) >= m_. I wrote a partition optimizer that put together bigger and smaller partitions and nicely minimized the waste. While some results really impressed me, in other cases the measured false-positive-rate _epsilon_ completely derailed. To be honest, I should have known that before, because section _C_ and _D_ in the above mentioned conference paper explain that and why partition sizes _must_ be as close as possible to each other. Less formal, I call it _premature decrease of k_, if the partition layout contains larger and smaller (maybe tiny) partitions. The problem is that each insert into the bloom filter causes a write (one bit) to _every_ partition. Consequently, a small partition will be _full_ (all bits set to 1) sooner than a large partition. Let's say some partition has a size of 2 bits. Latest after a handful of inserts most likely both bits will be set to 1. This means, for every subsequent insert this partition is meaningless. Effectively, the number of hash-functions _k_ has been virtually decreased, with major impact on the further behavior of the filter. Thus, for any reliable (predictable) partitioned Bloom filter it is essential to create partitions of (almost) equal size.

For the time being I decided to drop the optimizer and to ignore the waste issue. The new partitioner simply selected _m<sub>a</sub> = 2^x <= m/k_ and increased partitions _(x+1)_ partition by partition until _m* = sum<sub>i=1..k</sub>(m<sub>i</sub>) >= m_. The maximum waste this method causes is _m/k - 1_, potentially high and maybe unacceptable, but still ok for my experiment.

The results were actually not too bad. The false-positive rate was below the configured/predicted rate in most of my tests. Furthermore, the common estimation for Bloom filters, the [prediction of the number of elements in the filter](https://en.wikipedia.org/wiki/Bloom_filter#Approximating_the_number_of_items_in_a_Bloom_filter) based on the count of 1s worked quite well. Only the high waste deviation (sometimes low, in other cases close to _m/k_) made the solution uncomely. A main motivation to use bloom filters is memory efficiency, so, if the configured _n_ is large and the estimated _k_ is low, the difference between expected and real memory consumption could be too high.

I decided to go back to the drawing-board. Obviously, the goal must be choosing a single fixed partition size independent from _2^x_. Best would be to use _m<sub>a</sub> = Math.ceil(m/k)_. If so, the maximum waste would be _k-1_ and thus negligible. But this idea leads to a new problem: Each value _h<sub>i</sub>_ I read from the hash bits (result of one of the _k_ virtual hash-functions) is by definition in a range _[ 0 .. 2^x )_, while the partition bit index can now be any natural number in range _[ 0 .. m<sub>a</sub> )_. Assuming _h<sub>i</sub>_ to be _random enough_, I only need to efficiently distribute the _h<sub>i</sub> .. h<sub>k</sub>_ in range _[ 0 .. 2^x )_ to the partition's bits _[ 0 .. m<sub>a</sub>-1 )_ **in a fair manner**. To avoid modulo for each of the _k_ partition indexes I convert 4 bytes of the hash into a an unsigned 32-bit integer, convert it into a long (64 bits), multiply it by the partition size followed by a right-shift by 32 bits. You can find a [nice article](https://lemire.me/blog/2016/06/27/a-fast-alternative-to-the-modulo-reduction/) written by Prof. Daniel Lemire about this technique. While this is a couple of times faster than modulo and easy to implement in Java, the need to consume 32 bits per hash worried me (it requires _k * 32_ hash bits). Thus, I decided to implement what I call _shingled conversion_. While reading the bits from the hash I move the pointer only by 2 bytes (16 bits) instead of 4, hoping that the new dependency between _h<sub>i</sub>_ and _h<sub>i-1</sub>_ would not kill the uniform distribution of the _k_ hashes _h<sub>0</sub> .. h<sub>k-1</sub>_. Instead of _k * 32_ hash bits this technique only requires _(k+1) * 16_ hash bits. So, with _k < 32_ a single SHA-512 hash meets the requirements, above [HashGenerators](HashGenerators.java) uses chained salted hashes relying on the [avalanche effect](https://en.wikipedia.org/wiki/Avalanche_effect). This of course has a negative impact on performance. I will ignore this for the moment. This would be subject to optimization.


## Results

In general for small _n_ I saw fluctuation regarding the false-positive rate, but I blame the low numbers here. I decided to start above _n=10000_ and quite stable numbers. In my multi-setup tests the false-positive rate _epsilon_ did not deviate more than +/- 10% from the configured one, better/worse around 50/50. The estimation of elements in the filter never deviated much more than 3.015% from the counted number of elements.

I did not do any performance tests. I am not even sure if this would make sense, because for simplification and convenience reasons this implementation uses data structures and instructions which are not performance-optimized (besides avoiding modulo). However, the tests involve hundreds of million times the Bloom filter gets accessed and still run within an acceptable time. Taking into account that the implementation is thread-safe (overhead!) and performance was not my main concern, this is not bad.

### Conclusion

I could demonstrate that the idea works and leads to a convenient, configurable and reliable bloom filter with a **maximum waste of _k - 1 + 63_ bits** (the 63 come from the 64-bit-alignment of the bit vector based on type long). It is easy to use and does not require the user to do any complicated "guessing". 

Obviously, the implementation could be refined and improved. As a first step I recommend more systematic testing to better explain under which circumstances _epsilon_ derails, and what lets the estimation of elements in the filter deviate for different setups between practically zero and around 3%. The second thing to investigate would be making the best use of the hash bits. The "shingled conversion" was a best guess solution, but maybe there is more to achieve. Also, it would be interesting if there was any more efficient approach than _hash chaining_ to generate the required hash bits for _k > 31_.
