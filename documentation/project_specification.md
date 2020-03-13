# Project specification

Plortz is a software written in Java to generate rectangular areas of terrains suitable to be used in games etc. The terrains consists of data points to define the landscape elevation data and other attributes, such as the type of the land (for example dirt, sand, snow, water). These are commonly referred to as heightmaps.

Initial implementation will contain only one layer of the data accross the landscape. Optionally this is extended into multiple layers so that underground land formation can be taken into account when applying tools and physics simulation.

The terrain generation starts with a generator, and the user is then able to apply different tools to further modify the terrain.

Generators contain simple flat land generator, different noise generators, and other useful algorithms that produce suitable results.

The tools user can apply include water source, earthquake, erosion, meteor hit, etc. Water source adds water to the given point, and from that point the water starts to flow carving rivers and possibly ponds/lakes. Earthquake causes rough erosion and shifts in the landscape. Erosion smooths the landscape and rolls loose land mass downhill. Meteor hit produces a crater and blows up land mass around. Other tools can be added.

Two user interfaces are provided, commands can be given through the standard input, or via a GUI with the help of mouse. The output will be two 2d images, a grayscale image with the elevation information, and a color image with the land type information. Optionally a 3d viewer is created to see the result from any angle.
