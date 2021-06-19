package net.thumbtack.school.hospital.response;
public class GetDoctorInfoFromPatientResponse {
    private final String firstName;
    private final String lastName;
    private final String speciality;

    public GetDoctorInfoFromPatientResponse(String firstName, String lastName, String speciality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpeciality() {
        return speciality;
    }
}
