/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "LinkedArrays" is part of ahmed.library.
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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Ahmed Mazher
 * @param <E>
 *
 */
public class LinkedArrays<E> implements Collection<E> {

    int minCapacity;//the minimum capacity of data structure
    int currCapacity;//the current currCapacity of data structure
    int eleCount; // total number of elements currently stored
    int arrCount;//current number of arrays
    int maxArraySize;//maximum allowed size of array
    ArrayNode head;//point to the head array
    ArrayNode tail; //point to the tail array

    public LinkedArrays() {
        this(100);
    }

    public LinkedArrays(int capacity) {
        minCapacity = capacity;
        currCapacity = capacity;
        maxArraySize = (int) Math.sqrt(capacity);
        head = new ArrayNode(maxArraySize).asHead().withNext(new ArrayNode(maxArraySize).asTail());
        tail = head.nextArr.withPrev(head);
        arrCount = 2;
        eleCount = 0;
    }

    boolean availableSpaceForAppending() {
        return (tail.lastEmptyIndex < tail.elements.length - tail.reservedSpace);
    }

    boolean availableSpaceForInserting(ArrayNode a) {
        return (a.count < a.elements.length);
    }

    public boolean withinBounds(int index) {
        return index < eleCount || index >= 0;
    }

    void checkBound(int index) {
        if (!withinBounds(index)) {
            throw new IndexOutOfBoundsException("the specified index exceeds"
                    + " the number of elements stored");
        }
    }

    @Override
    public int size() {
        return eleCount;
    }

    @Override
    public boolean isEmpty() {
        return eleCount < 1;
    }

    void appendArrayBlock() {
        //make new arrayblock and make tail object point to it as next
        //while it will point to tail opject as previou
        tail.nextArr = new ArrayNode(maxArraySize).asTail().withPrev(tail);
        //increase array size by one
        arrCount++;
        //make the tail variable refer to the new arrayblock
        tail = tail.nextArr;
    }

    private void adjustCapacity() {
        if (eleCount > currCapacity) {
            currCapacity *= 10;
            maxArraySize = (int) Math.sqrt(currCapacity);
        } else if (eleCount > minCapacity && eleCount < currCapacity / 10) {
            currCapacity /= 10;
            maxArraySize = (int) Math.sqrt(currCapacity);
        }
    }

    private class ItemInf {

        E item;
        /**
         * index of item
         */
        int itemIndex;
        /**
         * ArrayNode that contain the item
         */
        ArrayNode itemArray = head;
        /**
         * index of the item in its containing array
         */
        int indexInArray;
        int beforeLength = 0; //total length of array blocks before current array

    }

    @SuppressWarnings("unchecked")
    private ItemInf getIndexInf(int index) {
        if (!withinBounds(index)) {
            throw new IndexOutOfBoundsException("the specified index exceeds"
                    + " the number of elements stored");
        }
        ItemInf inf = new ItemInf();
        int beforeLength = 0;
        ArrayNode a = head;
        while (a != null) {
            if (index < beforeLength + a.count) {
                inf.itemArray = a;
                inf.itemIndex = index;
                inf.indexInArray = index - beforeLength + a.firstEmptyIndex + 1;
                inf.beforeLength = beforeLength;
                inf.item = (E) a.elements[inf.indexInArray];
                return inf;
            } else {
                beforeLength += a.count;
                a = a.nextArr;
            }
        }
        return null;
    }

    private ItemInf getItemInf(Object o) {
        if (notHomogenous()) {
            homogenize();
        }
        ItemInf inf = new ItemInf();
        int beforeLength = 0;
        ArrayNode a = head;
        while (a != null) {
            for (int i = a.firstEmptyIndex + 1; i < a.lastEmptyIndex; i++) {
                @SuppressWarnings("unchecked")
                E e = (E) a.elements[i];
                if ((o == null && e == null) || (e != null && e.equals(o))) {
                    inf.itemArray = a;
                    inf.indexInArray = i;
                    inf.itemIndex = i + beforeLength;
                    inf.beforeLength = beforeLength;
                    inf.item = e;
                    return inf;
                }
            }
            beforeLength += a.count;
            a = a.nextArr;
        }
        return null;
    }

    void removeByInf(ItemInf inf) {
        ArrayNode a = inf.itemArray;
        int i = inf.indexInArray; // index of the element in the a.elements array
        System.arraycopy(a.elements, i + 1, a.elements, i, a.count - 1 - i);
        a.lastEmptyIndex--;
        a.count--;
        eleCount--;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedArraysIterator();
    }

