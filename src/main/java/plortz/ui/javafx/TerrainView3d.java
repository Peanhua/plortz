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
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import plortz.util.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;

/**
 * A widget showing the current terrain in 3d.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class TerrainView3d extends TerrainView {
    private MeshView      terrain_mesh;
    private SubScene      scene3d;
    // The terrain size for which the current mesh is constructed for:
    private int           terrain_mesh_width;
    private int           terrain_mesh_length;
    private WritableImage terrain_mesh_texture;
    // Camera controls:
    private boolean         camera_controls;
    private final Vector    mouse_oldpos;
    private double          camera_rotate_speed;
    private int             camera_movement_speed;
    private ControlledPerspectiveCamera camera;
    
    public TerrainView3d(UserInterface ui) {
        super(ui);
        this.terrain_mesh         = null;
        this.scene3d              = null;
        this.terrain_mesh_width   = 0;
        this.terrain_mesh_length  = 0;
        this.terrain_mesh_texture = null;
        this.camera_controls      = false;
        this.mouse_oldpos         = new Vector(0, 0);
        this.camera_rotate_speed   = 0.15;
        this.camera_movement_speed = 3;
    }
    
    @Override
    protected Node createUserInterface() {
        super.createUserInterface();
        
        this.terrain_mesh = new MeshView();
        this.container.getChildren().add(this.terrain_mesh);
        
        //AmbientLight light = new AmbientLight(Color.YELLOW);
        //light.setTranslateZ(1);

        Box box = new Box(0.5, 0.5, 0.5);
        box.setMaterial(new PhongMaterial(Color.WHITE));
        
        PhongMaterial m = new PhongMaterial(Color.RED);
        this.terrain_mesh.setMaterial(m);
        //light.getScope().add(this.terrain_mesh);
        
        this.camera = new ControlledPerspectiveCamera();
        this.camera.setPosition(new Vector(0, -20, -100));
        this.camera.rotatePitch(-30);
        this.camera.setFarClip(1000);
        
        Group root3D = new Group(this.camera, box, this.terrain_mesh);
        scene3d = new SubScene(root3D, 1, 1, true, SceneAntialiasing.DISABLED);
        scene3d.setFill(Color.BLACK);
        scene3d.setCamera(this.camera);
        
        this.container.setCenter(scene3d);

        scene3d.setOnMousePressed((e) -> {
            this.camera_controls = true;
            this.mouse_oldpos.setX(e.getSceneX());
            this.mouse_oldpos.setY(e.getSceneY());
            scene3d.setCursor(Cursor.NONE);
        });
        scene3d.setOnMouseReleased((e) -> {
            this.camera_controls = false;
            scene3d.setCursor(Cursor.DEFAULT);
        });
        scene3d.setOnMouseDragged((e) -> {
            Vector mouse_pos = new Vector(e.getSceneX(), e.getSceneY());
            this.camera.rotatePitch(-this.camera_rotate_speed * (mouse_pos.getY() - this.mouse_oldpos.getY()));
            this.camera.rotateYaw(this.camera_rotate_speed * (mouse_pos.getX() - this.mouse_oldpos.getX()));
            this.mouse_oldpos.set(mouse_pos);
        });
        scene3d.setOnScroll((e) -> {
            boolean changed = false;
            if (e.getDeltaY() < 0.0) {
                this.camera_movement_speed--;
                if (this.camera_movement_speed < 1) {
                    this.camera_movement_speed = 1;
                } else {
                    changed = true;
                }
            } else if (e.getDeltaY() > 0.0) {
                this.camera_movement_speed++;
                if (this.camera_movement_speed > 10) {
                    this.camera_movement_speed = 10;
                } else {
                    changed = true;
                }
            }
            if (changed) {
                this.user_interface.showMessage("Movement speed: " + this.camera_movement_speed);
            }
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
            Vector pos = this.camera.getPosition();
            Vector minmax = this.user_interface.getTerrain().getAltitudeRange();
            pos.setY(-(minmax.getY() + 1.0));
            this.camera.setPosition(pos);
        } else {
            this.updateMesh(terrain);
        }
    }
    
    
    private TriangleMesh generateMesh(Terrain terrain) {
        TriangleMesh mesh = new TriangleMesh();
        Vector minmax = terrain.getAltitudeRange();
        float offsetx = -terrain.getWidth() / 2;
        float offsety = -terrain.getLength() / 2;
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                mesh.getPoints().addAll(offsetx + x, (float) -(t.getAltitude(true) - minmax.getX()), offsety + y);
                mesh.getTexCoords().addAll(((float) x + 0.5f) / (float) terrain.getWidth(), ((float) y + 0.5f) / (float) terrain.getLength());
            }
        }
        for (int y = 0; y < terrain.getLength() - 1; y++) {
            for (int x = 0; x < terrain.getWidth() - 1; x++) {
                /*
                * Generate a quad from two triangles:
                * AB
                * CD
                */
                // Point indices:
                int a = x + y * terrain.getWidth();
                int b = a + 1;
                int c = a + terrain.getWidth();
                int d = c + 1;
                // Texture indices:
                int ta = x + y * terrain.getWidth();
                int tb = ta + 1;
                int tc = ta + terrain.getWidth();
                int td = tc + 1;
                // The triangles:
                mesh.getFaces().addAll(c, tc, a, ta, b, tb);
                mesh.getFaces().addAll(b, tb, d, td, c, tc);
            }
        }
        return mesh;
    }
    
    private void updateMesh(Terrain terrain) {
        TriangleMesh mesh = (TriangleMesh) this.terrain_mesh.getMesh();
        Vector minmax = terrain.getAltitudeRange();
        // Update the y coordinates of the points (x and z remain the same):
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                int pos = 3 * (x + y * terrain.getWidth());
                mesh.getPoints().set(pos + 1, (float) -(t.getAltitude(true) - minmax.getX()));
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

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (!this.camera_controls) {
            return;
        }
        double factor = (double) this.camera_movement_speed / 5.0;
        switch (event.getCode()) {
            case W:
                this.camera.moveForward(3.0 * factor);
                break;
            case A:
                this.camera.moveRight(-1.0 * factor);
                break;
            case S:
                this.camera.moveForward(-3.0 * factor);
                break;
            case D:
                this.camera.moveRight(1.0 * factor);
                break;
        }
    }
}
