package com.brcthn.battleship.persistance.dto;

public class PlayerDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
//    private Long gpid;
//
//    public Long getGpid() {
//        return gpid;
//    }
//
//    public void setGpid(Long gpid) {
//        this.gpid = gpid;
   // }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
