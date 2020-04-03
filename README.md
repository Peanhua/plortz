# plortz
Plortz is a software written in Java to generate rectangular areas of terrains suitable to be used in games etc. The terrains consists of data points to define the landscape elevation data and other attributes, such as the type of the land (for example dirt, sand, snow, water). These are commonly referred to as heightmaps.

This project is made for a course named "tiralabra", one of the requirements in the course is that all algorithms and data structures are self made.


## Documentation:
* [Project specification](documentation/project_specification.md)
* [Implementation documentation](documentation/implementation.md)
* [Testing documentation](documentation/testing.md)
* [Weekly reports](weekly_reports/)
  * [Week 1](weekly_reports/week1.md)
  * [Week 2](weekly_reports/week2.md)
  * [Week 3](weekly_reports/week3.md)
  * [Week 4](weekly_reports/week4.md)


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

### Running
The program can also be run with ```mvn javafx:run``` in addition to the normal ```mvn exec:java```.

By default, the program starts in graphical user interface, but it also has a command-line user interface which can be activated by specifying --no-gui when starting:
```mvn exec:java -Dexec.args="--no-gui"```


## Commands:
Most commands require arguments, giving incorrect number of arguments prints the usage.

Commands are read from the graphical console or stdin and produce no output on success.

* Create new terrain: ```new```
* Apply gaussian distribution to generate a bump (a mountain/hill/hole): ```gauss```
* Apply diamond-square algorithm to adjust altitudes: ```ds```
* Apply random noise to adjust altitudes: ```random```
* Apply perlin noise to adjust altitudes: ```perlin```
* Apply sheet erosion: ```sheet_erosion```
* Add soil layer: ```add_soil```
* Insert soil at bottom of the layers: ```insert_soil```
* Simple ascii dump: ```dump``` (any extra argument dumps normalized values)
* Save to .TGA file: ```save```
* Quit: ```quit``` (or EOF)
* Help: ```help```
* Command history: ```history```
* Re-execute commands from history: ```!```
* Set random seed: ```random_seed```
* Add random patches of soil: ```random_soil```

Example terrain generation:
```
new 500 500
perlin 1 0.0125
perlin 0.5 0.025
perlin 0.25 0.05
perlin 0.125 0.1
perlin 0.0625 0.2
add_soil sand 0.01 rect 250 250 500 500
gauss 250 390 0.3 150 0.5
sheet_erosion
save terrain.tga
```

Another example:
```
new 257 257
ds 10
add_soil sand 1 rect 128 128 260 260
sheet_erosion
sheet_erosion
sheet_erosion
```

Yet another:
```
new 200 200
gauss 100 100 0.4 30 10
add_soil sand 1 rect 50 50 100 100
sheet_erosion
sheet_erosion
sheet_erosion
sheet_erosion
```
