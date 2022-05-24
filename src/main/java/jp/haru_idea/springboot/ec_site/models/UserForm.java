package jp.haru_idea.springboot.ec_site.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class UserForm {
    private int id;
    
    private String lastName;
    
    private String firstName;
    
    private String mail;

    @DateTimeFormat(pattern = "[yyyy-MM-dd]")
    private LocalDate birthDate;
    
    private String password;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
