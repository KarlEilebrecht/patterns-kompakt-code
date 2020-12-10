#### [Project Overview](../../../../../../../README.md)
----

# Master-Slave

## Scenario

In this example we want to test (long) character sequences for being a [palindrome](https://en.wikipedia.org/wiki/Palindrome) or not.
The process of testing large sequences shall be sped up.

## Choice of Pattern

In this scenario we want to apply the **Master-Slave Pattern** to _meet the performance, fault-tolerance, or accuracy requirements of the component via a 'divide and conquer' strategy. Split its services into independent subtasks that can be executed in parallel, and combine the partial results returned by these subtasks to provide the service's final result_ (POSA). 

![Test](../../../../../../../doc/patterns/images/master_slave_dn.png)

The palindrome test checks if the sequence is exactly the same when being read from the start to the end or vice-versa. This makes it quite easy to parallelize the test because we can cut the total sequence in subsequences, each consisting of some characters from the begin plus the same number of characters from the end of the source sequence. These _partitions_ all must be palindromes. If any of the partitions is not a palindrome, the source sequence cannot be a palindrome. 

![Test](../../../../../../../doc/patterns/images/master_slave_cx.png)

The _PalindromeCheckMaster_ (virtually) cuts the input sequence into subsequences, each of these partitions becomes by a _PalindromeCheckSlaveTask_. A configurable number of threads (the slaves) execute the slave tasks asynchronously.

![Test](../../../../../../../doc/patterns/images/master_slave_dx.png)

The _PalindromeCheckMaster_ derives the total test result (input sequence is a palindrome or not) from the partial results reported by the slaves.

An important aspect is the ability of the master to stop processing the slave tasks as soon as one slave reported that a subsequence was not a palindrome.


## Try it out!

Open [MasterSlaveTest.java](MasterSlaveTest.java) to start playing with this pattern. By setting the log-level for this pattern to DEBUG in [logback.xml](../../../../../../../src/main/resources/logback.xml) you can watch the pattern working step by step. 

To process large palindrome files I use here the [IndexedTextFileAcessor](../../../../../../main/java/de/calamanari/pk/util/itfa/IndexedTextFileAccessor.java) to index the input file not to entirely load it into memory. This utility internally also implements the Master-Slave pattern for indexing with multiple threads.

## Remarks
* The name "Master-Slave" has recently been a subject of controversy due to its association with slavery. Some organizations have decided to replace it with anything else, e.g. "leader/follower".

## References

* (POSA) Buschmann, F., Henney, K., Schmidt, D.C.: Pattern-Oriented Software Architecture: A Pattern Language for Distributed Computing. Wiley, Hoboken (NJ, USA) (2007)
