/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.database;

import cook.util.PrintUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import play.exception.BancoDadosException;
import play.exception.ConfiguracaoException;

/**
 *
 * @author alex
 */
public class ResourceUtil
{

    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcDriver;
    private String jdbcPass;
    Properties bundle = new Properties();
    public static ResourceUtil resourceUtil = null;

    public void setJdbcDriver(String jdbcDriver)
    {
        this.jdbcDriver = jdbcDriver;
    }

    public void setJdbcPass(String jdbcPass)
    {
        this.jdbcPass = jdbcPass;
    }

    public void setJdbcUrl(String jdbcUrl)
    {
        this.jdbcUrl = jdbcUrl;
    }

    public void setJdbcUser(String jdbcUser)
    {
        this.jdbcUser = jdbcUser;
    }

    private void LoadProperties(String fullPath) throws FileNotFoundException
    {

        InputStream is = null;
        try {
            is = new java.io.FileInputStream(fullPath);
            //is = ResourceUtil.class.getResourceAsStream(fullPath);
            if (is != null) {
                bundle.load(is);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    public static ResourceUtil getInstance()
    {
        if (resourceUtil == null) {
            resourceUtil = new ResourceUtil();
        }
        return resourceUtil;
    }

    public ResourceUtil()
    {
        super();
    }

    public int getXmlJson(String caminho) throws FileNotFoundException
    {
        try {
            File f = new File(caminho);
            String filePath = f.getPath();
            if (!new File(filePath).exists()) {
                PrintUtil.outn("A pasta conf nao existe !");
                filePath = "";
            }
            filePath += "/conf/application.conf";
            LoadProperties(filePath);
        } catch (Exception e) {
            throw new FileNotFoundException("Verifique o arquivo application.conf");
        }
        if(this.bundle.getProperty("rest").equals("json"))
        {
            return 1;
        }else if(this.bundle.getProperty("rest").equals("xml")){
            return 2;
        }else{
            return 1;
        }
    }

    public void ResourceUtil(String caminho) throws ConfiguracaoException, FileNotFoundException, BancoDadosException
    {
        try {
            File f = new File(caminho);
            String filePath = f.getParent();
            if (!new File(filePath).exists()) {
                PrintUtil.outn("A pasta conf nao existe !");
                filePath = "";
            }
            filePath += "/conf/application.conf";
            //PrintUtil.outn("Carregando as informações do arquivo !");


            LoadProperties(filePath);
            if (!verificaMysql()) {
                this.setJdbcUrl(this.bundle.getProperty("db.url"));
                this.setJdbcDriver(this.bundle.getProperty("db.driver"));
                this.setJdbcUser(this.bundle.getProperty("db.user"));
                this.setJdbcPass(this.bundle.getProperty("db.pass"));
            }
        } catch (FileNotFoundException fex) {
            PrintUtil.outn("Nao foi realizada a conexao.");
            throw new FileNotFoundException("Verifique o arquivo application.conf");
        } catch (BancoDadosException ex) {
            throw new BancoDadosException("Nao e possivel gerar models pois nao existem tabelas"
                    + " no banco");
        } catch (Exception ex) {
            PrintUtil.outn("Nao foi realizada a conexao.");
            throw new ConfiguracaoException("Verifique o arquivo application.conf");
        }
    }

    public String getJdbcUrl()
    {
        return this.jdbcUrl;
    }

    public String getJdbcDriver()
    {
        return this.jdbcDriver;
    }

    public String getJdbcUser()
    {
        return this.jdbcUser;
    }

    public String getJdbcPass()
    {
        return this.jdbcPass;
    }

    public boolean verificaArquivo()
    {
        if (this.getJdbcDriver().equals("")) {
            PrintUtil.outn("O driver de conexao está vazio !");
            return false;
        } else if (this.getJdbcUrl().equals("")) {
            PrintUtil.outn("A url de conexao está vazio !");
            return false;
        } else {
            return true;
        }
    }

    private Boolean verificaMysql() throws BancoDadosException
    {
        if (bundle.getProperty("db") != null) {
            if (bundle.getProperty("db").toLowerCase().equals("mem")
                    || bundle.getProperty("db").toLowerCase().equals("fs")) {
                throw new BancoDadosException("");
            }

            if (this.bundle.getProperty("db") != null && this.bundle.getProperty("db").substring(0, 5).toLowerCase().equals("mysql")) {
                String[] valores = this.bundle.getProperty("db").split(":");
                String senha[] = valores[2].split("@");
                this.setJdbcDriver("com.mysql.jdbc.Driver");
                this.setJdbcUrl("jdbc:mysql://localhost:3306/" + senha[1]);
                this.setJdbcUser(valores[1]);
                this.setJdbcPass(senha[0]);
                return true;
            }
        }
        return false;
    }
}
