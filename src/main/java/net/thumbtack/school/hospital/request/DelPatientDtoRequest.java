package net.thumbtack.school.hospital.request;

public class DelPatientDtoRequest {
    private String tokenDoc;
    private int idPatient;

    public DelPatientDtoRequest(String tokenDoc, int idPatient) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
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
}
