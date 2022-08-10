package blogSwingClient.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("admin")
    private int admin;

    public User() {}

    public User(int id, String username, String firstName, String lastName, int admin) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAdmin() {
        return admin;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
