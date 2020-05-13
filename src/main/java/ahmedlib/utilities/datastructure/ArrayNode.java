/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "ArrayNode" is part of ahmed.library.
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
package ahmedlib.utilities.datastructure;

/**
 *
 * @author Ahmed Mazher
 */
class ArrayNode {

    ArrayNode prevArr; // pointer to previous array in list of arrays
    ArrayNode nextArr; // it point to next array
    Object[] elements; // the array of elements
    int firstEmptyIndex; // index of empty space for inserting new element at head
    int lastEmptyIndex; //index of empty space for inserting new element at tail
    int count; // numer of elements in array block
    int reservedSpace; //reserved for insertion operation

    ArrayNode(int size) {
        reservedSpace = (int) Math.sqrt(size);
        elements = new Object[size + reservedSpace];
        count = 0;
        prevArr = null;
        nextArr = null;
    }

    ArrayNode withPrev(ArrayNode prev) {
        prevArr = prev;
        return this;
    }

    ArrayNode withNext(ArrayNode next) {
        nextArr = next;
        return this;
    }

    ArrayNode asHead() {
        firstEmptyIndex = elements.length - 1;
        lastEmptyIndex = elements.length;
        return this;
    }

    ArrayNode asTail() {
        firstEmptyIndex = -1;
        lastEmptyIndex = 0;
        return this;
    }

    void expandTail() {
        reservedSpace = (int) Math.sqrt(elements.length);
        Object[] temp = new Object[elements.length + reservedSpace];
        System.arraycopy(elements, firstEmptyIndex + 1, temp, firstEmptyIndex + 1, count);
        elements = temp;
    }

    void expandHead() {
        reservedSpace = (int) Math.sqrt(elements.length);
        Object[] temp = new Object[elements.length + reservedSpace];
        System.arraycopy(elements, 0, temp, reservedSpace, count);
        elements = temp;
    }

}
