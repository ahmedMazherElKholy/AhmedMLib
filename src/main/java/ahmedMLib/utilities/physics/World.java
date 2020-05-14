/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "World" is part of ahmed.library.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class World {

    private final List<Collider> colliders = new ArrayList<>();
    private final Queue<Collider> collisionQueue = new LinkedList<>();
    public static final double FRAMETIME = 1;
    public static final double GAPTIME = 0.000000000001;

    public void addColliders(Collider... cs) {
        colliders.addAll(Arrays.asList(cs));
    }

    public CollisionInf predectCollision(Collider cl1, Collider cl2, double startTime) {
        List<CollisionInf> infs = new ArrayList<>();
        for (Vector v1 : cl1.getCoPointsVectors()) {
            Vector pre = null;
            for (Vector v2 : cl2.getCoPointsVectors()) {
                CollisionInf ci = predectPointPointCollision(cl1, v1, cl2, v2, startTime);
                if (ci.occurred) {
                    infs.add(ci);
                }
                if (pre != null) {
                    ci = predectPointLineCollision(cl1, v1, cl2, pre, v2, startTime);
                    if (ci.occurred) {
                        infs.add(ci);
                    }
                }
                pre = v2;
            }
            if (cl2.getCoPointsVectors().size() > 1) {
                CollisionInf ci = predectPointLineCollision(cl1, v1, cl2,
                        pre, cl2.getCoPointsVectors().get(0), startTime);
                if (ci.occurred) {
                    infs.add(ci);
                }
            }
        }
        for (Vector v1 : cl2.getCoPointsVectors()) {
            Vector pre = null;
            for (Vector v2 : cl1.getCoPointsVectors()) {
                if (pre != null) {
                    CollisionInf ci = predectPointLineCollision(cl2, v1, cl1, pre, v2, startTime);
                    if (ci.occurred) {
                        infs.add(ci);
                    }
                }
                pre = v2;
            }
            if (cl1.getCoPointsVectors().size() > 1) {
                CollisionInf ci = predectPointLineCollision(cl2, v1, cl1,
                        pre, cl1.getCoPointsVectors().get(0), startTime);
                if (ci.occurred) {
                    infs.add(ci);
                }
            }
        }
        if (!infs.isEmpty()) {
            Collections.sort(infs);
            return infs.get(0);
        } else {
            return CollisionInf.noCollision(cl1, cl2);
        }
    }

    private CollisionInf predectPointPointCollision(Collider cl1, Vector p1,
            Collider cl2, Vector p2, double startTime) {
        double ra = cl1.getCollisionDistance();
        double rb = cl2.getCollisionDistance();
        Point ao = cl1.getRefPointAtTime(startTime).plus(p1);//point 1 of collider a before collision
        Point bo = cl2.getRefPointAtTime(startTime).plus(p2);//point 2 of collider b before collision
        Vector av = cl1.getSpeedAtTime(startTime);
        Vector bv = cl2.getSpeedAtTime(startTime);
        Vector ov = new Vector(ao, bo);//vector from point a to p
        Vector xv = bv.minus(av);//vector result from va-vb
        //a,b,c are terms of quadratic formula for solving quadratic equation
        double a = xv.vx * xv.vx + xv.vy * xv.vy;
        double b = 2 * (ov.vx * xv.vx + ov.vy * xv.vy);
        double c = ov.vx * ov.vx + ov.vy * ov.vy - (ra + rb) * (ra + rb);
        double discriminant = b * b - 4 * a * c;
        double timeToCollision = 0;
        if (a == 0) {
            return CollisionInf.noCollision(cl1, cl2);
        } else if (discriminant == 0) {
            timeToCollision = -b / 2 * a;
        } else if (discriminant > 0) {
            double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
            double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double min = Math.min(t1, t2);
            if (min < 0) {
                timeToCollision = Math.max(t1, t2);
            } else {
                timeToCollision = min;
            }
        }
        double correctedCollisionTime = timeToCollision + startTime;
        if (correctedCollisionTime > startTime) {
            Point ad = ao.plus(av.getScaledBy(timeToCollision));
            Point bd = bo.plus(bv.getScaledBy(timeToCollision));
            Vector collisionPlane = new Vector(ad, bd).getPerpendicularVector().getUnitVector();
            return new CollisionInf(cl1, cl2, correctedCollisionTime, collisionPlane);
        } else {
            return CollisionInf.noCollision(cl1, cl2);
        }
    }

    private CollisionInf predectPointLineCollision(Collider pCl, Vector pv,
            Collider lCl, Vector lpv1, Vector lpv2, double startTime) {
        Point co = pCl.getRefPointAtTime(startTime).plus(pv);
        Point ao = lCl.getRefPointAtTime(startTime).plus(lpv1);
        Point bo = lCl.getRefPointAtTime(startTime).plus(lpv2);
        Vector lv = new Vector(ao, bo);
        Vector plv = lv.getPerpendicularVector();
        Vector ov = new Vector(ao, co);
        Vector xv = pCl.getSpeedAtTime(startTime).minus(lCl.getSpeedAtTime(startTime));
        double r = pCl.getCollisionDistance() + lCl.getCollisionDistance();
        double a = (plv.vx * xv.vx + plv.vy * xv.vy) * (plv.vx * xv.vx + plv.vy * xv.vy)
                / (plv.vx * plv.vx + plv.vy * plv.vy);
        double b = 2 * (plv.vx * xv.vx + plv.vy * xv.vy) * (plv.vx * ov.vx + plv.vy * ov.vy)
                / (plv.vx * plv.vx + plv.vy * plv.vy);
        double c = ((plv.vx * ov.vx + plv.vy * ov.vy) * (plv.vx * ov.vx + plv.vy * ov.vy)
                / (plv.vx * plv.vx + plv.vy * plv.vy))
                - r * r;
        double discriminant = b * b - 4 * a * c;
        double timeToCollision = 0;
        if (a == 0) {
            return CollisionInf.noCollision(pCl, lCl);
        } else if (discriminant == 0) {
            timeToCollision = -b / 2 * a;
        } else if (discriminant > 0) {
            double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
            double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double min = Math.min(t1, t2);
            if (min < 0) {
                timeToCollision = Math.max(t1, t2);
            } else {
                timeToCollision = min;
            }
        }
        //the correctedCollisionTime <= startTime as the collision continue to be
        //found at the moment it is happened as time to collsion equals zero in
        //this case ,i thought that changeing collider direction at time of 
        //colllision is sfficient but seems it don't solve collision at time zero
        //trapping collider at state of continious collision
        double correctedCollisionTime = timeToCollision + startTime;
        //to calculate collision point and determine if it are on the
        //line segment not on its infinite extension
        Vector zv = ov.plus(xv.getScaledBy(timeToCollision));
        double s = zv.dotWith(lv.getUnitVector());
        if (s >= 0 && s <= lv.getMagnitude() && correctedCollisionTime > startTime) {
            Vector collisionPlane = lv.getUnitVector();
            return new CollisionInf(pCl, lCl, correctedCollisionTime, collisionPlane);
        } else {
            return CollisionInf.noCollision(pCl, lCl);
        }
    }

    void resolveCollision(CollisionInf inf) {
        //unit vector that represent collision plane aka surface 
        Vector ut = inf.collisionPlane;
        //unit vector that is perpendicular to collision surface
        Vector un = ut.getPerpendicularVector();
        //speed vector of two circle pefore collision
        Vector v1 = inf.cl1.getSpeedAtTime(inf.collisionTime);
        Vector v2 = inf.cl2.getSpeedAtTime(inf.collisionTime);
        //normal and tangential components of speed vectors before collision
        //is done by using dot product of the speed vector with unit normal 
        //"perpendicular" and tangential vectors
        double nv1 = un.dotWith(v1);
        double tv1 = ut.dotWith(v1);
        double nv2 = un.dotWith(v2);
        double tv2 = ut.dotWith(v2);
        //calculates the result of collision note that collision involve only 
        //normal components of velocities as they are perpendicular to collsion 
        //surface aka facing each other in contrast to tangential components
        //also note that they are scalar number as dot product produce numbers
        //not vectors we will use their values to scale un and ut vectors by
        //there magnetitude 
        //------------------------------------------
        //normal component of velocity vector 1 after collision
        double an1;
        //normal component of velocity vector 2 after collision
        double an2;
        //tangential component of velocity vector 1,2 after collision is 
        //the same before collision
        double at1, at2;
        if (inf.cl1.isStatic) {
            an1 = 0;
            at1 = 0;
            an2 = -nv2;
            at2 = tv2;
        } else if (inf.cl2.isStatic) {
            an1 = -nv1;
            at1 = tv1;
            an2 = 0;
            at2 = 0;
        } else {
            an1 = (nv1 * (inf.cl1.getMass() - inf.cl2.getMass())
                    + (2 * inf.cl2.getMass() * nv2))
                    / (inf.cl1.getMass() + inf.cl2.getMass());
            an2 = (nv2 * (inf.cl2.getMass() - inf.cl1.getMass())
                    + (2 * inf.cl1.getMass() * nv1))
                    / (inf.cl1.getMass() + inf.cl2.getMass());
            at1 = tv1;
            at2 = tv2;
        }
        //converting scalar values to vectors
        Vector anv1 = un.getScaledBy(an1);
        Vector atv1 = ut.getScaledBy(at1);
        Vector anv2 = un.getScaledBy(an2);
        Vector atv2 = ut.getScaledBy(at2);
        //claculating final velocity vectors
        Vector av1 = anv1.plus(atv1);
        Vector av2 = anv2.plus(atv2);
        //update movement steps note that end collisionTime of 1 mean end of frame collisionTime
        double lastStepDuration = FRAMETIME - inf.collisionTime;
        inf.cl1.getPath().stopAtTime(inf.collisionTime);
        inf.cl1.getPath().addStep(new Step(av1, lastStepDuration));
        inf.cl2.getPath().stopAtTime(inf.collisionTime);
        inf.cl2.getPath().addStep(new Step(av2, lastStepDuration));
    }

    public void resolveAllCollisions() {
        double searchingStartTime = 0;
        boolean collisionCheckingNeeded = true;
        while (collisionCheckingNeeded && searchingStartTime <= FRAMETIME) {
            collisionCheckingNeeded = false;
            collisionQueue.addAll(colliders);
            List<CollisionInf> grandInfs = new ArrayList<>();
            while (!collisionQueue.isEmpty()) {
                Collider curr = collisionQueue.remove();
                List<CollisionInf> privateInfs = new ArrayList<>();
                for (Collider c : collisionQueue) {
                    CollisionInf ci = predectCollision(curr, c, searchingStartTime);
                    if (ci.occurred && ci.collisionTime <= FRAMETIME) {
                        System.err.println("in collsion occured #######################################");
                        privateInfs.add(ci);
                    }
                }
                //nearest possible collision of one collider
                if (!privateInfs.isEmpty()) {
                    Collections.sort(privateInfs);
                    grandInfs.add(privateInfs.get(0));
                }
            }
            //nearest possible collision of all colliders
            if (!grandInfs.isEmpty()) {
                System.err.println("in grand################################################");
                Collections.sort(grandInfs);
                double nearestTime = grandInfs.get(0).collisionTime;
                searchingStartTime = nearestTime + GAPTIME;
                collisionCheckingNeeded = true;
                for (CollisionInf i : grandInfs) {
                    if (i.collisionTime == nearestTime) {
                        System.err.println("pre collision time= " + i.collisionTime
                                + "\ncl1 " + i.cl1.getRefPointAtTime(i.collisionTime)
                                + " " + i.cl1.getSpeedAtTime(i.collisionTime)
                                + "\ncl2 " + i.cl2.getRefPointAtTime(i.collisionTime)
                                + " " + i.cl2.getSpeedAtTime(i.collisionTime));
                        resolveCollision(i);
                        System.err.println("after collision time= " + i.collisionTime
                                + "\ncl1 " + i.cl1.getRefPointAtTime(i.collisionTime)
                                + " " + i.cl1.getSpeedAtTime(i.collisionTime)
                                + "\ncl2 " + i.cl2.getRefPointAtTime(i.collisionTime)
                                + " " + i.cl2.getSpeedAtTime(i.collisionTime));
                        if (searchingStartTime <= FRAMETIME) {
                            System.err.println("after collision time= " + searchingStartTime
                                    + "\ncl1 " + i.cl1.getRefPointAtTime(searchingStartTime)
                                    + " " + i.cl1.getSpeedAtTime(searchingStartTime)
                                    + "\ncl2 " + i.cl2.getRefPointAtTime(searchingStartTime)
                                    + " " + i.cl2.getSpeedAtTime(searchingStartTime));
                        }
                    }
                }
            }
        }
    }

    public void applyAllMoves() {
        for (Collider c : colliders) {
            if (!c.isStatic) {
                Vector lastSpeed = c.getPath().getStepByIndex(c.getPath().getSteps().size() - 1).getSpeed();
                Point lastPos = c.getRefPoint().plus(c.getPath().getTranslationAtTime(FRAMETIME));
                System.err.println("sum " + c.getPath().getTranslationAtTime(FRAMETIME)
                        + "apply " + lastPos + " " + lastSpeed);
                c.setRefPoint(lastPos);
                c.setSpeed(lastSpeed);
            } else {
                Vector initialSpeed = c.getSpeedAtTime(0);
                c.setSpeed(initialSpeed);
            }
        }
    }

}
