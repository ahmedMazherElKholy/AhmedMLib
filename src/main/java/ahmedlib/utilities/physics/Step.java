/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Step" is part of ahmed.library.
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
 * represent movement step that a collider will walk in a portion of the frame's
 * time ,list of steps will represent the entire path that collider will take in
 * the full frame time
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class Step {

    private Vector speed;
    private double duration;

    /**
     *
     * @param speed the object speed vector
     * @param duration that is required to pass the distance in nano unit time
     * (1 unit time = 10^9 nano unit time)speed equal distance / duration and so
     * duration can't be zero
     */
    public Step(Vector speed, double duration) {
        this.speed = speed;
        this.duration = duration;
    }

    public Vector getSpeed() {
        return speed;
    }

    public Vector getDistance() {
        return speed.getScaledBy(duration);
    }

    public double getDuration() {
        return duration;
    }

    public void setSpeed(Vector speed) {
        this.speed = new Vector(speed);
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

}
