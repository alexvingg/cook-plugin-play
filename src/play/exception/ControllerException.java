/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.exception;

/**
 *
 * @author alex
 */
public class ControllerException extends Exception
{

    private String msg;

    public ControllerException(String msg)
    {
        this.msg = msg;
    }

    @Override
    public String getMessage()
    {
        return this.msg;
    }
}
