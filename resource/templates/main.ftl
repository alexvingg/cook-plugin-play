<!DOCTYPE html>

<html>
    <head>
        <title>#${r"{"}get 'title' /}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" media="screen" href="@${r"{"}'/public/stylesheets/main.css'}">

        #${r"{"}get 'moreStyles' /}

        <link rel="shortcut icon" type="image/png" href="@${r"{"}'/public/images/favicon.png'}">
        <script src="@${r"{"}'/public/javascripts/jquery-1.4.2.min.js'}" type="text/javascript" charset="utf-8"></script>

        <link type="text/css" href="@${r"{"}'/public/javascripts/jquery.ui/css/'}/$${r"{"}tema}/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
        <script src="@${r"{"}'/public/javascripts/jquery.ui/js/jquery-ui-1.8.4.custom.min.js'}" type="text/javascript" charset="utf-8"></script>

        <link type="text/css" href="@${r"{"}'/public/javascripts/jquery.thememenu/ddsmoothmenu.css'}" rel="stylesheet" />
        <script src="@${r"{"}'/public/javascripts/jquery.thememenu/ddsmoothmenu.js'}" type="text/javascript" charset="utf-8"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="@${r"{"}'/public/stylesheets/grid.css'}">

        #${r"{"}get 'moreScripts' /}

        <script type="text/javascript">

            ddsmoothmenu.init(${r"{"}
                mainmenuid: "smoothmenu1", //menu DIV id
                orientation: 'h', //Horizontal or vertical menu: Set to "h" or "v"
                classname: 'ddsmoothmenu', //class added to menu's outer DIV
                //customtheme: ["#5c9ccc url(images/ui-bg_gloss-wave_55_5c9ccc_500x100.png) 50% 50% repeat-x", "#dfeffc url(images/ui-bg_glass_85_dfeffc_1x400.png) 50% 50% repeat-x"],
                //customtheme: [$('#take').css('backgroundColor')+" "+$('#take').css('background-image')+" 50% 50% repeat-x", "#dfeffc url(images/ui-bg_glass_85_dfeffc_1x400.png) 50% 50% repeat-x"],
                contentsource: "markup" //"markup" or ["container_id", "path_to_menu_file"]
            });
           
	    $(function() ${r"{"}
                $(".btn_delete").click(function()${r"{"}
                    if(confirm("Deseja realmente excluir o registro ? "))${r"{"}
                        location.href=$(this).attr('action');
                    }else${r"{"}
                        return false;
                    }
                });
            });

        </script>

    </head>
    <body>

        <table cellspacing="0" cellspacing="0" width="100%">
            <tr>
                <td class="ui-state-default" style="height: 70px;"></td>
            </tr>
        </table>


        <div id="smoothmenu1">
            <ul>

                <li><a href="#">Cadastros</a>
                    <ul class="ui-widget-header">
			<#list menu as m>
                        <li class="ui-widget-header"><a href="@${r"{"}m.index()}">m</a></li>
			<#/list>
                    </ul>
                </li>
                <li><a href="#">Temas</a>
                    <ul class="ui-widget-header">
                        <li class="ui-widget-header"><a href="@${r"{"}DefaultController.changeTema("redmond")}">Redmond</a></li>
                        <li class="ui-widget-header"><a href="@${r"{"}DefaultController.changeTema("ui-lightness")}">Ui-lightness</a></li>
                        <li class="ui-widget-header"><a href="@${r"{"}DefaultController.changeTema("sunny")}">Sunny</a></li>
                        <li class="ui-widget-header"><a href="@${r"{"}DefaultController.changeTema("le-frog")}">Le Frog</a></li>
                        <li class="ui-widget-header"><a href="@${r"{"}DefaultController.changeTema("blitzer")}">Blitzer</a></li>
                    </ul>
                </li>

            </ul>
            <br style="clear: left" />
        </div>

        

        <div style="padding:8px">

            #${r"{"}if flash.success}

            <script>
                window.onload = function()${r"{"}
                    setInterval("document.getElementById('msginfo').style.display='none'", 5000);
                }
            </script>

            <div id="msginfo" class="ui-widget">
                #${r"{"}box_success message:flash.success/}
            </div>

            #${r"{"}/if}

            #${r"{"}if flash.error || error}

            <script>
                window.onload = function()${r"{"}
                    setInterval("document.getElementById('msginfo').style.display='none'", 5000);
                }
            </script>

            <div id="msginfo" class="ui-widget">
                #${r"{"}box_error label:'Alerta:', message:error ?: flash.error/}
            </div>
            #${r"{"}/if}

            #${r"{"}doLayout /}

        </div>

    </body>
</html>
