# Patterns Kompakt Source Code

This repository contains executable examples to illustrate and demonstrate patterns described in our book _[Patterns Kompakt - Entwurfsmuster für effektive Softwareentwicklung, Verlag Springer Vieweg, Februar 2024](https://link.springer.com/book/9783658432331)_.

Information in German and a quick start project download can be found at the book's website [patterns-kompakt.de](http://patterns-kompakt.de).

## Project Setup

The repository contains Java code and runs as a [Maven](https://maven.apache.org/)-build. Until Java 7 the project was entirely independent and just leveraging classes contained in the JDK. Because since then the JDK has been trimmed considerably, the example code base was reshaped into a Maven project with a couple of external dependencies. While this may be an extra hurdle for newbees it increases flexibility a lot and better meets common standards and best practices. 

Besides a working git installation for cloning the repository you need:

* Java JDK 21 (e.g., [OpenJDK](https://openjdk.java.net/))
* [Maven](https://maven.apache.org/download.cgi) 3.8 (older versions may work as well)

Once done with the preparation simply run `mvn clean install`. This will run tests for all patterns with minimal logging. The first time it may take a while to download the maven artifacts the project depends on.


### Recommendations

* Install any IDE (e.g., [Eclipse](https://www.eclipse.org/downloads/) or [Visual Studio Code](https://code.visualstudio.com/)) for easier coding and better overview.
* [SonarLint](https://www.sonarlint.org/) can help preventing bugs, and due to its built-in documentation it gives valuable insights about the programming language. At least I learned a lot by reviewing the reasoning behind the SonarLint rules.
* At the time of writing I am using [Linux MINT](https://linuxmint.com/) inside a [Virtual Box](https://www.virtualbox.org/). This way, you don't need to worry about ruining your local installation with a different JDK, IDE or Maven.
* [Markdown](https://guides.github.com/features/mastering-markdown/) is a light-weight way to do necessary documentation _along with your code_. For editing on Linux I use [Ghostwriter](https://ghostwriter.kde.org/de/), which I found easy to install and pretty intuitive. On MacOS I use [MacDown](https://macdown.uranusjr.com/). There are also excellent Markdown plugins available for [Visual Studio Code](https://code.visualstudio.com/).
* In this project I use a minimalistic template to introduce each scenario and describe a particular pattern. For lean documention of real projects I recommend taking a look at [arc42](https://docs.arc42.org/home/).

## Package Structure

I have organized the patterns as separate packages with [JUnit](https://junit.org/junit5/) test cases to demonstrate each pattern. 

Every scenario is documented in a dedicated Markdown file. By intention I have chosen different scenarios than those described in the book, for practical programming reasons on the one hand and on the other hand to give the readers a second view on each pattern. Leveraging test cases allowed me to separate setup and dynamic demonstration aspects from the class structure of a pattern.

By adjusting the log-level ([SLF4J](https://www.slf4j.org/)+[Logback](https://logback.qos.ch/)) you can watch a pattern _at work_. The pattern examples do not depend on each other, so that (besides a few utility classes) you have maximum cohesion and focus inside each pattern's package.

I invite you to experiment with the patterns and to think about the choice that was made. Maybe you will come to the conclusion that a different pattern would have been better for a particular scenario?

Most important: **Have fun! :smirk:**

_Karl Eilebrecht, January 2024_


## The Patterns


### Creational Patterns
* [Abstract Factory](src/test/java/de/calamanari/pk/abstractfactory/README.md)
* [Builder](src/test/java/de/calamanari/pk/builder/README.md)
* [Factory Method](src/test/java/de/calamanari/pk/factorymethod/README.md)
* [Singleton](src/test/java/de/calamanari/pk/singleton/README.md)
* [Object Pool](src/test/java/de/calamanari/pk/objectpool/README.md)

### Behavioral Patterns

* [Command](src/test/java/de/calamanari/pk/command/README.md)
* [Command Processor](src/test/java/de/calamanari/pk/commandprocessor/README.md)
* [Iterator](src/test/java/de/calamanari/pk/iterator/README.md)
* [Visitor](src/test/java/de/calamanari/pk/visitor/README.md)
* [Strategy](src/test/java/de/calamanari/pk/strategy/README.md)
* [Template Method](src/test/java/de/calamanari/pk/templatemethod/README.md)
* [Observer](src/test/java/de/calamanari/pk/observer/README.md)

### Structural Patterns

* [Adapter](src/test/java/de/calamanari/pk/adapter/README.md)
* [Bridge](src/test/java/de/calamanari/pk/bridge/README.md)
* [Decorator](src/test/java/de/calamanari/pk/decorator/README.md)
* [Facade](src/test/java/de/calamanari/pk/facade/README.md)
* [Proxy](src/test/java/de/calamanari/pk/proxy/README.md)
* [Model View Controller (MVC)](src/test/java/de/calamanari/pk/modelviewcontroller/README.md)
* [Flyweight](src/test/java/de/calamanari/pk/flyweight/README.md)
* [Composite](src/test/java/de/calamanari/pk/composite/README.md)

### Distribution

* [Combined Method](src/test/java/de/calamanari/pk/combinedmethod/README.md)
* [Data Transfer Object (DTO)](src/test/java/de/calamanari/pk/datatransferobject/README.md)
* [Transfer Object Assembler](src/test/java/de/calamanari/pk/transferobjectassembler/README.md)
* [Active Object](src/test/java/de/calamanari/pk/activeobject/README.md)
* [Leader-Follower](src/test/java/de/calamanari/pk/leaderfollower/README.md)

### Integration

* [Wrapper](src/test/java/de/calamanari/pk/wrapper/README.md)
* [Gateway](src/test/java/de/calamanari/pk/gateway/README.md)
* [Plugin](src/test/java/de/calamanari/pk/plugin/README.md)
* [Mapper](src/test/java/de/calamanari/pk/mapper/README.md)
* [Dependency Injection](src/test/java/de/calamanari/pk/dependencyinjection/README.md)

### Persistence

* [Identity Map](src/test/java/de/calamanari/pk/identitymap/README.md)
* [Lazy Load](src/test/java/de/calamanari/pk/lazyload/README.md)
* [Coarse-Grained Lock](src/test/java/de/calamanari/pk/coarsegrainedlock/README.md)
* [Optimistic Offline Lock](src/test/java/de/calamanari/pk/optimisticofflinelock/README.md)
* [Pessimistic Offline Lock](src/test/java/de/calamanari/pk/pessimisticofflinelock/README.md)

### Database Keys

* [Identity Field](src/test/java/de/calamanari/pk/identityfield/README.md)
* [Sequence Block](src/test/java/de/calamanari/pk/sequenceblock/README.md)
* [UUID](src/test/java/de/calamanari/pk/uuid/README.md)
* [MUHAI](src/test/java/de/calamanari/pk/muhai/README.md)

### Other Patterns

* [Money](src/test/java/de/calamanari/pk/money/README.md)
* [Null Object](src/test/java/de/calamanari/pk/nullobject/README.md)
* [Registry](src/test/java/de/calamanari/pk/registry/README.md)
* [Service Stub (Mock)](src/test/java/de/calamanari/pk/servicestub/README.md)
* [Value Object](src/test/java/de/calamanari/pk/valueobject/README.md)

### Experimental Stuff

* [Generic One-Hashing Bloom Filter](src/main/java/de/calamanari/pk/ohbf/README.md)
* [The BloomBox](src/main/java/de/calamanari/pk/ohbf/bloombox/README.md)
* [Distribution, Random, Hash, Encryption](src/test/java/de/calamanari/pk/drhe/README.md)
