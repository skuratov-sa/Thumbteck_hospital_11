package net.thumbtack.school.hospital.request;

public class DoctorGetAllPatientsWithDiseaseDtoRequest {
    private final String token;
    private final String disease;

    public DoctorGetAllPatientsWithDiseaseDtoRequest(String token, String disease) {
        this.token = token;
        this.disease = disease;
    }

    public String getToken() {
        return token;
    }

    public String getDisease() {
        return disease;
    }
}
