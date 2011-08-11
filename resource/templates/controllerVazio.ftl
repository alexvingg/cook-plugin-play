package controllers;

import java.util.List;

public class ${class} extends DefaultController {

    public static void index(){
		//List<${inflector.singularize("${class}")}> ${class?lower_case} = ${inflector.singularize("${class}")}.findAll();
		//render(${class?lower_case});	
		render();
    }

    public static void form(){	
		render();
    }
	
    public static void view(){    
		render();
    }
}
