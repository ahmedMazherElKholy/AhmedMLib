/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "Initializable" is part of ahmed.library.
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
package ahmedlib.utilities;

/**
 * @author ahmed mazher
 * @param <T> making class initializable my super class should implement methods
 * of initializable interface it has two methods 1) initialize method in which i
 * write code to do the following in order
 *
 * a) call setter methods and other initialization code b) make call to
 * overridable method putting them at end of the method
 *
 * 2) isInitialzed method to check if the initialization done or not when a call
 * to a method in my class that reference a variable that is initialized in
 * initialize method and initialization not done i throw my
 * NonInitializedInstanceException
 *
 * the subclass should override the initialize method and do the following in
 * order
 *
 * 1)initialize its variables 2)can call super method that don't throw
 * NonInitializedInstanceException 3)call super.initialize() method 4)call super
 * class setter methods "note that the super call the setters so to avoid our
 * setter been useless we should call it after super initialize method call"
 * 5)can call super method that throw NonInitializedInstanceException
 */
public interface Initializable<T> {

    /**
     * do the initialization job and return reference to the implementing class
     * current instance
     *
     * @return
     */
    T initialize();

    /**
     * test the variables that you initialize in the initialize method if it was
     * initialized or not "or what ever test you see appropriate"
     *
     * @return
     */
    boolean isInitialized();

}
