package com.company.springmvcweb.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginSearchDto1 {
    @JsonProperty("e_mail")
    private String eMail;

    @JsonProperty("password")
    private String password;


    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginSearchDto1(String eMail, String password) {
        this.eMail = eMail;
        this.password = password;


    }
}
