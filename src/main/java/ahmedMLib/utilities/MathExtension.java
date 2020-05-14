/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "MathExtension" is part of ahmed.library.
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
 *
 * @author Ahmed Mazher
 */
public class MathExtension {

    public static int fac(int n) {
        int r = n;
        for (int i = n - 1; i > 0; i--) {
            r *= i;
        }
        return r;
    }

    public static byte[] bitsArray(byte c) {
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
            temp[7 - i] = (byte) (c >> i & 1);
        }
        return temp;
    }

    public static String getZeroCode(byte[] bits) {
        String temp = "";
        byte flag = (byte) (bits[6] == 0 ? 1 : 0); //make the flag diffrent from the value of byte[i] during first itreation of for loop
        for (int i = 1; i < 8; i++) {
            if (bits[i] != flag) {
                flag = bits[i];
                temp += (flag == 0 ? " 00 " : " 0 ");
                temp += "0";
            } else {
                temp += "0";
            }
        }
        return temp;
    }

    public static String binaryONString(String s1, String s2, char operation) {
        String res = "";
        char[] ch1 = s1.toCharArray();
        char[] ch2 = s2.toCharArray();
        for (int i = 0; i < ch1.length; i++) {
            if (operation == '&') {
                res += Character.getNumericValue(ch1[i]) & Character.getNumericValue(ch2[i]);
            }
            if (operation == '^') {
                res += Character.getNumericValue(ch1[i]) ^ Character.getNumericValue(ch2[i]);
            }
            if (operation == '|') {
                res += Character.getNumericValue(ch1[i]) | Character.getNumericValue(ch2[i]);
            }
        }
        return res;
    }

}
