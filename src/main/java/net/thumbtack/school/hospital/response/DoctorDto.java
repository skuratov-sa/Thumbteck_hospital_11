package net.thumbtack.school.hospital.response;

import net.thumbtack.school.hospital.model.Doctor;

public class DoctorDto {
    private String firstName;
    private String lastName;
    private String speciality;
    private String login;
    private int id;

    public DoctorDto(Doctor doctor) {
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.speciality = doctor.getSpeciality();
        this.login = doctor.getLogin();
        this.id = doctor.getId();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
