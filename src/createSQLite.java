/**
 * Created by dremon on 09/11/15.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class createSQLite {


    public static void main(String[] args) {
        //Connection -> para gestionar connexiones
        Connection c = null;

        //Statement -> fromata instrucciones sql
        Statement stmt = null;
        try {

            //driver -> Ctrl + Alt + shift + S -> añadir la libreria
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            System.out.println("Opened database Crear Tablas successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE if not exists Pokemon " +
                    "(Codigo int PRIMARY KEY not null,"+
                    " Nombre           TEXT    NOT NULL)";

            stmt.executeUpdate(sql);
            sql = "CREATE TABLE if not exists Tipo " +
                    "(Codigo int PRIMARY KEY not null,"+
                    " Tipo           TEXT    NOT NULL)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE if not exists PokeTipo " +
                    "(IdPokemon           int    NOT NULL," +
                    " IdTipo           int    NOT NULL)";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

}



