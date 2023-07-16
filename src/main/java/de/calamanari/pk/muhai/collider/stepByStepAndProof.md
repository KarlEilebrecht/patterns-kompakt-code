#### [Project Overview](../../../../../../../../README.md)
#### [Expected Number Of Collisions](collisionExpectation.md)
----

# Creating the function _c(m,n)_ for the series _S<sub>n</sub>_

The goal is to develop a closed form for the following series:

![s1](../../../../../../../../doc/patterns/images/01-series-raw.svg)

First, let’s simplify the series definition, so that the predecessor _S<sub>n-1</sub>_ only appears once:

![s2](../../../../../../../../doc/patterns/images/02-series-simplified.svg)

To get a feeling for behavior of the series, we will now take a look at the first couple of elements:

![s3](../../../../../../../../doc/patterns/images/03-examples-raw.svg)

Well, there is a glimpse of a pattern, but multiple occurrences of $(1-\frac{1}{m})$ make it annoyingly hard to catch it.

Thus, we will introduce an auxiliary variable to improve readability.

![s4](../../../../../../../../doc/patterns/images/04-auxvar-b.svg)

![s5](../../../../../../../../doc/patterns/images/05-examples-b.svg)

This looks way better! 

Obviously, there is a pattern, which happens to be closely related to the [Geometric Series](https://en.wikipedia.org/wiki/Geometric_series). 

More precisely, it looks similar to a **partial sum** as depicted below:

![s6](../../../../../../../../doc/patterns/images/06-geo-raw.svg)

Somehow our series _S<sub>n</sub>_ is based on _s'<sub>n-1</sub>_. Our series looks like anything where the geometric series got subtracted from. Only the **-1** at the end is missing. This problem can be solved by adding **1** at the beginning and subtracting **1** at the end (which won’t change the result):

![s7](../../../../../../../../doc/patterns/images/07-examples-b-geo.svg)

Based on our observations we assume:

![s8](../../../../../../../../doc/patterns/images/08-hypothesis.svg)

This shall be our **working hypothesis**.

However, we haven’t achieved much, yet. It is still a series.

Luckily, for any $b≠1$ (remember: $b=(1-\frac{1}{m}), m>0$) we are allowed to replace a partial sum with its closed form as follows:

![s9](../../../../../../../../doc/patterns/images/09-geo-closed.svg)

Let’s do that to get a closed form of _S<sub>n</sub>_:

![s10](../../../../../../../../doc/patterns/images/10-series-closed-b.svg)

Now it is time to replace $b$ with its original definition $b=(1-\frac{1}{m})$:

![s11](../../../../../../../../doc/patterns/images/11-series-closed.svg)

**Given the series**

![s12](../../../../../../../../doc/patterns/images/12-series-def-short.svg)

**We claim**

![s13](../../../../../../../../doc/patterns/images/13-claim.svg)

**Proof**

![s14](../../../../../../../../doc/patterns/images/16-proof-s0.svg)

![s15](../../../../../../../../doc/patterns/images/15-proof.svg)

**Conclusion**

![s16](../../../../../../../../doc/patterns/images/17-conclusion.svg)


