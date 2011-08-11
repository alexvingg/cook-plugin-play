${r"#{extends 'main.html' /}"}

<script type="text/javascript">

    ${r"$(function(){"}

        ${r"#{button_fnc obj:'.btn_edit', icon:'ui-icon-pencil', text:false/}"}
        ${r"#{button_fnc obj:'.btn_delete', icon:'ui-icon-trash', text:false/}"}
        ${r"#{button_fnc obj:'#btn_novo', icon:'ui-icon-plus'/}"}	
        ${r"#{confirm_fnc obj:'#dialog', width: '300'/}"}
        ${r"#{grid_fnc/}"}


    });
    ${r"#{confirm_method /}"}
	
</script>

${r"#{box}"}
${r"#{"}button label:'Novo ${inflector.singularize("${class}")}', id:'btn_novo', href:@${class}.form()/${r"}"}
${r"#{/box}"}

${r"#{confirm id:'dialog' , msg:'Deseja realmente apagar este registro?'/}"}

${r"#{"}grid titulo:'Lista de ${class}', rodape:'Foram encontrados um total de '+ ${class?lower_case}.size() +' ${inflector.singularize("${class}")?lower_case}(s)'${r"}"}
<table class="ui-grid-content ui-widget-content">
    <thead>	
        <tr>
            <th align="center" class="ui-state-default">&nbsp;</th>
	<#list atributosSemRelacao as at>
            <th align="left" class="ui-state-default">${at.getNameHumanize()?capitalize}</th>
	</#list>
	<#list atributosRelacao as at>
            <#if at.getAnnotation() = "ManyToOne">
            <th align="left" class="ui-state-default">${at.getNameHumanize()?capitalize}</th>
	    </#if>
	</#list>
        </tr>
    </thead>
    <tbody>
	<#assign contador= 1>
        ${r"#{"}list items:${class?lower_case}, as:'${class[0]?lower_case}'${r"}"}
        <tr>
            <td style="width:60px" class="ui-widget-content">
				${r"#{"}button label:'Editar', class:'btn_edit', href:@${class}.form(${class[0]?lower_case}.id)/${r"}"} 
				${r"#{"}button label:'Excluir', class:'btn_delete',confirm:'"#dialog"' ,href:@${class}.delete(${class[0]?lower_case}.id) /${r"}"}</td>		
	<#list atributosSemRelacao as at>	
            <td style="text-align: left" class="ui-widget-content">
		        <#if contador = 1><a href="${r"@{"}${class}.view(${class[0]?lower_case}.id)${r"}"}"></#if>${r"${"}${class[0]?lower_case}.${at.getNome()}<#if at.getTipo() = "Date">?.format('dd/MM/yyyy')</#if> ${r"}"}<#if contador = 1><#assign contador= contador + 1></#if></a>
            </td>
	</#list>
	<#list atributosRelacao as at>
	    <#if at.getAnnotation() = "ManyToOne">		
            <td style="text-align: left" class="ui-widget-content">${r"${"}${class[0]?lower_case}.${at.getNome()}<#if at.getTipo() = "Date">?.format('dd/MM/yyyy')</#if> ${r"}"}</td>
            </#if>
	</#list>
        </tr>
        ${r"#{"}/list${r"}"}
    </tbody>
</table>
${r"#{/grid}"}
