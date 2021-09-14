package net.thumbtack.school.hospital.request;

public class RegistrationDoctorDtoRequest {
    private String firstName;
    private String lastName;
    private String speciality;
    private String login;
    private String password;

    public RegistrationDoctorDtoRequest(String firstName, String lastName, String speciality, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }


}
