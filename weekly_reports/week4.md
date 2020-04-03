# Week 4

Added tool using perlin noise to adjust the altitudes, a sheet erosion tool to roll loose landmass downhill, and a smoothing operation using median filtering. Accidentally first created average filter and the difference was surprisingly easy to see in naked eye, so it was an interesting accident. Will be adding at least one more filter to the smoothing tool based on edge detection and using that information to do less smoothing at the edges. That will probably be slow because the whole terrain needs to be scanned at least twice, but interested to see how the results differ.

Added soil layer system with couple simple tools. The system allows different types of soils to be stacked on top of each others, and is quite instrumental for the sheet erosion to work in a more natural way, for example sand rolls down from top of steep cliff exposing the cliff. It will also be important when carving rivers and such.

Added a simple graphical user interface and made it the default, the old stdin/stdout user interface is still available of course. Commands are still input with the keyboard, but now there is history and ability to recall the history. And most importantly, the results can immediately be seen on the screen. Some issues are really easy to spot when the program is interactive and results immediately available. Of course there is the downside that the visualization makes the program slower.

Also started to work on 3d view of the terrain, and while it is still in its early days and hardly usable, it already helped to see some issues and missing things.

And because of the coming peer review, the help command was added to make life easier, there are also couple examples in the project page to get started.

Used more time than last week, probably around 20 to 50 hours. Next week the remaining missing data structures will be made, I am not sure but I think there is only the hash map missing. Would be VERY AMAZINGLY REALLY NICE if there was some kind of a checklist or "top 10 things missed in previous years" :) Also the water system needs to be done, the diamond square needs to have a horizontal scaling parameter (became obvious with the 3d view) and it is also missing some fine tuning as explained in the Wikipedia page about it. Need to add timing of all the tools, so there can be some work done on optimization and comparing. And the 3d view needs more love if there is some extra time to spend.
