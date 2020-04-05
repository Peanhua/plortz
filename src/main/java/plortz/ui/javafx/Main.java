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
        if (Platform.isSupported(ConditionalFeature.SCENE3D)) {
            TerrainView tv2d = new TerrainView2d(my_ui);
            TerrainView tv3d = new TerrainView3d(my_ui);
            Node terrain_view2d = tv2d.createUserInterface();
            Node terrain_view3d = tv3d.createUserInterface();
            StackPane terrain_view_container = new StackPane();
            terrain_view_container.getChildren().addAll(terrain_view3d, terrain_view2d);
            root.setCenter(terrain_view_container);
            tv2d.setActive(true);

            Button button = new Button("3d");
            button.setOnAction(e -> {
                if (this.is2d) {
                    tv2d.setActive(false);
                    terrain_view3d.toFront();
                    tv3d.setActive(true);
                    button.setText("2d");
                } else {
                    tv3d.setActive(false);
                    terrain_view2d.toFront();
                    tv2d.setActive(true);
                    button.setText("3d");
                }
                this.is2d = !this.is2d;
            });
            root.setLeft(button);
        } else {
            TerrainView tv2d = new TerrainView2d(my_ui);
            root.setCenter(tv2d.createUserInterface());
            tv2d.setActive(true);
        }
        
        my_ui.showMessage("Welcome to Plortz.");
        my_ui.showMessage("Type \"help\" to get started.");
    }
    
    public static void run(UserInterface my_ui) {
        Main.my_ui = my_ui;
        launch();
    }
}
