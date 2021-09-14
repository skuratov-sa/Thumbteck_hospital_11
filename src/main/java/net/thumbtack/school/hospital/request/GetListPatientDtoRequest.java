package net.thumbtack.school.hospital.request;

public class GetListPatientDtoRequest {
    private final String token;

    public GetListPatientDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
