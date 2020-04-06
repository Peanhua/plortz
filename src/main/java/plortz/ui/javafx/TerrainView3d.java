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
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import plortz.util.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainView3d extends TerrainView {
    private MeshView      terrain_mesh;
    private SubScene      scene3d;
    // The terrain size for which the current mesh is constructed for.
    private int           terrain_mesh_width;
    private int           terrain_mesh_length;
    private WritableImage terrain_mesh_texture;
    
    private final Vector  mouse_oldpos;
    private final Rotate  rotate_x;
    private final Rotate  rotate_y;
    private double        mouse_rotate_speed;
    
    public TerrainView3d(UserInterface ui) {
        super(ui);
        this.terrain_mesh         = null;
        this.scene3d              = null;
        this.terrain_mesh_width   = 0;
        this.terrain_mesh_length  = 0;
        this.terrain_mesh_texture = null;
        this.mouse_oldpos         = new Vector(0, 0);
        this.rotate_x             = new Rotate(-30, Rotate.X_AXIS);
        this.rotate_y             = new Rotate(0, Rotate.Y_AXIS);
        this.mouse_rotate_speed   = 0.2;
    }
    
    @Override
    public Node createUserInterface() {
        super.createUserInterface();
        
        this.terrain_mesh = new MeshView();
        this.terrain_mesh.setCullFace(CullFace.NONE);
        this.container.getChildren().add(this.terrain_mesh);
        
        AmbientLight light = new AmbientLight(Color.YELLOW);
        //light.setTranslateZ(1);

        Box box = new Box(0.5, 0.5, 0.5);
        box.setMaterial(new PhongMaterial(Color.WHITE));
        
        PhongMaterial m = new PhongMaterial(Color.RED);
        this.terrain_mesh.setMaterial(m);
        light.getScope().add(this.terrain_mesh);
        
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(500);
        camera.getTransforms().addAll(this.rotate_x, this.rotate_y, new Translate(0, -10, -100));

        Group root3D = new Group(camera, box, this.terrain_mesh);
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
    

    @Override
    protected void onResized() {
        super.onResized();
        this.scene3d.setWidth(this.width);
        this.scene3d.setHeight(this.height);
        this.refresh();
    }


    @Override
    public void refresh() {
        Terrain terrain = this.user_interface.getTerrain();
        if (!this.active || this.width == 0 || terrain == null) {
            return;
        }
        if (terrain.getWidth() != this.terrain_mesh_width || terrain.getLength() != this.terrain_mesh_length) {
            this.terrain_mesh_width = terrain.getWidth();
            this.terrain_mesh_length = terrain.getLength();
            this.terrain_mesh.setMesh(this.generateMesh(this.user_interface.getTerrain()));
            PhongMaterial m = new PhongMaterial();
            this.terrain_mesh_texture = this.getImage(this.user_interface.getTerrain());
            m.setDiffuseMap(this.terrain_mesh_texture);
            this.terrain_mesh.setMaterial(m);
        } else {
            this.updateMesh(terrain);
        }
    }
    
    
    private TriangleMesh generateMesh(Terrain terrain) {
        TriangleMesh mesh = new TriangleMesh();
        float offsetx = -terrain.getWidth() / 2;
        float offsety = -terrain.getLength() / 2;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                mesh.getPoints().addAll(offsetx + x, (float) -t.getAltitude(true), offsety + y);
                mesh.getTexCoords().addAll((float) x / (float) terrain.getWidth(), (float) y / (float) terrain.getLength());
            }
        }
        for (int y = 0; y < terrain.getLength() - 1; y++) {
            for (int x = 0; x < terrain.getWidth() - 1; x++) {
                /*
                * Generate a quad from two triangles:
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
                int ta = x + y * terrain.getWidth();
                int tb = ta + 1;
                int tc = ta + terrain.getWidth();
                int td = tc + 1;
                mesh.getFaces().addAll(c, tc, a, ta, b, tb);
                mesh.getFaces().addAll(b, tb, d, td, c, tc);
            }
        }
        return mesh;
    }
    
    private void updateMesh(Terrain terrain) {
        TriangleMesh mesh = (TriangleMesh) this.terrain_mesh.getMesh();
        // Update the y coordinates of the points (x and z remain the same):
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                int pos = 3 * (x + y * terrain.getWidth());
                mesh.getPoints().set(pos + 1, (float) -t.getAltitude(true));
            }
        }
        // Update the texture:
        PixelWriter pw = this.terrain_mesh_texture.getPixelWriter();
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile tile = terrain.getTile(x, y);
                pw.setArgb(x, y, this.getTileARGB(tile, 1.0));
            }
        }
    }
    
    private WritableImage getImage(Terrain terrain) {
        WritableImage image = new WritableImage(terrain.getWidth(), terrain.getLength());

        PixelWriter pw = image.getPixelWriter();

        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile tile = terrain.getTile(x, y);
                pw.setArgb(x, y, this.getTileARGB(tile, 1.0));
            }
        }
        return image;
    }
}
