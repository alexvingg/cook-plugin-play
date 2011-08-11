${r"#{extends 'main.html' /}"}

<script type="text/javascript">

    $(function(){
        ${r"#{tab_fnc tab:'tabs'/}"}
        ${r"#{button_fnc obj:'.btn_salvar', icon:'ui-icon-disk'/}"}
        ${r"#{button_fnc obj:'#btn_voltar', icon:'ui-icon-arrowreturnthick-1-w'/}"}
	${r"#{date_fnc/}"}
    });

</script>
<form action="${r"@{"}${class}.index() ${r"}"}" method="POST">

    ${r"#{"}ifErrors${r"}"}
    ${r"#{"}box_error label:'Alerta:', message:'Verifique os erros.'/${r"}"}
    <br>
    ${r"#{"}/ifErrors${r"}"}

    ${r"#{box}"}
    ${r"#{"}button label:'Voltar', id:'btn_voltar', href:@${class}.index()/${r"}"}
    ${r"#{/box}"}

    ${r"#{"}tab name:'tabs', tabs:'tabs-1', labels:'Informações de ${inflector.singularize("${class}")}'${r"}"}
    <div id="tabs-1">
	

	${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.nome'${r"}"}
        <label>Nome:</label>
        <input style="width: 300px" class="text ui-widget-content ui-corner-all" type="text" name="${r"${"}field.name${r"}"} value="" />
        ${r"#{/field}"}
        <br/>

	${r"#{"}field '${inflector.singularize("${class}")?lower_case}VO.nome'${r"}"}
        <label>Idade:</label>
        <input style="width: 300px" class="text ui-widget-content ui-corner-all" type="text" name="${r"${"}field.name${r"}"}" value="" />
        ${r"#{/field}"}
        <br/>

        <label></label>
		${r"#{"}button label:'Salvar', class:'btn_salvar', href:@${class}.index()/${r"}"}

    </div>
    ${r"#{/tab}"}
</form>
