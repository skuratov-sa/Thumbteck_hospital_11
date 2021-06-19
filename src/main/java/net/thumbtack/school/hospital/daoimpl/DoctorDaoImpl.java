package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.db.DataBase;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.exeptions.ServerException;

import java.util.*;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public String registerDoctor(Doctor newDoctor) throws ServerException {
        return DataBase.getInstance().registerDoctor(newDoctor);
    }

    public void registerPatient(Doctor doctor, Patient newPatient) throws ServerException {
         DataBase.getInstance().registerPatient(doctor,newPatient);
    }

    @Override
    public void addDestination(Patient patient, String destination) throws ServerException {
        DataBase.getInstance().addDestinationFromDoctor(patient,destination);
    }

    @Override
    public void deleteDestination(Patient patient, String destination) {
        DataBase.getInstance().delDestinationFromDoctor(patient,destination);
    }

    @Override
    public void addDrugFromDoctor(Patient patient, Drug drug) throws ServerException {
        DataBase.getInstance().addDrugPatient(patient, drug);
    }

    @Override
    public Collection<Doctor> getDoctorsBySpeciality(String speciality) throws ServerException {
        return DataBase.getInstance().getSpecialtyToDoctor(speciality);
    }

    @Override
    public Set<String> getKeySpeciality(){
        return DataBase.getInstance().getKeySpecialty();
    }


    @Override
    public void deleteDoctor(Doctor token) throws ServerException {
        DataBase.getInstance().deleteDoctor(token);
    }
    @Override
    public void deletePatient(Doctor doctor, Patient patient) throws ServerException {
        DataBase.getInstance().deletePatient(doctor,patient);
    }

    @Override
    public Collection<Patient> getPatientListByDoctor(Doctor doctor) {
        return DataBase.getInstance().getListPatientsByDoctor(doctor);
    }

    @Override
    public List<Patient> getDoctorToPatientWithDirections(Doctor doctor, String directionsClient) throws ServerException {
        return DataBase.getInstance().getPatientWithDirectionsByDoctor(doctor,directionsClient);
    }


    @Override
    public List<Patient> getDoctorToPatientWithDisease(Doctor doctor, String diseaseClient) throws ServerException {
        return DataBase.getInstance().getPatientWithDiseaseByDoctor(doctor,diseaseClient);
    }

    @Override
    public ArrayList<Doctor> getAllPatientAllDoctors() throws ServerException {
        return DataBase.getInstance().getAllPatientAllDoctors();
    }

    @Override
    public void delDrugFromDoctor(Patient patient, Drug drug) {
        DataBase.getInstance().delDrug(patient, drug);
    }

    @Override
    public void clear() {
        DataBase.getInstance().clear();
    }

    @Override
    public User getUserById(int id) throws ServerException {
        return  DataBase.getInstance().getByIdUser(id);
    }

    @Override
    public User getByToken(String token) throws ServerException {
        return DataBase.getInstance().getByTokenUser(token);
    }


}
