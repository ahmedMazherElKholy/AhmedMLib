/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "JLine" is part of ahmed.library.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ahmedMLib.jtools;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.*;

/**
 * my own implementation of jcomponent to make a line object 2015/10
 *
 * @author ahmed mazher
 */
@SuppressWarnings("serial")
public class JLine extends JComponent {

    private int x1, y1, x2, y2;//the two line points as present in the container of line
    private int cx, cy;//the two line points as present internally in the component
    //this was solution of a proplem in calculation as i discovered that the contains method
    //passes the x and y in side component "in relation to its top left corner"
    //also pass it if the mouse was out of container as a negative numbers !? wired isn't it
    //i calculaated cx , cy "component x and y " as the smallest of x1,x2 and y1,y2
    //i will later substract cx,cy to find the value of x1,x2,y1,y2 inside component
    private int length, width, height;
    double angle;

    public JLine(int x1, int y1, int x2, int y2) {
        super();
        setPoints(x1, y1, x2, y2);
    }

    @Override
    protected final void paintComponent(Graphics g) {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public final boolean contains(int x, int y) {
        if (super.contains(x, y)) {
            int deltaX = Math.abs(x1 - x);
            int deltaY = Math.abs(y1 - y);
            int expectedY = (int) (Math.tan(angle) * deltaX);
//            System.err.println("x= "+ x +" y= " + y + " expected y = " + expectedY);
//            System.err.println("x1= "+ x1 +" y1= " + y1 + " y2 = " + y2 + " x2 " + x2);
//            System.err.println("angel quarter me"+ angelQuarter(x1, y1, x2, y2) 
//                    +" angel quarter mouse" + angelQuarter(x1, y1, x, y));
            return angelQuarter(x1, y1, x2, y2) == angelQuarter(x1, y1, x, y)
                    && Math.abs(expectedY - deltaY) < 5;
        } else {
            return false;
        }
    }

    private double calAngel(int x1, int y1, int x2, int y2) {
        int deltaX = Math.abs(x1 - x2);
        int deltaY = Math.abs(y1 - y2);
        return Math.atan((double) deltaY / (double) deltaX);
    }

    /**
     * -------------------------- |x2-x1>=0 && y1-y2>=0 (x2,y2) | (x2,y2) 2 | 1
     * | | ----------(x1,y1)--------- |same as 1 but y1-y2&lt0 3 | 4 | (x2,y2)
     * (x2,y2) | --------------------------
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private int angelQuarter(int x1, int y1, int x2, int y2) {
        int xl = x2 - x1;
        int yl = y1 - y2;
        if (xl >= 0) {
            if (yl >= 0) {
                return 1;
            } else {
                return 4;
            }
        } else if (yl >= 0) {
            return 2;
        } else {
            return 3;
        }
    }

    private int calLength(int x1, int y1, int x2, int y2) {
        int xl = x2 - x1;
        int yl = y1 - y2;
        return (int) Math.sqrt(Math.pow(xl, 2) + Math.pow(yl, 2));
    }

    public final void setPoints(int x1, int y1, int x2, int y2) {
        //we do this calculation to find the actual position of point x1,y1
        //and point x2,y2 in the component
        //have a look at the following 
        /////////////////////////////////////////////
        //                              //
        // (cx,cy)////////////////      //
        //        //     (x1,y1)//      //
        //        //       /    //      //
        //        //      /     //      //
        //        //     /      //      //
        //        //    /       //      //
        //        //(x2,y2)     //      //
        //        ////////////////      //
        //                              //
        //////////////////////////////////
        //as you can see the cx of component = x2 and its cy =y1 
        //i mean the smallest of them so to find the actual position
        //of points inside component we substract from them the component x 
        // and component y
        cx = Math.min(x1, x2);
        cy = Math.min(y1, y2);
        this.x1 = x1 - cx;
        this.y1 = y1 - cy;
        this.x2 = x2 - cx;
        this.y2 = y2 - cy;
        width = Math.abs(x1 - x2);
        height = Math.abs(y1 - y2);
        length = calLength(x1, y1, x2, y2);
        angle = calAngel(x1, y1, x2, y2);
        super.setBounds(cx, cy, width, height);
    }

    public int getLength() {
        return length;
    }

    @Override
    @Deprecated
    /**
     * not supported do not use it using it throws unsupported exception
     */
    public void setBounds(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("unsupported use setPoints");
    }

    @Override
    @Deprecated
    /**
     *
     * not supported do not use it using it throws unsupported exception
     */
    public void setBounds(Rectangle r) {
        throw new UnsupportedOperationException("use unsupported use setPoints");
    }

}
