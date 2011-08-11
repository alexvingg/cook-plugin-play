${r"#{extends 'main.html' /}"}

<script type="text/javascript">

    ${r"$(function(){"}

        ${r"#{tab_fnc tab:'tabs'/}"}
        ${r"#{button_fnc obj:'#btn_novo', icon:'ui-icon-plus'/}"}
        ${r"#{button_fnc obj:'#btn_voltar', icon:'ui-icon-arrowreturnthick-1-w'/}"}
        ${r"#{button_fnc obj:'#btn_edit', icon:'ui-icon-pencil'/}"}
        
    });

</script>
${r"#{box}"}
${r"#{"}button label:'Voltar', id:'btn_voltar', href:@${class}.index()/${r"}"}
${r"#{"}button label:'Editar', id:'btn_edit', href:@${class}.form(${inflector.singularize("${class}")?lower_case}.id)/${r"}"}
${r"#{/box}"}
<#assign tabs="tabs-1">
<#assign contador= 1>
<#assign labels = "${class}">
<#list atributosRelacao as at>
        <#if at.getAnnotation() = "ManyToMany">
           <#assign contador= contador + 1>
	   <#assign tabs= tabs + ",tabs-"+contador>
	   <#assign labels= labels + "," + "${at.getNome()?capitalize}">
	 </#if>
        <#if at.getAnnotation() = "OneToMany">
           <#assign contador= contador + 1>
	   <#assign tabs= tabs + ",tabs-"+contador>
	   <#assign labels= labels + "," + "${at.getNome()?capitalize}">
	 </#if>
</#list>

${r"#{"}tab name:'tabs', tabs:'${tabs}', labels:'${labels}'${r"}"}


<div id="tabs-1">
    <#list atributosSemRelacao as at>
    <label>${at.getNameHumanize()?capitalize}</label>
    <div class="labelform">${r"${"}${inflector.singularize("${class}")?lower_case}.${at.getNome()}<#if at.getTipo() != "Date">${r"}"}</#if><#if at.getTipo() = "Date">.format('dd/MM/yyyy')}</#if>&nbsp</div>
    </#list>

    <#list atributosRelacao as at>
<#if at.getAnnotation() = "ManyToOne">
    <label>${at.getNameHumanize()?capitalize}</label>
    <div class="labelform">${r"${"}${inflector.singularize("${class}")?lower_case}.${at.getNome()}<#if at.getTipo() != "Date">${r"}"}</#if><#if at.getTipo() = "Date">.format('dd/MM/yyyy')}</#if>&nbsp</div>
</#if>
    </#list>

</div>
<#assign contador= 1>
<#list atributosRelacao as at>
<#if at.getAnnotation() = "ManyToMany">
<#assign contador= contador + 1>
<div id="tabs-${contador}">

    ${r"#{grid}"}
    <table class="ui-grid-content ui-widget-content">
        <thead>
            <tr>
                <th align="left" class="ui-state-default">${at.getNome()?capitalize}</th>
            </tr>
        </thead>
        <tbody>
            ${r"#{"}list ${inflector.singularize("${class}")?lower_case}.${at.getNome()}, as:'${at.getNome()[0]}'${r"}"}
            <tr>
                <td style="width:300px;text-align: left" class="ui-widget-content">
			${r"${"}${at.getNome()[0]}.toString()${r"}"}
		</td>
            </tr>
            ${r"#{/list}"}
        </tbody>
    </table>
    ${r"#{/grid}"}

</div>
</#if>
<#if at.getAnnotation() = "OneToMany">
<#assign contador= contador + 1>
<div id="tabs-${contador}">

    ${r"#{grid}"}
    <table class="ui-grid-content ui-widget-content">
        <thead>
            <tr>
                <th align="left" class="ui-state-default">${at.getNome()?capitalize}</th>
            </tr>
        </thead>
        <tbody>
            ${r"#{"}list ${inflector.singularize("${class}")?lower_case}.${at.getNome()}, as:'${at.getNome()[0]}'${r"}"}
            <tr>
                <td style="width:300px;text-align: left" class="ui-widget-content">
			${r"${"}${at.getNome()[0]}.toString()${r"}"}
		</td>
            </tr>
            ${r"#{/list}"}
        </tbody>
    </table>
    ${r"#{/grid}"}

</div>
</#if>
</#list>
${r"#{/tab}"}
