# Week 3

Improved documentation, test coverage, and removed some checkstyle errors. Added RLE (run length encoding) for Targa file saving to compress the output images. Replaced couple things with self-made ones: ArrayList with MyArrayList, Random with Mersenne Twister implementation, and String.split() in CommandFactory. Added the use of HashMap so I can later implement that too.

Optimized the gaussian distribution tool to adjust only tiles really affected using a circle drawing algorithm. Couldn't find an existing algorithm that would've filled a circle from inside to out, which I originally planned to do, in order to stop when the change goes below some threshold. So a simple circle filling algorithm was used, and the radius of the circle is detected separately. That causes some extra processing to be done, but is still a large improvement over the initial naive approach of doing calculations on every tile on the terrain.

Googled for some terrain viewer, but couldn't find anything suitable, so am still staring at 2d images on screen.

Used less time than last week, something between 10 to 30 hours. Will probably start the perlin noise implementation later today, but it probably wont be finished before midnight, so it'll be on next week. Also next week I want to start on the tile properties and tools that use/affect those, such as erosion, where some of the types do not roll downhill at all (such as cliff) and others roll downhill different amounts (for example sand rolls easily). Hopefully also have time for some of the water tools.