    public class LinkedArraysIterator implements Iterator<E> {

        /**
         * index of last returned item
         */
        int currIndex;
        /**
         * ArrayNode that contain the last returned item
         */
        ArrayNode currArray = head;
        /**
         * index of last returned item in its containing array
         */
        int indexInArray;
        int beforeLength = 0; //total length of array blocks before current array

        public LinkedArraysIterator() {
            currIndex = -1;
        }

        public LinkedArraysIterator(int start) {
            if (!withinBounds(start)) {
                throw new IndexOutOfBoundsException("the specified index exceeds"
                        + " the number of elements stored");
            }
            if (start > 0) {
                ItemInf inf = getIndexInf(start - 1);
                currIndex = inf.itemIndex;
                currArray = inf.itemArray;
                indexInArray = inf.indexInArray;
                beforeLength = inf.beforeLength;
            }
        }

        @Override
        public boolean hasNext() {
            return currIndex + 1 < eleCount;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (hasNext()) {
                currIndex++;
                if (currIndex < beforeLength + currArray.count) {
                    indexInArray = currIndex - beforeLength + currArray.firstEmptyIndex + 1;
                    return (E) currArray.elements[indexInArray];
                } else {
                    beforeLength += currArray.count;
                    currArray = currArray.nextArr;
                    indexInArray = currIndex - beforeLength + currArray.firstEmptyIndex + 1;
                    return (E) currArray.elements[indexInArray];
                }
            } else {
                throw new IndexOutOfBoundsException("the specified index exceeds"
                        + " the number of elements stored");
            }
        }

        @Override
        public void remove() {
            removeByInf(last());
            currIndex--;
            if (indexInArray - 1 > currArray.firstEmptyIndex) {
                indexInArray--;
            } else {
                currArray = currArray.prevArr;
                beforeLength -= currArray.count;
                indexInArray = currArray.lastEmptyIndex - 1;
            }
        }

        @SuppressWarnings("unchecked")
        ItemInf last() {
            ItemInf inf = new ItemInf();
            inf.itemIndex = currIndex;
            inf.itemArray = currArray;
            inf.indexInArray = indexInArray;
            inf.item = (E) inf.itemArray.elements[inf.indexInArray];
            inf.beforeLength = beforeLength;
            return inf;
        }

    }

    public void append(E item) {
        if (!availableSpaceForAppending()) {
            adjustCapacity();
            appendArrayBlock();
        }
        //add the element to the empty slot indicated by index
        tail.elements[tail.lastEmptyIndex] = item;
        //increase index by one to point again to empty slot
        tail.lastEmptyIndex++;
        //increase array element size by one
        tail.count++;
        //increase total element size by one
        eleCount++;
    }

    @Override
    public boolean add(E e) {
        append(e);
        return true;
    }

