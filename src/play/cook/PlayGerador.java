/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.cook;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import play.exception.ModelException;
import java.sql.DatabaseMetaData;
import cook.core.FreemarkerWrapper;
import cook.core.ResultProcess;
import cook.util.FileUtil;
import cook.util.PrintUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import play.database.Atributo;
import play.database.Conexao;
import play.database.CriaModel;
import play.database.ResourceUtil;
import play.exception.BancoDadosException;
import play.exception.ConfiguracaoException;
import play.exception.ControllerException;
import play.exception.DriverException;
import play.utils.AtributoUtil;
import play.utils.ClasseUtil;
import play.utils.Inflector;
import play.utils.ModificaArquivos;

/**
 *
 * @author alex
 */
public class PlayGerador
{

    private static PlayGerador play = null;

    public PlayGerador()
    {
        
    }

    /**
     * 
     * @return
     */
    public static PlayGerador getPlayGerador()
    {
        if (PlayGerador.play == null) {
            PlayGerador.play = new PlayGerador();
        }
        return PlayGerador.play;
    }

    /**
     * Metodo que cria o a view vazio com exemplos.
     * @param Path do model
     * @param Path da aplicação play
     * @param Nome do controller
     */
    public ResultProcess criaViewVazio(String PATH_OUT_MODEL, String PATH_OUT, String nomeClasse)
    {
        ResultProcess out = new ResultProcess();
        try {

            String templates[] = new String[3];
            templates[0] = "formVazio";
            templates[1] = "indexVazio";
            templates[2] = "viewVazio";

            if (!(new File(PATH_OUT + "/" + nomeClasse)).exists()) {
                new File(PATH_OUT + "/" + nomeClasse + "/").mkdir();
                PrintUtil.outn("A pasta da view " + nomeClasse + " foi criada!");
                PrintUtil.outn("");
                ModificaArquivos.getInstance().inserirMenu(PATH_OUT, nomeClasse);
            }
            FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
            FreemarkerWrapper.getInstance().addVar("class", nomeClasse);
            for (int i = 0; i < 3; i++) {
                String arq = FreemarkerWrapper.getInstance().parseTemplate(templates[i] + ".ftl");                
                PrintUtil.outn("" + PATH_OUT + "/" + nomeClasse + "/" + templates[i].replaceFirst("Vazio", "") + ".html");
                FileUtil.saveToPath(PATH_OUT + "/" + nomeClasse + "/" + templates[i].replaceFirst("Vazio", "") + ".html", arq);
                out.setResultProcess(ResultProcess.SUCESS, "Gerado com sucesso.");
            }
        } catch (ControllerException nc) {
            out.setResultProcess(ResultProcess.ERROR, nc.getMessage());
        } catch (Exception ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } finally {
            return out;
        }
    }

