/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package play.database;

import play.exception.DriverException;
import play.exception.ModelException;
import cook.util.PrintUtil;
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import play.utils.Inflector;

/**
 *
 * @author alex
 */
public class CriaModel
{

    private Conexao c;
    private String caminhoModel;

    public String getCaminhoModel()
    {
        return caminhoModel;
    }

    public void setCaminhoModel(String caminho_model)
    {
        Inflector.getInstance().humanize(caminho_model);
        this.caminhoModel = caminho_model;
    }

    public CriaModel(Conexao c)
    {
        this.c = c;
    }

    private Conexao getConexao()
    {
        return c;
    }

    public boolean verificaAssociativa(String nomeTabela) throws ClassNotFoundException, SQLException,DriverException
    {
        int primayKey = 0;
        DatabaseMetaData metadata;
        ResultSet rs;
        int contadorAtributos = 0;
        int contadorChaveFK = 0;
        try {
            getConexao().openConnection();
            metadata = getConexao().getConnection().getMetaData();
            rs = metadata.getColumns(null, null, nomeTabela, "%");

            while (rs.next()) {
                contadorAtributos++;
            }
            
            rs = null;
            rs = metadata.getImportedKeys(getConexao().getConnection().getCatalog(), null, nomeTabela);
            while (rs.next()) {
                contadorChaveFK++;
            }
            rs.close();

            rs = null;
            rs = metadata.getPrimaryKeys(null, null, nomeTabela);
            while (rs.next()) {
                primayKey++;
            }
        } catch (SQLException ex) {
            throw new SQLException();
        } finally {
            getConexao().closeConnection();
        }
        if (primayKey > 1) {
            if (contadorAtributos == contadorChaveFK) {
                return true;
            }
        } else if (primayKey == 1) {
            if (contadorAtributos == contadorChaveFK + 1) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param has_atributos HashMap<String, Atribute> de atributos da tabela.
     * @param tabela Nome da tabela para pegar os atributos.
     * @return HashMap<String, Atribute>
     */
    private LinkedHashMap<String, Atributo> carregaManyToOne(LinkedHashMap<String, Atributo> has_atributos, String tabela) throws ModelException, ClassNotFoundException, DriverException
    {
        DatabaseMetaData metadata;
        ResultSet rs;
        try {
            getConexao().openConnection();
            metadata = getConexao().getConnection().getMetaData();
            rs = metadata.getImportedKeys(getConexao().getConnection().getCatalog(), null, tabela);
            List<String> anotacoes;
            while (rs.next()) {
                int op = -1;
                int cont = 0;

                while (op != 0 && op != 1 && op != 2) {
                    if (cont == 1) {
                        PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta !" + PrintUtil.getColorReset());
                        PrintUtil.outn("");
                        cont = 0;
                    }
                    PrintUtil.outn("Existe uma ligacao nao identificada com "
                            + PrintUtil.getGreenFont() + Inflector.getInstance().singularize(corrigeNome(rs.getString(3)))
                            + PrintUtil.getColorReset()
                            + ", esta ligacao pode ser: ");
                    PrintUtil.outn("");
                    PrintUtil.outn(PrintUtil.getYellowFont() + "[0] Nenhuma" + PrintUtil.getColorReset());
                    PrintUtil.outn(PrintUtil.getYellowFont() + "[1] belongsTo (ManyToOne)" + PrintUtil.getColorReset());
                    //PrintUtil.outn(PrintUtil.getYellowFont() + "[2] hasOne (OneToOne)" + PrintUtil.getColorReset());
                    PrintUtil.outn("");

                    op = PrintUtil.inInt("Digite a opcao: ");
                    PrintUtil.outn("");
                    cont++;
                }

                anotacoes = new ArrayList<String>();
                if (has_atributos.containsKey(rs.getString("FKCOLUMN_NAME"))) {

                    switch (op) {
                        case 1:
                            this.verificaExisteModel(caminhoModel + "/", Inflector.getInstance().singularize(corrigeNome(rs.getString(3))) + ".java");
                            anotacoes.add("@ManyToOne(fetch = FetchType.LAZY)");
                            anotacoes.add("@JoinColumn(name =\"" + rs.getString("FKCOLUMN_NAME") + "\")");
                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setType(Inflector.getInstance().singularize(corrigeNome(rs.getString(3))));
                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setField(Inflector.getInstance().singularize(rs.getString(3)));
                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setAnnotation(anotacoes);

                            break;
//                        case 2:
//                            this.verificaExisteModel(caminho_model + "/", Inflector.getInstance().singularize(corrigeNome(rs.getString(3))) + ".java");
//                            anotacoes.add("@OneToOne(fetch = FetchType.LAZY)");
//                            anotacoes.add("@JoinColumn(name =\"" + rs.getString("FKCOLUMN_NAME") + "\")");
//                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setType(Inflector.getInstance().singularize(corrigeNome(rs.getString(3))));
//                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setField(Inflector.getInstance().singularize(rs.getString(3)));
//                            has_atributos.get(rs.getString("FKCOLUMN_NAME")).setAnnotation(anotacoes);
//
//                            break;
                        default:
                            has_atributos.remove(rs.getString("FKCOLUMN_NAME"));
                    }

                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(CriaModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            getConexao().closeConnection();
        }
        return has_atributos;
    }

    /**
     * Verifica se existe o model na pasta
     * @param Diretorio do arquivo
     * @return Boolean
     */
    private void verificaExisteModel(String dir, String arq)
    {
        if ((new File(dir + arq)).exists()) {
        } else {
            PrintUtil.outn(PrintUtil.getRedFont()
                    + "Atencao e necessario que seja criado o model "
                    + arq + "." + PrintUtil.getColorReset());
            PrintUtil.outn("");
        }

    }

    /**
     * 
     * @param has_atributos Lista de atributos
     * @param tabela Nome da tabela que vai pegar os atributos
     * @return HashMap<String, Atribute> Lista de atributos
     */
    private LinkedHashMap<String, Atributo> carregaOneToManyAndManyToMany(LinkedHashMap<String, Atributo> has_atributos, String tabela) throws ModelException, ClassNotFoundException, DriverException
    {
        DatabaseMetaData metadata;
        ResultSet rs;
        String opcao;
        List<String> anotacoes;
        try {
            getConexao().openConnection();
            String tab_aux = "";
            metadata = getConexao().getConnection().getMetaData();
            rs = metadata.getExportedKeys(getConexao().getConnection().getCatalog(), null, tabela);
            List<String> campo;
            String campo_tabela = "";
            while (rs.next()) {
                campo = new ArrayList<String>();
                ResultSet rst;
                ResultSet rstt;
                anotacoes = new ArrayList<String>();
                int count = 0;

                int op = -1;
                int cont = 0;

                if (this.verificaAssociativa(rs.getString("FKTABLE_NAME"))) {
                    while (op != 0 && op != 1 && op != 2) {
                        if (cont == 1) {
                            PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta !" + PrintUtil.getColorReset());
                            PrintUtil.outn("");
                            cont = 0;
                        }
                        PrintUtil.outn("Existe uma ligacao nao identificada com "
                                + PrintUtil.getGreenFont() + Inflector.getInstance().singularize(corrigeNome(rs.getString("FKTABLE_NAME")))
                                + PrintUtil.getColorReset()
                                + ", esta ligacao pode ser: ");
                        PrintUtil.outn("");
                        PrintUtil.outn(PrintUtil.getYellowFont() + "[0] Nenhuma" + PrintUtil.getColorReset());
                        PrintUtil.outn(PrintUtil.getYellowFont() + "[1] hasMany (OneToMany) " + PrintUtil.getColorReset());
                        PrintUtil.outn(PrintUtil.getYellowFont() + "[2] hasAndBelongsToMany (ManyToMany)" + PrintUtil.getColorReset());
                        PrintUtil.outn("");

                        op = PrintUtil.inInt("Digite a opcao: ");
                        PrintUtil.outn("");
                        cont++;
                    }
                } else {
                    while (op != 0 && op != 1) {
                        if (cont == 1) {
                            PrintUtil.outn(PrintUtil.getRedFont() + "Digite a opcao correta !" + PrintUtil.getColorReset());
                            PrintUtil.outn("");
                            cont = 0;
                        }
                        PrintUtil.outn("Existe uma ligacao nao identificada com "
                                + PrintUtil.getGreenFont() + Inflector.getInstance().singularize(corrigeNome(rs.getString("FKTABLE_NAME")))
                                + PrintUtil.getColorReset()
                                + ", esta ligacao pode ser: ");
                        PrintUtil.outn("");
                        PrintUtil.outn(PrintUtil.getYellowFont() + "[0] Nenhuma" + PrintUtil.getColorReset());
                        PrintUtil.outn(PrintUtil.getYellowFont() + "[1] hasMany (OneToMany) " + PrintUtil.getColorReset());
                        PrintUtil.outn("");

                        op = PrintUtil.inInt("Digite a opcao: ");
                        PrintUtil.outn("");
                        cont++;
                    }
                }

                switch (op) {
                    case 1:
                        this.verificaExisteModel(caminhoModel + "/", Inflector.getInstance().singularize(corrigeNome(rs.getString("FKTABLE_NAME"))) + ".java");
                        Atributo a = new Atributo();
                        a.setField(rs.getString("FKTABLE_NAME"));
                        a.setType("Set<" + Inflector.getInstance().singularize(corrigeNome(rs.getString("FKTABLE_NAME"))) + ">");
                        anotacoes.add("@OneToMany(cascade=CascadeType.ALL)");
                        anotacoes.add("@JoinColumn(name = \"" + rs.getString("FKCOLUMN_NAME") + "\")");
                        a.setAnnotation(anotacoes);
                        has_atributos.put(rs.getString("FKTABLE_NAME"), a);

                        break;

                    case 2:
                        tab_aux = rs.getString("FKTABLE_NAME");

                        rst = metadata.getColumns(null, null, rs.getString("FKTABLE_NAME"), "%");
                        if (verificaAssociativa(rs.getString("FKTABLE_NAME"))) {
                            while (rst.next()) {
                                if (!rs.getString("FKCOLUMN_NAME").equals(rst.getString(4))) {
                                    if (count == 0) {
                                        PrintUtil.outn("Qual o outro campo que faz chave com a tabela?");
                                        PrintUtil.outn("");
                                    }
                                    PrintUtil.outn(PrintUtil.getYellowFont() + "Campo: " + "[" + count + "] " + rst.getString(4) + PrintUtil.getColorReset());
                                    campo.add(rst.getString(4));
                                    count++;
                                }
                            }
                            PrintUtil.outn("");
                            int opcao_campo = PrintUtil.inInt("Digite a opcao: ");
                            PrintUtil.outn("");
                            count = 0;
                            rstt = metadata.getImportedKeys(getConexao().getConnection().getCatalog(), null, tab_aux);
                            while (rstt.next()) {
                                if (campo.get(opcao_campo).equals(rstt.getString("FKCOLUMN_NAME"))) {
                                    campo_tabela = rstt.getString(3);
                                }
                            }

                            rstt.close();   
                            this.verificaExisteModel(caminhoModel + "/", corrigeNome(Inflector.getInstance().singularize(campo_tabela)) + ".java");
                            anotacoes.add("@ManyToMany()");
                            anotacoes.add("@JoinTable(name=\"" + rs.getString("FKTABLE_NAME") + "\", joinColumns={@JoinColumn"
                                    + "(name=\"" + rs.getString("FKCOLUMN_NAME") + "\")}, "
                                    + "inverseJoinColumns={@JoinColumn(name=\"" + campo.get(opcao_campo) + "\")})");
                            Atributo a2 = new Atributo();
                            a2.setAnnotation(anotacoes);
                            a2.setField(campo_tabela);
                            a2.setType("Set<" + corrigeNome(Inflector.getInstance().singularize(campo_tabela)) + ">");
                            has_atributos.put(campo_tabela, a2);
                        } else {
                            PrintUtil.outn("Essa tabela nao e associativa !");
                        }
                        rst.close();

                        break;
                    default:
                        break;
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(CriaModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            getConexao().closeConnection();
        }
        return has_atributos;
    }

    private String corrigeNome(String nome)
    {
        String classe = Inflector.getInstance().humanize(nome);

        String[] nomeClasse = classe.split(" ");

        String nomeClasseCorreta = "";

        for (int i = 0; i < nomeClasse.length; i++) {
            nomeClasseCorreta += Inflector.getInstance().humanize(nomeClasse[i]);
        }
        return nomeClasseCorreta;
    }

    /**
     * Metodo que cria o atributo com tipo e nome.
     * @param tabela Nome da tabela.
     * @return HasMap de atributos preenchidos com tipo e o nome.
     */
    public LinkedHashMap<String, Atributo> criarAtributo(String tabela) throws ModelException, ClassNotFoundException, DriverException
    {
       LinkedHashMap<String, Atributo> has_atributos = new LinkedHashMap<String, Atributo>();
        DatabaseMetaData metadata;
        ResultSet rs;
        try {
            getConexao().openConnection();
            metadata = getConexao().getConnection().getMetaData();
            rs = metadata.getColumns(null, null, tabela, "%");
            /**
             * Cria os atributos.
             */
            PrintUtil.outn("Qual o displayField ?");
            PrintUtil.outn("");
            int count = 0;
            List<String> display = new ArrayList<String>();
            while (rs.next()) {
                PrintUtil.outn(PrintUtil.getYellowFont() + "[" + count + "]" + rs.getString(4) + PrintUtil.getColorReset());
                count++;
                display.add(rs.getString(4));
                Atributo a = new Atributo();
                a.setField(rs.getString(4));
                a.setType(rs.getString("TYPE_NAME"));
                
                if(rs.getString(11).equals("0")){
                    a.getAnnotation().add("@Required(message=\"* obrigatório.\")");
                }
                
                if(a.getType().equals("Integer") || 
                        a.getType().equals("String") || 
                        a.getType().equals("int") || 
                        a.getType().equals("Double") ||
                        a.getType().equals("BigInteger")){
                    a.getAnnotation().add("@MaxSize(message = \"* tamanho máximo %2$s caracteres.\",value="+rs.getString(7)+")");
                }
                
                
                has_atributos.put(rs.getString(4), a);
            }
                       
            PrintUtil.outn("");
            int opcao_display = PrintUtil.inInt("Digite a opcao: ");
            PrintUtil.outn("");
            has_atributos.get(display.get(opcao_display)).setDisplay(true);
            rs.close();

            /**
             * Define a primary key
             */
            rs = metadata.getPrimaryKeys(getConexao().getConnection().getCatalog(), null, tabela);
            while (rs.next()) {
                if (has_atributos.containsKey(rs.getString(4))) {
                    has_atributos.get(rs.getString(4)).setPk(true);
                }
            }
            rs.close();
            /**
             * Define as relações ManyToOne
             */
            has_atributos = this.carregaManyToOne(has_atributos, tabela);

            /**
             * Define as relações OneToMany e ManyToOne
             */
            has_atributos = this.carregaOneToManyAndManyToMany(has_atributos, tabela);

        } catch (SQLException ex) {
            Logger.getLogger(CriaModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            getConexao().closeConnection();
        }
        return has_atributos;
    }
}
