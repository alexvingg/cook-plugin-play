/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.database;

import java.util.List;

/**
 *
 * @author alex
 */
public class Atributo
{

    private List<String> annotation;
    private String type;
    private String field;
    private boolean pk = false;
    private boolean display = false;

    public boolean isDisplay()
    {
        return display;
    }

    public void setDisplay(boolean display)
    {
        this.display = display;
    }

    public Atributo()
    {
    }

    /**
     * 
     * @param annotation Anotação do atributo
     * @param type Tipo do atributo
     * @param field Nome do atributo
     */
    public Atributo(List<String> annotation, String type, String field)
    {
        this.annotation = annotation;
        this.type = this.getTypeJava(type);
        this.field = field;
    }

    /**
     *
     * @return Se o atributo é chave primária.
     */
    public boolean isPk()
    {
        return pk;
    }

    /**
     *
     * @param pk Define o atributo como chave primária.
     */
    public void setPk(boolean pk)
    {
        this.pk = pk;
    }

    /**
     * 
     * @return Lista de anotações
     */
    public List<String> getAnnotation()
    {
        return annotation;
    }

    /**
     *
     * @param annotation Lista de anotações(String)
     */
    public void setAnnotation(List<String> annotation)
    {
        this.annotation = annotation;
    }

    /**
     *
     * @return Nome do campo.
     */
    public String getField()
    {
        return field;
    }

    /**
     *
     * @param field Nome do campo
     */
    public void setField(String field)
    {
        this.field = field;
    }

    /**
     *
     * @return Tipo do campo.
     */
    public String getType()
    {
        return type;
    }

    /**
     *
     * @param type Tipo do campo.
     */
    public void setType(String type)
    {
        this.type = this.getTypeJava(type);
    }
    
    /**
     * Metodo que recebe o tipo atributo do banco e converte para um tipo em java
     * @param typeBd Tipo do atributo dobanco (Mysql ou Postgres)
     * @return Tipo do atributo em java
     */
    public String getTypeJava(String typeBd)
    {
        if (typeBd.toLowerCase().equals("bool") || typeBd.toLowerCase().equals("tinyint") || typeBd.toLowerCase().equals("bit")) {
            return "Boolean";
        } else if (typeBd.toLowerCase().equals("bigint")) {
            return "BigInteger";
        } else if (typeBd.toLowerCase().equals("serial")) {
            return "Integer";
        } else if (typeBd.toLowerCase().equals("varchar") || typeBd.toLowerCase().equals("text") || typeBd.toLowerCase().equals("bpchar")) {
            return "String";
        } else if (typeBd.toLowerCase().equals("numeric") || typeBd.toLowerCase().equals("float") || typeBd.toLowerCase().equals("decimal")) {
            return "Double";
        } else if (typeBd.toLowerCase().equals("datetime") || typeBd.toLowerCase().equals("date") || typeBd.toLowerCase().equals("timestamp")) {
            return "Date";
        } else if (typeBd.toLowerCase().substring(0, 3).equals("int")) {
            return "Integer";
        }
        return typeBd;
    }
}
