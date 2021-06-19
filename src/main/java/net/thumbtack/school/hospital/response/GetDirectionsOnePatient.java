package net.thumbtack.school.hospital.response;

import java.util.List;

public class GetDirectionsOnePatient {
    private final List<String> directions;

    public GetDirectionsOnePatient(List<String> directions) {
        this.directions = directions;
    }

    public List<String> getDirections() {
        return directions;
    }
}
