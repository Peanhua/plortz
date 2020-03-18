# Project specification
Plortz is a software written in Java to generate rectangular areas of terrains suitable to be used in games etc. The terrains consists of data points to define the landscape elevation data and other attributes, such as the type of the land (for example dirt, sand, snow, water). These are commonly referred to as heightmaps.

Initial implementation will contain only one layer of the data accross the landscape. Optionally this is extended into multiple layers so that underground land formation can be taken into account when applying tools and physics simulation.

The terrain generation starts with a generator, and the user is then able to apply different tools to further modify the terrain.

Two user interfaces are provided, commands can be given through the standard input, or via a GUI with the help of mouse. The output will be two 2d images, a grayscale image with the elevation information, and a color image with the land type information. Optionally a 3d viewer is created to see the result from any angle.


## Generators
The following is a preliminary list of generators to include:
* flat land generator, complexity O(n)
* [Perlin noise](https://en.wikipedia.org/wiki/Perlin_noise), the "mother" of noise generators, complexity O(2^n) for n dimensions (n=2 will be used)
* [Diamond-square algorithm](https://en.wikipedia.org/wiki/Diamond-square_algorithm), O(n)
Possibly the following if time permits:
* [Wavelet noise](https://en.wikipedia.org/wiki/Wavelet_noise)
* [Value noise](https://en.wikipedia.org/wiki/Value_noise)
* [Worley noise](https://en.wikipedia.org/wiki/Worley_noise)


## User tools
"Water source" adds water to the given point, and from that point the water starts to flow carving rivers and possibly ponds/lakes. Path finding algorithm (some form of the best search algorithm) is used to find the general path of the water flow, then the terrain is modified along that path. The path generally goes downwards, but is allowed to go slightly up. The path can end at the edge of the map, on another water, or on "dead end". In the case of "dead end", a lake/pond is formed around it by adding water around the location until the water source is exhausted. The edges of the lake/pond are tested to see if new rivers can be created.

"Earthquake" causes rough erosion and shifts in the landscape.

"Erosion" smoothes the landscape and rolls loose land mass downhill. For each location selected, the lowest neighbor location is found, if the slope from current location to the lowest neighbor is greater than a threshold value, then land mass is moved to the lower location, the lower location is then added to the selected locations.

"Meteor hit" produces a crater and blows up land mass around. A circular area is lowered and flattened around the center of the meteor hit, and the land mass is moved around the edges of the circle. Small amount of erosion is then applied to the modified locations.

"Mountain" produces a mountain/hill at the selected location, raising the land. The land can be raised using a sharp peak, or a round peak.


## Data structures
Each layer is made of a 2d grid of data points, where each data points contains three values. A floating point value representing the elevation information, the vertical position of the land. An integer (enum) describing the terrain type. And a floating point number describing the water height. The surface of the water is at "elevation"+"water height".

For performance reasons, this data may later be arranged into 3 separate arrays.

Space complexity is O(nm) where "n" and "m" defines the size of the rectangle.
