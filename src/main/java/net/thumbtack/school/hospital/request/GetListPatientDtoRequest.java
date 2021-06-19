package net.thumbtack.school.hospital.request;

public class GetListPatientDtoRequest {
    private String token;

    public GetListPatientDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
