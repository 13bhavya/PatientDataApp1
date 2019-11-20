package com.example.patientdataapp;

public class InfoDataPatient {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String sex;
    private String dateOfBirth;
    private String department;
    private String doctor;


    public InfoDataPatient() {
    }

    public InfoDataPatient(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public String getDoctor() {
        return doctor;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