    /**
     * Metodo que cria o controller vazio com exemplos.
     * @param Path do model
     * @param Path da aplicação play
     * @param Nome do controller
     */
    public ResultProcess criaControllerVazio(String PATH_OUT_MODEL, String PATH_OUT, String nomeClasse)
    {
        ResultProcess out = new ResultProcess();
        String arq;
        nomeClasse = Inflector.getInstance().humanize(nomeClasse);
        try {
            FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
            FreemarkerWrapper.getInstance().addVar("class", nomeClasse);
            arq = FreemarkerWrapper.getInstance().parseTemplate("controllerVazio.ftl");
            PrintUtil.outn("Salvando arquivo: " + PATH_OUT + "/" + nomeClasse + ".java");
            this.criaRotaInicial(PATH_OUT.replaceFirst("/app/controllers", "/conf"), nomeClasse);
            FileUtil.saveToPath(PATH_OUT + "/" + nomeClasse + ".java", arq);
            out.setResultProcess(ResultProcess.SUCESS, "Gerado com sucesso.");
        } catch (Exception ex) {
            Logger.getLogger(PlayGerador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    /**
     * Metodo que cria o controller.
     * @param Path do model
     * @param Path da aplicação play
     * @param Nome do controller
     */
    public ResultProcess criaController(String PATH_OUT_MODEL, String PATH_OUT)
    {
        ArrayList<String> models = new ArrayList<String>();
        FilenameFilter filter = filtroArquivo();

        File[] arquivos = new File(PATH_OUT_MODEL).listFiles(filter);
        ResultProcess out = new ResultProcess();
        ClasseUtil classe = new ClasseUtil();

        try {

            if (arquivos.length == 0) {
                throw new ModelException("Nao existem models criados !");
            }

            PrintUtil.outn("Qual controller deseja criar ?");
            PrintUtil.outn("");
            for (int i = 0; i < arquivos.length; i++) {
                models.add(Inflector.getInstance().pluralize(arquivos[i].getName().replaceFirst(".java", "")));
                PrintUtil.outn(PrintUtil.getYellowFont() + "[" + i + "] "
                        + Inflector.getInstance().pluralize(arquivos[i].getName().replaceFirst(".java", ""))
                        + PrintUtil.getColorReset());
            }
            PrintUtil.outn("");
            int op = PrintUtil.inInt("Digite a opcao: ");
            PrintUtil.outn("");

            while (op > models.size() - 1) {
                PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta." + PrintUtil.getColorReset());
                PrintUtil.outn("");
                op = PrintUtil.inInt("Digite a opcao: ");
                PrintUtil.outn("");
            }

            String nameClass = models.get(op);

            String nomeController = nameClass;
            if (verificaControllerIgual(PATH_OUT.replaceFirst("/app/controllers", "/conf"), nameClass)) {
                String classeString = classe.verificaStringClass(PATH_OUT + "/" + nomeController + ".java");
                if (classeString.contains("extends RestDefaultController")) {
                        PrintUtil.outn(PrintUtil.getRedFont() + "Nao e recomendavel sobrescrever o controller pois sao de tipos diferentes." + PrintUtil.getColorReset());
                        nomeController = PrintUtil.inString("Informe um novo nome: ");
                        nomeController = Inflector.getInstance().humanize(nomeController);
                        PrintUtil.outn("");
                }

            }
            List<AtributoUtil> atributos = classe.getAtributes(PATH_OUT_MODEL + "/" + Inflector.getInstance().singularize(nameClass) + ".java");
            List<AtributoUtil> atributosRelacao = new ArrayList<AtributoUtil>();

            for (int i = 0; i < atributos.size(); i++) {
                if (atributos.get(i).getAnnotation() != null) {
                    if (atributos.get(i).getAnnotation().toString().equals("ManyToMany")
                            || atributos.get(i).getAnnotation().toString().equals("ManyToOne")) {
                        atributosRelacao.add(atributos.get(i));
                    }
                }
            }


            FreemarkerWrapper.getInstance().addVar("class", nameClass);
            FreemarkerWrapper.getInstance().addVar("controlador", nomeController);
            FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
            FreemarkerWrapper.getInstance().addVar("atributosRelacao", atributosRelacao);
            FreemarkerWrapper.getInstance().addVar("atributos", atributos);

            String arq = FreemarkerWrapper.getInstance().parseTemplate("controller.ftl");

            PrintUtil.outn("Salvando arquivo: " + PATH_OUT + "/" + nomeController + ".java");

            this.criaRotaInicial(PATH_OUT.replaceFirst("/app/controllers", "/conf"), nomeController);

            FileUtil.saveToPath(PATH_OUT + "/" + nomeController + ".java", arq);

            out.setResultProcess(ResultProcess.SUCESS, "Gerado com sucesso.");

        } catch (Exception ex) {

            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());

        } finally {

            return out;

        }
    }

    /**
     * Metodo que cria o filtro para .java
     * @return O filtro .java
     */
    private FilenameFilter filtroArquivo()
    {
        FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".java");
            }
        };
        return filter;
    }

    /**
     * Metodo que cria as views
     * @param Path do model
     * @param Path da aplicação play
     * @param Nome do controller
     */
    public ResultProcess criaView(String PATH_OUT_MODEL, String PATH_OUT)
    {
        ResultProcess out = new ResultProcess();
        ClasseUtil classe = new ClasseUtil();
        ArrayList<String> controller = new ArrayList<String>();
        FilenameFilter filter = this.filtroArquivo();
        File[] arquivos = new File(PATH_OUT.replaceFirst("views", "controllers")).listFiles(filter);

        try {

            if (arquivos.length == 0 || arquivos.length == 2) {
                throw new ControllerException("Nao existem controllers criados !");
            }

            PrintUtil.outn("Qual view deseja criar ?");
            PrintUtil.outn("");
            int cont = 0;
            for (int i = 0; i < arquivos.length; i++) {

                if (!(arquivos[i].getName().equals("Application.java") 
                        || arquivos[i].getName().equals("DefaultController.java")
                        || arquivos[i].getName().equals("RestDefaultController.java"))) {
                    controller.add(arquivos[i].getName().replaceFirst(".java", ""));
                    PrintUtil.outn(PrintUtil.getYellowFont() + "[" + cont + "] "
                            + arquivos[i].getName().replaceFirst(".java", "")
                            + PrintUtil.getColorReset());
                    cont++;
                }
            }
            PrintUtil.outn("");
            int op = PrintUtil.inInt("Digite a opcao: ");
            PrintUtil.outn("");
            while (op > controller.size() - 1) {
                PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta." + PrintUtil.getColorReset());
                PrintUtil.outn("");
                op = PrintUtil.inInt("Digite a opcao: ");
                PrintUtil.outn("");
            }
            String nameClass = controller.get(op);
            List<AtributoUtil> atributos = classe.getAtributes(PATH_OUT_MODEL + "/" + Inflector.getInstance().singularize(nameClass) + ".java");
            List<AtributoUtil> atributosSemRelacao = new ArrayList<AtributoUtil>();
            List<AtributoUtil> atributosRelacao = new ArrayList<AtributoUtil>();

            for (int i = 0; i < atributos.size(); i++) {
                if (atributos.get(i).getAnnotation() != null) {
                    if (atributos.get(i).getAnnotation().toString().equals("ManyToMany")
                            || atributos.get(i).getAnnotation().toString().equals("ManyToOne")
                            || atributos.get(i).getAnnotation().toString().equals("OneToMany")) {
                        atributosRelacao.add(atributos.get(i));
                    }
                }
            }
            for (int i = 0; i < atributos.size(); i++) {
                if (atributos.get(i).getAnnotation() == null) {
                    atributosSemRelacao.add(atributos.get(i));
                }
            }
            String templates[] = new String[3];
            templates[0] = "form";
            templates[1] = "index";
            templates[2] = "view";

            if (!(new File(PATH_OUT + "/" + nameClass)).exists()) {
                new File(PATH_OUT + "/" + nameClass + "/").mkdir();
                PrintUtil.outn("A pasta da view " + nameClass + " foi criada!");
                PrintUtil.outn("");
                ModificaArquivos.getInstance().inserirMenu(PATH_OUT, nameClass);
            } 

            for (int i = 0; i < 3; i++) {
                FreemarkerWrapper.getInstance().addVar("class", nameClass);
                FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
                FreemarkerWrapper.getInstance().addVar("atributosRelacao", atributosRelacao);
                FreemarkerWrapper.getInstance().addVar("atributosSemRelacao", atributosSemRelacao);
                FreemarkerWrapper.getInstance().addVar("atributos", atributos);


                String arq = FreemarkerWrapper.getInstance().parseTemplate(templates[i] + ".ftl");


                PrintUtil.outn("" + PATH_OUT + "/" + nameClass + "/" + templates[i] + ".html");

                FileUtil.saveToPath(PATH_OUT + "/" + nameClass + "/" + templates[i] + ".html", arq);


            }
            out.setResultProcess(ResultProcess.SUCESS, "Gerado com sucesso.");
        } catch (ControllerException nc) {
            out.setResultProcess(ResultProcess.ERROR, nc.getMessage());
        } catch (Exception ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } finally {
            return out;
        }
    }

    /**
     * Metodo que cria o model vazio com exemplos para o usuario.
     * @param PATH_OUT_MODEL
     * @param PATH_OUT
     * @param PATH_ARQUIVOS
     * @param nomeClasse Nome da classe que o usuário vai criar
     * @return
     */
    public ResultProcess criaModelVazio(String PATH_OUT_MODEL, String PATH_OUT, String PATH_ARQUIVOS, String nomeClasse)
    {
        ResultProcess out = new ResultProcess();

        try {
            FreemarkerWrapper.getInstance().addVar("class", Inflector.getInstance().humanize(nomeClasse));
            FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
            String arq = FreemarkerWrapper.getInstance().parseTemplate("modelVazio.ftl");
            FileUtil.saveToPath(PATH_OUT_MODEL + "/" + Inflector.getInstance().humanize(nomeClasse) + ".java", arq);
            out.setResultProcess(ResultProcess.SUCESS, "Model criado com sucesso !");
        } catch (Exception e) {
            out.setResultProcess(ResultProcess.ERROR, e.getMessage());
        }
        return out;
    }

    /**
     * Metodo que cria a view pegando informações do banco
     * @param PATH_OUT_MODEL
     * @param PATH_OUT
     * @param PATH_ARQUIVOS
     * @return
     */
    public ResultProcess criaModel(String PATH_OUT_MODEL, String PATH_OUT, String PATH_ARQUIVOS)
    {
        ResultProcess out = new ResultProcess();
        CriaModel model;
        DatabaseMetaData metadata;
        ResultSet rs;
        ResourceUtil conf = ResourceUtil.getInstance();
        try {
            conf.ResourceUtil(PATH_ARQUIVOS + "/conf");
            Conexao c = new Conexao(conf.getJdbcUrl(), conf.getJdbcDriver(), conf.getJdbcUser(), conf.getJdbcPass());
            model = new CriaModel(c);
            model.setCaminhoModel(PATH_OUT_MODEL);
            List<String> tabelas = null;
            String[] tableType = {"TABLE"};
            c.openConnection();
            List<Atributo> lista_atributos = new ArrayList<Atributo>();
            List<AtributoUtil> la_utils = new ArrayList<AtributoUtil>();
            metadata = (DatabaseMetaData) c.getConnection().getMetaData();

            rs = metadata.getTables(null, null, "%", tableType);
            if (!rs.next()) {
                throw new BancoDadosException("Nao e possivel gerar models pois nao existem tabelas"
                        + " no banco");
            }

            rs = metadata.getTables(null, null, "%", tableType);
            PrintUtil.outn("");
            PrintUtil.outn("Qual tabela voce quer criar o model ?");
            PrintUtil.outn("");
            tabelas = new ArrayList<String>();
            int cont = 0;
            while (rs.next()) {
                tabelas.add(rs.getString("TABLE_NAME"));
                PrintUtil.outn(PrintUtil.getYellowFont() + "[" + cont + "] " + rs.getString("TABLE_NAME") + PrintUtil.getColorReset());
                cont++;
            }
            rs.close();
            PrintUtil.outn("");
            int valor = PrintUtil.inInt("Digite a opcao: ");
            PrintUtil.outn("");
            while (valor > tabelas.size()) {
                PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta." + PrintUtil.getColorReset());
                PrintUtil.outn("");
                valor = PrintUtil.inInt("Digite a opcao: ");
                PrintUtil.outn("");
            }
            String tabela = tabelas.get(valor);
            LinkedHashMap<String, Atributo> listaAtributos = model.criarAtributo(tabela);
            Set HostKeys = listaAtributos.keySet();
            Iterator It = HostKeys.iterator();
            String display = "";
            while (It.hasNext()) {
                AtributoUtil a = new AtributoUtil();
                List l = new ArrayList();
                String key = (String) (It.next());
                lista_atributos.add(listaAtributos.get(key));
                String annotation = "";
                if (listaAtributos.get(key).getAnnotation() != null) {
                    l = listaAtributos.get(key).getAnnotation();
                }
                for (int i = 0; i < l.size(); i++) {
                    annotation = annotation + l.get(i) + "\n    ";
                }
                if (!listaAtributos.get(key).isPk()) {
                    a.setAnnotation(annotation);
                    a.setNome(listaAtributos.get(key).getField());
                    a.setTipo(listaAtributos.get(key).getType(), true);
                    la_utils.add(a);
                }
                if (listaAtributos.get(key).isDisplay()) {
                    display = listaAtributos.get(key).getField();
                }
            }

            String classe = Inflector.getInstance().humanize(tabela);

            String[] nomeClasse = classe.split(" ");

            String nomeClasseCorreta = "";

            for (int i = 0; i < nomeClasse.length; i++) {
                nomeClasseCorreta += Inflector.getInstance().humanize(nomeClasse[i]);
            }

            FreemarkerWrapper.getInstance().addVar("class", Inflector.getInstance().singularize(nomeClasseCorreta));
            FreemarkerWrapper.getInstance().addVar("tabela", tabela);
            FreemarkerWrapper.getInstance().addVar("atributos", la_utils);
            FreemarkerWrapper.getInstance().addVar("displayField", display);
            String arq = FreemarkerWrapper.getInstance().parseTemplate("model.ftl");
            FileUtil.saveToPath(PATH_OUT_MODEL + "/" + Inflector.getInstance().singularize(nomeClasseCorreta) + ".java", arq);

            out.setResultProcess(ResultProcess.SUCESS, "Model criado com sucesso !");
        } catch (SQLException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (ModelException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (ClassNotFoundException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (DriverException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (FileNotFoundException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (ConfiguracaoException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (BancoDadosException ex) {
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
        } catch (Exception ex) {
            out.setResultProcess(ResultProcess.ERROR, "Erro, " + ex.getMessage());
        }
        return out;
    }

    /**
     * Cria controlador Rest
     * @param PATH_OUT_MODEL Caminho do model que referencia o controller.
     * @param PATH_OUT  Caminho onde ele vai ser criado.
     * @return
     */
    public ResultProcess criaControllerRest(String PATH_OUT_MODEL, String PATH_OUT)
    {
        ArrayList<String> models = new ArrayList<String>();
        FilenameFilter filter = filtroArquivo();

        File[] arquivos = new File(PATH_OUT_MODEL).listFiles(filter);
        ResultProcess out = new ResultProcess();
        ClasseUtil classe = new ClasseUtil();

        try {

            if (arquivos.length == 0) {
                throw new ModelException("Nao existem models criados !");
            }

            PrintUtil.outn("Qual controller-rest deseja criar ?");
            PrintUtil.outn("");
            for (int i = 0; i < arquivos.length; i++) {
                models.add(Inflector.getInstance().pluralize(arquivos[i].getName().replaceFirst(".java", "")));
                PrintUtil.outn(PrintUtil.getYellowFont() + "[" + i + "] "
                        + Inflector.getInstance().pluralize(arquivos[i].getName().replaceFirst(".java", ""))
                        + PrintUtil.getColorReset());
            }
            PrintUtil.outn("");
            int op = PrintUtil.inInt("Digite a opcao: ");
            PrintUtil.outn("");

            while (op > models.size() - 1) {
                PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta." + PrintUtil.getColorReset());
                PrintUtil.outn("");
                op = PrintUtil.inInt("Digite a opcao: ");
                PrintUtil.outn("");
            }

            String nameClass = models.get(op);

            List<AtributoUtil> atributos = classe.getAtributes(PATH_OUT_MODEL + "/" + Inflector.getInstance().singularize(nameClass) + ".java");
            List<AtributoUtil> atributosRelacao = new ArrayList<AtributoUtil>();

            for (int i = 0; i < atributos.size(); i++) {
                if (atributos.get(i).getAnnotation() != null) {
                    if (atributos.get(i).getAnnotation().toString().equals("ManyToMany")
                            || atributos.get(i).getAnnotation().toString().equals("ManyToOne")) {
                        atributosRelacao.add(atributos.get(i));
                    }
                }
            }
            String nomeController = nameClass;
            if (verificaControllerIgual(PATH_OUT.replaceFirst("/app/controllers", "/conf"), nameClass)) {
                String classeString = classe.verificaStringClass(PATH_OUT + "/" + nomeController + ".java");
                if (classeString.contains("extends DefaultController")) {
                        PrintUtil.outn(PrintUtil.getRedFont() + "Nao e recomendavel sobrescrever o controller pois sao de tipos diferentes." + PrintUtil.getColorReset());
                        nomeController = PrintUtil.inString("Informe um novo nome: ");
                        nomeController = Inflector.getInstance().humanize(nomeController);
                        PrintUtil.outn("");
                }

            }

            String caminho_conf = PATH_OUT.replaceAll("/app/controllers", "");

            FreemarkerWrapper.getInstance().addVar("op_rest", ResourceUtil.getInstance().getXmlJson(caminho_conf));
            FreemarkerWrapper.getInstance().addVar("controlador", nomeController);
            FreemarkerWrapper.getInstance().addVar("class", nameClass);
            FreemarkerWrapper.getInstance().addVar("inflector", Inflector.getInstance());
            FreemarkerWrapper.getInstance().addVar("atributosRelacao", atributosRelacao);
            FreemarkerWrapper.getInstance().addVar("atributos", atributos);

            String arq = FreemarkerWrapper.getInstance().parseTemplate("controller_rest.ftl");

            PrintUtil.outn("Salvando arquivo: " + PATH_OUT + "/" + nomeController + ".java");

            FileUtil.saveToPath(PATH_OUT + "/" + nomeController + ".java", arq);

            out.setResultProcess(ResultProcess.SUCESS, "Gerado com sucesso.");

            PrintUtil.outn("");
            PrintUtil.outn("Para acessar use : http://localhost:9000/ws/" + Inflector.getInstance().lowerCamelCase(nomeController));

        } catch (Exception ex) {

            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());

        }
        return out;
    }

    private boolean verificaControllerIgual(String caminho, String controller)
    {
        FilenameFilter filter = this.filtroArquivo();
        File[] arquivos = new File(caminho.replaceAll("/conf", "/app/controllers")).listFiles(filter);

        int cont = 0;
        for (int i = 0; i < arquivos.length; i++) {
            if ((arquivos[i].getName().equals(controller + ".java"))) {
                cont++;
            }
        }
        if (cont == 1) {
            return true;
        }
        return false;
    }

    /**
     * Cria rota inicial
     */
    public void criaRotaInicial(String caminho, String controller) throws IOException, Exception
    {
        FilenameFilter filter = this.filtroArquivo();
        File[] arquivos = new File(caminho.replaceAll("/conf", "/app/controllers")).listFiles(filter);
        ClasseUtil c = new  ClasseUtil();
        String caminho_controller = caminho.replaceAll("/conf", "/app/controllers");
        int cont = 0;
        for (int i = 0; i < arquivos.length; i++) {
            if (!(arquivos[i].getName().equals("Application.java")
                    || arquivos[i].getName().equals("DefaultController.java")
                    || arquivos[i].getName().equals("RestDefaultController.java"))) {
                String classeString = c.verificaStringClass(caminho_controller + "/" + arquivos[i].getName());
                if(classeString.contains("extends DefaultController")){
                    cont++;
                }

            }
        }

        if (cont == 0) {
            ModificaArquivos.getInstance().alteraRouteInicial(caminho.replaceAll("/conf", ""), controller);
        }
    }
}
