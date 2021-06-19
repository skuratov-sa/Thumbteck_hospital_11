package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.exeptions.ServerException;

import java.util.*;

public interface DoctorDao {
    String registerDoctor(Doctor newDoctor) throws ServerException;

    void registerPatient(Doctor doctor, Patient newPatient) throws ServerException;

    void deletePatient(Doctor doctor, Patient patient) throws ServerException;

    Collection<Patient> getPatientListByDoctor(Doctor doctor) throws ServerException;

    void deleteDoctor(Doctor doctor) throws ServerException;

    List<Patient> getDoctorToPatientWithDisease(Doctor doctor, String diseaseClient) throws ServerException;

    User getUserById(int id) throws ServerException;

    User getByToken(String token) throws ServerException;

    List<Patient> getDoctorToPatientWithDirections(Doctor byTokenDoctor, String directionsClient) throws ServerException;

    void deleteDestination(Patient patient, String destination);

    void addDrugFromDoctor(Patient patient, Drug drug) throws ServerException;

    Collection<Doctor> getDoctorsBySpeciality(String speciality) throws ServerException;

    Set<String> getKeySpeciality() throws ServerException;

    void addDestination(Patient patient, String destination) throws ServerException;

    ArrayList<Doctor> getAllPatientAllDoctors() throws ServerException;

    void delDrugFromDoctor(Patient patient, Drug drug);

    void clear();
}
