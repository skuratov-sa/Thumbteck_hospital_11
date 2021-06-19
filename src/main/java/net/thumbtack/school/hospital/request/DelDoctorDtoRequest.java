package net.thumbtack.school.hospital.request;

public class DelDoctorDtoRequest {
    private String token;

    public DelDoctorDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
