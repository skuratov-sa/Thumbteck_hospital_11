package net.thumbtack.school.hospital.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient extends User implements Serializable {
    private final String diseaseName;
    private List<String> directionsList = new ArrayList<>();
    private final List<Drug> drugList = new ArrayList<>();
    // REVU лучше 
    // private Doctor doctor;
    private Doctor doctor;

    public Patient(String firstName, String lastName, String nameDisease, String login, String password) {
        super(firstName, lastName, login, password);
        this.diseaseName = nameDisease;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public List<String> getDirectionsList() {
        return directionsList;
    }

    public void setDirectionsList(String newDirections) {
        this.directionsList.add(newDirections);
    }

    public List<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(Drug drug) { drugList.add(drug); }

    public void setDirectionsList(List<String> directionsList) {
        this.directionsList = directionsList;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return  Objects.equals(getLogin(),patient.getLogin()) && Objects.equals(getPassword(),patient.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(),getPassword());
    }

    public void removeDirections(String destination) {
        directionsList.remove(destination);
    }

    public void removeDrug(Drug drug) {
        drugList.remove(drug);
    }
}
