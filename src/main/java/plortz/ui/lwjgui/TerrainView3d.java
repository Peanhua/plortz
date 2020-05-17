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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lwjgui.event.KeyEvent;
import lwjgui.event.MouseEvent;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import lwjgui.gl.Renderer;
import lwjgui.scene.Context;
import lwjgui.scene.Node;
import lwjgui.scene.layout.OpenGLPane;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import plortz.util.Vector;
import plortz.terrain.Terrain;
import plortz.terrain.Tile;
import plortz.ui.UserInterface;
import plortz.util.Camera;
import plortz.util.MersenneTwister;

/**
 * Widget showing the current terrain in 3d.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */

public class TerrainView3d extends Widget implements Renderer {
    // The user interface this widget is part of:
    private UserInterface user_interface;

    // The terrain size for which the current mesh is constructed for:
    private int terrain_mesh_width;
    private int terrain_mesh_length;
    private int vertex_count;
    
    // OpenGL objects:
    private Shader shader;
    private int vao;
    private int vbo;
    
    // Camera:
    private Matrix4f model;
    private Matrix4f proj;
    private Camera   camera;
    private boolean  camera_controls;
    private Vector   mouse_oldpos;
    private double   camera_rotate_speed;
    private int      moving_forward;
    private int      moving_right;
    
    public TerrainView3d(UserInterface ui) {
        this.user_interface = ui;
        this.terrain_mesh_width  = 0;
        this.terrain_mesh_length = 0;

        this.shader = new Shader("plortz/shaders/TerrainView3d");
        this.vbo = glGenBuffers();
        this.vao = glGenVertexArrays();

        this.model = new Matrix4f();
        this.model.identity();
        this.proj = new Matrix4f();
        this.proj.setPerspective((float) Math.toRadians(30.0f), (float) 1024 / 768, 0.1f, 2000.0f);
        this.camera = new Camera();
        this.camera.setPosition(0, -10, 4);
        this.camera_controls     = false;
        this.camera_rotate_speed = 0.15;
        this.mouse_oldpos        = new Vector(0, 0);
        this.moving_forward = 0;
        this.moving_right   = 0;
        
        ui.listenOnTerrainChange(() -> {
            this.updateGeometry();
            this.user_interface.getTerrain().listenOnChange(() -> {
                this.updateGeometry();
            });
        });

        this.updateGeometry();
    }

    @Override
    protected Node createUserInterface() {
        OpenGLPane pane = new OpenGLPane();
        pane.setRendererCallback(this);
        pane.setFillToParentWidth(true);
        pane.setFillToParentHeight(true);

        pane.setOnKeyPressed((event)    -> this.onKeyPressed(event));
        pane.setOnKeyReleased((event)   -> this.onKeyReleased(event));
        pane.setOnMousePressed((event)  -> this.onMousePressed(event));
        pane.setOnMouseReleased((event) -> this.onMouseReleased(event));
        pane.setOnMouseDragged((event)  -> this.onMouseDragged(event));

        return pane;
    }

