/*
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Path" is part of ahmed.library.
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
public class Path {

    private final List<Step> steps = new ArrayList<>();

    public void addStep(Step step) {
        steps.add(step);
    }

    void clearSteps() {
        steps.clear();
    }

    public int getStepIndexAtTime(double t) {
        if (t < 0) {
            return -1;
        }
        double preTime = 0;
        int i = 0;
        while (i < steps.size()) {
            Step curr = steps.get(i);
            if (preTime + curr.getDuration() >= t) {
                return i;
            } else {
                i++;
                preTime += curr.getDuration();
            }
        }
        return steps.size();
    }

    public Step getStepByIndex(int i) {
        if (i < 0 || i > steps.size()) {
            return null;
        } else {
            return steps.get(i);
        }
    }

    /**
     * this method return this step start time which is the same as the previous
     * step end time this special point of time is included in this step time
     * frame not in the previous step time frame i.e if you searched for step at
     * the time returned by this step you will end finding the same step
     *
     * @param index step index
     * @return the step start time
     */
//    public  double getStepStartTime(int index) {
//         double preTime = 0;
//        for (int j = 0; j < index; j++) {
//            preTime += steps.get(j).getDuration();
//        }
//        return preTime;
//    }
    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public double getPathDuration() {
        double total = 0;
        for (Step s : steps) {
            total += s.getDuration();
        }
        return total;
    }

    public Vector getTranslationAtTime(double time) {
        int i = getStepIndexAtTime(time);
        Vector sum = new Vector(0, 0);
        double preTime = 0;
        for (int j = 0; j < i; j++) {
            preTime += steps.get(j).getDuration();
            sum = sum.plus(steps.get(j).getDistance());
        }
        //we scaled last vector by t-pretime as this will represent the portion 
        //of duration we used from this step
        if (i >= 0 && i < steps.size()) {
//            System.err.println("" + sum + steps + steps.get(i) + steps.get(i).getDistance());
            sum = sum.plus(steps.get(i).getSpeed().getScaledBy(time - preTime));
        }
        return sum;
    }

    public Vector getSpeedAtTime(double time) {
        int i = getStepIndexAtTime(time);
        if (i >= 0 && i < steps.size()) {
            return steps.get(i).getSpeed();
        } else {
            throw new IllegalArgumentException("time out side path time range"
                    + " '0-getPathDuration()'");
        }
    }

    /**
     * stop the path at particular point of time limit the duration of the step
     * containing this point of time so that the sum of all steps time will
     * equals this time note that this point of time will mark the end of the
     * last step but will not be included in its time frame meaning that if you
     * searched for step at this time after using stop method you will find
     * nothing
     *
     * @param time the time at which last step end
     */
    public void stopAtTime(double time) {
        int i = getStepIndexAtTime(time);
        if (i >= 0 && i < steps.size()) {
            double preTime = 0;
            for (int j = 0; j < i; j++) {
                preTime += steps.get(j).getDuration();
            }
            double lastStepElapsedTime = time - preTime;
            if (lastStepElapsedTime == 0) {
                throw new IllegalStateException("time elspsed = zero");
                //steps.subList(i, steps.size()).clear();//remove steps starting from this one
            } else {
                Step lastStep = steps.get(i);
                Vector lastStepSpeed = lastStep.getSpeed();
                lastStep.setDuration(lastStepElapsedTime);
                steps.subList(i + 1, steps.size()).clear();//remove the rest of steps
            }
        }
    }

}
