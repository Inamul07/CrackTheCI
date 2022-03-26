package com.inamul.users;

import android.net.Uri;

public class User {

    String email, password, username;
    Uri imageUri;

    public User() {

    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String email, String username, String password, Uri imageUri) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageUri = imageUri;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
