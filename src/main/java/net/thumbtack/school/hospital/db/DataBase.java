package net.thumbtack.school.hospital.db;

import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.io.Serializable;
import java.util.*;

public class DataBase implements Serializable {

    private static DataBase dataBase;

    // REVU сначала поля, потом остальное
    private DataBase() {
        counterId = 1;
    }

    public static DataBase getInstance() {
        if (dataBase == null) {
            dataBase = new DataBase();
        }
        return dataBase;
    }


    private final Map<Integer, User> idToUser = new HashMap<>();
    private final DualHashBidiMap<String, User> tokenToUser = new DualHashBidiMap<>();
    private final MultiValuedMap<String, Doctor> doctorBySpeciality = new HashSetValuedHashMap<>();
    private final MultiValuedMap<String,Patient> patientIdByDrug = new HashSetValuedHashMap<>();
    private final MultiValuedMap<String,Patient> patientIdByDisease = new HashSetValuedHashMap<>();
    private final Map<String, User> loginToUser = new HashMap<>();
    private int counterId;

    public static void startServer(DataBase dataBase) {
        DataBase.dataBase = dataBase;
    }

    public static DataBase stopServer() {
     return dataBase;
    }


    public String registerDoctor(Doctor doctor) throws ServerException {
        if (loginToUser.putIfAbsent(doctor.getLogin(), doctor) != null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_THIS_USER_HAS_ALREADY_REGISTERED);
        }
        String token = String.valueOf(UUID.randomUUID());
        int id = counterId;
        doctor.setId(id);
        idToUser.put(id, doctor);
        tokenToUser.put(token, doctor);
        doctorBySpeciality.put(doctor.getSpeciality(), doctor);
        loginToUser.put(doctor.getLogin(), doctor);
        counterId++;
        return token;
    }


    public String logInUser(String login, String password) throws ServerException {
        User user = loginToUser.get(login);
        if (user == null) {
            throw new ServerException(AnswerErrorCode.LOGIN_ERROR);
        }

        if (!user.getPassword().equals(password)) {
            throw new ServerException(AnswerErrorCode.PASSWORD_ERROR);
        }

        String token = String.valueOf(UUID.randomUUID());
        tokenToUser.put(token, idToUser.get(user.getId()));
        return token;
    }


    public void logOutUser(User user) {
        tokenToUser.removeValue(user);
    }

    public void updatePasswordUser(User user, String password) throws ServerException {
        user.setPassword(password);
        idToUser.replace(user.getId(), user);
    }


    public void deleteDoctor(Doctor doctor) throws ServerException {
        if (idToUser.remove(doctor.getId()) == null) {
            throw new ServerException(AnswerErrorCode.ID_ERROR);
        }
        tokenToUser.inverseBidiMap().remove(doctor);
        doctorBySpeciality.get(doctor.getSpeciality()).remove(doctor);
    }


    public void addDestinationFromDoctor(Patient patient, String directions) throws ServerException {
        patient.setDirectionsList(directions);
        idToUser.replace(patient.getId(), patient);
    }

    public void delDestinationFromDoctor(Patient patient, String destination) {
        patient.removeDirections(destination);
        idToUser.replace(patient.getId(), patient);
    }

    public void addDrugPatient( Patient patient, Drug drug) throws ServerException {
        patient.setDrugList(drug);
        patientIdByDrug.put(drug.getName(),patient);
        idToUser.replace(patient.getId(), patient);
    }

    public List<Drug> getDrugForPatient(Patient patient) {
        return patient.getDrugList();
    }

    public void delDrug(Patient patient, Drug drug) {
        patient.removeDrug(drug);
        patientIdByDrug.get(drug.getName()).remove(patient);
        idToUser.replace(patient.getId(), patient);
    }

    //PATIENT
    public void registerPatient(Doctor doctor, Patient patient) throws ServerException {
        if (loginToUser.putIfAbsent(patient.getLogin(), patient) != null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_THIS_USER_HAS_ALREADY_REGISTERED);
        }

        patient.setDoctor(doctor);
        patient.setId(counterId);
        String token = String.valueOf(UUID.randomUUID());
        idToUser.put(counterId, patient);
        tokenToUser.put(token, patient);
        loginToUser.put(patient.getLogin(), patient);
        doctor.getPatients().add(patient);
        for (Drug drug: patient.getDrugList()) {
            patientIdByDrug.put(drug.getName(),patient);
        }
        patientIdByDisease.put(patient.getDiseaseName(),patient);
        counterId++;
    }


    //Доктор удоляет клиента
    public void deletePatient(Doctor doctor, Patient patient) throws ServerException {
        idToUser.remove(patient.getId());
        idToUser.remove(patient.getId());
        doctor.getPatients().remove(patient);
        for (Drug drug: patient.getDrugList()) {
            patientIdByDrug.get(drug.getName()).remove(patient);
        }
        patientIdByDisease.get(patient.getDiseaseName()).remove(patient);
    }

    public List<Patient> getListPatientsByDoctor(Doctor doctor) {
        return doctor.getPatients();
    }

    public List<Patient> getPatientWithDiseaseByDoctor(Doctor doctor, String patientDisease) throws ServerException {
        List<Patient> ans = new ArrayList<>();
        for (Patient patient : doctor.getPatients()) {
            if (patient.getDiseaseName().equals(patientDisease)) {
                ans.add(patient);
            }
        }
        return ans;
    }

    public List<Patient> getPatientWithDirectionsByDoctor(Doctor doctor, String directionsPatient) throws ServerException {
        List<Patient> ans = new ArrayList<>();
        for (Patient patient : doctor.getPatients()) {
            if (patient.getDirectionsList().contains(directionsPatient)) {
                ans.add(patient);
            }
        }
        return ans;
    }

    public List<Doctor> getAllPatientAllDoctors() throws ServerException {
        return new ArrayList<>(doctorBySpeciality.values());
    }

    public List<Patient> getPatientListByDrug(String drugName) {
        List<Patient> ans = new ArrayList<>();
        patientIdByDrug.get(drugName).iterator().forEachRemaining(ans::add);
        return ans;
    }

    //Пациент просматривает  своего доктора
    public Doctor getDoctorInfoFromPatient(Patient patient) throws ServerException {
        return patient.getDoctor();
    }

    //Пациент просматривает свои направления
    public List<String> getPatientToDirections(Patient patient) {
        return patient.getDirectionsList();
    }


    public User getByIdUser(int id) throws ServerException {
        User user = idToUser.get(id);
        if (user == null) {
            throw new ServerException(AnswerErrorCode.ID_ERROR);
        }
        return user;
    }

    public User getByTokenUser(String token) throws ServerException {
        User user = tokenToUser.get(token);
        if (user == null) {
            throw new ServerException(AnswerErrorCode.TOKEN_ERROR);
        }
        return user;
    }

    public List<Doctor> getSpecialtyToDoctor(String specialty) {
        List<Doctor> list = new ArrayList<>();
        doctorBySpeciality.get(specialty).iterator().forEachRemaining(list::add);
        return list;
    }

    public Set<String> getKeySpecialty() {
        return doctorBySpeciality.keySet();
    }

    public void clear() {
        tokenToUser.clear();
        idToUser.clear();
        patientIdByDrug.clear();
        doctorBySpeciality.clear();
        patientIdByDisease.clear();
        loginToUser.clear();
    }

    public List<Patient> getAllPatient() {
        return new ArrayList<>(patientIdByDisease.values());
    }
}
