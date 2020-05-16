/*
 * Copyright (C) 2020 Joni Yrjana {@literal <joniyrjana@gmail.com>}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package plortz.ui.lwjgui;

import lwjgui.LWJGUIApplication;
import lwjgui.event.listener.EventListener;
import lwjgui.event.listener.MouseButtonListener;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.control.ToolBar;
import lwjgui.scene.layout.BorderPane;
import plortz.ui.GraphicalUI;
import plortz.ui.UserInterface;

/**
 * The JavaFX application class responsible of setting up the JavaFX stuffs.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Main extends LWJGUIApplication {
    
    private static UserInterface my_ui = null;
    public static Window main_window = null;
    
    /**
     * Called by JavaFX, the starting point of the applications user interface code.
     * 
     */
    @Override
    public void start(String[] args, Window window) {
        if (my_ui == null) {
            my_ui = new GraphicalUI();
        }
        
        main_window = window;
        
        BorderPane root = new BorderPane();
        root.setBackgroundLegacy(null);
        
        Scene scene = new Scene(root, 1024, 768);
        window.setScene(scene);
        window.setTitle("Plortz");
        window.show();
        
        ToolBar toolbar = new ToolBar();
        root.setTop(toolbar);
        
        Widget console = new Console(my_ui);
        root.setBottom(console.getRootNode());
       
        var tv = new TerrainView3d(my_ui);
        root.setCenter(tv.getRootNode());

        my_ui.showMessage("Welcome to Plortz.");
        my_ui.showMessage("Type \"help\" to get started.");
    }
    
    /**
     * A wrapper function to setup the static variables required by this user interface.
     * <p>
     * The use of static variables are needed because JavaFX calls a static method,
     * but the user interface object is already created when the JavaFX portion is initialized in this application.
     * 
     * @param my_ui The user interface instance to use.
     */
    public static void run(UserInterface my_ui) {
        Main.my_ui = my_ui;
        launch(null);
    }
}
