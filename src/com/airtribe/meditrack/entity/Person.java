package com.airtribe.meditrack.entity;

public class Person {

    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String email;

    public Person(String name, int age, String gender, String contactNumber, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
