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

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import plortz.Vector;
import plortz.terrain.SoilLayer;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainView3d extends Widget {
    private UserInterface   ui;
    private BorderPane      container;
    private int             width;
    private int             height;
    
    private MeshView[]      terrain_meshes;
    private SubScene        scene3d;
    
    private final Vector    mouse_oldpos;
    private final Rotate    rotate_x;
    private final Rotate    rotate_y;
    private double          mouse_rotate_speed;
    
    public TerrainView3d(UserInterface ui) {
        this.ui               = ui;
        this.container        = null;
        this.width            = 0;
        this.height           = 0;
        this.terrain_meshes   = new MeshView[1]; //SoilLayer.Type.values().length];
        this.mouse_oldpos     = new Vector(0, 0);
        this.rotate_x         = new Rotate(-30, Rotate.X_AXIS);
        this.rotate_y         = new Rotate(0, Rotate.Y_AXIS);
        this.mouse_rotate_speed = 0.2;
        
        ui.listenOnTerrainChange(() -> {
            this.refresh();
            this.ui.getTerrain().listenOnChange(() -> {
                this.refresh();
            });
        });
    }
    
    @Override
    public Node createUserInterface() {
        this.container = new BorderPane();
        this.container.setMinWidth(0);
        this.container.setMinHeight(0);
        this.container.setMaxWidth(Double.MAX_VALUE);
        this.container.setMaxHeight(Double.MAX_VALUE);

        this.width  = 0;
        this.height = 0;
        
        this.container.widthProperty().addListener((o) -> this.onResized());
        this.container.heightProperty().addListener((o) -> this.onResized());
        
        for (int i = 0; i < this.terrain_meshes.length; i++) {
            this.terrain_meshes[i] = new MeshView();
            this.terrain_meshes[i].setCullFace(CullFace.NONE);
        }
        
        //this.terrain_mesh.setMesh(trapezoid);
        this.container.getChildren().addAll(this.terrain_meshes);
        
        AmbientLight light = new AmbientLight(Color.YELLOW);
        //light.setTranslateZ(1);

        Box box = new Box(0.5, 0.5, 0.5);
        box.setMaterial(new PhongMaterial(Color.WHITE));
        
        PhongMaterial m = new PhongMaterial(Color.RED);
        this.terrain_meshes[0].setMaterial(m);
        light.getScope().add(this.terrain_meshes[0]);
        
        /*
        this.terrain_meshes[1].setMaterial(new PhongMaterial(Color.GREEN));
        this.terrain_meshes[2].setMaterial(new PhongMaterial(Color.BLUE));
        */
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(500);
        camera.getTransforms().addAll(this.rotate_x, this.rotate_y, new Translate(0, 0, -100));

        Group root3D = new Group(camera, box, this.terrain_meshes[0]); //, this.terrain_meshes[1], this.terrain_meshes[2]);
        scene3d = new SubScene(root3D, 1, 1, true, SceneAntialiasing.BALANCED);
        scene3d.setFill(Color.BLACK);
        scene3d.setCamera(camera);
        
        this.container.setCenter(scene3d);

        scene3d.setOnMousePressed((e) -> {
            this.mouse_oldpos.setX(e.getSceneX());
            this.mouse_oldpos.setY(e.getSceneY());
        });
        scene3d.setOnMouseDragged((e) -> {
            Vector mouse_pos = new Vector(e.getSceneX(), e.getSceneY());
            rotate_x.setAngle(rotate_x.getAngle() - this.mouse_rotate_speed * (mouse_pos.getY() - this.mouse_oldpos.getY()));
            rotate_y.setAngle(rotate_y.getAngle() + this.mouse_rotate_speed * (mouse_pos.getX() - this.mouse_oldpos.getX()));
            this.mouse_oldpos.set(mouse_pos);
        });
        return this.container;
    }
    


    private void onResized() {
        this.width  = (int) this.container.getWidth();
        this.height = (int) this.container.getHeight();

        this.scene3d.setWidth(this.width);
        this.scene3d.setHeight(this.height);
        
        this.refresh();
    }


    @Override
    public void refresh() {
        if (this.width == 0 || this.ui.getTerrain() == null) {
            return;
        }
        for (int i = 0; i < this.terrain_meshes.length; i++) {
            this.terrain_meshes[i].setMesh(this.generateMesh(SoilLayer.Type.values()[i], this.ui.getTerrain()));
        }
    }
    
    
    private TriangleMesh generateMesh(SoilLayer.Type soil_type, Terrain terrain) {
        TriangleMesh mesh = new TriangleMesh();

        float offsetx = -terrain.getWidth() / 2;
        float offsety = -terrain.getLength() / 2;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                mesh.getPoints().addAll(offsetx + x, (float) -t.getAltitude(true), offsety + y);
            }
        }

        mesh.getTexCoords().addAll(0, 0);
        
        for (int y = 0; y < terrain.getLength() - 1; y++) {
            for (int x = 0; x < terrain.getWidth() - 1; x++) {
                /*
                * AB
                * CD
                */
                int a = x + y * terrain.getWidth();
                int b = a + 1;
                int c = a + terrain.getWidth();
                int d = c + 1;
                /*
                mesh.getFaces().addAll(c, 0, b, 0, a, 0);
                mesh.getFaces().addAll(c, 0, d, 0, b, 0);
                */
                mesh.getFaces().addAll(c, 0, a, 0, b, 0);
                mesh.getFaces().addAll(b, 0, d, 0, c, 0);
            }
        }
        
        return mesh;
    }
}
