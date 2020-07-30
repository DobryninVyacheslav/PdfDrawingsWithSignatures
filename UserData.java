package ru.ruselprom.signs;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private List<String> roles;
    private List<String> users;
    private List<String> dates;

    public void addRole(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addUser(String user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public List<String> getUsers() {
        return users;
    }

    public void addDate(String date) {
        if (dates == null) {
            dates = new ArrayList<>();
        }
        dates.add(date);
    }

    public List<String> getDates() {
        return dates;
    }
}
