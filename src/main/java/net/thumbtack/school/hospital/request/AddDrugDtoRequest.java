package net.thumbtack.school.hospital.request;

import net.thumbtack.school.hospital.response.DrugDto;

public class AddDrugDtoRequest {
    private final String token;
    private final int idPatient;
    private final DrugDto drug;

    public AddDrugDtoRequest(String token, int idPatient, DrugDto drug) {
        this.token = token;
        this.idPatient = idPatient;
        this.drug = drug;
    }

    public String getToken() {
        return token;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public DrugDto getDrug() {
        return drug;
    }

}
