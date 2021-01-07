#### [Project Overview](../../../../../../../README.md)
----

# The collider package

## Overview

[KeyCollisionProcessor](KeyCollisionProcessor.java) and its supplementary classes have been created to support (and encourage) collision behavior analysis for MUHAI.

The processor can analyse billions of generated keys to find collisions and report a few statistics.

## Collision detection and collection

Let's define a collision as a key that occurred multiple times in a sequence of **n** generated keys in a keyspace of **m** keys.

The questions to be answered are:

1. How many collisions occurred in total?
2. How many keys were involved?
3. How many times do keys occur?
4. How many collisions occurred after *k* generated keys and what would be expected?

Finding collisions in a list of keys is a _holistic_ operation, because you need all keys. Even the last created key could collide with any created before. Thus we must collect all keys with some payload in the first place.

For a small number of keys we can do everything in memory. A HashMap that maps keys to payload (e.g. positions we found the key) serves as the collection data structure. For each generated key we either create a new entry in the map or update the payload (add position) if the key was present before. In a second step we iterate over the map entries and discard each key that only has a single occurrence. As a result we know all collisions (question 1), the involved keys (question 2) as well as the number of collisions per each key. The latter information can be taken to answer question 3 and 4.

With a few lines of code we should be done.

Unfortunately, this solution won't scale. A couple of 100,000 keys may work out of the box, with some tricks (e.g. using efficient primitive maps like [ALM Works](https://bitbucket.org/almworks/integers/src/master/)) we can collect a few millions. But creating and storing billions of java objects in memory is not a good idea.

So we need anything else ...

### A sequential approach

The simplicity of the in-memory-solution above results from the random access capability. 
Alternatively, we can process elements sequentially and make use of the power of sorting.

This design allows us to store most of the data in a file system and only a rather small portion at a time in memory.

### Parallel processing?

A relatively simple candidate for parallel processing is the key-generation. If n parallel key-producers create keys, we can let them create separate chunks and give every instance its own start position, so that they create a set of disjoint sets, where the superset contains the total number of positions.

As mentioned before, collision detection is a _holistic_ operation. We could of course introduce an intermediate step that detects collisions in a chunk but preserves the singleton keys. Further steps would then merge these intermediate chunks. Nice, but at the end the solution would be most likely _slower_ then before, because the expected number of collisions compared to the entirety of keys is typically small. So, we would just carry most of the singletons through this additional step. 

Collision insights and statistic computation would be again a good candidate for parallel exection. Computing counters and filling slots for a histogram can be done for several chunks independently followed by a merge.

### Implementation

This scenario is an ideal candidate for leveraging [Apache Spark](https://spark.apache.org/docs/0.9.1/scala-programming-guide.html). The data is potentially large (several billions of records) and operations like sorting and writing to chunks (see [RDD](https://spark.apache.org/docs/0.9.1/scala-programming-guide.html#resilient-distributed-datasets-rdds)) as well as the work distribution should be ideal.

However, the code examples for the book are all written in Java. Spark jobs should be implemented in Scala (believe me). Mixing Scala and Java in the same project ist not ideal, also the _dependency trail_ (large number of libraries) of Spark is significant.

Long story short, I decided to implement the sequential approach in Java as compromise and took the chance to show the patterns VALUE OBJECT, ITERATOR, DECORATOR, POLICY and FACTORY METHOD playing together in a further scenario.


