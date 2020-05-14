/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "DataStructureTester" is part of ahmed.library.
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
package ahmedMLib.utilities.datastructure;

/**
 *
 * @author Ahmed Mazher
 */
public class DataStructureTester {

    void test(TestableDataStructure<Number> t, int num) {
        long start;
        long end;
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            t.addT(i);
        }
        end = System.nanoTime();
        System.out.print("time of adding is " + (end - start) + "     ");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            int c = (int) (Math.random() * i);
            t.insertT(i, c);
        }
        end = System.nanoTime();
        System.out.print("time of inserting is " + (end - start) + "     ");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            int c = (int) (Math.random() * i);
            t.deletT(c);
        }
        end = System.nanoTime();
        System.out.print("time of delet is " + (end - start) + "     ");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            int c = (int) (Math.random() * i);
            t.getT(c);
        }
        end = System.nanoTime();
        System.out.print("time of getting is " + (end - start) + "     ");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            int c = (int) (Math.random() * i);
            t.findT(c);
        }
        end = System.nanoTime();
        System.out.println("time of searching is " + (end - start));
    }

}
