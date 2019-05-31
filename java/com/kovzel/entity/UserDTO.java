package com.kovzel.entity;

public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public UserDTO(){}

    public UserDTO(int id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
