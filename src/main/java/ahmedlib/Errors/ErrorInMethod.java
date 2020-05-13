/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedlib.Errors;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
@SuppressWarnings("serial")
public class ErrorInMethod extends RuntimeException {

    String errorCause;
    String errorLocation;

    public ErrorInMethod(String errorLocation, String errorCause) {
        this.errorCause = errorCause;
        this.errorLocation = errorLocation;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " there was error in " + errorLocation
                + " due to " + errorCause;
    }

}
