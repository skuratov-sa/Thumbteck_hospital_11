package net.thumbtack.school.hospital.request;

public class GetPatientOnDiseaseDtoRequest {
    private final String token;
    private final String disease;

    public GetPatientOnDiseaseDtoRequest(String token, String disease) {
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
