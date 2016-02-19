# Packaging JRuby and Gems with Maven

## Why we need Maven

If we want to add some non-trivial dependency to the Java project, which has a deep dependency-tree, and it's not feasible to keep multiple JARs up-to-date ( for example ActiveMQ ).
Maven can solve this problem, by providing a build-environment, that is (mostly) easily configured, and together with some plugins, it can pack everything into 1 runnable JAR.

Advantages from our point of view:
* it handles Java dependencies ( including jruby.jar )
* with the `gem-maven-plugin` it can handle Gem dependencies for Ruby
* with the `maven-shade` plugin it can pack everything into one JAR
* the build script is one XML file, it is platform independent
* if we need to do some extra step within the build process, Maven can accomodate this
* superb documentation
* XML file is verbose & ugly, but is readable once one learns the basics of Maven, there is a project by which the pom.xml can be written as a Ruby DSL, YAML, Scala etc.

## What you will need to run this

Java & [Maven](https://maven.apache.org/).

Nothing else. OS doesn't matter.

## How to run the example project

In the root of the project ( where the pom.xml sits ) run:
```bash
mvn package
```

Give it some time while it downloads half the internet to find the dependencies, but subsequent runs are much faster.

After that the final JAR will be  `target/jruby-test-0.1-SNAPSHOT-shaded.jar`. This already includes the JRuby jar, the gems, our Ruby code, configuration files for gems (for example .irbrc can be put in, right now it's .pryrc).

Run it with: `java -jar target/jruby-test-0.1-SNAPSHOT-shaded.jar`

## What's going on?

The `pom.xml` is Mavens build-file. It contains everything needed to build the project:

The project info:
```xml
	<groupId>de.changepoint</groupId>
	<artifactId>jruby-test</artifactId>
	<version>0.1-SNAPSHOT</version>
	<packaging>jar</packaging>
```

It adds a repository for Gems. This is not the standard [Rubygems.org](https://rubygems.org/), but a service which translates gem dependencies to maven dependencies:
```xml
	<repositories>
		<repository>
			<id>rubygems-release</id>
			<url>http://rubygems-proxy.torquebox.org/releases</url>
		</repository>
	</repositories>
```

It adds the dependencies for Java and also the gems:
```xml
	<dependencies>
		<dependency>
			<groupId>org.jruby</groupId>
			<artifactId>jruby</artifactId>
			<version>1.7.24</version>
		</dependency>

		...

		<dependency>
			<groupId>rubygems</groupId>
			<artifactId>awesome_print</artifactId>
			<version>1.6.1</version>
			<type>gem</type>
		</dependency>
	</dependencies>
```

And finally it configures the plugins to produce the Uber-Jar.

On loading, it adds `GEM_HOME` and `GEM_PATH` to the Ruby script environment, both point to paths inside the JAR, and unpacks a tmp directory which it sets as `HOME`. (This is because some gems want to write to `HOME`. For example: pry gem wants to write to `~/.pryrc`)

# Hopefully:

![Very nice](http://socalledfantasyexperts.com/wp-content/uploads/2015/02/Borat-Outfield-Sleepers-Great-Success-300x336.jpg)
