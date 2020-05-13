/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "TestableDataStructure" is part of ahmed.library.
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

// <editor-fold defaultstate="collapsed" desc="data tester">
public interface TestableDataStructure<T> {

    public void addT(T o);

    public void insertT(T o, int index);

    public void deletT(int index);

    public T getT(int index);

    public int findT(T o);

}
