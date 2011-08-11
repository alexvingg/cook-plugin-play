package controllers;

import java.util.List;
import utils.Result;
import models.${inflector.singularize("${class}")};
<#list atributosRelacao as at>
import models.${at.getTipo()};
</#list>  

public class ${controlador} extends RestDefaultController {
    
    public static void list(){
        List<${inflector.singularize("${class}")}> ${class?lower_case} = ${inflector.singularize("${class}")}.findAll();			
		<#if op_rest = 1>
	renderJSON(new Result(${class?lower_case}));
		</#if>
		<#if op_rest = 2>
	renderXml(new Result(${class?lower_case}));
		</#if>
    }
    
    public static void view(Long id) {
        if(id != null) {
            ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")}.findById(id);
            if(${inflector.singularize("${class}")?lower_case}!=null){
                		<#if op_rest = 1>
		renderJSON(new Result(${inflector.singularize("${class}")?lower_case}));
						</#if>
						<#if op_rest = 2>
		renderXml(new Result(${inflector.singularize("${class}")?lower_case}));
						</#if>
            }else{
				<#if op_rest = 1>
		renderJSON(new Result(2, "${inflector.singularize("${class}")} não encontrado."));
				</#if>    
				<#if op_rest = 2>
		renderXml(new Result(2, "${inflector.singularize("${class}")} não encontrado."));
				</#if> 
            }
        }else{
			<#if op_rest = 1>
	    renderJSON(new Result(3,"É necessário informar um ${inflector.singularize("${class}")?lower_case}."));
			</#if>
			<#if op_rest = 2>
	    renderXml(new Result(3,"É necessário informar um ${inflector.singularize("${class}")?lower_case}."));
			</#if>    
        }       
    }
    
    public static void delete(Long id) {
        try {
            ${inflector.singularize("${class}")} ${inflector.singularize("${class}")?lower_case} = ${inflector.singularize("${class}")}.findById(id);
            ${inflector.singularize("${class}")?lower_case}.delete();
            <#if op_rest = 1>
	    renderJSON(new Result("${inflector.singularize("${class}")} apagado com sucesso."));
			</#if>
			<#if op_rest = 2>
	    renderXml(new Result("${inflector.singularize("${class}")} apagado com sucesso."));	
			</#if>
        } catch (Exception ex) {
			<#if op_rest = 1>
            renderJSON(new Result("Erro ao apagar ${inflector.singularize("${class}")?lower_case}.",ex.getMessage()));
            </#if>
            <#if op_rest = 2>
            renderXml(new Result("Erro ao apagar ${inflector.singularize("${class}")?lower_case}.",ex.getMessage()));
            </#if>
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
			  <#if op_rest = 1>
              renderJSON(new Result(2, "${inflector.singularize("${class}")} não encontrado."));
              </#if>
              <#if op_rest = 2>
              renderXml(new Result(2, "${inflector.singularize("${class}")} não encontrado."));
              </#if>
          }
      }
      ${inflector.singularize("${class}")?lower_case}.save();
      <#if op_rest = 1>
      renderJSON(new Result("${inflector.singularize("${class}")} salvo com sucesso."));
      </#if>
      <#if op_rest = 2>
      renderXml(new Result("${inflector.singularize("${class}")} salvo com sucesso."));
	  </#if>
    }    
}
