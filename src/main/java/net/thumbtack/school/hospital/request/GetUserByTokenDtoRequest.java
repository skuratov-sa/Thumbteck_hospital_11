package net.thumbtack.school.hospital.request;

public class GetUserByTokenDtoRequest {
    private String token;

    public GetUserByTokenDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
