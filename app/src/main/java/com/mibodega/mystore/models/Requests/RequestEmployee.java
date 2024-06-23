package com.mibodega.mystore.models.Requests;

public class RequestEmployee {
    private String name;
    private String lastname;
    private String email;
    private String dni;
    private String phone;
    private String password;

    public RequestEmployee(String name, String lastname, String email, String dni, String phone, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
