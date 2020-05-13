/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedlib.utilities;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public class StringList {

    String str;
    String sep;

    /**
     *
     * @param str the string representation of the list of items
     * @param sep the separator that separate items
     */
    public StringList(String str, String sep) {
        this.str = str;
        this.sep = sep;
    }

    public boolean hasSubStr(String sub) {
        for (String s : str.split(sep)) {
            if (s.equals(sub)) {
                return true;
            }
        }
        return false;
    }

}
