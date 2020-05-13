/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedlib.utilities.physics;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class BoxCollider extends Collider implements Box {

    int width, height;

    public BoxCollider(int width, int height, double mass, Point refPoint, Vector speed) {
        super(mass, refPoint, speed, boxCoPoints(refPoint, width, height));
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private static List<Vector> boxCoPoints(Point refPoint, int width, int height) {
        Vector p1 = new Vector(width / 2, -height / 2);
        Vector p2 = new Vector(width / 2, height / 2);
        Vector p3 = new Vector(-width / 2, height / 2);
        Vector p4 = new Vector(-width / 2, -height / 2);
        return Arrays.asList(p1, p2, p3, p4);
    }

}
