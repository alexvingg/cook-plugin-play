/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.database;

import cook.util.PrintUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import play.exception.DriverException;

/**
 *
 * @author alex
 */
public class Conexao
{

    public final static int POSTGRES = 1;
    public final static int MYSQL = 2;
    private int type;
    private String url;
    private String driver;
    private String user;
    private String password;
    private Connection con;

    public Conexao(String url, String driver, String user, String password)
    {
        this.user = user;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.setBanco();
    }

    private void setBanco()
    {
        try {
            if (this.driver.equals("org.postgresql.Driver")) {
                this.type = 1;
            } else if (this.driver.equals("com.mysql.jdbc.Driver")) {
                this.type = 2;
            }
        } catch (Exception e) {
        }
    }

    public void openConnection() throws ClassNotFoundException, DriverException, SQLException
    {
        if (type == POSTGRES) {
            driver = "org.postgresql.Driver";
        } else if (type == MYSQL) {
            driver = "com.mysql.jdbc.Driver";
        }
        if(driver == null)
        {
            PrintUtil.outn("Nao foi realizada a conexao.");
            throw new DriverException("Verifique o arquivo application.conf");
        }
        Class.forName(driver);
        try {
            con = DriverManager.getConnection(getUrl(), user, password);
        } catch (SQLException ex) {
            PrintUtil.outn("Nao foi realizada a conexao.");
            throw new SQLException("Verifique o arquivo application.conf");
        }
    }

    public void closeConnection()
    {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
           PrintUtil.outn("Nao foi possivel encerrar conexao com o banco de dados");
        }
    }

    public Connection getConnection()
    {
        return this.getCon();
    }

    private String getUrl()
    {
        if (type == POSTGRES) {
            return url;
        } else if (type == MYSQL) {
            return url;
        }
        return null;
    }

    /**
     * @return the drive
     */
    public String getDrive()
    {
        return driver;
    }

    /**
     * @param drive the drive to set
     */
    public void setDrive(String drive)
    {
        this.driver = drive;
    }

    /**
     * @return the login
     */
    public String getLogin()
    {
        return user;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login)
    {
        this.user = login;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the con
     */
    public Connection getCon()
    {
        return con;
    }

    /**
     * @param con the con to set
     */
    public void setCon(Connection con)
    {
        this.con = con;
    }

    public int getBanco()
    {
        return this.type;
    }
}
