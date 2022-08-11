package com.company.springmvcweb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PasswordValidator {

    private int minPasswordLength;

    public PasswordValidator() {
        this.minPasswordLength = 8;
    }

    public boolean validate(String password1, String password2){

        var length = validateLength(password1);

        var validation = validateCapitalLettersAndNumbers(password1);

        var match = validateMatch(password1, password2);

        if (length && validation && match) {
            return true;
        }
        return false;
    }


    public boolean validateLength(String password) {
        return password.length() >= minPasswordLength;
    }

    public boolean validateMatch(String password1, String password2) {
        return (password1.equals(password2));
    }

    public boolean validateCapitalLettersAndNumbers(String password) {
        var digitCount = 0;
        var smallLetterCount = 0;
        var capitalLetterCount = 0;
        char ch;
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                digitCount++;
            } else if (Character.isUpperCase(ch)) {
                capitalLetterCount++;
            } else if (Character.isLowerCase(ch)) {
                smallLetterCount++;
            }
            if (digitCount >= 1 && capitalLetterCount >= 1 && smallLetterCount >= 1)
                return true;
        }
        return false;
    }
}
