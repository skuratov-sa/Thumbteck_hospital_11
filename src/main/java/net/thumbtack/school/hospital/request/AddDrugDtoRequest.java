package net.thumbtack.school.hospital.request;

import net.thumbtack.school.hospital.model.Drug;

public class AddDrugDtoRequest {
    private String token;
    private int idPatient;
    private Drug drug;

    public AddDrugDtoRequest(String token, int idPatient, Drug drug) {
        this.token = token;
        this.idPatient = idPatient;
        this.drug = drug;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }
}
