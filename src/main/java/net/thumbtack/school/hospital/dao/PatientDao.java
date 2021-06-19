package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.exeptions.ServerException;

import java.util.Collection;
import java.util.List;

public interface PatientDao {

    Doctor getDoctorInfoFromPatient(Patient patient) throws ServerException;
    Collection<String> getDirections(Patient patient);
    User getByLoginPatient(int id) throws ServerException;
    User getByTokenPatient(String token) throws ServerException;
    List<Drug> getDrug(Patient patient);
    User getInfo(String token) throws ServerException;
}
