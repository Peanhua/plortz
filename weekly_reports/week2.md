# Week 2

Started the coding this week, so created the basic framework for the software. Doing the commandline user interface first, because it can then be used to create the graphical user interface by translating the GUI commands to text commands.

Created first few tools. First was a tool to generate bump (or recession if given negative vertical scale) with Gaussian distribution, the result is round and smooth, and quite pleasant for the eye. The natural irregularities can be created with other tools later. The tool can be optimized a lot with some simple means: circle can be divided into 8, so the maths need to be calculated 1/8th and for the remaining 7/8 portions can just be filled in. The current method is to go through whole terrain and do calculations for every tile, but depending on the size of the terrain and the size of the bump, large portions of the terrain will go unchanged. Thus using an algorithm to draw a filled circle the amount of work can be greatly reduced.

Early on it became apparent that some visualization is needed, staring at floating point numbers in a 2d grid on terminal window is sometimes difficult to visualize as a shape, and copy-pasting the numbers to spreadsheet program to generate a graph is slow. So a Targa-file saver was created. The Targa files are currently saved uncompressed and thus the files become very large very fast. Targa file format supports RLE compression, that will need to be added.

Also created a tool to adjust the terrain with diamond-square algorithm, which is commonly used for heightmap generation. The results from that algorithm are already looking usable. And then of course a simple randomizer tool, good for applying some small irregularities to the terrain.

Decided that the distinction between generators and tools is not warranted, all the planned generators could be useful as a tools too. The original idea was to have generators that set the terrain, whereas tools adjust the terrain, but if one starts from flat terrain and adjusts with a tool it equals a "generator". So now there are only tools.

Wrote unit tests for the most important bits: tools and data structures used. Couple bugs were found and corrected when writing the unit tests, so they were already useful.

Added checkstyle plugin and some rules from one of my previous projects, slightly modified the rules. Few methods are too long (over 20 lines of code), but not by too much (all are below 30 lines), so leaving them as is for now. Also added jacoco plugin for code coverage reports, and simple usage instructions. Didn't keep track of hours, spent something between 20 and 40 hours.

Next week I'll probably work on Perlin noise, optimize the Gaussian distribution code, add RLE compression for the Targa file writer, and implement some data structures such as ArrayList replacement.
