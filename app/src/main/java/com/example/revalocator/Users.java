package com.example.revalocator;

public class Users {
    String  name, srn ,pass ,cfrpass,mail,mob ,city,pin , Dob ,yoj,sem,school,gender;
    Users(String name,String srn ,String pass , String cfrpass, String mail, String mob , String city, String pin ,String Dob,String yoj,String sem, String gender,String school)
    {
        this.name = name;
        this.cfrpass =cfrpass;
        this.city =city;
        this.mob =mob;
        this.mail =mail;
        this.Dob =Dob;
        this.gender= gender;
        this.pin =pin;
        this.pass =pass;
        this.school =school;
        this.sem =sem;
        this.srn =srn;
        this.yoj =yoj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrn() {
        return srn;
    }

    public void setSrn(String srn) {
        this.srn = srn;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCfrpass() {
        return cfrpass;
    }

    public void setCfrpass(String cfrpass) {
        this.cfrpass = cfrpass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYoj() {
        return yoj;
    }

    public void setYoj(String yoj) {
        this.yoj = yoj;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}



