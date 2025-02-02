package com.example.demo.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class LoginRequest {

    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s{2})[A-Za-z.0-9@\\-]{2,50}(?<!\\s)$", message = "Username name must contain only letters, spaces, dots, digits, and dashes, and no leading or trailing spaces.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character from '@$!%*?&'")
    private String password;
    public void setUsername(String username) {
        if(StringUtils.isBlank(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty.");
        }
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }
    public void setPassword(String password) {
        if(StringUtils.isBlank(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }
        this.password = password;
    }
    public String getpassword() {
        return this.password;
    }
}
