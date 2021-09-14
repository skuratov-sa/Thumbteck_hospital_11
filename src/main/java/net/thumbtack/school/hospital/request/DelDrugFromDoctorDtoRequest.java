package net.thumbtack.school.hospital.request;

import net.thumbtack.school.hospital.response.DrugDto;

public class DelDrugFromDoctorDtoRequest {
    private final String tokenDoc;
    private final int idPatient;
    private final DrugDto drug;

    public DelDrugFromDoctorDtoRequest(String tokenDoc, int idPatient, DrugDto drug) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
        this.drug = drug;
    }

    public String getTokenDoc() {
        return tokenDoc;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public DrugDto getDrug() {
        return drug;
    }
}
