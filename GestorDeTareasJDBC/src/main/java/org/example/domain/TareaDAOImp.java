package org.example.domain;

import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
@Log
public class TareaDAOImp implements TareaDAO {

    private Connection connection;

    private final static String queryLoadAll = "SELECT * FROM tarea";
    private final static String queryLoad = "select * from tarea where id = ?";
    private final static String queryUpdate = "update tarea( titulo, prioridad, usuario_id, categoria, descripcion) \" +\n" +
            "            \"VALUES (?,?,?,?,?) WHERE id=?";
    private final static String queryDelete = "";
    private final static String querySave = "insert into tarea( titulo, prioridad, usuario_id, categoria, descripcion) " +
            "VALUES (?,?,?,?,?)";
    private final static String queryLoadAllByResponsable = "SELECT * FROM tarea WHERE usuario_id=?";

    public TareaDAOImp(Connection c){
        connection = c;
    }

    @Override
    public Tarea load(Long id) {
        Tarea salida = null;

        try( var pst = connection.prepareStatement(queryLoad)){
            pst.setLong(1,id);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = (new TareaAdapter()).loadFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        salida.setUsuario( new UserDAOImp(DBConnection.getConnection()).load(salida.getUsuario_id()));

        return salida;

    }

    @Override
    public ArrayList<Tarea> loadAll() {
        var salida = new ArrayList<Tarea>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery(queryLoadAll);

            while(rs.next()){
                salida.add( (new TareaAdapter()).loadFromResultSet(rs) );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public ArrayList<Tarea> loadAllByResponsable(Long responsable) {
        var salida = new ArrayList<Tarea>();

        try(PreparedStatement pst = connection.prepareStatement(queryLoadAllByResponsable)){
            pst.setLong(1,responsable);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                salida.add( (new TareaAdapter()).loadFromResultSet(rs) );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public Tarea save(Tarea t) {
        try(PreparedStatement pst = connection.prepareStatement(querySave,Statement.RETURN_GENERATED_KEYS)){

            log.info(querySave);
            log.info(String.valueOf(t));
            pst.setString(1,t.getTitulo());
            pst.setString(2,t.getPrioridad());
            pst.setLong(3,t.getUsuario_id());
            pst.setString(4,t.getCategoria());
            pst.setString(5,t.getDescripcion());

            if(pst.executeUpdate()==1){
                ResultSet ids = pst.getGeneratedKeys();
                ids.next();
                Long generatedId = ids.getLong(1);
                log.info("generatedId = "+generatedId);
                t.setId(generatedId);
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    @Override
    public Tarea update(Tarea t) {
        return null;
    }

    @Override
    public void remove(Tarea t) {

    }
}
