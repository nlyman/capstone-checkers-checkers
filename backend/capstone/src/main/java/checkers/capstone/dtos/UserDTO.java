package checkers.capstone.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    
    @JsonProperty("username")
    private final String username;
    @JsonProperty("password")
    private final String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
