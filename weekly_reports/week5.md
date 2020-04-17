# Week 5

Fixed the diamond square algorithm, my initial thoughts about what was wrong with it were incorrect. Also did some other fixes, refactoring, documentation stuffs, and tests, as usual.

Improved the 3d terrain view by adding colors to it, and the ability to move around freely. The JavaFX transform does not seem to give out the forward vector or any such basic stuff, and nearly started to make my own matrix class, but found a way to approximate the movement close enough that not everyone will notice the difference, and I'm not going to tell it here either (insert Simpsons Nelsons ha-ha here) :)

Implemented some containers: a simple set using ArrayList called ListSet, just so that the code needing a set worked. Later added HashSet to obtain a nice performance boost over the ListSet. Left the ListSet in place, even though it is no longer used, if there's room in the implementation documentation then a performance comparison between the two can be done. Also implemented HashMap.

Added some quality of life stuff to the user commands, such as saving and loading the history, and special command to run a script file. The script run command is also useful when timing the commands.

Implemented water adding system, which required couple algorithms: A-Star, Breadth First Search, and Flood Fill. The water adding system is still quite messy piece of code, and needs some refactoring and cleaning up. Also proper unit testing should be added. Although it works most of the time perfectly, there is at least one bug with it (it seems to occasionally create a sinkhole at the end).

An edge preserving smoothing filter was also implemented, not sure if this type of edge preserving filter has been previously implemented anywhere (didn't find exact hits with google). While it probably is not the best one, it was fun to come up with my own idea for it, just like with the water adding system.

Used approximately same amount of time as in week 4, probably around 20 to 40 hours. Next week there'll be more work on gathering timing data and writing the final documentation (implementation and testing). The implementation documentation is already getting a bit long, so might need to cut down on it later. It's a bit vague what should be in testing documentation and what should be in the implementation documentation, performance testing could go to either one. But from what I gather the testing documentation is more about testing whether the program works correctly or not, so I decided to put the performance testing stuffs into the implementation documentation. Apart from the bug, missing tests and code cleanup/refactoring for the water adding system, I can't remember right now if there is other major things to do in the coding side of things, so probably some polishing is in order.
