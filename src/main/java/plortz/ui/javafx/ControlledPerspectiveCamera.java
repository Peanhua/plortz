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

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import plortz.util.Vector;

/**
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ControlledPerspectiveCamera extends PerspectiveCamera {
    
    private final Rotate rotate_x;
    private final Rotate rotate_y;
    
    public ControlledPerspectiveCamera() {
        super(true);
        this.rotate_x = new Rotate(0, Rotate.X_AXIS);
        this.rotate_y = new Rotate(0, Rotate.Y_AXIS);
        this.getTransforms().addAll(this.rotate_x, this.rotate_y);
    }
    
    /**
     * Constructor, making sure the fixedEyeAtCameraZero is always set to true.
     * 
     * @param fixedEyeAtCameraZero Ignored.
     */
    public ControlledPerspectiveCamera(boolean fixedEyeAtCameraZero) {
        this();
    }
    
    public Vector getPosition() {
        return new Vector(
                this.getTranslateX(),
                this.getTranslateY(),
                this.getTranslateZ()
        );
    }
    
    public void setPosition(Vector position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
    }
    
    public void rotateVertically(double amount) {
        rotate_x.setAngle(rotate_x.getAngle() + amount);
    }
    
    public void rotateHorizontally(double amount) {
        rotate_y.setAngle(rotate_y.getAngle() + amount);
    }
    
    public void moveForward(double amount) {
        Point3D pos = rotate_y.deltaTransform(0, 0, amount);
        Vector newpos = new Vector(
                pos.getX() + this.getTranslateX(),
                pos.getY() + this.getTranslateY(),
                pos.getZ() + this.getTranslateZ());
        this.setPosition(newpos);
    }

    public void moveRight(double amount) {
        Point3D pos = rotate_y.deltaTransform(amount, 0, 0);
        Vector newpos = new Vector(
                pos.getX() + this.getTranslateX(),
                pos.getY() + this.getTranslateY(),
                pos.getZ() + this.getTranslateZ());
        this.setPosition(newpos);
    }
}
