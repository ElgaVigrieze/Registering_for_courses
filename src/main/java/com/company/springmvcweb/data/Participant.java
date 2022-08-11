package com.company.springmvcweb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
//@AllArgsConstructor
@Entity(name = "Participant")
@Table(name="participant")


public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "e_mail")
    private String eMail;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String geteMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }



    public Participant(int id, String name, String surname, String eMail, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Participant() {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
