# plortz
Plortz is a software written in Java to generate rectangular areas of terrains suitable to be used in games etc. The terrains consists of data points to define the landscape elevation data and other attributes, such as the type of the land (for example dirt, sand, snow, water). These are commonly referred to as heightmaps.

This project is made for a course named "tiralabra", one of the requirements in the course is that all algorithms and data structures are self made.


## Documentation:
* [Project specification](documentation/project_specification.md)
* [Weekly reports](weekly_reports/)
  * [Week 1](weekly_reports/week1.md)
  * [Week 2](weekly_reports/week2.md)


## Maven goals:
Java 11 is required.
* Checkstyle: ```mvn jxr:jxr checkstyle:checkstyle```
* Run tests:  ```mvn test```
* List possible plugin updates: ```mvn versions:display-plugin-updates```
* List possible dependency updates: ```mvn versions:display-dependency-updates```
* Jacoco report: ```mvn jacoco:report``` (may require ```mvn compile && mvn test``` to be run first)
* Compile the program: ```mvn compile```
* Run the program: ```mvn exec:java```
* Compile and run the program: ```mvn compile exec:java```
* Generate javadoc: ```mvn javadoc:javadoc```


## Commands:
Most commands require arguments, giving incorrect number of arguments prints the usage.

Commands are read from stdin and produce no output on success.

* Create new terrain: ```new```
* Apply gaussian distribution to generate a bump (a mountain/hill/hole): ```gauss```
* Apply diamond-square algorithm to adjust altitudes: ```ds```
* Apply random noise to adjust altitudes: ```random```
* Simple ascii dump: ```dump``` (any extra argument dumps normalized values)
* Save to .TGA file: ```save```
* Quit: ```quit``` (or EOF)
