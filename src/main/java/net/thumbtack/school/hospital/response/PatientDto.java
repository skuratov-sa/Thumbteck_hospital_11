package net.thumbtack.school.hospital.response;

import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;

import java.util.List;

public class PatientDto {
    private final String firstName;
    private final String lastName;
    private final String login;
    private final String diseaseName;
    private final List<String> directionsList;
    private final List<Drug> drugList;
    private final int doctorId;
    private final int id;

    public PatientDto(Patient patient) {
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.login = patient.getLogin();
        this.diseaseName = patient.getDiseaseName();
        this.directionsList = patient.getDirectionsList();
        this.drugList = patient.getDrugList();
        this.doctorId = patient.getDoctor().getId();
        this.id = patient.getId();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }
}
