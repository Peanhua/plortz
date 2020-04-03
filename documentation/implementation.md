# Implementation

## The program structure
The program works on a rectangular area of terrain which has elevation and other data. The area is divided by square tiles, each tile containing information about the layers of soils underneath it. The data is modified using instances of tools. Only the top-most surface of the data will be in the generated output.

Here is an illustration about the tiles and their soil layers, as looking from side, different soil layers are colored differently:
![Example tile 1](soil_layers.png)
Because each tile has its own soil layers, the soil layers are not connected between the tiles.

There are two modes of user interface, a graphical user interface using JavaFX, and a console user interface reading commands from stdin and outputting to stdout. Both user interfaces use the same commands, which are constructed from the user supplied command strings. The commands use the tools to perform the actions. Most tools have a one-to-one mapping with a command.

