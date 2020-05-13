/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "JBox" is part of ahmed.library.
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
import javax.swing.JComponent;

/**
 *
 * @author Ahmed Mazher
 */
@SuppressWarnings("serial")
public class JBox extends JComponent {

    public JBox(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
    }

    @Override
    public final void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    @Override
    protected final void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        } else {
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        }
    }

}