    public void appendAll(E[] items) {
        int currIndex = 0;//current index of item to start copy from
        while (currIndex < items.length) {
            if (!availableSpaceForAppending()) {
                adjustCapacity();
                appendArrayBlock();
            }
            int numToCopy = Math.min(items.length - currIndex, tail.elements.length - tail.reservedSpace - tail.lastEmptyIndex);//numer of elements to be copied
            //copy the elements to the empty slots till array become filled
            System.arraycopy(items, currIndex, tail.elements, tail.lastEmptyIndex, numToCopy);
            //increase index by one to point again to empty slot
            tail.lastEmptyIndex += numToCopy;
            //increase array element count by one
            tail.count += numToCopy;
            //increase total element count by one
            eleCount += numToCopy;
            currIndex += numToCopy;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends E> c) {
        appendAll((E[]) c.toArray());
        return true;
    }

    public void appendRange(E[] items, int start, int end) {
        int currIndex = start;//current index of item to start copy from
        while (currIndex <= end) {
            if (!availableSpaceForAppending()) {
                adjustCapacity();
                appendArrayBlock();
            }
            int numToCopy = Math.min(end - currIndex + 1, tail.elements.length - tail.reservedSpace - tail.lastEmptyIndex);//numer of elements to be copied
            //copy the elements to the empty slots till array become filled
            System.arraycopy(items, currIndex, tail.elements, tail.lastEmptyIndex, numToCopy);
            //increase index by one to point again to empty slot
            tail.lastEmptyIndex += numToCopy;
            //increase array element count by one
            tail.count += numToCopy;
            //increase total element count by one
            eleCount += numToCopy;
            currIndex += numToCopy;
        }
    }

    public void insert(E item, int index) {
        ItemInf inf = getIndexInf(index);
        ArrayNode a = inf.itemArray;
        if (!availableSpaceForInserting(inf.itemArray)) {
            adjustCapacity();
            a.expandTail();
        }
        int i = inf.indexInArray; // index of the element in the a.elements array
        if (a.lastEmptyIndex < a.elements.length) {//the available space are in tail of array
            System.arraycopy(a.elements, i, a.elements, i + 1, a.count - i + a.firstEmptyIndex + 1);
            a.elements[i] = item;
            a.lastEmptyIndex++;
        } else {//avaialable space are in head of array
            System.arraycopy(a.elements, a.firstEmptyIndex + 1, a.elements, a.firstEmptyIndex, i - (a.firstEmptyIndex + 1));
            a.elements[i - 1] = item;
            a.firstEmptyIndex--;
        }
        a.count++;
        eleCount++;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        ItemInf inf = getIndexInf(index);
        removeByInf(inf);
        return inf.item;
    }

    @Override
    public boolean remove(Object o) {
        ItemInf inf = getItemInf(o);
        if (inf != null) {
            removeByInf(inf);
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public E getByIndex(int index) {
        ItemInf inf = getIndexInf(index);
        return (E) inf.itemArray.elements[inf.indexInArray];
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return getItemInf(o) != null;
    }

    public int getIndexOf(E item) {
        ItemInf inf = getItemInf(item);
        if (inf != null) {
            return inf.itemIndex;
        } else {
            return -1;
        }
    }

    public <T> T[] toArray(Class<T> cls) {
        if (eleCount > 0) {
            if (!cls.isAssignableFrom(classOfListFirstItem())) {
                throw new ClassCastException(classOfListFirstItem().getName()
                        + "[] can't be converted/casted to "
                        + cls.getName() + "[]");
            }
            @SuppressWarnings("unchecked")
            T[] temp = (T[]) Array.newInstance(cls, eleCount);
            ArrayNode a = head;
            int total = 0;
            while (a != null) {
                if (a.count > 0) {
                    System.arraycopy(a.elements, a.firstEmptyIndex + 1, temp, total, a.count);
                    total += a.count;
                }
                a = a.nextArr;
            }
            return temp;
        } else {
            return null;
        }
    }

    Class<?> classOfListFirstItem() {
        Class<?> comType;
        if (head.count > 0) {
            comType = head.elements[head.firstEmptyIndex + 1].getClass();
        } else {
            comType = tail.elements[tail.firstEmptyIndex + 1].getClass();
        }
        return comType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        return (E[]) toArray(classOfListFirstItem());
    }

    @Deprecated
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Unsupported please use"
                + "E[] toArray() or <T> T[] convertToArray(Class<T> cls)"
                + "or overide it your self if realy need it.");
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(Collection<?> c) {
        boolean allElePresent = true;
        for (Object o : c) {
            if (!this.remove(o)) {
                allElePresent = false;
            }
        }
        return allElePresent;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        LinkedArraysIterator it = new LinkedArraysIterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
            }
        }
        return true;
    }

    @Override
    public void clear() {
        LinkedArrays<E> newOne = new LinkedArrays<>();
        currCapacity = newOne.currCapacity;
        head = newOne.head;
        tail = newOne.tail;
        arrCount = newOne.arrCount;
        maxArraySize = newOne.maxArraySize;
        eleCount = newOne.eleCount;
    }

    @SuppressWarnings("unchecked")
    public void homogenize() {
        //tester();
        adjustCapacity();
        LinkedArrays<E> newOne = new LinkedArrays<>(currCapacity);
        ArrayNode a = head;
        while (a != null) {
            newOne.appendRange((E[]) a.elements, a.firstEmptyIndex + 1, a.lastEmptyIndex - 1);
            a = a.nextArr;
        }
        currCapacity = newOne.currCapacity;
        head = newOne.head;
        tail = newOne.tail;
        arrCount = newOne.arrCount;
        maxArraySize = newOne.maxArraySize;
        eleCount = newOne.eleCount;
    }

    boolean notHomogenous() {
        int currentArraySize = (eleCount / arrCount);
        return currentArraySize < maxArraySize * 0.75 || currentArraySize > maxArraySize * 1.25;
    }

    void tester() {
        System.err.println("\neleCount " + eleCount + " capacity " + currCapacity
                + " arrCount " + arrCount + " maxArraySize " + maxArraySize
                //+ " ratio " + arraySizeCountRatio
                + " current ratio " + ((eleCount - tail.lastEmptyIndex) / (arrCount - 1d)) / currCapacity //(arrCount - 1d)
                + " current arraySize " + eleCount / arrCount);
    }

}
