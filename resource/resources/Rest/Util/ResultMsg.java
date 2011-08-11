package utils;

import java.util.HashMap;

public class ResultMsg {
    
    private static ResultMsg instance;
    
    private HashMap<Integer,String> MSGS;
    
    private ResultMsg(){
        MSGS = new HashMap<Integer, String>();
        MSGS.put(0, "");
        MSGS.put(1, "Usuário não autenticado");
        MSGS.put(2, "Registro não encontrado");
        MSGS.put(3, "Parametro não informado");
        MSGS.put(100, "Excessão gerada");
    }
    
    public static ResultMsg getInstance(){
        if(instance==null){
            instance = new ResultMsg();
        }
        return instance;
    }
    
    public String getMessage(int num){
        return MSGS.get(num);
    }

    
}

