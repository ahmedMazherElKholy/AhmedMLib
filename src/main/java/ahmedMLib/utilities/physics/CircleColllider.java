/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "CircleColllider" is part of ahmed.library.
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

import java.util.Arrays;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class CircleColllider extends Collider implements Circle {

    int radius;

    public CircleColllider(double mass, Point refPoint, Vector speed, int radius) {
        super(mass, refPoint, speed, Arrays.asList(new Vector(0, 0)));
        this.radius = radius;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    protected int getCollisionDistance() {
        return getRadius();
    }

}
