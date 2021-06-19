package net.thumbtack.school.hospital.response;

public class PatientInfoDtoRequest {
    private final String firstNameDoctor;
    private final String lastNameDoctor;
    private PatientDto patient;

    public PatientInfoDtoRequest(PatientDto patient,String firstNameDoctor, String lastNameDoctor) {
        this.firstNameDoctor = firstNameDoctor;
        this.lastNameDoctor = lastNameDoctor;
        this.patient = patient;
    }

    public String getFirstNameDoctor() {
        return firstNameDoctor;
    }

    public String getLastNameDoctor() {
        return lastNameDoctor;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }
}
