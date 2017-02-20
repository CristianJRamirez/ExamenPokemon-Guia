import org.apache.http.client.fluent.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class pkdb {


    public static String getHTML(String urlAddress) throws Exception{
        String data = "";
        try {
            data = Request.Get("http://pokeapi.co/api/v1/" + urlAddress).execute().returnContent().asString();
          //  System.out.println(data);
        } catch (Exception e){
            e.printStackTrace();
        }

        return data;

    }

    /*
            MAIN: Tria la funció que vols realitzar de manera pulida.
            Un cop i sortir.
     */
    /* OPCIONES ESCOGIDAS : ---->>>>
    • Afegiu una funcionalitat que permeti a l'usuari saber quins pokemons hi ha a la base de dades. S’ha de poder seleccionar un pokemon i mostrar els seus tipus.
    • Afegiu una funcionalitat que permeti a l'usuari saber quins tipus hi ha a la base de dades. S’ha de poder seleccionar un tipus i mostrar tots els pokemons
        d’aquella classe.
      Tracta correctament les excepcions, per exemple, s’introdueix un pokemon individualment, i ja hi era avisi.
     */

    public static void main(String[] args) throws Exception {
        System.out.println("Tria una opció:");
        System.out.println("1. Insereix pokemon de la API( HECHA !!) ");
        System.out.println("2. Mostra lista de pokemons y selecionar uno y dar sus tipos ( HECHA !!) ");
        System.out.println("3. Mostra lista de tipos y selecionar uno y que pokemons tienen ese tipo ( HECHA !!) ");
        System.out.println("4. Mostra pokemons d'un tipus");
        System.out.println("5. Mostra info d'un pokemon");
        System.out.println("Altres: Sortir");

        Scanner sc = new Scanner(System.in);
        int opcio = Integer.parseInt(sc.nextLine());


            switch (opcio) {
                case 1:
                    insereix_poke();
                    break;
                case 2:
                    mostraPokes();
                    break;
                case 3:
                    mostraTipus();
                    break;
                case 4:
                    mostraPokesperTipus();
                    break;
                case 5:
                    mostraPokesmon();
                    break;
                default:
                    System.out.println("Que tingui un bon dia!");
            }
    }

    /**
     * Implementada la funció mostra pokemon de manera que té relació amb
     * la base de dades.
     * @throws Exception
     */
    private static void mostraPokesmon() throws Exception {
        return;

    }

    /**
     * Implementaada la funció que insereix pokemons de la Api
     * @throws Exception
     */
    public static void insereix_poke() throws Exception{

        int ids [] = {61,86,90,98,183,79};
        for (int id:ids) {

            Pk pk2 = getPoke(id);

            pk2.imprimir();

            insertarPokemon(pk2);
        }
    }

    /**
     * Insertar el pokemon en la BBDD
     * @param pk Pokemon a introducir
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void insertarPokemon(Pk pk) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        String sql_insert=  "INSERT INTO Pokemon" +
                            "(Codigo,Nombre)  VALUES" +
                            " (?,?);";


            PreparedStatement preparedStatement = c.prepareStatement(sql_insert);

        //comprovamos si existe elpokemon en la BBDD
        if (!existePokemon(pk.getIdentificador())) {
            preparedStatement.setInt(1, pk.getIdentificador());
            preparedStatement.setString(2, pk.getNombre());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            c.close();

            añadirDatosRelacionYTipo(pk);

            System.out.println("S'ha inserit el "+pk.getNombre());
        }else

        {
            System.out.println("El poke ja existia");
            añadirDatosRelacionYTipo(pk);
        }
    }

    /**
     * Añadir datos del tipo del pokemon selecionado
     * @param pk pokemon introducido
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static void añadirDatosRelacionYTipo(Pk pk) throws SQLException, ClassNotFoundException {

        //añadimos la Relacion Pokemon-tipos del pokemon introducido si no existe en la bbdd
        for (int i = 0; i < pk.getTipos().length; i++) {
            if (!existePokemonTipo(pk.getIdentificador(),pk.getTipos()[i].getIdty())) {
                insertarPokemonTipo(pk.getIdentificador(), pk.getTipos()[i].getIdty());
            }else
            {
                System.out.println("Ya existe la relacion Pokemon-Tipo");
            }
        }


        //añadimos el tipos del pokemon introducido si no existe en la bbdd
        for (int j = 0; j < pk.getTipos().length; j++) {
            if (!existeTipo(pk.getTipos()[j].getIdty())) {
                insertarTipo(pk.getTipos()[j]);
            }else
            {
                System.out.println("Ya existe el Tipo con ese Id");
            }
        }
    }


    /**
     * insertar datos de la relacion Pokemon-tipo en la BBDD
     * @param idPoke
     * @param idTipo
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void insertarPokemonTipo(int idPoke,int idTipo) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        String sql_insert = "INSERT INTO PokeTipo" +
                "(IdPokemon,IdTipo)  VALUES" +
                " (?,?);";


        PreparedStatement preparedStatement = c.prepareStatement(sql_insert);

        preparedStatement.setInt(1, idPoke);
        preparedStatement.setInt(2, idTipo);

        preparedStatement.executeUpdate();
        c.close();
    }

    /**
     * insertar datos del tipo en la BBDD
     * @param tp
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void insertarTipo(Tipo tp) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        String sql_insert = "INSERT INTO Tipo" +
                "(Codigo,Tipo)  VALUES" +
                " (?,?);";


        PreparedStatement preparedStatement = c.prepareStatement(sql_insert);


        preparedStatement.setInt(1, tp.getIdty());
        preparedStatement.setString(2, tp.getTipus());

        preparedStatement.executeUpdate();
        c.close();
    }



    public static Pk getPoke(int k) throws Exception {
        String url = "pokemon/"+k+"/";
        String jsonurl = getHTML(url);
        return SJPokeApi(jsonurl);

    }

    /**
     * Mostra lista de pokemons y selecionar uno y dar sus tipos
     * @throws Exception
     */
    public static void mostraPokes() throws Exception{

        listaPokemons();

        Scanner sc = new Scanner(System.in);
        System.out.println("Introdueix la Id del pokemon a buscar:");
        int idLeer = sc.nextInt();

        if (existePokemon(idLeer)) {
            ArrayList<Integer> tipos=verRelacionTipos(idLeer);

            verTipos(tipos);
        }
            else
        {
            System.out.println("Ese Id es incorrecto");
        }
        sc.close();


    }


    /**
     * Mostra lista de tipos y selecionar uno y que pokemons tienen ese tipo
     * @throws Exception
     */
    public static void mostraTipus() throws Exception{

        listaTipos();

        Scanner sc = new Scanner(System.in);
        System.out.println("Introdueix la Id del Tipo a buscar:");
        int idLeer = sc.nextInt();

        if (existeTipo(idLeer)) {
            ArrayList<Integer> pokes = verRelacionPoke(idLeer);

            verPokes(pokes);
        }
        else
        {
            System.out.println("Ese Id es incorrecto");
        }
        sc.close();


    }

    public static void mostraPokesperTipus() throws Exception{
    }

    public static Pk SJPokeApi(String cadena){
        Object obj = JSONValue.parse(cadena);
        JSONObject jobj = (JSONObject)obj; // Contiene toda la información del JSON
        String pknom = (String)jobj.get("name");
        String id = Long.toString((Long)jobj.get("national_id"));
        JSONArray jarray = (JSONArray)jobj.get("types");
        String mmg[] = new String[jarray.size()];

        int mmg_id[] = new int[jarray.size()];

        for (int i = 0; i < jarray.size(); i++) {
            JSONObject jobjda = (JSONObject)jarray.get(i);
            mmg[i] = (String)jobjda.get("name");
            mmg_id[i] = tratar_cadena_id_tipo((String)jobjda.get("resource_uri"));
        }
        return new Pk(pknom,id,mmg,mmg_id);
    }

    private static int tratar_cadena_id_tipo(String cadena){
        String definitiva = cadena.substring(13,cadena.length()-1);
        System.out.println(definitiva);
        return  Integer.parseInt(definitiva);
    }

    /**
     * Comprovar si existe un pokemon apartir de su id
     * @param id id del pokemon a comprovar
     * @return valor booleano si existe o no el pokemon
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static boolean existePokemon(int id) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Pokemon WHERE Codigo ="+id+";" );

        boolean existe = false;
        while ( rs.next() ) {
            existe=true;
            String  nombre = rs.getString("Nombre");
            System.out.println();
        }

        if(!existe)
        {
            System.out.println("No existe el Pokemoncon ese id en la BBDD");

        }

        stmt.close();
        rs.close();
        c.close();

        return existe;
    }

    /**
     * Comprovar si existe la relacion pokemon tipo
     * @param idpoke id del pokemon a comprovar
     * @param idtipo id del tipo a comprovar
     * @return valor booleano si existe o no el pokemon
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static boolean existePokemonTipo(int idpoke,int idtipo) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM PokeTipo WHERE IdPokemon ="+idpoke+" and IdTipo ="+idtipo+";" );

        boolean existe = false;
        while ( rs.next() ) {
            existe=true;
            String  ID = rs.getString("IdTipo");
            System.out.println();
        }

        if(!existe)
        {
            System.out.println("No existe el Pokemon con el id en la BBDD de la tabla POKEMON/TIPO");

        }

        stmt.close();
        rs.close();
        c.close();

        return existe;
    }

    /**
     * Comprovar si existe un tipo apartir de su id
     * @param id id del tipo a comprovar
     * @return valor booleano si existe o no el pokemon
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static boolean existeTipo(int id) throws ClassNotFoundException, SQLException {

        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Tipo WHERE Codigo ="+id+";" );

        boolean existe = false;
        while ( rs.next() ) {
            existe=true;
            String  ID = rs.getString("Codigo");
            System.out.println();
        }

        if(!existe)
        {
            System.out.println("No existe el Pokemon con el id en la BBDD de la tabla POKEMON/TIPO");

        }

        stmt.close();
        rs.close();
        c.close();

        return existe;
    }


    /**
     * listado pokemons para mostrar
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void listaPokemons() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Pokemon;" );


        System.out.println("Pokemon -> Nombre [ID]");
        while ( rs.next() ) {
            int id =rs.getInt("Codigo");
            String  nombre = rs.getString("Nombre");
            System.out.println("Pokemon -> "+nombre +" ["+id+"]");
        }


        stmt.close();
        rs.close();
        c.close();
    }

    /**
     * lista de tipos a partir de un pokemon
     * @param idPoke pokemon a buscar idtipo
     * @return lista de tipos con el id del pokemon selecionado
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static ArrayList<Integer> verRelacionTipos(int idPoke) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM PokeTipo WHERE IdPokemon= "+idPoke+";" );


        ArrayList<Integer> tipos =new ArrayList<Integer>();
        while ( rs.next() ) {
            int idtipo =rs.getInt("IdTipo");
            tipos.add(idtipo);
        }


        stmt.close();
        rs.close();
        c.close();
        return tipos;
    }

    /**
     * mostrar los tipos que tiene el pokemo selecionado anteriormente
     * @param tipos
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void verTipos(List<Integer>tipos) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();
        ResultSet rs=null;
        System.out.println("Tipo -> Tipo [ID]");
        for (int tipo:tipos) {


            rs = stmt.executeQuery("SELECT * FROM Tipo WHERE Codigo="+tipo+";");


            while (rs.next()) {
                int id = rs.getInt("codigo");
                String tp = rs.getString("Tipo");
                System.out.println("Tipo -> " + tp + " [" + id + "]");
            }
        }


        stmt.close();
        rs.close();
        c.close();
    }

    /**
     * listado tipos para mostrar
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void listaTipos() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Tipo;" );


        System.out.println("Tipo -> Tipo [ID]");
        while ( rs.next() ) {
            int id =rs.getInt("Codigo");
            String  tipo = rs.getString("Tipo");
            System.out.println("Tipo -> "+tipo +" ["+id+"]");
        }


        stmt.close();
        rs.close();
        c.close();
    }

    /**
     * lista de tipos a partir de un pokemon
     * @param idTipo tipo a buscar idPokemon
     * @return lista de pokemons con el idtipo selecionado
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static ArrayList<Integer> verRelacionPoke(int idTipo) throws ClassNotFoundException, SQLException {


        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM PokeTipo WHERE IdTipo= "+idTipo+";" );


        ArrayList<Integer> pokes =new ArrayList<Integer>();
        while ( rs.next() ) {
            int  idpk = rs.getInt("IdPokemon");
            pokes.add(idpk);
        }


        stmt.close();
        rs.close();
        c.close();
        return pokes;
    }

    /**
     * ver los pokemons del tipo selecionado anteriormente
     * @param pokes
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void verPokes(List<Integer>pokes) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
        c.setAutoCommit(false);
        System.out.println("Opened database SELECT successfully");

        Statement stmt = c.createStatement();
        ResultSet rs=null;

        System.out.println("Nombre -> Nombre [ID]");
        for (int idPk:pokes) {


            rs = stmt.executeQuery("SELECT * FROM Pokemon WHERE Codigo="+idPk+";");


            while (rs.next()) {
                int id = rs.getInt("codigo");
                String tp = rs.getString("Nombre");
                System.out.println("Nombre -> " + tp + " [" + id + "]");
            }
        }


        stmt.close();
        rs.close();
        c.close();
    }
}
