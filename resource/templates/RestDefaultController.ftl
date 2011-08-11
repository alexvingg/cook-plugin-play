package controllers;

import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Result;
import utils.ResultMsg;

public class RestDefaultController extends Controller {

    @Before
    public static void validCredentials() {
	/**
	* 
        if (!(request.action.equals("RestDefaultController.login") || request.action.equals("RestDefaultController.logout"))) {
            if (Cache.get("admin", String.class) == null) {
<#if op_rest = 1>
                renderJSON(new Result(false, 1, ResultMsg.getInstance().getMessage(1)));
</#if>
<#if op_rest = 2>
                renderXml(new Result(false, 1, ResultMsg.getInstance().getMessage(1)));
</#if>
            }
        }
	*/
    }

    public static void login(String login, String senha) {
        Cache.set("admin", "Administrador", "5mn");
<#if op_rest = 1>
        renderJSON(new Result("Usuário logado com sucesso"));
</#if>
<#if op_rest = 2>
		renderXml(new Result("Usuário logado com sucesso"));
</#if>
    }

    public static void logout() {
        Cache.delete("admin");
<#if op_rest = 1>
        renderJSON(new Result("Usuário deslogado com sucesso"));
</#if>
<#if op_rest = 2>
		renderXml(new Result("Usuário deslogado com sucesso"));
</#if>
    }
}

