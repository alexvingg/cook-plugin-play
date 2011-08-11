package utils;


public class Result {
    
    private boolean SUCESS;
    private int CODRESULT;
    private String EXCEPTION;
    private Object CONTENT;
    
    
    public Result(boolean SUCESS, int RESULT, Object CONTENT){
        this.SUCESS = SUCESS;
        this.CODRESULT = RESULT;
        this.CONTENT = CONTENT;
        this.EXCEPTION = ResultMsg.getInstance().getMessage(this.CODRESULT);
    }
    public Result(Object CONTENT){
        this.SUCESS = true;
        this.CODRESULT = 0;
        this.CONTENT = CONTENT;
        this.EXCEPTION = ResultMsg.getInstance().getMessage(this.CODRESULT);
    }
    public Result(int RESULT, Object CONTENT){
        this.SUCESS = false;
        this.CODRESULT = RESULT;
        this.CONTENT = CONTENT;
        this.EXCEPTION = ResultMsg.getInstance().getMessage(this.CODRESULT);
    }
    public Result(Object CONTENT, String EXCEPTION){
        this.SUCESS = false;
        this.CODRESULT = 100;
        this.CONTENT = CONTENT;
        this.EXCEPTION = EXCEPTION;
    }
    
}

