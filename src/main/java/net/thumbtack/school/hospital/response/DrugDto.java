package net.thumbtack.school.hospital.response;

public class DrugDto {
    private final String name;

    public DrugDto(String drugName) {
        this.name = drugName;
    }

    public String getName() {
        return name;
    }
}
