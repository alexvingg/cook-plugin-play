/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.utils;

import cook.core.FreemarkerWrapper;
import cook.util.FileUtil;
import cook.util.PrintUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import play.cook.Play;

/**
 *
 * @author alex
 */
public class ArquivosPlay {

    private static ArquivosPlay arquivo = null;

    public boolean verificaExisteArquivos(String PATH_ARQUIVOS) {
        if (!(new File(PATH_ARQUIVOS + "/app/controllers/DefaultController.java")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/app/views/Application")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/app/views/tags")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/app/views/errors")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/app/views/main.html")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/conf/application.conf")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/conf/routes")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/conf/messages")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/public/images")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/public/javascripts")).exists()) {
            return false;
        } else if (!(new File(PATH_ARQUIVOS + "/public/stylesheets")).exists()) {
            return false;
        } else {
            return true;
        }
    }

    public void copiaArquivosPlay(String PATH_ARQUIVOS) {
        
        String verifica = PATH_ARQUIVOS.substring(PATH_ARQUIVOS.length() - 4, PATH_ARQUIVOS.length());

        //Verifica se está no diretório app.
        if (verifica.equals("/app")) {
            PATH_ARQUIVOS = PATH_ARQUIVOS.substring(0, PATH_ARQUIVOS.length() - 4);
        }

        if (!verificaExisteArquivos(PATH_ARQUIVOS)) {
            PrintUtil.outn("");
            PrintUtil.outn("Criando o esqueleto para sua aplicacao ...");
            PrintUtil.outn("");

            try {
                ModificaArquivos.getInstance().alteraConfData(PATH_ARQUIVOS);
            } catch (FileNotFoundException ex) {
                PrintUtil.outn(ex.getMessage());
            } catch (IOException ex) {
                PrintUtil.outn(ex.getMessage());
            }
            
            File pluginController = new File(FileUtil.getApplicationPath() + "/plugins/play/resources/controllers");
            File pathController = new File(PATH_ARQUIVOS + "/app/controllers");

            File pluginView = new File(FileUtil.getApplicationPath() + "/plugins/play/resources/views");
            File pathView = new File(PATH_ARQUIVOS + "/app/views");

            File pluginPublic = new File(FileUtil.getApplicationPath() + "/plugins/play/resources/public");
            File pathPublic = new File(PATH_ARQUIVOS + "/public");

            File pluginConf = new File(FileUtil.getApplicationPath() + "/plugins/play/resources/conf");
            File pathConf = new File(PATH_ARQUIVOS + "/conf");
            try {
                IOUtils.copyDirectory(pluginController, pathController);
                IOUtils.copyDirectory(pluginView, pathView);
                IOUtils.copyDirectory(pluginPublic, pathPublic);
                IOUtils.copyDirectory(pluginConf, pathConf);
                ModificaArquivos.getInstance().alteraRouteWeb(PATH_ARQUIVOS);
            } catch (IOException ex) {
                Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            PrintUtil.outn("");
        }


    }

    /**
     * Verifica se existe os arquivos para funcionar o serviço rest.
     * @param PATH_ARQUIVOS
     * @return
     */
    public boolean verificaArquivosRest(String PATH_ARQUIVOS)
    {
        if(!new File(PATH_ARQUIVOS+"/app/utils/").exists())
        {
            return false;
        }else if(!new File(PATH_ARQUIVOS+"/app/controllers/RestDefaultController.java").exists()){
            return false;
        }
        return true;
    }


    public void copiaArquivosPlayRest(String PATH_ARQUIVOS) throws IOException, Exception
    {    
        if(!this.verificaArquivosRest(PATH_ARQUIVOS))
        {
            PrintUtil.outn("Deseja gerar como: ");
            PrintUtil.outn("");
            PrintUtil.outn(PrintUtil.getYellowFont()+"[1] JSON"+PrintUtil.getColorReset());
            PrintUtil.outn(PrintUtil.getYellowFont()+"[2] XML"+PrintUtil.getColorReset());
            PrintUtil.outn("");
            int op_rest = PrintUtil.inInt("Digite a opcao: ");
            while (op_rest > 2) {
                PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta." + PrintUtil.getColorReset());
                PrintUtil.outn("");
                op_rest = PrintUtil.inInt("Digite a opcao: ");
                PrintUtil.outn("");
            }
            PrintUtil.outn("");
            File pluginUtils = new File(FileUtil.getApplicationPath() + "/plugins/play/resources/Rest/Util");
            File pathUtils = new File(PATH_ARQUIVOS + "/app/utils");

            try {
                IOUtils.copyDirectory(pluginUtils, pathUtils);

                FreemarkerWrapper.getInstance().addVar("op_rest", op_rest);
                String arq = FreemarkerWrapper.getInstance().parseTemplate("RestDefaultController.ftl");
                FileUtil.saveToPath(PATH_ARQUIVOS+"/app/controllers/RestDefaultController.java", arq);
                ModificaArquivos.getInstance().insereConfgRest(PATH_ARQUIVOS, op_rest);
                ModificaArquivos.getInstance().alteraRouteRest(PATH_ARQUIVOS);
            } catch (IOException ex) {
                throw new IOException(ex.getMessage());
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
    }

    public static ArquivosPlay getArquivoPlay() {
        if (arquivo == null) {
            arquivo = new ArquivosPlay();
        }
        return arquivo;
    }
}
