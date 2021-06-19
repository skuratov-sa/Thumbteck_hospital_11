package net.thumbtack.school.hospital.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor extends User implements Serializable {
    private String speciality;
    private List<Patient> patients = new ArrayList<>();

    public Doctor(String firstName, String lastName, String speciality, String login, String password) {
        super(firstName, lastName, login, password);
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setPatients(List<Patient> patients) {
        this.patients.addAll(patients);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(speciality, doctor.speciality) && Objects.equals(patients, doctor.patients) && Objects.equals(getLogin(), doctor.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(speciality);
    }
}
