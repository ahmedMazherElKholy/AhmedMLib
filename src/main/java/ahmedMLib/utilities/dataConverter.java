/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedMLib.utilities;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 * @param <D> the data type to convert
 * @param <C> the target data type to convert to
 */
public interface dataConverter<D, C> {

    public C convert(D data);

}
