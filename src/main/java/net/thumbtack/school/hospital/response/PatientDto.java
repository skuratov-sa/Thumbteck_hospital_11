package net.thumbtack.school.hospital.response;

import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;

import java.util.List;

public class PatientDto {
    private String firstName;
    private String lastName;
    private String login;
    private String diseaseName;
    private List<String> directionsList;
    private List<Drug> drugList;
    private int doctorId;
    private int id;

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public List<String> getDirectionsList() {
        return directionsList;
    }

    public void setDirectionsList(List<String> directionsList) {
        this.directionsList = directionsList;
    }

    public List<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<Drug> drugList) {
        this.drugList = drugList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
