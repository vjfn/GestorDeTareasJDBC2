package org.example.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAOImp implements UserDAO{

    private final Connection connection;

    public UserDAOImp(Connection c){
        connection = c;
    }

    private final static String queryLoadAll = "SELECT * FROM user";
    private final static String queryLoad = "select * from user where id = ?";
    private final static String queryLoadbyName = "select * from user where name = ?";

    @Override
    public User load(Long id) {
        User salida = null;

        try( var pst = connection.prepareStatement(queryLoad)){
            pst.setLong(1,id);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = new User(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    public User load(String name) {
        User salida = null;

        try( var pst = connection.prepareStatement(queryLoadbyName)){
            pst.setString(1,name);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = new User(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    @Override
    public ArrayList<User> loadAll() {
        var salida = new ArrayList<User>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery(queryLoadAll);

            while(rs.next()){
                salida.add( new User(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public User save(User t) {
        return null;
    }

    @Override
    public User update(User t) {
        return null;
    }

    @Override
    public void remove(User t) {

    }
}
