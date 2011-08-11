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
${r"#{"}button label:'Editar', id:'btn_edit', href:@${class}.form()/${r"}"}
${r"#{/box}"}

${r"#{"}tab name:'tabs', tabs:'tabs-1', labels:'${class}'${r"}"}

<div id="tabs-1">
	<label>Nome:</label>
	 <div class="labelform">Exemplo de view</div>
	<label>Idade:</label>
	 <div class="labelform">14</div>
</div>
${r"#{/tab}"}
