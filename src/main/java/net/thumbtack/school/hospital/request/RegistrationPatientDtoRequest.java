package net.thumbtack.school.hospital.request;

public class RegistrationPatientDtoRequest {
    private final String firstName;
    private final String lastName;
    private final String diseaseName;
    private String login;
    private String password;
    private final String tokenDoc;

    public RegistrationPatientDtoRequest(String firstName, String lastName, String nameDisease, String login, String password, String tokenDoc) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.diseaseName = nameDisease;
        this.login = login;
        this.password = password;
        this.tokenDoc = tokenDoc;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDiseaseName() {
        return diseaseName;
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

    public String getTokenDoc() {
        return tokenDoc;
    }
}
