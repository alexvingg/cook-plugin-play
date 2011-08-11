/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package play.utils;

/**
 *
 * @author alex
 */
public class AtributoUtil {

    private String tipo;
    private String nome;
    private String annotation;

    public AtributoUtil()
    {

    }

    public AtributoUtil(String tipo, String nome){
        this.nome = nome;
        this.tipo = tipo;
        this.annotation = null;
    }

   public AtributoUtil(String tipo, String nome, String annotation){
        this.nome = nome;
        this.tipo = tipo;
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        int parametroExiste = nome.lastIndexOf("=");
        if(parametroExiste != -1)
        {
            this.nome = nome.substring(0, parametroExiste - 1);
        }else{
            this.nome = nome;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        int posicaoParametroUm = tipo.indexOf("<");
        int posicaoParametroDois = tipo.lastIndexOf(">");
        if(posicaoParametroUm != -1)
        {
            this.tipo = tipo.substring(posicaoParametroUm + 1, posicaoParametroDois) ;
        }else{
            this.tipo = tipo;
        }
   }

    public void setTipo (String tipo , Boolean t)
    {
        this.tipo = tipo;
    }

    public String getNameHumanize()
    {
        return Inflector.getInstance().humanize(this.getNome());
    }

}
