package net.thumbtack.school.hospital.response;

import net.thumbtack.school.hospital.model.Doctor;

public class DoctorDto {
    private final String firstName;
    private final String lastName;
    private final String speciality;
    private final String login;

    public DoctorDto(Doctor doctor) {
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.speciality = doctor.getSpeciality();
        this.login = doctor.getLogin();
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

    public String getLogin() {
        return login;
    }
}
