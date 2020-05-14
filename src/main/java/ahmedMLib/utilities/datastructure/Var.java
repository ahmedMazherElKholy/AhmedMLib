/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Var" is part of ahmed.library.
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
 * this class serve as container of single reference value
 * <br\>giving the capability of declaring generic variable where
 * <br\><br\>T is the type of variable
 * <br\> The field T val is the carried value
 *
 * @author Ahmed Mazher
 * @param <T> is type parameter which represent the type of contained variable
 */
public class Var<T> {

    /**
     * the contained value
     */
    @SuppressWarnings("PublicField")
    public T val;

    public Var() {
    }

    public Var(T var) {
        this.val = var;
    }

}