    @Override
    public void refresh() {
    }
    
    
    private void updateGeometry() {
        
        Terrain terrain = this.user_interface.getTerrain();
        if (terrain == null) {
            return;
        }
        Vector minmax = terrain.getAltitudeRange();
        float offsetx = -terrain.getWidth() / 2;
        float offsety = -terrain.getLength() / 2;
        var points = new ArrayList<Float>();
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                points.add(offsetx + x);
                points.add(offsety + y);
                points.add((float) (t.getAltitude(true) - minmax.getX()));
            }
        }
        
        this.vertex_count = (terrain.getWidth() - 1) * (terrain.getLength() - 1) * 2 * 3;
                 
        int vertSize = 3;
        int colorSize = 4;
        int normalSize = 3;
        int size = vertSize + colorSize + normalSize; // Stride length
        int bytes = Float.BYTES; // Bytes per element (float)

        Random r = new MersenneTwister(0);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertex_count * size);
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
                // Normal:
                var n = this.calculateNormal(
                        new Vector3f(0, 0, points.get(a * 3 + 2)),
                        new Vector3f(0, 1, points.get(c * 3 + 2)),
                        new Vector3f(1, 0, points.get(b * 3 + 2))
                );
                var color = new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat());
                // The triangles:
                if (false) {
                this.addPoint(buffer, points, color, n, a);
                this.addPoint(buffer, points, color, n, c);
                this.addPoint(buffer, points, color, n, b);

                this.addPoint(buffer, points, color, n, c);
                this.addPoint(buffer, points, color, n, d);
                this.addPoint(buffer, points, color, n, b);
                } else {
                this.addPoint(buffer, points, color, n, a);
                this.addPoint(buffer, points, color, n, b);
                this.addPoint(buffer, points, color, n, c);

                this.addPoint(buffer, points, color, n, b);
                this.addPoint(buffer, points, color, n, d);
                this.addPoint(buffer, points, color, n, c);
                }
            }
        }
        buffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindVertexArray(this.vao);
        glEnableVertexAttribArray(0); // layout 0 shader
        glEnableVertexAttribArray(1); // layout 1 shader
        glEnableVertexAttribArray(2); // layout 2 shader
        int offset = 0;
        glVertexAttribPointer(0, vertSize,  GL_FLOAT, false, size * bytes, offset);
        offset += vertSize * bytes;
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, size * bytes, offset);
        offset += colorSize * bytes;
        glVertexAttribPointer(2, normalSize, GL_FLOAT, false, size * bytes, offset);
        offset += normalSize * bytes;

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    private Vector3f calculateNormal(Vector3f a, Vector3f b, Vector3f c) {
        Vector3f cMa = new Vector3f(c).sub(a);
        Vector3f bMa = new Vector3f(b).sub(a);
        return cMa.cross(bMa).normalize();
    }
    
    private void addPoint(FloatBuffer buffer, List<Float> points, Vector3f color, Vector3f normal, int pos) {
        buffer.put(points.get(pos * 3 + 0)).put(points.get(pos * 3 + 1)).put(points.get(pos * 3 + 2));
        buffer.put(color.x).put(color.y).put(color.z).put(1.0f);
        buffer.put(normal.x).put(normal.y).put(normal.z);
    }
    
    @Override
    public void render(Context context, int width, int height) {
        float factor = 1.0f / 2.0f;
        if (this.moving_forward != 0) {
            this.camera.moveForward(3.0f * factor * (float) this.moving_forward);
        }
        if (this.moving_right != 0) {
            this.camera.moveRight(1.0f * factor * (float) this.moving_right);
        }
        
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        var terrain = this.user_interface.getTerrain();
        if(terrain == null) {
            return;
        }
        
        this.shader.bind();
        this.shader.setMVP(this.model, this.camera.getTransformMatrix(), this.proj);

        glBindVertexArray(this.vao);
        glDrawArrays(GL_TRIANGLES, 0, this.vertex_count);
    }
    
    /*
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
/*
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
*/

    public void onKeyPressed(KeyEvent event) {
        //System.out.println("TerrainView3d.onKeyPressed(" + event.getKeyName() + "): consumed=" + event.isConsumed() + ", event=" + event);
        if (event.isConsumed()) {
            return;
        }
        if (!this.camera_controls) {
            return;
        }
        switch (event.key) {
            case GLFW.GLFW_KEY_W:
                this.moving_forward = 1;
                break;
            case GLFW.GLFW_KEY_A:
                this.moving_right = -1;
                break;
            case GLFW.GLFW_KEY_S:
                this.moving_forward = -1;
                break;
            case GLFW.GLFW_KEY_D:
                this.moving_right = 1;
                break;
        }
        event.consume();
    }

    public void onKeyReleased(KeyEvent event) {
        if (event.isConsumed()) {
            return;
        }
        if(!this.camera_controls) {
            return;
        }
        switch (event.key) {
            case GLFW.GLFW_KEY_W:
            case GLFW.GLFW_KEY_S:
                this.moving_forward = 0;
                break;
            case GLFW.GLFW_KEY_A:
            case GLFW.GLFW_KEY_D:
                this.moving_right = 0;
                break;
        }
        event.consume();
    }
    
    public void onMousePressed(MouseEvent event) {
        //System.out.println("TerrainView3d.onMousePressed()");
        this.getRootNode().getWindow().getContext().setSelected(this.getRootNode());
        this.camera_controls = true;
        this.mouse_oldpos.setX(event.mouseX);
        this.mouse_oldpos.setY(event.mouseY);
        event.consume();
    }

    public void onMouseReleased(MouseEvent event) {
        //System.out.println("TerrainView3d.onMouseReleased()");
        this.camera_controls = false;
        this.moving_forward = 0;
        this.moving_right = 0;
        event.consume();
    }
    
    public void onMouseDragged(MouseEvent event) {
        //System.out.println("TerrainView3d.onMouseDragged()");
        Vector mouse_pos = new Vector(event.mouseX, event.mouseY);
        this.camera.rotatePitch((float) (this.camera_rotate_speed * (mouse_pos.getY() - this.mouse_oldpos.getY())));
        this.camera.rotateYaw((float) (this.camera_rotate_speed * (mouse_pos.getX() - this.mouse_oldpos.getX())));
        this.mouse_oldpos.set(mouse_pos);
    }

}
