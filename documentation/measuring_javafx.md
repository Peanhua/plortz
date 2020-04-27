# Measuring JavaFX rendering times
The graphical user interface uses JavaFX which runs two or more threads, one thread for the application and one or more threads for the renderer and media, see [JavaFX architecture](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-architecture.htm#A1107438). When the program executes a command the terrain is updated, the JavaFX scene is updated, and the program calculates the time used. The user interface is then rendered a bit later in the other thread(s). The amount of time spent rendering is significant in 3d view, but is not visible in the timing information obtained by the program. Because the user interface is not responding while the rendering is performed, it must also be measured and taken into account. This is done using the built-in logger by starting with "javafx.pulseLogger=true", for example: ```mvn compile exec:java -Dexec.args="--timing" -Djavafx.pulseLogger=true```.

Example output of the pulse logger:
```
PULSE: 1 [0ms:276ms]
T19 (0 +143ms): CSS Pass
T19 (144 +13ms): Layout Pass
T19 (157 +1ms): Update bounds
T19 (159 +0ms): Waiting for previous rendering
T19 (159 +17ms): Copy state to render graph
T16 : Slow background path for null
T16 : Slow background path for null
T16 : Slow background path for null
T16 : Slow background path for null
T16 : Slow background path for null
T16 : Slow background path for null
T16 : Slow background path for null
T16 (192 +78ms): Painting
T16 (270 +6ms): Presenting
Counters:
        CacheFilter rebuilding: 1
        Cached region background image used: 4
        NGRegion renderBackgrounds slow path: 7
        Nodes rendered: 24
        Nodes visited during render: 31
        Rendering region background image to cache: 4
```
The most important bit is the first line ```PULSE: 1 [0ms:276ms]```, it means that this was the first event, 0ms after the previous, and it took 276ms in total. When there are multiple events occuring within a short period of time, the line ```T19 (159 +0ms): Waiting for previous rendering``` becomes also important, because time spent on waiting on previous rendering is not part of this rendering, and needs to be subtracted from the total, otherwise the same time is counted for multiple times.
