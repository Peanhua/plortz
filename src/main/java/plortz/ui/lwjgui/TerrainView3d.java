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
import lwjgui.event.KeyEvent;
import lwjgui.event.MouseEvent;
import lwjgui.event.ScrollEvent;
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

/**
 * Widget showing the current terrain in 3d.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */

public class TerrainView3d extends Widget implements Renderer {
    // The user interface this widget is part of:
    private UserInterface user_interface;

    // The terrain size for which the current mesh is constructed for:
    private int vertex_count;
    private boolean dirty;
    
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
    private int      camera_movement_speed;
    private int      moving_forward;
    private int      moving_right;
    
    public TerrainView3d(UserInterface ui) {
        this.user_interface = ui;

        this.shader = new Shader("plortz/shaders/TerrainView3d");
        this.vbo = glGenBuffers();
        this.vao = glGenVertexArrays();

        this.model = new Matrix4f();
        this.model.identity();
        this.proj = new Matrix4f();
        this.proj.setPerspective((float) Math.toRadians(30.0f), (float) 1024 / 768, 0.1f, 2000.0f);
        this.camera = new Camera();
        this.camera.setPosition(0, -10, 4);
        this.camera_controls       = false;
        this.camera_rotate_speed   = 0.15;
        this.camera_movement_speed = 3;
        this.mouse_oldpos          = new Vector(0, 0);
        this.moving_forward        = 0;
        this.moving_right          = 0;
        
        ui.listenOnTerrainChange(() -> {
            this.dirty = true;
            this.user_interface.getTerrain().listenOnChange(() -> {
                this.dirty = true;
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
        pane.setOnMouseScrolled((event) -> this.onMouseScrolled(event));
        
        return pane;
    }

    @Override
    public void refresh() {
        this.dirty = true;
    }
    
    
    private void updateGeometry() {
        if (!this.dirty) {
            return;
        }
        this.dirty = false;

        Terrain terrain = this.user_interface.getTerrain();
        if (terrain == null) {
            return;
        }
        Vector minmax = terrain.getAltitudeRange();
        float offsetx = -terrain.getWidth() / 2;
        float offsety = -terrain.getLength() / 2;
        var points = new ArrayList<Float>();
        var colors = new ArrayList<Vector>();
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = terrain.getTile(x, y);
                points.add(offsetx + x);
                points.add(offsety + y);
                points.add((float) (t.getAltitude(true) - minmax.getX()));
                colors.add(this.getTileColor(t));
            }
        }
        var normals = new ArrayList<Vector3f>();
        for (int y = 0; y < terrain.getLength(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if (y == terrain.getLength() - 1 || x == terrain.getWidth() - 1) {
                    normals.add(new Vector3f(0, 0, 1));
                    continue;
                }
                int a = x + y * terrain.getWidth();
                int b = a + 1;
                int c = a + terrain.getWidth();
                int d = c + 1;
                
                var v1 = new Vector3f(0, 0, points.get(a * 3 + 2));
                var v2 = new Vector3f(0, 1, points.get(c * 3 + 2));
                var v3 = new Vector3f(1, 0, points.get(b * 3 + 2));
                normals.add(this.calculateNormal(v1, v2, v3));
            }
        }
        
        this.vertex_count = (terrain.getWidth() - 1) * (terrain.getLength() - 1) * 2 * 3;
                 
        int vertSize = 3;
        int colorSize = 4;
        int normalSize = 3;
        int size = vertSize + colorSize + normalSize; // Stride length
        int bytes = Float.BYTES; // Bytes per element (float)

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
                // The triangles:
                this.addPoint(buffer, points, colors.get(a), normals.get(a), a);
                this.addPoint(buffer, points, colors.get(b), normals.get(b), b);
                this.addPoint(buffer, points, colors.get(c), normals.get(c), c);

                this.addPoint(buffer, points, colors.get(b), normals.get(b), b);
                this.addPoint(buffer, points, colors.get(d), normals.get(d), d);
                this.addPoint(buffer, points, colors.get(c), normals.get(c), c);
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
        Vector3f ca = new Vector3f(c).sub(a);
        Vector3f ba = new Vector3f(b).sub(a);
        return ca.cross(ba).normalize();
    }
    
    private void addPoint(FloatBuffer buffer, List<Float> points, Vector color, Vector3f normal, int pos) {
        buffer.put(points.get(pos * 3 + 0)).put(points.get(pos * 3 + 1)).put(points.get(pos * 3 + 2));
        buffer.put((float) color.getX()).put((float) color.getY()).put((float) color.getZ()).put(1.0f);
        buffer.put(normal.x).put(normal.y).put(normal.z);
    }
    
    @Override
    public void render(Context context, int width, int height) {
        this.updateGeometry();
        
        float factor = (float) this.camera_movement_speed / 3.0f;
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
    
    /**
     * Return the color of the given tile.
     * 
     * @param tile The tile.
     * @return     The color.
     */
    protected Vector getTileColor(Tile tile) {
        Vector rgb;
        if (tile.getWater() > 0.0) {
            rgb = new Vector(0, 0, 1);
        } else {
            rgb = tile.getTopSoil().getRGB();
        }
        return rgb;
    }

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

    public void onMouseScrolled(ScrollEvent event) {
        boolean changed = false;
        if (event.y < 0.0) {
            this.camera_movement_speed--;
            if (this.camera_movement_speed < 1) {
                this.camera_movement_speed = 1;
            } else {
                changed = true;
            }
        } else if (event.y > 0.0) {
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
    }
}
