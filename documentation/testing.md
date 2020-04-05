# Testing

## Unit testing
JUnit is used for unit testing. The tests can be run with:
```
mvn test
```

### Code coverage
Code coverage report can be generated using Jacoco:
```
mvn jacoco:report
```
The user interface code (everything under plortz.ui) is excluded from the code coverage report.

There is a lack of unit tests performed on certain areas that are considered unimportant:
* The main class responsible of starting the program.
* Not implemented methods, that just throw an "UnsupportedOperation" exception.
* Overrides of toString() method, those are used only for debugging purposes.
* Some simple getter and setter methods.


## Manual testing
Manual testing has been performed mostly using the command line user interface, as it allows using scripts. For example:
```
echo -e 'new 500 500\nperlin 1 0.0125\nperlin 0.5 0.025\nperlin 0.25 0.05\nperlin 0.125 0.1\nperlin 0.0625 0.2\nsave foo.tga\n' | mvn compile exec:java -Dexec.args="--no-gui" && display foo.tga
```


## Performance testing
The important action happens inside the different tools. There are couple different ways to produce similar results, but there are no real overlapping of the tools created. Thus the performance testing does not try to achieve identical end products, but instead "usable" end products via different methods. A usable end product is something that would work as a terrain for some game.

The main goal of the project is to be able to produce terrains usable in games, secondary goal is to have the system operate fast enough for generating the terrains in real-time in the games.


### The main goal: produce terrains
The terrains can be generated via scripting, but to script, one has to know good parameters and tool combinations to use. Thus the users are going to use the interactive user interface and its operating speed is important, user should not need to wait for minutes or hours for an operation to complete. Instead the operations should be near real-time. Ideally the operations should take less than 1 second, but a maximum of 10 seconds is tolerable [Response Times: The 3 Important Limits by Jakob Nielsen](https://www.nngroup.com/articles/response-times-3-important-limits/).

The size of the terrain has a large impact because most tools operate on the whole terrain. The size of the terrain depends greatly on the game in question, what kind of world the game depicts, how the world is viewed, and what kind of rendering techniques are used. Some games might have one large terrain spanning several square kilometers, with lot's of detail. Some games might have streaming and/or level system so that the size of a single terrain remains small, tens of square meters. If the terrain is viewed from the ground level, more detail is required than when viewing from higher altitudes. Some kind of middle ground is taken here, reference terrain size shall be 1024x1024 tiles.
