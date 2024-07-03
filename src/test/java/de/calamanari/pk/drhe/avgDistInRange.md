####  [Project Overview](../../../../../../../README.md) > [Distribution, Random, Hash, Encryption](README.md)
-----

# About the average distance of two numbers in an integer interval

We want to find a function that quickly tells us the *average distance* of two *randomly chosen* numbers in an integer interval.

* [Definitions](#Definitions)
* [Goal](#Goal)
  * [Simplification](#Simplification)
* [Analysis](#Analysis)
  * [Observations](#Observations)
* [The series $s(n)$ for the sum of distances](#the-series-sn-for-the-sum-of-distances)
* [Developing a closed form of $s(n)$](#developing-a-closed-form-of-sn)
  * [The function $f(n)$](#the-function-fn)
    * [Limit](#limit)
* [Summary](#summary)



## Definitions

$I := [a, b]$ with $a, b \in \mathbb{Z}$ and $b \ge a$ is defined as the list of of numbers $e \in \mathbb{Z}$ with $a \le e \le b$.

For two numbers $a, b \in \mathbb{Z}$ with $b \ge a$ the *distance* $d$ of two elements $e_1, e_2 \in I$ is defined as $d(e_1,e_2) := |e_2 - e_1|$. The case $e_1 = e_2$ is valid (hence $d(e_1,e_2) \ge 0$).

The *number of elements* $n$ in an interval $I := [a,b]$ with $a, b \in \mathbb{Z}$ and $b \ge a$ is defined as $n := b - a + 1$ (upper and lower bounds included).

The *average distance* $d_{avg}$ of two numbers in an interval $[a, b]$ in $\mathbb{Z}$ is the arithmetic mean of all *possible* distances of two numbers in that interval including the distance $0$ of a number to itself, e.g. $dist(a,a) = 0$.

## Goal

Find a function $f(n)$ to compute the average distance $d_{avg}$ of two numbers in an interval $I := [a,b]$ with $a, b \in \mathbb{Z}$ and $b \ge a$ and $n := (b - a) + 1$ elements including the lower and upper bounds $a$ and $b$.

### Simplification

#### Claim

Moving a given interval $I := [a, b]$ by any $\delta \in \mathbb{Z}$ on the number line does not change the distance of two numbers $e_1, e_2 \in I$.

#### Proof

Be $I := [a,b]$ with $a, b \in \mathbb{Z}$ with $b \ge a$ then every number $e \in I$ can be written as $e = a+k$ or $e = b-l$ with $k, l \in \mathbb{Z}$. 

Accordingly, for $e_1, e_2 \in I$: $d(e_1,e_2) := |a + k_2 - (a + k_1)|) = |b - l_2 - (b - l_1)|)$ with $k_1, k_1, k_2, l_2 \in \mathbb{Z}$.

Moving the whole interval on the number line means adding $\delta \in \mathbb{Z}$ to $a$ and $b$: $I' := [a + \delta,b + \delta]$.

$\Rightarrow$ for $e_1', e_2' \in I'$: $d(e_1',e_2') := |a + \delta + k_2 - (a + \delta + k_1)|) = |b + \delta - l_2 - (b + \delta - l_1)|) = |a + k_2 - (a + k_1)|) = |b - l_2 - (b - l_1)|) = d(e_1,e_2)$.

$\blacksquare$

#### Claim

The number of elements in the interval $I := [a, b]$ by any $\delta \in \mathbb{Z}$ will be unaffected when moving the interval on the number line.

#### Proof

The number of elements in $I$ is calculated as $n := (b - a) + 1$

$\Rightarrow$ the number of elements in $I'$ is calculated as $n':= (b + \delta - (a + \delta)) + 1 = (b - a) + 1 = n$

$\blacksquare$

Consequently, instead of investigating the distances in $I := [a,b]$ we can look at $I' := [a + \delta, b + \delta] \Leftrightarrow I' := [a + \delta, (n - 1 + a) + \delta]$ using *any* $\delta \in \mathbb{Z}$.

> With $\delta := 1 - a$ 

$ \Rightarrow I_{focus} = [1, n]$

If we find and prove an average distance function $f(n)$ for $I_{focus} := [1,n]$ with $n \in \mathbb{N}$ being the number of elements in $I_{focus}$ then this function will be applicable to *every* interval in $\mathbb{Z}$ with $n$ elements.

## Analysis

First of all we need to get an impression. So, let's take a look at some examples.

For a couple of interval sizes ($n$) we will draw all possible distances in an $n \times n$ matrix and measure the results.

*Why a matrix? That's redundant!*

When we compute all distances in *both directions*, then the number of distances will always be $n^2$, no matter if $n$ is even or odd. This reduces complexity.

* sum of all distances: $s$
* number of distances: $d_{cnt} := n^2$
* average distance: $d_{avg}=\dfrac{s}{d_{cnt}}=\dfrac{s}{n^2}$


$n=1, f(1) = 0$:
```
(1,1)
```
$s=0$, $d_{sum}=1$, $d_{avg}=\dfrac{0}{1}=0$

$n=2, f(2) = \dfrac{1}{2}$:
```
(1,1) (1,2)
(2,1) (2,2) 
```
$s=2$, $d_{sum}=4$, $d_{avg}=\dfrac{2}{4}=\dfrac{1}{2}$

$n=3, f(3) = \dfrac{8}{9}$:
```
(1,1) (1,2) (1,3)
(2,1) (2,2) (2,3)
(3,1) (3,2) (3,3)
```
$s=8$, $d_{sum}=9$, $d_{avg}=\dfrac{8}{9}$

$n=4, f(4) = \dfrac{5}{4}$:
```
(1,1) (1,2) (1,3) (1,4)
(2,1) (2,2) (2,3) (2,4)
(3,1) (3,2) (3,3) (3,4)
(4,1) (4,2) (4,3) (4,4)
```
$s=20$, $d_{sum}=16$, $d_{avg}=\dfrac{20}{16}=\dfrac{5}{4}$

$n=5, f(5) = \dfrac{8}{5}$:

![fn5](fn5-matrix.svg)

### Observations

* The sum of all distances $s(n)$ for $n>1$ obviously can be described *based on* $s(n-1)$. 
* $f(n) := \dfrac{s(n)}{n^2}$
* If we can find a closed form for the series $s(n)$ we will also get one for $f(n)$.

Let's take a closer look at $s(n)$.

![f1-5n](f1-5n.svg)

## The series $s(n)$ for the sum of distances

For every increment of $n$, additionally to the distances summed up before we must add the distances of all smaller numbers to $n$ plus all distances back from $n$ to all smaller numbers.

$s(n+1) := s(n) + 2 \times \displaystyle\sum\limits_{k=1}^n k$ 

> Applying [Gauss, Triangular number](https://en.wikipedia.org/wiki/Triangular_number): $\displaystyle\sum\limits_{k=1}^n k = \dfrac{n \times (n+1)}{2}$

$\Leftrightarrow s(n+1) = s(n) + 2 \times \dfrac{n \times (n+1)}{2} = s(n) + n^2 + n$

$\Leftrightarrow s(n) = s(n-1) + n^2 - 2n + 1 + n - 1$

$\Leftrightarrow s(n) = s(n-1) + n^2 - n$

## Developing a closed form of $s(n)$

> With $s(1) = 0$

$\Rightarrow s(n) = \displaystyle\sum\limits_{i=1}^n (i^2 - i) = \displaystyle\sum\limits_{i=1}^{n-1} (i^2 - i) + n^2 - n$

$\Leftrightarrow s(n) = \displaystyle\sum\limits_{i=1}^{n-1} i^2 - \displaystyle\sum\limits_{i=1}^{n-1} i + n^2 - n$

> Applying [Gauss, Triangular number](https://en.wikipedia.org/wiki/Triangular_number): $\displaystyle\sum\limits_{k=1}^n k = \dfrac{n \times (n+1)}{2}$

$\Leftrightarrow s(n) = \displaystyle\sum\limits_{i=1}^{n-1} i^2 - \dfrac{(n-1) \times ((n-1) + 1)}{2} +n^2 - n$

$\Leftrightarrow s(n) = \displaystyle\sum\limits_{i=1}^{n-1} i^2 - \dfrac{n^2 - n}{2} + \color{blue} n^2 - n \color{black} = \displaystyle\sum\limits_{i=1}^{n-1} i^2 - \dfrac{n^2 - n}{2} + \color{blue} 2 \times \dfrac{n^2 - n}{2}$

$\Leftrightarrow s(n) = \displaystyle\sum\limits_{i=1}^{n-1} i^2 + \dfrac{n^2 - n}{2}$

> Applying [Square pyramidal number](https://en.wikipedia.org/wiki/Square_pyramidal_number): $\displaystyle\sum\limits_{k=1}^n k^2 = \dfrac{n \times (n+1) \times (2n +1)}{6}$

$\Leftrightarrow s(n) = \dfrac{(n-1) \times ((n-1) + 1) \times (2 \times (n-1) +1)}{6} + \dfrac{n^2 - n}{2}$

$\Leftrightarrow s(n) = \dfrac{(n-1) \times n \times (2n -1)}{6} + \dfrac{n^2 - n}{2} = \dfrac{2n^3 - 3n^2 + n}{\color{blue}6\color{black}} + \dfrac{n^2 - n}{2}$

$\Leftrightarrow s(n) = \dfrac{\dfrac{2}{\color{blue}3\color{black}}n^3 - n^2 + \dfrac{1}{\color{blue}3\color{black}}n + n^2 -n}{2} = \dfrac{\dfrac{\color{blue}2\color{black}}{3}(n^3 - n)}{\color{blue}2\color{black}} = \dfrac{1}{3}(n^3 - n)$

### Claim

> With $g(n) = \dfrac{1}{3}(n^3 - n)$

$s(n) = s(n-1) + n^2 -n = g(n)$

### Proof

$s(1) = s(0) + 1^2 -1 = 0 = \dfrac{1}{3}(1^3 -1) = \dfrac{1}{3} \times 0 = g(1)$ :white_check_mark:

$s(n+1) = s(n) + (n+1)^2 - n - 1 = \color{blue}g(n)$ $+ (n+1)^2 - n - 1$

$= \color{blue}\dfrac{1}{3}(n^3 - n) \color{black} + (n+1)^2 - n- 1$

$= \dfrac{1}{3}n^3 - \dfrac{1}{3}n + n^2 + 2n + 1 -n -1$

$= \dfrac{1}{3}n^3 + n^2 + \dfrac{2}{3}n$

$= \dfrac{1}{3}(n^3 + 3n^2 + 2n)$

> For the required formula $g(n+1) = \dfrac{1}{3}((n+1)^3 - (n+1))$ we need the term $\color{blue}-(n+1)$ *inside* the brackets. We can achieve this by adding and subtraction at the same time.

$\Rightarrow s(n+1) = \dfrac{1}{3}(n^3 + 3n^2 + 2n \color{blue}+(n+1)-(n+1)$ $) = \dfrac{1}{3}(n^3 + 3n^2 + 3n + 1 - (n+1))$

> Applying [binomial expension 3<sup>rd</sup> degree](https://en.wikipedia.org/wiki/Binomial_theorem): $(x+y)^3 = x^3 + 3x^2y + 3xy^2 + y^3 \Rightarrow (x+1)^3 = x^3 + 3x^2 + 3x +1$

$s(n+1) = \dfrac{1}{3}((n+1)^3 - (n+1)) = g(n+1)$ :white_check_mark:

$\blacksquare$

### The function $f(n)$

Consequently, we can replace $s(n)$ in $f(n)=\dfrac{s(n)}{n^2}$ with $g(n)$.

$f(n) = \dfrac{\frac{1}{3}(n^3 - n)}{n^2} = \dfrac{1}{3}(n - \dfrac{1}{n})$

#### Limit

$\displaystyle\lim_{n \to \infty}(f(n)) = \displaystyle\lim_{n \to \infty}(\dfrac{1}{3}(n-\dfrac{1}{n})) = \dfrac{1}{3}n$

## Summary

* $f(n) = \dfrac{1}{3}(n - \dfrac{1}{n})$ computes the average distance $Dist_{avg}$ of two randomly selected numbers in an interval $I := [a,b]$ with $a, b \in \mathbb{Z}$ with $n = b - a + 1$ elements. 
* The function's limit $\dfrac{1}{3}n$ is a convienient estimator for the *expected* average distance in large intervals.

