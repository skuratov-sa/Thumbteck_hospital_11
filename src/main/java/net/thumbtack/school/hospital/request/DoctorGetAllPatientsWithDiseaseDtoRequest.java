package net.thumbtack.school.hospital.request;

public class DoctorGetAllPatientsWithDiseaseDtoRequest {
    private String token;
    private String disease;

    public DoctorGetAllPatientsWithDiseaseDtoRequest(String token, String disease) {
        this.token = token;
        this.disease = disease;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
