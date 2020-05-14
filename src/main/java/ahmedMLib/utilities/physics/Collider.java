/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Collider" is part of ahmed.library.
 * ahmed.library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ahmed.library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * a double with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ahmedMLib.utilities.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class Collider implements MultiPoint, Locatable {

    private final List<Vector> collisionPoints = new ArrayList<>();
    private Point refPoint;
    private double mass;
    private final Path path = new Path();
    boolean isStatic = false;

    public Collider(double mass, Point refPoint, Vector speed, List<Vector> collisionPoints) {
        this.mass = mass;
        this.refPoint = refPoint;
        path.addStep(new Step(speed, World.FRAMETIME));
        this.collisionPoints.addAll(collisionPoints);
    }

    public Collider asStatic() {
        setSpeed(new Vector(0, 0));
        this.isStatic = true;
        return this;
    }

    public Point getRefPointAtTime(double time) {
        if (isStatic) {
            return refPoint;
        } else {
            return refPoint.plus(path.getTranslationAtTime(time));
        }
    }

    public void setRefPoint(Point refPoint) {
        this.refPoint = refPoint;
    }

    public Vector getSpeedAtTime(double time) {
        if (isStatic) {
            return new Vector(0, 0);
        } else {
            return path.getSpeedAtTime(time);
        }
    }

    public void setSpeed(Vector speed) {
        if (!isStatic) {
            path.clearSteps();
            path.addStep(new Step(speed, World.FRAMETIME));
        }
    }

    public void translate(Vector translation) {
        refPoint = refPoint.plus(translation);
    }

    public void applyForce(Vector force) {
        if (!isStatic) {
            Vector resultSpeed = force.getScaledBy(1 / mass);//v=(f*t)/m consider t = 1
            Vector finalSpeed = getSpeedAtTime(0).plus(resultSpeed);
            setSpeed(finalSpeed);
        }
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    Path getPath() {
        return path;
    }

//    void setPath(Path path) {
//        this.path = path;
//    }
    @Override
    public List<Vector> getCoPointsVectors() {
        return Collections.unmodifiableList(collisionPoints);
    }

    protected int getCollisionDistance() {
        return 0;
    }

    @Override
    public Point getRefPoint() {
        return refPoint;
    }

    @Override
    public String toString() {
        return "" + this.getClass().getSimpleName();
    }

}
