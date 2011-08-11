package models;

import java.util.*;
import java.math.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity(name="${tabela}")
public class ${class} extends Model{


    <#list atributos as at>
    ${at.getAnnotation()}public ${at.getTipo()} ${at.getNome()};

    </#list>  

    @Override
    public String toString(){
        return ${displayField};
    }
}
