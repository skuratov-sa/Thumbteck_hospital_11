package net.thumbtack.school.hospital.response;

public class GetTokenDtoResponse {
    private final String token;
    public GetTokenDtoResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
