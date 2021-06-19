package net.thumbtack.school.hospital.request;

import net.thumbtack.school.hospital.model.Drug;

public class DelDrugFromDoctorDtoRequest {
    private String tokenDoc;
    private int idPatient;
    private Drug drug;

    public DelDrugFromDoctorDtoRequest(String tokenDoc, int idPatient, Drug drug) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
        this.drug = drug;
    }

    public String getTokenDoc() {
        return tokenDoc;
    }

    public void setTokenDoc(String tokenDoc) {
        this.tokenDoc = tokenDoc;
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
