/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "NonInitializedInstanceException" is part of ahmed.library.
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
package ahmedMLib.utilities;

/**
 * @author Ahmed mazher runtime exception to be thrown by method that reference
 * variables that are initialized by the initialize method of the initializable
 * interface you can test if initialization was done or not by isInialized
 * method of the same interface if false throw the exception
 */
public class NonInitializedInstanceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return super.getMessage()
                + " you must call instance.initialize() method "
                + "before calling this method";
    }

}
