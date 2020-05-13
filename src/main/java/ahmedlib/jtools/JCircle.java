/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "JCircle" is part of ahmed.library.
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
package ahmedlib.jtools;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author Ahmed Mazher
 */
@SuppressWarnings("serial")
public class JCircle extends JComponent {

    private int centerX, centerY; //circule center x , y 
    int rx, ry; //relative position of x,y inside component
    private int radius;

    public JCircle(int x, int y, int radius) {
        setBounds(x, y, radius);
    }

    public int getCX() {
        return centerX;
    }

    public int getCY() {
        return centerY;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    @Deprecated
    /**
     * not supported do not use it using it throws unsupported exception
     */
    public void setBounds(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("use other version with the radius argument");
    }

    @Override
    @Deprecated
    /**
     * not supported do not use it using it throws unsupported exception
     */
    public void setBounds(Rectangle r) {
        throw new UnsupportedOperationException("use other version with the radius argument");
    }

    /**
     *
     * @param centerX x axis of center point in relation to the container
     * @param centerY y axis of center point in relation of component
     * @param radius the radius of the circle
     */
    public final void setBounds(int centerX, int centerY, int radius) {
        int cx, cy;//component x,y
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        cx = centerX - radius;
        cy = centerY - radius;
        rx = centerX - cx;
        ry = centerY - cy;
        super.setBounds(cx, cy, 2 * radius + 1, 2 * radius + 1);
    }

    @Override
    protected final void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.fillOval(0, 0, 2 * radius, 2 * radius);
        } else {
            g.drawOval(0, 0, 2 * radius, 2 * radius);
        }
    }

    @Override
    public final boolean contains(int x, int y) {
        int deltaX = Math.abs(x - this.rx);
        int deltaY = Math.abs(y - this.ry);
        int expedtedRadius = (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        return super.contains(x, y) && expedtedRadius <= radius;
    }

}
