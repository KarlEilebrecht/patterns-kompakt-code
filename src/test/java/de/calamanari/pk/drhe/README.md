#### [Project Overview](../../../../../../../README.md)
----

# Distribution, Random, Hash, Encryption

## Introduction

Shuffling the numbers to create a pseudo-random distribution is usually a holistic operation. You take the list with all the elements and apply any kind of shuffling method.

However, what do you do if the range is too large and thus contains way too many elements to hold them in memory?

Wouldn't it be great if there was a function `f(n)` that for any number in a given range R returns a number in R where all the mappings are unique and the output sequence for `n=1,2,3,4...` looks pseudo-random?

The experiments below provide an investigation of the above random distribution problem.

We will then use the created distribution function to implement a random number generator, a hash function and finally our own symmetric encryption method.

### A Word of Caution

This is a pure fun project to demonstrate how certain algorithms may be constructed, their interrelation and and how distribution can be judged.

:warning: **You should not implement your own random number generators, hash functions or encryption methods for use in production!**

There are plenty of well-tested and investigated implementations available. Relying on robust libraries will save you time and headaches. :wink:

## Goals

.

## Idea

.

## Preparation


### Concept of Result Assessment

Before we start we should have a solid foundation, some tests and tools to judge the outcome: the randomness of the distribution created by our algorithm.

#### What is a pseudo-random distribution?

Unfortunately, there is no rule or algorithm to simply decide whether a distribution is random or not.

Until the 1980s there was not even a proper foundation for randomness!

Here are two prominent quotes that show how shady the field of pseudo-randomness was (and is):

> *"Anyone who attempts to generate random numbers by deterministic means is, of course, living in a state of sin."*

[John von Neumann](https://en.wikipedia.org/wiki/John_von_Neumann)

> *"A random sequence is a vague notion embodying the idea of a sequence in which each term is unpredictable to the uninitiated and whose digits pass a certain number of tests, traditional with statisticians and depending on the uses to which the sequence is to be put."*

[Derrick Henry Lehmer], 1941

Finally, in 1982, [Andrew Yao](https://en.wikipedia.org/wiki/Andrew_Yao) developed the concept of **Computational Indistinguishability**. In 2000 he recieved the Turing Award for his work on pseudo-randomness.

Drastically simplified: A sequence is pseudo-random if you cannot distinguish it from a truly random sequence (e.g. from radio-active decay) using stastical methods. This leads to a set of tests along with thresholds to judge the randomness of any given distribution.

Yao's work can be seen as the theoretic foundation of the [NIST test suite](https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software).

Luckily, with the [Random Bitstream Tester](https://mzsoltmolnar.github.io/random-bitstream-tester/) Zsolt Molnár provides an a JavaScript-implementation of the test suite with an easy web-interface.

Although the Random Bitstream tester is no officially certified implementation, it should be sufficient for our purposes.

#### How to quickly detect a *bad* distribution?

Whenever we implement a new algorithm or tweak/tune an existing one we need to perform again all the tests on a large number of output bits.

Obviously, this is tedious work. We cannot avoid these tests for promising candidates but maybe we can implement a quicker test to detect and discard *bad* candidates early.

So, how can we observe a stream and tell that its output is *not* random?

When we perform a bijective mapping of numbers of a range on the numbers in the same range, then there are a few cheap KPIs we can gather easily. 

* KPI `sucDist`: The distance between two subsequently generated (mapped) numbers.

If our function `output = f(input)` creates an even and pseudo-random distribution, then the *average* of the successor distances `avgSucDist` should converge towards the *average* distance of two randomly chosen numbers in that interval, if we generate (map) a large count of numbers. If this is not the case then our distribution is not worth further investigation. 

Reasoning: If `avgSucDist` is too low or too high, then our distribution process must produce an unwanted pattern. 

* KPI `srcDist`: The distance between the input (sequential number) and the output (mapped number).

When we think beyond, then also the average distance `avgSrcDist`  between the sequence number `input` and the mapped number `output = f(input)` should also tend towards the average distance of two randomly selected numbers in the interval over time. It may take longer because the input is heavily biased (it is not randomly picked but a steadily increasing sequence with increment 1).
If the average is not converging to the expected value, then there must be another hidden pattern disqualifying the candidate from further investigation.

**Clarification:** This test only detects bad candidates early. You cannot *prove* a good distribution just because this KPI looks good!

**Conclusion:** When we observe the two KPIs while generating (mapping) a sequence of numbers we should be able to discard the majority of bad algorithms, so we can concentrate on the remaining good ones. To those we still must apply the expensive tests.


### Providing a Tool Set



## The Experiments

### Experiment I: Distribution of Numbers

### Experiment II: Random Numbers

### Experiment III: Hashing

### Experiment IV: Encryption



## Observations and Criticism

### Consider Godhart's Law

First of all: **I plead guilty for cheating!** :wink:

If you just read through this article you may wonder about the excellent test results I achieved.

However, I had full control about the selected example inputs.

1975, [Charles Godhart](https://en.wikipedia.org/wiki/Goodhart%27s_law) stated 

> *"When a measure becomes a target, it ceases to be a good measure."*

Although it was hard work to get this NIST test suite fully green for a particular input, it was not impossible.
If you change the input certain tests may fail, especially the Runs test is painful to pass.

It would be way harder to design an algorithm that passes all the tests for arbitrary inputs!

Play with the examples and the code to get a better understanding of this effect.

### Keep It Simple Stupid

The KISS-principle also applies to such experiments. Adding too many parameters or fancy operations to your recipe make it hard to understand what is happening and why. Also, any obscure implementation may blur the impact of later optimization.

### Avoid *Desparately Fixing*

In the shady field of pseudo-randomness you often see results which seem to be *close to perfect*, only a little bit off, etc.
Tweaking here and there seems to be so promising!

However, a personal observation during my experiments was that you should not try to fix a broken concept by adding further complex operations (another rotate, invert, whatever you like).

Whenever you do so, you just increase complexity, runtime and obscurity!

It is almost guaranteed that improving one end will lead to worse results on the other.

**Conclusion:** Concentrate on a few operations. If your approach doesn't work well, think about a new attempt, again with only a few operations.

### Repetition is not your friend

Temptation is high to try improving results by applying the same operation again: `f'(input) = f(f(input))`, like shaking a bottle again when mixing a cocktail.

Sometimes, you may see test results getting worse rather than better. 

*But why?*

The NIST test suite among others focusses on unexpected regularities (patterns). By applying the same imperfect transformation multiple times you increase the likelihood of *amplifying* an unwanted pattern hidden in your approach.

If you want to apply your recipe in multiple *rounds*, you must ensure that every subsequent run clearly differs from the previous one (either algorithmic or through parameters), like `f'(input) = g(f(input))` or `f'(input) = f(f(input, X), Y)`.

However, as the 64-bit example shows, chaining or recursion has a drastic negative impact on performance.

