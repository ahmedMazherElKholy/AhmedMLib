/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "CollisionInf" is part of ahmed.library.
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

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class CollisionInf implements Comparable<CollisionInf> {

    boolean occurred;
    double collisionTime;
    Collider cl1;
    Collider cl2;
    Vector collisionPlane;

    public CollisionInf(Collider cl1, Collider cl2, double time, Vector collisionPlane) {
        this.collisionTime = time;
        this.cl1 = cl1;
        this.cl2 = cl2;
        this.collisionPlane = collisionPlane;
        this.occurred = true;
    }

    public static CollisionInf noCollision(Collider cl1, Collider cl2) {
        CollisionInf ci = new CollisionInf(cl1, cl2, -1, null);
        ci.occurred = false;
        return ci;
    }

    @Override
    public int compareTo(CollisionInf o) {
        if (collisionTime > o.collisionTime) {
            return 1;
        } else if (collisionTime < o.collisionTime) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "time=" + collisionTime + " " + collisionPlane
                + " " + cl1 + " " + cl2;
    }

}
