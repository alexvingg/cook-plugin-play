package play.cook;

import cook.core.IFCook;
import cook.core.ResultProcess;
import cook.util.FileUtil;
import cook.util.PrintUtil;
import java.io.File;
import java.io.IOException;
import play.utils.*;

public class Play implements IFCook
{

    //Path outn file
    private String PATH_OUT;
    private String PATH_ARQUIVOS;
    //In param
    private String[] param;
    private String PATH_OUT_MODEL;
    private String action;
    //Return the version of plugin

    @Override
    public String getVersion()
    {
        return "0.2";
    }

    //Print header message of plugin start
    @Override
    public void printHeader()
    {

        PrintUtil.outn(PrintUtil.getGreenFont() + "       _            _ " + PrintUtil.getColorReset());
        PrintUtil.outn(PrintUtil.getGreenFont() + " _ __ | | __ _ _  _| |" + PrintUtil.getColorReset());
        PrintUtil.outn(PrintUtil.getGreenFont() + "| '_ \\| |/ _' | || |_|" + PrintUtil.getColorReset());
        PrintUtil.outn(PrintUtil.getGreenFont() + "|  __/|_|\\____|\\__ (_)" + PrintUtil.getColorReset());
        PrintUtil.outn(PrintUtil.getGreenFont() + "|_|            |__/" + PrintUtil.getColorReset());
        PrintUtil.outn("");
        PrintUtil.outn("Play Framework CRUD plugin generator. Version " + getVersion());
        PrintUtil.outn("");

    }

    //Print help invoke
    @Override
    public void printHelp()
    {
        PrintUtil.outn("Use: cook play [acao]");
        PrintUtil.outn("");
        
        PrintUtil.outn("Acoes disponiveis:");
        PrintUtil.outn("~~~~~~~~~~~~~~~~~~");
        PrintUtil.outn("model");
        PrintUtil.outn("controller");
        PrintUtil.outn("view");
        PrintUtil.outn("controller-rest");

    }

    //Start cook plugin. Use thi method for valid in param
    @Override
    public boolean start(String[] param)
    {

        //Valid in param
        if (param.length == 1 || param[1].equals("")) {
            printHelp(); //show help
            PrintUtil.outn("");
            return false;
        }
        if(!(param[1].toLowerCase().equals("controller") ||
                param[1].toLowerCase().equals("view") ||
                param[1].toLowerCase().equals("controller-rest") ||
                param[1].toLowerCase().equals("model"))){
            printHelp();
            PrintUtil.outn("");
            return false;
        }

        this.param = param;
        this.action = param[1].toLowerCase();
        return true;

    }

    //Valid directory for execute the plugin
    @Override
    public boolean validDirectory()
    {

        boolean saida;

        //get the path of user execute script
        String pwd = FileUtil.getPromptPath();
        if ((new File(pwd + configureDirectory()[0])).exists()) {
            PATH_OUT = pwd + configureDirectory()[0];
            this.PATH_ARQUIVOS = pwd;
            PATH_OUT_MODEL = pwd + "/app/models";
            saida = true;
            PrintUtil.outn("Diretorio destino: " + PATH_OUT);
            PrintUtil.outn("");
        } else if ((new File(pwd + configureDirectory()[1])).exists()) {
            PATH_OUT = pwd + configureDirectory()[1];
            this.PATH_ARQUIVOS = pwd;
            PATH_OUT_MODEL = pwd + "/models";
            saida = true;
            PrintUtil.outn("Diretorio destino: " + PATH_OUT);
            PrintUtil.outn("");
        } else {
            String appPATHG = PrintUtil.inString("Informe o caminho da applicacao: ");
            if ((new File(appPATHG + configureDirectory()[0])).exists()) {
                PATH_OUT = appPATHG + configureDirectory()[0];
                this.PATH_ARQUIVOS = appPATHG;
                PATH_OUT_MODEL = appPATHG + "/app/models";
                saida = true;
                PrintUtil.outn("Diretorio destino: " + PATH_OUT);
                PrintUtil.outn("");
            } else {
                saida = false;
                PrintUtil.outn("Diretorio invalido");
            }
        }

        return saida;
    }

    //Execute plugin
    @Override
    public ResultProcess cook()
    {
        ResultProcess outn = new ResultProcess();

        if (!this.action.equals("model")) {
            //if ((new File(PATH_OUT_MODEL + "/" + Inflector.getInstance().singularize(param[2]) + ".java")).exists()) {
            if (this.action.equals("controller")) {
                ArquivosPlay.getArquivoPlay().copiaArquivosPlay(this.PATH_ARQUIVOS);
                if(param.length >= 3)
                {
                    outn = PlayGerador.getPlayGerador().criaControllerVazio(PATH_OUT_MODEL, PATH_OUT, param[2]);
                }else{
                    outn = PlayGerador.getPlayGerador().criaController(PATH_OUT_MODEL, PATH_OUT);
                }
            } else if (this.action.equals("view")) {
                if(param.length >= 3){
                    outn = PlayGerador.getPlayGerador().criaViewVazio(PATH_OUT_MODEL, PATH_OUT, param[2]);
                }else{
                    outn = PlayGerador.getPlayGerador().criaView(PATH_OUT_MODEL, PATH_OUT);
                }
            }else if(this.action.equals("controller-rest")){
                try {
                    ArquivosPlay.getArquivoPlay().copiaArquivosPlayRest(PATH_ARQUIVOS);
                    outn = PlayGerador.getPlayGerador().criaControllerRest(PATH_OUT_MODEL, PATH_OUT);
                } catch (IOException ex) {
                    outn.setResultProcess(ResultProcess.ERROR, ex.getMessage());
                }catch (Exception e){
                    outn.setResultProcess(ResultProcess.ERROR, e.getMessage());
                }
            } else {
                outn.setResultProcess(ResultProcess.ERROR, "Acao nao encontrada");
            }
            //} else {
            //  outn.setResultProcess(ResultProcess.ERROR, "O model do controller " + param[2] + " nao foi encontrado");
            //}
        } else {
                if(param.length >= 3)
                {
                    outn = PlayGerador.getPlayGerador().criaModelVazio(PATH_OUT_MODEL, PATH_OUT, PATH_ARQUIVOS, param[2]);
                }else{
                    outn = PlayGerador.getPlayGerador().criaModel(PATH_OUT_MODEL, PATH_OUT, this.PATH_ARQUIVOS);
                }
 
        }
        return outn;
    }

    //End of file cicle
    @Override
    public void end()
    {
        PrintUtil.outn("");
        PrintUtil.outn("Arquivo criado.");
    }

    private String[] configureDirectory()
    {
        String path[] = new String[2];
        if (this.action.equals("controller") || this.action.equals("controller-rest")) {
            path[0] = "/app/controllers";
            path[1] = "/controllers";
        } else if (this.action.equals("view")) {
            path[0] = "/app/views";
            path[1] = "/views";
        } else if (this.action.equals("model")) {
            path[0] = "/app/models";
            path[1] = "/models";
        }
        return path;
    }
}
