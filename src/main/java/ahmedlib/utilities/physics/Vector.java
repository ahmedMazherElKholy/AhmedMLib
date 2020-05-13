/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Vector" is part of ahmed.library.
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
package ahmedlib.utilities.physics;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class Vector {

    public final double vx, vy;

    public Vector(double x, double y) {
        this.vx = x;
        this.vy = y;
    }

    public Vector(Point start, Point end) {
        if (start != null && end != null) {
            vx = end.getX() - start.getX();
            vy = end.getY() - start.getY();
        } else {
            vx = 0;
            vy = 0;
        }
    }

    public Vector(Vector v) {
        this.vx = v.vx;
        this.vy = v.vy;
    }

    public double getMagnitude() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    public Vector getScaledBy(double scalar) {
        double sx = scalar * vx;
        double sy = scalar * vy;
        return new Vector(sx, sy);
    }

    public Vector getUnitVector() {
        double ux = vx / getMagnitude();
        double uy = vy / getMagnitude();
        return new Vector(ux, uy);
    }

    public Vector getNegativeVector() {
        return new Vector(-vx, -vy);
    }

    public Vector getPerpendicularVector() {
        return new Vector(vy, -vx);
    }

    public double dotWith(Vector v) {
        return (this.vx * v.vx) + (this.vy * v.vy);
    }

    public Vector plus(Vector v) {
        return new Vector(this.vx + v.vx, this.vy + v.vy);
    }

    public Vector minus(Vector v) {
        return new Vector(this.vx - v.vx, this.vy - v.vy);
    }

    public double angleWith(Vector v) {
        return Math.acos(dotWith(v) / (this.getMagnitude() * v.getMagnitude()));
    }

    @Override
    public String toString() {
        return "vx=" + vx + " vy=" + vy;
    }

}
