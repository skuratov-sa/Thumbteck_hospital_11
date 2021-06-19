package net.thumbtack.school.hospital.request;

public class DelDestinationFromDoctorDtoRequest {
    private String tokenDoc;
    private int idPatient;
    private String destination;

    public DelDestinationFromDoctorDtoRequest(String tokenDoc, int idPatient, String destination) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
        this.destination = destination;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
