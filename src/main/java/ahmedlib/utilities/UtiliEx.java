/* 
 * Copyright (C) 2016 Ahmed Mazher <ahmzel2012@gmail.com>
 *
 * This file "UtiliEx" is part of ahmed.library.
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

import static ahmedlib.utilities.MathExtension.fac;

/**
 *
 * @author ahmed mazher
 */
public class UtiliEx {

    public static boolean isvov(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'y' || c == 'o' || c == 'u';
    }

    public static boolean twoSuccessive(char[] row) {
        boolean res = false;
        for (int i = 0; i < row.length - 1; i++) {
            String t1 = String.valueOf(row[i]);
            String t2 = String.valueOf(row[i + 1]);
            if (t1.equalsIgnoreCase(t2)) {
                res = true;
            }
        }
        return res;
    }

    public static String[] permu(String[] a) {
        int n = a.length;
        String[] per = new String[fac(n)];
//        for(int i=0;i<per.length;i++){
//            per[i]="";
//        }
        String[] temp;
        if (n > 1) {
            for (int i = 0; i < n; i++) {
                String[] remain = new String[n - 1];
                for (int j = 0, d = 0; j < n - 1; j++, d++) {
                    if (i == j) {
                        d++;
                    }
                    remain[j] = a[d];
                }
                temp = permu(remain);
                int block = per.length / n * i;
                for (int j = 0; j < temp.length; j++) {
                    per[j + block] = a[i];
                    per[j + block] += temp[j];
                }
            }
        } else {
            per[0] = a[0];
        }
        return per;
    }

    public static char[][] permu(char[] a) {
        int n = a.length;
        char[][] per = new char[fac(n)][n];
//        for(int i=0;i<per.length;i++){
//            per[i]="";
//        }
        char[][] temp;
        if (n > 1) {
            for (int i = 0; i < n; i++) {
                char[] remain = new char[n - 1];
                for (int j = 0, d = 0; j < n - 1; j++, d++) {
                    if (i == j) {
                        d++;
                    }
                    remain[j] = a[d];
                }
                temp = permu(remain);
                int block = per.length / n * i;
                for (int j = 0; j < temp.length; j++) {
                    per[j + block][0] = a[i];
                    System.arraycopy(temp[j], 0, per[j + block], 1, n - 1);
                }
            }
        } else {
            per[0] = a;
        }
        return per;
    }

    public static int[] charToInt(char[] c) {
        int[] temp = new int[c.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = Character.digit(c[i], 10);
        }
        return temp;
    }

    public static boolean twoWayWord(String w) {
        boolean res = true;
        char[] ch = w.toCharArray();
        for (int i = 0; i < ch.length / 2; i++) {
            if (ch[i] != ch[ch.length - 1 - i]) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static String appendspace(int length, String... s) {
        return "test";
    }

    public static String decode(String s, String[] look, String[] repl) {
        char[] ch = s.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            for (int j = 0; j < 10; j++) {
                if (String.valueOf(ch[i]).equals(look[j])) {
                    ch[i] = repl[j].charAt(0);
                }
            }
        }
        return String.copyValueOf(ch);
    }

    /**
     * @author ahmed mazher this function check if a given string is number by
     * iterating through its chars and check if they are digits with the
     * exception of first char which can be '+' or '-' and a point '.' which can
     * exist any where in the string but only once all the previous non digit
     * character can't exist alone their must be an digit also for whole string
     * to evaluate to number e.g "+.0" will evaluate to 0.0 but "+." gives error
     * this was my first simple approach but to improve performance in loops !?
     * i add some complexity by avoiding make the same check again if i had
     * "+0.1" i will check the char after plus to see if it digit then again
     * when we come to the '.' we will do the check again of the surrounding to
     * find a digit that including the char we test for it before so i decided
     * to maintain a property that the previous char to current checked char
     * must be a digit "if present" so we never check that the previous char is
     * digit only if needed we check the next char also when i check some thing
     * next to current char i advance the index of the loop so the next loop
     * will not check it again ^-^ i really don't know if this is necessary
     * @param s
     * @return
     *
     *
     */
    @SuppressWarnings("AssignmentToForLoopParameter")
    public static boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        char c0, c1, c2;
        boolean pointFound = false; //allow one point in number
        for (int i = 0; i < s.length(); i++) {
            c1 = s.charAt(i);
            if (!Character.isDigit(c1)) {
                if (c1 == '.') {
                    if (!pointFound) {
                        if (s.length() > i + 1 && Character.isDigit(s.charAt(i + 1))) {
                            pointFound = true;
                            i++;
                        } else if (i > 0) { // if their is a prevoius char
                            pointFound = true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if ((c1 == '+' || c1 == '-') && i == 0 && s.length() > 1) { //first char
                    if (Character.isDigit(c2 = s.charAt(i + 1))) { //have a digit after it
                        i++;
                        //maintain the property that a previous char during the next
                        //for loop is a digit i.e during next loop if i did'nt advance
                        //the i index the current checked char will be '.' which 
                        //have a '+' as a previous char which is not accepted 
                    } else if (c2 == '.' && s.length() > 2 && Character.isDigit(s.charAt(2))) {
                        pointFound = true;
                        i = 2;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static Number getNumber(String s) {
        return Double.parseDouble(s);
    }

    public static String strJoin(String separator, Object... pieces) {
        StringBuilder sb = new StringBuilder();
        if (pieces.length > 0) {
            sb.append(pieces[0]);
            for (int i = 1; i < pieces.length; i++) {
                sb.append(separator).append(pieces[i]);
            }
        }
        return sb.toString();
    }

}
