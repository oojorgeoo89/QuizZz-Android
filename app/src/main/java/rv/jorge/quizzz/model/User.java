package rv.jorge.quizzz.model;

/**
 * Created by jorgerodriguez on 01/01/18.
 */

public class User {
    private long id;
    private String email;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
