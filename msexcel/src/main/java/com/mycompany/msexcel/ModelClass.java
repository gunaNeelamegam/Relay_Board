package com.mycompany.msexcel;

import org.apache.poi.hpsf.Date;

public class ModelClass {



    private String sno;
    private String firstName;
    private String lastName;
    private String gender;
    private String Country;
    private long age;
    private Date date;
    private long id;
    private boolean data;

        public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "ModelClass{" + "sno=" + sno + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", Country=" + Country + ", age=" + age + ", date=" + date + ", id=" + id + '}';
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
