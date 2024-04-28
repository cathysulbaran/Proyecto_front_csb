package com.example.proyecto_front_csb.model;

public class Usuarios {
    private String email;
    private boolean password;

    public Usuarios(String email, boolean password) {
        this.email = email;
        this.password = password;
    }

    public Usuarios() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }
}
