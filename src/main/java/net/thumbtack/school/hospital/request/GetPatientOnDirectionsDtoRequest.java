package net.thumbtack.school.hospital.request;

public class GetPatientOnDirectionsDtoRequest {
    private final String token;
    private final String directions;

    public GetPatientOnDirectionsDtoRequest(String token, String directions) {
        this.token = token;
        this.directions = directions;
    }

    public String getToken() {
        return token;
    }

    public String getDirections() {
        return directions;
    }
}
