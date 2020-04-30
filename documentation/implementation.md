# Implementation

Description of the programs structure and architecture can be found from [here](architecture.md).

## Putting it all together, performance, flaws, and future improvements
The important action happens inside the different tools. There are couple different ways to produce similar results, but there are no real overlapping of the tools created. Thus the performance testing does not try to achieve identical end products, but instead "usable" end products via different methods. A usable end product is something that would work as a terrain for some game.

The primary goal of the project is to be able to pre-generate terrains to be used in games, secondary goal is to have the system operate fast enough for generating the terrains in real-time in the games. See [this](goals.md) for more information about the goals.

The [performance was measured](performance.md), and the individual operations can be considered fast enough for the purposes, except the water adding tool with larger water amounts.

### The water adding system
The water adding system mimics what I intuitively think is water flowing, and while it gives nice results, the implementation is slow. The slowness comes mainly from the way the lakes are filled. When the lake size is increased the water is levelled to the new height, and then the system tries to start a river from the lowest dry land position. This usually expands the river only few tiles, and the whole lake is then again passed. This is repeated a lot with small increments to the height (depth) of the lake. It could probably be sped up by making a first rough approximation of the size of the lake to be filled, and then finish up with the precise measures.

### Basic terrain generation
When generating terrains with the perlin noise, multiple passes of different "octaves" (as they're commonly referred to) are needed. Diamond-square gives approximately same results with just one pass, also a single diamond-square pass is a bit faster than perlin noise. The perlin noise is a bit more versatile though, and can be combined with the diamond-square algorithm.

### Primary goal
For the primary goal the 3d terrain view is the bottleneck, it seems like the culprit is the JavaFX 3d system as updating even a single value in the terrain data (be it in the beginning or the end) causes a delay that is as long as the initial creation of the 3d terrain. The user experience could be improved with the current system by keeping the 3d terrain view in small chunks, and then update the chunks over time with delays, that way the user would see slow update but the user interface would not completely stall for the duration of the update. However that would increase the total time needed for each update, and in order to fully fix the very slow updates the JavaFX 3d system would need to be changed to something else, the [Lightweight Java Game Library](https://www.lwjgl.org/) would probably be a good choice. Switching to LWJGL might also require switching away from JavaFX.

### Secondary goal
With the secondary goal, the terrain generation with the example cases varies between 0.5 and 1.5 seconds (again ignoring the very slow water adding system). In the worst case scenario, the maximum number of terrains needed is five, which brings the total time up to about 7.5 seconds. The size of those terrains are about 500x500, and assuming one unit is one meter, they cover about 500 meters in one direction. The maximum (5) number of terrains is needed when the player moves diagonally, which is about 1.4*500=700 meters. Thus if the players maximum speed is approximately 700m/7.5s=93 meters per second, the terrain generation is fast enough.

### User interface
The graphical user interface is very bare bones, and would greatly benefit from proper user interface elements instead of the stdin/stdout style console. More user friendly commands should also be created, with good default values and combinations of tools. Also the ability to point and click on the terrain to position the wanted effect would help users a lot. Nice advanced user interface is not part of the course, so it was deliberately left on low priority.

### Other
The sheet erosion and water adding tools are currently the only tools that emulates some kind of physics. It would be interesting to add more tools that mimic physics in some way. There could be more different types of erosions: wind erosion, water erosion (from the rivers), and shoreline erosion (lakes and sea).

With some soil types, the angle of repose should vary whether the soil is under water, moist, or dry. Together with the different types of erosions this would probably create interesting results.

New types of soils would be great additions. And they should be read from a resource file instead of being hard-coded into the Java source file.
