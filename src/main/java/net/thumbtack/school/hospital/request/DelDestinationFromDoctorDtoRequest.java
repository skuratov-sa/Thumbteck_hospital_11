package net.thumbtack.school.hospital.request;

public class DelDestinationFromDoctorDtoRequest {
    private final String tokenDoc;
    private final int idPatient;
    private final String destination;

    public DelDestinationFromDoctorDtoRequest(String tokenDoc, int idPatient, String destination) {
        this.tokenDoc = tokenDoc;
        this.idPatient = idPatient;
        this.destination = destination;
    }

    public String getTokenDoc() {
        return tokenDoc;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public String getDestination() {
        return destination;
    }

}
