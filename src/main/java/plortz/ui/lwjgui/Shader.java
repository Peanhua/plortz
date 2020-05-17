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
import java.nio.ByteBuffer;
import java.util.Scanner;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import org.lwjgl.opengl.GL13;
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
    private final float[] matrix_buf;
    private int texId;
    
    public Shader(String name) {
        this.matrix_buf = new float[4 * 4];
        this.loadShaderProgram(name);
        this.checkError();
        this.getLocations();
        this.checkError();
		// Generic white texture
		texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		int wid = 1;
		int hei = 1;
		ByteBuffer data = BufferUtils.createByteBuffer(wid*hei*4);
		while(data.hasRemaining()) {
			data.put((byte) (255 & 0xff));
		}
		data.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, wid, hei, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        this.checkError();
    }
    
    public void bind() {
        glUseProgram(this.shader_program_id);
        this.checkError();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
        this.checkError();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        this.checkError();
    }
    
    public void setModelMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(model_matrix_location, false, this.matrix_buf);
        this.checkError();
    }
    public void setViewMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(view_matrix_location, false, this.matrix_buf);
        this.checkError();
    }

    public void setProjectionMatrix(Matrix4f mat) {
        mat.get(this.matrix_buf);
        glUniformMatrix4fv(projection_matrix_location, false, this.matrix_buf);
        this.checkError();
    }


    private void checkError() {
        var error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("GL error: " + error);
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
        String source = this.loadStringResource(resource_name);

        int s = glCreateShader(type);
        glShaderSource(s, source);
        glCompileShader(s);
        var success = glGetShaderi(s, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Failed to compile shader '" + resource_name + "': " + glGetShaderInfoLog(s, 9999));
            glDeleteShader(s);
            return -1;
        }
        System.out.println(source);
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
        
        glBindAttribLocation(this.shader_program_id, 0, "inPos");
        glBindAttribLocation(this.shader_program_id, 1, "inTexCoord");
        
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
        this.projection_matrix_location = this.getLocation("projectionMatrix");
        this.view_matrix_location       = this.getLocation("viewMatrix");
        this.model_matrix_location      = this.getLocation("worldMatrix");
    }
    
    private int getLocation(String name) {
        var loc = glGetUniformLocation(this.shader_program_id, name);
        if (loc < 0) {
            System.out.println("Failed to get location for '" + name + "'.");
        }
        return loc;
    }
}
