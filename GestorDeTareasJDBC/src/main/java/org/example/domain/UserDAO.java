package org.example.domain;

import java.util.ArrayList;

public interface UserDAO {
    public User load(Long id);
    public ArrayList<User> loadAll();
    public User save(User t);
    public User update(User t);
    public void remove(User t);
}
