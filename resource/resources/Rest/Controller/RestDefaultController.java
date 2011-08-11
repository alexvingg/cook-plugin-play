package controllers;

import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Result;
import utils.ResultMsg;

public class RestDefaultController extends Controller {

    @Before
    public static void validCredentials() {
        if (!(request.action.equals("RestDefaultController.login") || request.action.equals("RestDefaultController.logout"))) {
            if (Cache.get("admin", String.class) == null) {
                renderJSON(new Result(false, 1, ResultMsg.getInstance().getMessage(1)));
                //OU
                //renderXml(new Result(false, 1, ResultMessages.getInstance().getMessage(1)));
            }
        }
    }

    public static void login(String login, String senha) {
        Cache.set("admin", "Administrador", "5mn");
        renderJSON(new Result("Usuário logado com sucesso"));
    }

    public static void logout() {
        Cache.delete("admin");
        renderJSON(new Result("Usuário deslogado com sucesso"));
    }
}

