# plortz
* [Project specification](documentation/project_specification.md)
* [Weekly reports](weekly_reports/)
  * [Week 1](weekly_reports/week1.md)


## Maven goals:
* Checkstyle: ```mvn jxr:jxr checkstyle:checkstyle```
* Run tests:  ```mvn test```
* List possible plugin updates: ```mvn versions:display-plugin-updates```
* List possible dependency updates: ```mvn versions:display-dependency-updates```
* Jacoco report: ```mvn jacoco:report``` (may require ```mvn compile && mvn test``` to be run first)
* Compile the program: ```mvn compile```
* Run the program: ```mvn exec:java```
* Compile and run the program: ```mvn compile exec:java```


## Commands:
Most commands require arguments, giving incorrect number of arguments prints the usage.

Commands are read from stdin and produce no output on success.

* Create new terrain: ```new```
* Apply gaussian distribution to generate a bump (a mountain/hill/hole): ```gauss```
* Simple ascii dump: ```dump``` (any extra argument dumps normalized values)
* Save to .TGA file: ```save```
* Quit: ```quit``` (or EOF)
