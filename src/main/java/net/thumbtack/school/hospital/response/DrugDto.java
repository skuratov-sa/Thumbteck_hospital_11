package net.thumbtack.school.hospital.response;

public class DrugDto {
    private String name;

    public DrugDto(String drugName) {
        this.name = drugName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
