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
package plortz.ui.javafx;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import plortz.ui.GraphicalUI;
import plortz.ui.UserInterface;

/**
 * The JavaFX application class responsible of setting up the JavaFX stuffs.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Main extends Application {
    
    private static UserInterface my_ui = null;
    private boolean is2d;
    
    @Override
    public void start(Stage stage) {
        if (my_ui == null) {
            my_ui = new GraphicalUI();
        }
        
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Plortz");
        stage.show();
        
        ToolBar toolbar = new ToolBar();
        root.setTop(toolbar);
        
        Widget console = new Console(my_ui);
        root.setBottom(console.createUserInterface());
        
        this.is2d = true;
        Node terrain_view2d = new TerrainView2d(my_ui).createUserInterface();
        Node terrain_view3d = new TerrainView3d(my_ui).createUserInterface();
        StackPane terrain_view = new StackPane();
        terrain_view.getChildren().addAll(terrain_view3d, terrain_view2d);
        root.setCenter(terrain_view);
        
        Button button = new Button("3d");
        button.setOnAction(e -> {
            if (this.is2d) {
                terrain_view3d.toFront();
                button.setText("2d");
            } else {
                terrain_view2d.toFront();
                button.setText("3d");
            }
            this.is2d = !this.is2d;
        });
        root.setLeft(button);
        
        my_ui.showMessage("Welcome to Plortz.");
        my_ui.showMessage("Type \"help\" to get started.");
        
        //System.out.println("3d supported? " + Platform.isSupported(ConditionalFeature.SCENE3D));
    }
    
    public static void run(UserInterface my_ui) {
        Main.my_ui = my_ui;
        launch();
    }
}
