package org.example.domain;

import com.mysql.cj.log.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Database {

    private static Connection connection;

    private static Logger logger;

    static{
        logger = Logger.getLogger(Database.class.getName());

        String url = "jdbc:mysql://localhost:3306/ad";
        String user = "root";
        String password = "";
        try {
            connection = DriverManager.getConnection(url,user,password);
            logger.info("Successful connection to database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection() {
        return connection;
    }

    public static ArrayList<String[]> getAll(){

        var salida = new ArrayList<String[]>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery("SELECT * FROM tarea");

            while(rs.next()){
                salida.add( convertToRow(rs) );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }


    public static ArrayList<Tarea> getAllTarea(){

        var salida = new ArrayList<Tarea>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery("SELECT * FROM tarea");

            while(rs.next()){
                salida.add( (new TareaAdapter()).loadFromResultSet(rs) );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }


    private static String[] convertToRow(ResultSet rs) throws SQLException {
        String[] fila = {
                String.valueOf(rs.getInt("id")),
                rs.getString("titulo"),
                rs.getString("prioridad"),
                String.valueOf(rs.getInt("usuario_id")),
                rs.getString("categoria"),
                rs.getString("descripcion")
        };
        return fila;
    }

}
