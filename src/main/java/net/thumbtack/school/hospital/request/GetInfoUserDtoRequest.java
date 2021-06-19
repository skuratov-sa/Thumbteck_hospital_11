package net.thumbtack.school.hospital.request;

public class GetInfoUserDtoRequest {
    private final String token;

    public GetInfoUserDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
