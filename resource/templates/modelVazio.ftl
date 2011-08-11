package models;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity(name="${inflector.pluralize("${class}")?lower_case}")
public class ${class} extends Model{
   
    public String nome;
    public int idade;

    @Override
    public String toString(){
        return nome;
    }
   
}

