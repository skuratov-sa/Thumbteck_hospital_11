package net.thumbtack.school.hospital.request;

public class UpdatePassUserDtoRequest {
    private String token;
    private String password;

    public UpdatePassUserDtoRequest(String tokenDoc, String password) {
        this.token = tokenDoc;
        this.password = password;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
