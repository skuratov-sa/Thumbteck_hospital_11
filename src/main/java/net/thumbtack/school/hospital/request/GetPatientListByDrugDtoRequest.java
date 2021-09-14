package net.thumbtack.school.hospital.request;

public class GetPatientListByDrugDtoRequest {
    private final String token;
    private final String drugName;

    public GetPatientListByDrugDtoRequest(String token, String drugName) {
        this.token = token;
        this.drugName = drugName;
    }

    public String getToken() {
        return token;
    }

    public String getDrugName() {
        return drugName;
    }
}
