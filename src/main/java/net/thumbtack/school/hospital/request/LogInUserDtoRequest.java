package net.thumbtack.school.hospital.request;

public class LogInUserDtoRequest {
    private final String login;
    private final String password;

    public LogInUserDtoRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
