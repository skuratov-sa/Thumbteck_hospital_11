package net.thumbtack.school.hospital.request;

public class AddDestinationFromDoctorDtoRequest {
    private final String tokenDoc;
    private final int idPatient;
    private final String destination;

    public AddDestinationFromDoctorDtoRequest(String tokenDoc, int idPatient, String destination) {
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
