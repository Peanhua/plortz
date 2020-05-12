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

import java.util.ArrayList;
import java.util.List;
import lwjgui.LWJGUIApplication;
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
    private int                  current_terrain_view;
    
    /**
     * Called by JavaFX, the starting point of the applications user interface code.
     * 
     * @param stage The stage.
     */
    @Override
    public void start(String[] args, Window window) {
        if (my_ui == null) {
            my_ui = new GraphicalUI();
        }
        
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 768);
        window.setScene(scene);
        window.setTitle("Plortz");
        window.show();
        
        ToolBar toolbar = new ToolBar();
        root.setTop(toolbar);
        
        Widget console = new Console(my_ui);
        root.setBottom(console.getRootNode());
       /* 
        this.current_terrain_view = 0;
        List<TerrainView> tvs = new ArrayList<>();
        tvs.add(new TerrainView2d(my_ui));
        
        if (Platform.isSupported(ConditionalFeature.SCENE3D)) {
            tvs.add(new TerrainView3d(my_ui));
        }

        StackPane terrain_view_container = new StackPane();
        tvs.forEach((t) -> {
            terrain_view_container.getChildren().add(t.getRootNode());
        });
        root.setCenter(terrain_view_container);

        // Need to catch keyboard events here because for some reason they're not received in the stackpane and its children:
        root.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            tvs.forEach((t) -> {
                t.onKeyPressed(e);
            });
        });
        root.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            tvs.forEach((t) -> {
                t.onKeyReleased(e);
            });
        });
            
        if (tvs.size() > 0) {
            Button button = new Button("2d/3d");
            button.setOnAction(e -> {
                TerrainView tv = tvs.get(this.current_terrain_view);
                tv.setActive(false);
                
                this.current_terrain_view++;
                if (this.current_terrain_view >= tvs.size()) {
                    this.current_terrain_view = 0;
                }
                
                tv = tvs.get(this.current_terrain_view);
                tv.getRootNode().toFront();
                tv.setActive(true);
            });
            root.setLeft(button);
        }
        
        tvs.get(this.current_terrain_view).getRootNode().toFront();
        tvs.get(this.current_terrain_view).setActive(true);
        */
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
