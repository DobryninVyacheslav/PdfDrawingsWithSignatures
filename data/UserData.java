package ru.ruselprom.signs.data;

import wt.org.WTUser;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private List<String> roles;
    private List<WTUser> users;
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

    public void addUser(WTUser user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public List<WTUser> getUsers() {
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
