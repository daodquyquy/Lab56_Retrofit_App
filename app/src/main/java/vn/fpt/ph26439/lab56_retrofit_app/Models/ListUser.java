package vn.fpt.ph26439.lab56_retrofit_app.Models;

public class ListUser {
    private User[] users;

    public ListUser(User[] users) {
        this.users = users;
    }

    public User[] getUsers() {
        return users;
    }
}
