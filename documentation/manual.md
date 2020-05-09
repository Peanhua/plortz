# User manual

Plortz is a software written in Java to generate rectangular areas of terrains suitable to be used in games etc. The terrains consists of data points to define the landscape elevation data and other attributes, such as the type of the land (for example dirt, sand, snow, water). These are commonly referred to as heightmaps.

Plortz has two user interfaces, the default graphical using JavaFX, and a commandline. The user issues commands and can see the results immediately in the GUI. The results can be written to Targa files.

## Requirements
Maven and Java 11.

## Maven goals:
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

To output the timing information, start with "--timing" argument, for example:
```mvn exec:java -Dexec.args="--timing"```

#### Running the release
The packaged jar file requires JavaFX, which can be downloaded for example from [Gluon](https://gluonhq.com/products/javafx/). Download and unzip the proper package for your system, and then define the ```--module-path``` and ```--add-modules``` to add the ```javafx.controls``` module:
```
$ java --module-path ./javafx-sdk-11.0.2/lib --add-modules=javafx.controls -jar plortz-1.0-SNAPSHOT.jar
```

Command-line arguments can be given directly, for example:
```
$ java --module-path ./javafx-sdk-11.0.2/lib --add-modules=javafx.controls -jar plortz-1.0-SNAPSHOT.jar --timing
```


## Graphical user interface
The window is split into three parts. <img align="right" src="../screenshot.png" width="400px">

Starting from top, a 2d or 3d view is shown. The current mode of the view can be toggled with the button "2d/3d" on the top-left corner.

In the 3d mode the mouse and keyboard can be used to rotate and move around. Hold mouse button down on the view and move mouse to rotate, also while holding the mouse button down WASD -keys can be used to move relative to the current looking direction. Make sure to de-activate the console input field for the WASD-keys to work, for example by first clicking on the message/history. The mousewheel can be used to adjust the movement speed.

In the middle of the window a messages and history of commands view is shown.

At the bottom is a text input field that can be used to issue commands.


## Output
All output is done using Targa files. There are three output types, of which two are intended to be used by programs, and one is intended for human viewing.

The ```save``` command is used to do the output, and it accepts the following options:
* ```heightmap``` - save the altitudes as 8 bit grayscale image
* ```soil``` - save the soil types as 24 bit color image, each soil type has unique color
* ```color``` - combine both the altitudes and soil types as a 24 bit color image

In the ```heightmap``` mode, the altitudes are normalized to [0, 1]. In the ```color``` mode, the altitudes are in range [0.1, 1].

Because the output files are in low 8 bit resolution for the elevation data, the best option is to include the generating source code in the game project, if the licensing scheme used allows. The routines use double floating point numbers for high precision.


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
* Scale all the heights: ```scale```
* Add water (rivers and lakes): ```water```
* Execute a script from file: ```run```
* Comment (mainly for script files): ```#```
* Show information about the terrain: ```info```
* Remove water: ```remove_water```
* Set sea level: ```sea```

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
save color terrain.tga
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

For more examples, see the [scripts/](../scripts) directory.
