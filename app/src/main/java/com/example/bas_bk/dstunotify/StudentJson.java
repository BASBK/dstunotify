package com.example.bas_bk.dstunotify;

/**
 * Created by BAS_BK on 28.08.2016.
 */
public class StudentJson {
    private String BookNumber;
    private String PhoneNumber;
    private String EmailAddress;
    private String Password;
    private String Group;

    public String getBookNumber() {
        return BookNumber;
    }

    public void setBookNumber(String bookNumber) {
        BookNumber = bookNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }


    public StudentJson(String bookNumber, String phoneNumber, String emailAddress, String password, String group) {
        BookNumber = bookNumber;
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
        Password = password;
        Group = group;
    }

}
