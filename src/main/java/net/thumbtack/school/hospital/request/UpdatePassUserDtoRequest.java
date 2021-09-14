package net.thumbtack.school.hospital.request;

public class UpdatePassUserDtoRequest {
    private final String token;
    private final String password;

    public UpdatePassUserDtoRequest(String tokenDoc, String password) {
        this.token = tokenDoc;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

}
