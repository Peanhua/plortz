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
package plortz.util;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Camera {
    private Vector3f position;
    private Vector3f up;
    private float    yaw;
    private float    pitch;
    
    public Camera() {
        this.position = new Vector3f();
        this.up       = new Vector3f(0, 0, 1);
        this.yaw      = 0;
        this.pitch    = 0;
    }
    
    public void moveForward(float amount) {
        var forward = this.getForwardVector();
        this.position.add(forward.mul(amount));
    }
    
    public void moveRight(float amount) {
        var right = this.getRightVector();
        this.position.add(right.mul(amount));
    }
    
    public void rotateYaw(float amount) {
        this.yaw = this.clampAngle(this.yaw + amount);
    }
    
    public void rotatePitch(float amount) {
        this.pitch = this.clampAngle(this.pitch + amount);
    }
    
    public Vector3f getPosition() {
        return new Vector3f(this.position);
    }
    
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    
    public Vector3f getForwardVector() {
        var q = this.getRotation();
        var fv = new Vector3f(0, 1, 0);
        fv.rotate(q);
        return fv.normalize();
    }
    
    public Vector3f getRightVector() {
        var rv = new Vector3f(0, 1, 0);
        rv.rotateZ((float) Math.toRadians(-90)).normalize();
        var q = this.getRotation();
        rv.rotate(q);
        return rv.normalize();
    }
    
    public Quaternionf getRotation() {
        var q = new Quaternionf();
        q.identity();
        q.rotateLocalX((float) Math.toRadians(this.pitch));
        q.rotateLocalZ((float) Math.toRadians(-this.yaw));
        return q;
    }
    
    public Matrix4f getTransformMatrix() {
        var m = new Matrix4f();
        m.lookAt(this.getPosition(), this.getPosition().add(this.getForwardVector()), this.up);
        return m;
    }
    
    private float clampAngle(float angle) {
        while (angle <= -360.0) {
            angle += 360.0;
        }
        while (angle >= 360.0) {
            angle -= 360.0;
        }
        return angle;
    }
}
