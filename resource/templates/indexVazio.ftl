${r"#{extends 'main.html' /}"}

<script type="text/javascript">

    ${r"$(function(){"}

        ${r"#{button_fnc obj:'.btn_edit', icon:'ui-icon-pencil', text:false/}"}
        ${r"#{button_fnc obj:'.btn_delete', icon:'ui-icon-trash', text:false/}"}
        ${r"#{button_fnc obj:'#btn_novo', icon:'ui-icon-plus'/}"}
        ${r"#{grid_fnc/}"}
		${r"#{confirm_fnc obj:'#dialog', width: '300'/}"}

    });

    ${r"#{confirm_method /}"}

</script>

${r"#{box}"}
${r"#{"}button label:'Novo ${inflector.singularize("${class}")}', id:'btn_novo', href:@${class}.form()/${r"}"}
${r"#{/box}"}

${r"#{confirm id:'dialog' , msg:'Deseja realmente apagar este registro?'/}"}

${r"#{"}grid titulo:'Lista de ${class}', rodape:'Foram encontrados um total de 1 ${inflector.singularize("${class}")?lower_case}(s)'${r"}"}
<table class="ui-grid-content ui-widget-content">
    <thead>	
        <tr>
            <th align="center" class="ui-state-default">&nbsp;</th>
            <th align="left" class="ui-state-default">Nome</th>
	    <th align="left" class="ui-state-default">Idade</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td style="width:60px" class="ui-widget-content">
				${r"#{"}button label:'Editar', class:'btn_edit', href:@${class}.form() /${r"}"} 
				${r"#{"}button label:'Excluir', class:'btn_delete', confirm:'"#dialog"', href:@${class}.index() /${r"}"}</td>		
            <td style="text-align: left" class="ui-widget-content">
				<a href="${r"@{"}${class}.view()${r"}"}">
					Exemplo De Index
				</a>
            </td>
            <td style="text-align: left" class="ui-widget-content">
				14
            </td>
        </tr>
    </tbody>
</table>
${r"#{/grid}"}
