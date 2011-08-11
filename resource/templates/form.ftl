${r"#{extends 'main.html' /}"}

<script type="text/javascript">

    $(function(){
        ${r"#{tab_fnc tab:'tabs'/}"}
        ${r"#{button_fnc obj:'.btn_salvar', icon:'ui-icon-disk'/}"}
        ${r"#{button_fnc obj:'#btn_voltar', icon:'ui-icon-arrowreturnthick-1-w'/}"}
	${r"#{date_fnc/}"}
    });

</script>

<form action="${r"@{"}${class}.save(${inflector.singularize("${class}")?lower_case}?.id) ${r"}"}" method="POST">

    ${r"#{"}ifErrors${r"}"}
    ${r"#{"}box_error label:'Alerta:', message:'Verifique os erros.'/${r"}"}
    <br>
    ${r"#{"}/ifErrors${r"}"}

    ${r"#{box}"}
    ${r"#{"}button label:'Voltar', id:'btn_voltar', href:@${class}.index()/${r"}"}
    ${r"#{/box}"}

    ${r"#{"}tab name:'tabs', tabs:'tabs-1', labels:'Informações de ${inflector.singularize("${class}")}'${r"}"}
    <div id="tabs-1">

<#list atributosSemRelacao as at>

<#if at.getTipo() = "Boolean">
	${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.${at.getNome()}'${r"}"}
            ${r"#{"}checkField name:field.name , label:'${at.getNameHumanize()?capitalize}:', value:${inflector.singularize("${class}")?lower_case}?.${at.getNome()} /${r"}"}
        ${r"#{"}/field ${r"}"}
	<br/>
<#else>
        ${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.${at.getNome()}'${r"}"}
        <label>${at.getNameHumanize()?capitalize}:</label>
        <input style="width: 300px" class="<#if at.getTipo() = "Date">datepicker </#if>text ui-widget-content ui-corner-all" type="text" name="${r"${"}field.name${r"}"}" value="${r"${"}${inflector.singularize("${class}")?lower_case}?.${at.getNome()}<#if at.getTipo() = "Date">?.format('dd/MM/yyyy')</#if>${r"}"}" />
        <span class="error">${r"#{"}error '${inflector.singularize("${class}")?lower_case}.${at.getNome()}' /${r"}"}</span>
        ${r"#{"}/field ${r"}"}
	<br/>
</#if>
</#list>

<#list atributosRelacao as at>

<#if at.getAnnotation() = "ManyToOne">
        ${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.${at.getNome()}.id'${r"}"}
        <label>${at.getNameHumanize()?capitalize}:</label>
        <select class="text ui-widget-content ui-corner-all" style="width: 300px" name="${r"${"}field.name ${r"}"}">
            ${r"#{"}list ${inflector.pluralize("${at.getNome()}")?lower_case}, as:'${at.getNome()[0]}'${r"}"}
            <option value="${r"${"}${at.getNome()[0]}.id${r"}"}" ${r"${"}${inflector.singularize("${class}")?lower_case}?.${at.getNome()}?.id == ${at.getNome()[0]}.id ? 'selected' : ''${r"}"}>${r"${"}${at.getNome()[0]}.toString()${r"}"}</option>
            ${r"#{"}/list${r"}"}
        </select>
        <span class="error">${r"#{"}error '${inflector.singularize("${class}")?lower_case}.${at.getNome()}' /${r"}"}</span>
        ${r"#{"}/field${r"}"}
	<br/>
	<br/>
<#else>
<#if at.getAnnotation() != "OneToMany">
        ${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.${at.getNome()}.id'${r"}"}
        <label>${at.getNameHumanize()?capitalize}:</label>
        ${r"#{listcheck width:'300', height:'200'}"}
        ${r"#{"}list ${at.getNome()}, as:'${at.getNome()[0]}'${r"}"}
        ${r"#{"}check value:${at.getNome()[0]}.id, label:${at.getNome()[0]}.toString(), name:field.name ${r"/}"}
        ${r"#{/list}"}
        ${r"#{/listcheck}"}
        <label></label><span style="position:absolute;bottom: 240px;left: 440px" class="error">${r"#{"}error '${inflector.singularize("${class}")?lower_case}.${at.getNome()}' /${r"}"}</span>

        ${r"#{"}if ${inflector.singularize("${class}")?lower_case}?.${at.getNome()}${r"}"}
        <script>
        ${r"#{"}list ${inflector.singularize("${class}")?lower_case}.${at.getNome()}, as:'${at.getNome()[0]}'${r"}"}
           ${r"#{"}check_scr name:field.name ,value:${at.getNome()[0]}.id/${r"}"}
        ${r"#{/list}"}
        </script>
        ${r"#{/if}"}
        ${r"#{/field}"}
        <br/>
</#if>
</#if>

</#list>
        <label></label>
        ${r"#{button label:'Salvar', class:'btn_salvar', type:'submit'/}"}

    </div>
    ${r"#{/tab}"}


</form>
