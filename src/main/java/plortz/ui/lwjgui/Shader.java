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

import java.net.URL;
import java.util.Scanner;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Shader {
    private int shader_program_id;
    private int vertex_shader_id;
    private int fragment_shader_id;
    private int model_matrix_location;
    private int view_matrix_location;
    private int projection_matrix_location;
    private int normal_matrix_location;
    private int sun_position_location;
    private final float[] matrix_buf;
    private final float[] sun_position;
    
    public Shader(String name) {
        this.matrix_buf = new float[4 * 4];
        this.sun_position = new float[3];
        this.loadShaderProgram(name);
        this.getLocations();
        this.bind();
        this.setSunPosition(1000, 0, 1000);
    }
    
    public final void bind() {
        glUseProgram(this.shader_program_id);
        this.checkError("glUseProgram");
    }
    
    private void setSunPosition(float x, float y, float z) {
        this.sun_position[0] = x;
        this.sun_position[1] = y;
        this.sun_position[2] = z;
        glUniform3fv(this.sun_position_location, this.sun_position);
    }
    
    public void setMVP(Matrix4f model, Matrix4f view, Matrix4f projection) {
        this.setModelMatrix(model);
        this.setViewMatrix(view);
        this.setProjectionMatrix(projection);
        Matrix4f n = new Matrix4f(view);
        n.mul(model);
        n.invert();
        n.transpose();
        this.setNormalMatrix(n);
        this.checkError("setMVP");
    }
    
    private void setModelMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(model_matrix_location, false, this.matrix_buf);
        this.checkError("setModelMatrix");
    }
    
    private void setViewMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(view_matrix_location, false, this.matrix_buf);
        this.checkError("setViewMatrix");
    }

    private void setProjectionMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(projection_matrix_location, false, this.matrix_buf);
        this.checkError("setProjectionMatrix");
    }
    
    private void setNormalMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(normal_matrix_location, false, this.matrix_buf);
        this.checkError("setNormalMatrix");
    }


    private void checkError(String func) {
        var error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("GL error at " + func + ": " + error);
        }
    }
    
    private String loadStringResource(String name) {
        URL url = this.getClass().getClassLoader().getResource(name);
        try {
            var in = url.openStream();
            var reader = new Scanner(in);
            var sb = new StringBuilder();
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
                sb.append("\n");
            }
            return sb.toString();
            
        } catch (Exception e) {
            System.out.println("Failed to load resource '" + name + "': " + e.getMessage());
            return null;
        }
    }
    
    private int loadShader(String resource_name, int type) {
        //System.out.println("Loading: " + resource_name);
        String source = this.loadStringResource(resource_name);
        //System.out.println(source);
        int s = glCreateShader(type);
        glShaderSource(s, source);
        glCompileShader(s);
        var success = glGetShaderi(s, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Failed to compile shader '" + resource_name + "': " + glGetShaderInfoLog(s, 9999));
            glDeleteShader(s);
            return -1;
        }
        return s;
    }
    
    private void loadShaderProgram(String name) {
        this.vertex_shader_id = this.loadShader(name + ".vert", GL_VERTEX_SHADER);
        this.fragment_shader_id = this.loadShader(name + ".frag", GL_FRAGMENT_SHADER);
        if (this.vertex_shader_id == -1 || this.fragment_shader_id == -1) {
            return;
        }
        this.shader_program_id = glCreateProgram();
        glAttachShader(this.shader_program_id, this.vertex_shader_id);
        glAttachShader(this.shader_program_id, this.fragment_shader_id);
        
        glBindAttribLocation(this.shader_program_id, 0, "in_position");
        glBindAttribLocation(this.shader_program_id, 1, "in_color");
        glBindAttribLocation(this.shader_program_id, 2, "in_normal");
        
        glLinkProgram(this.shader_program_id);
        var success = glGetProgrami(this.shader_program_id, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Failed to link shader program '" + name + "': " + glGetProgramInfoLog(this.shader_program_id, 9999));
            glDeleteProgram(this.shader_program_id);
            glDeleteShader(this.vertex_shader_id);
            glDeleteShader(this.fragment_shader_id);
            this.shader_program_id  = -1;
            this.vertex_shader_id   = -1;
            this.fragment_shader_id = -1;
        }
    }
    
    private void getLocations() {
        this.model_matrix_location      = this.getLocation("model_matrix");
        this.view_matrix_location       = this.getLocation("view_matrix");
        this.projection_matrix_location = this.getLocation("projection_matrix");
        this.normal_matrix_location     = this.getLocation("normal_matrix");
        this.sun_position_location      = this.getLocation("sun_position");
    }
    
    private int getLocation(String name) {
        var loc = glGetUniformLocation(this.shader_program_id, name);
        this.checkError("glGetUniformLocation");
        if (loc < 0) {
            System.out.println("Failed to get location for '" + name + "'.");
        }
        return loc;
    }
}
