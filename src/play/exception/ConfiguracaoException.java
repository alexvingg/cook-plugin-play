/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package play.exception;

/**
 *
 * @author alex
 */
public class ConfiguracaoException extends Exception {
    private String msg;

    public ConfiguracaoException(String msg)
    {
        this.msg = msg;
    }

    @Override
    public String getMessage()
    {
        return this.msg;
    }
}
