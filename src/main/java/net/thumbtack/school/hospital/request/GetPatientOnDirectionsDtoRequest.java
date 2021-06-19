package net.thumbtack.school.hospital.request;

public class GetPatientOnDirectionsDtoRequest {
    private String token;
    private String directions;

    public GetPatientOnDirectionsDtoRequest(String token, String directions) {
        this.token = token;
        this.directions = directions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }
}
