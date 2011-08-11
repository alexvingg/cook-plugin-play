/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package play.utils;

import japa.parser.JavaParser;
import japa.parser.JavaParser.*;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ClasseUtil {
    public List<AtributoUtil> getAtributes(String url) {
        CompilationUnit cu = null;
        List<AtributoUtil> atributos = new ArrayList<AtributoUtil>();
        File file = new File(url);
        try {
            
            cu = JavaParser.parse(file);
            List<TypeDeclaration> lista = cu.getTypes();
            List<BodyDeclaration> b2 = lista.get(0).getMembers();

            for (int i = 0; i < b2.size(); i++) {
                AtributoUtil AtributeUtils = new AtributoUtil();
                try {
                    try {
                        List<AnnotationExpr> ann = b2.get(i).getAnnotations();
                        for (int j = 0; j < ann.size(); j++) {
                            if (ann.get(j).getName().toString().equals("OneToMany")
                                    || ann.get(j).getName().toString().equals("ManyToMany")
                                    || ann.get(j).getName().toString().equals("ManyToOne")
                                    || ann.get(j).getName().toString().equals("OneToOne")) {
                                AtributeUtils.setAnnotation(ann.get(j).getName().toString());
                            }
                        }
                    } catch (Exception e) {
                        //System.out.println("Erro pois não tem annotation");
                    }
                    FieldDeclaration fiedl = (FieldDeclaration) b2.get(i);
                    //System.out.println("Tipo : "+fiedl.getType());
                    AtributeUtils.setTipo(fiedl.getType().toString());
                    List<VariableDeclarator> variaveis = fiedl.getVariables();
                    //System.out.println("Variavel :"+variaveis.get(0).toString());
                    AtributeUtils.setNome(variaveis.get(0).toString());
                    atributos.add(AtributeUtils);
                } catch (Exception e) {
                    //System.out.println("Erro não é Field");
                }
            }
        } catch (Exception ex) {
            //System.out.println("Saida:" + ex.getMessage());
        }

        return atributos;
    }

    public String verificaStringClass(String url) throws Exception
    {
        CompilationUnit cu = null;
        File file = new File(url);
        try {
            //in = new SEDInputStream(filename);
            cu = JavaParser.parse(file);
            List<TypeDeclaration> lista = cu.getTypes();
            List<BodyDeclaration> b2 = lista.get(0).getMembers();
            return lista.get(0).toString();
        }catch(Exception e){
            throw new  Exception(e.getMessage());
        }
    }
}
