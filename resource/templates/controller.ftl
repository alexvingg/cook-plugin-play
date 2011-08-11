package controllers;

import java.util.List;
import models.${inflector.singularize("${class}")};
<#list atributosRelacao as at>
import models.${at.getTipo()};
</#list>  

public class ${controlador} extends DefaultController {
    
    public static void index(){
        List<${inflector.singularize("${class}")}> ${class?lower_case} = ${inflector.singularize("${class}")}.findAll();			
        render(${class?lower_case});
    }


    public static void view(Long id) {
        if(id != null) {
            ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")}.findById(id);
            if(${inflector.singularize("${class}")?lower_case}!=null){
                render(${inflector.singularize("${class}")?lower_case});
            }else{
                flash.error("${inflector.singularize("${class}")} não encontrado.");
                index();
            }
        }else{
            flash.error("É necessário informar um ${inflector.singularize("${class}")?lower_case}.");
            index();
        }       
    }


    public static void delete(Long id) {
        try {
            ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")}.findById(id);
            ${inflector.singularize("${class}")?lower_case}.delete();
            flash.success("Registro apagado com sucesso.");
        } catch (Exception ex) {
            flash.error("Erro ao apagar registro.");
        }
        index();
    }
    
    
   public static void form(Long id) {

     <#list atributosRelacao as at>
	<#if at.getAnnotation() = "ManyToMany">
      	List<${at.getTipo()}> ${at.getNome()?lower_case} = ${at.getTipo()}.findAll();
	</#if>
	<#if at.getAnnotation() = "ManyToOne">
	List<${at.getTipo()}> ${inflector.pluralize("${at.getNome()}")?lower_case} = ${at.getTipo()}.findAll();
	</#if>
     </#list>  
      if(id != null) {
          ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case}  = ${inflector.singularize("${class}")}.findById(id);
          if (${inflector.singularize("${class}")?lower_case} != null) {
              render(${inflector.singularize("${class}")?lower_case}<#list atributosRelacao as at>,<#if at.getAnnotation() = "ManyToMany">${at.getNome()?lower_case}</#if><#if at.getAnnotation() = "ManyToOne">${inflector.pluralize("${at.getNome()}")?lower_case}</#if></#list>);
          } else {
              flash.error("Registro não encontrado.");
              index();
          }
      }else{
          render(<#list atributosRelacao as at><#if at.getAnnotation() = "ManyToMany">${at.getNome()?lower_case}</#if><#if at.getAnnotation() = "ManyToOne">${inflector.pluralize("${at.getNome()}")?lower_case}</#if><#if at_has_next>,</#if></#list>);
      }   
  }
  
  public static void save(Long id, ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case}VO){

      ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case};
      if(id == null){
          ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")?lower_case}VO;
          ${inflector.singularize("${class}")?lower_case}VO = null;
      }else{
          ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")}.findById(id);
          if(${inflector.singularize("${class}")?lower_case} != null){          
          <#list atributos as atr>
	      ${inflector.singularize("${class}")?lower_case}.${atr.getNome()}  = ${inflector.singularize("${class}")?lower_case}VO.${atr.getNome()};
          </#list>  
          }else{
              flash.error("Registro não encontrado.");
              index();
          }
      }
      validation.valid(${inflector.singularize("${class}")?lower_case});
      if (validation.hasErrors()) {
           render("@form", ${inflector.singularize("${class}")?lower_case});
      }
      ${inflector.singularize("${class}")?lower_case}.save();
      flash.success("Registro salvo com sucesso.");
      index();
    }
    
}
