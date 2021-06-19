package net.thumbtack.school.hospital.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    // REVU лучше здесь 0, а при сохранении в БД >0. Так принято при работе с реальными SQL БД
    private int id = 0;

     User(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
