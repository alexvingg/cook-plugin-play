/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package play.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author alex
 */
public class ModificaArquivos
{

    private static ModificaArquivos menu;

    public static ModificaArquivos getInstance()
    {
        if (menu == null) {
            menu = new ModificaArquivos();
        }
        return menu;
    }

    public void inserirMenu(String path, String controller) throws FileNotFoundException, IOException
    {
        File f = new File(path + "/main.html");
        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String nomeController = "<li class='ui-widget-header'><a href='@{" + controller + ".index()}'>" + controller + "</a></li>";
        sb.insert(sb.indexOf("<!-- Fim Menu -->"), nomeController + "\n");
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(sb.toString());
        arquivoGravar.close();
    }

    public void alteraConfData(String path) throws FileNotFoundException, IOException
    {
        File f = new File(path + "/conf/application.conf");

        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String texto_alterado = sb.toString().replaceAll("date.format=yyyy-MM-dd", "date.format=dd/MM/yyyy");
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(texto_alterado);
        arquivoGravar.close();
    }
    /**
     *
     * @param path
     * @param op_rest 1 json 2 XML
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void insereConfgRest(String path, int op_rest)throws FileNotFoundException, IOException
    {
        File f = new File(path + "/conf/application.conf");

        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String texto_alterado;
        if(op_rest == 1)
        {
            sb.insert(sb.length(), "\n rest=json");
            texto_alterado = sb.toString();
        }else if(op_rest == 2){
            sb.insert(sb.length(), "\n rest=xml");
            texto_alterado = sb.toString();
        }else{
            sb.insert(sb.length(), "\n rest=json");
            texto_alterado = sb.toString();
        }
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(texto_alterado);
        arquivoGravar.close();
    }

    public void alteraRouteWeb(String path) throws FileNotFoundException, IOException
    {
        File f = new File(path + "/conf/routes");
        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String rotas = "# Rotas Padrão \n"+
                        "GET     /{controller}                           {controller}.index \n"+
                        "GET     /{controller}/view/{id}                 {controller}.view \n" +
                        "GET     /{controller}/form/{id}                 {controller}.form \n" +
                        "GET     /{controller}/delete/{id}               {controller}.delete \n"+
                        "POST    /{controller}/save/{id}                 {controller}.save \n";
        sb.insert(sb.indexOf("GET     /public/                                staticDir:public"), rotas + "\n");
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(sb.toString());
        arquivoGravar.close();
    }

    public void alteraRouteRest(String path) throws FileNotFoundException, IOException
    {
        File f = new File(path + "/conf/routes");
        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String rotas = "# Rotas Rest-Padrão \n" +
                        "POST    /ws/login                                  RestDefaultController.login \n"+
                        "GET     /ws/logout                                 RestDefaultController.logout \n"+
                        "GET     /ws/{controller}                           {controller}.list \n"+
                        "POST    /ws/{controller}                           {controller}.save \n"+
                        "GET     /ws/{controller}/{id}                      {controller}.view \n"+
                        "DELETE  /ws/{controller}/{id}                      {controller}.delete \n"+
                        "POST    /ws/{controller}/{id}                      {controller}.save \n"+
                        "PUT     /ws/{controller}/{id}                      {controller}.save \n";
        sb.insert(sb.indexOf("GET     /public/                                staticDir:public"), rotas + "\n");
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(sb.toString());
        arquivoGravar.close();
    }

        public void alteraRouteInicial(String path, String controller) throws FileNotFoundException, IOException
    {
        File f = new File(path + "/conf/routes");
        FileReader file = new FileReader(f);
        int c;
        StringBuffer sb = new StringBuffer();
        do {
            c = file.read();
            if (c != -1) {
                sb.append((char) c);
            }
        } while (c != -1);
        String texto = sb.toString();
        sb = new StringBuffer(texto);
        String rotas = "GET     /                                       "+controller+".index";
        String texto_alterado = sb.toString().replaceAll("GET     /                                       Application.index", rotas);
        FileWriter arquivoGravar = new FileWriter(f);
        arquivoGravar.write(texto_alterado);
        arquivoGravar.close();
    }
}
