package net.thumbtack.school.hospital.request;

public class DelPatientDtoRequest {
    private final String tokenDoc;
    private final int idPatient;

    public DelPatientDtoRequest(String tokenDoc, int idPatient) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
    }

    public String getTokenDoc() {
        return tokenDoc;
    }

    public int getIdPatient() {
        return idPatient;
    }
}
