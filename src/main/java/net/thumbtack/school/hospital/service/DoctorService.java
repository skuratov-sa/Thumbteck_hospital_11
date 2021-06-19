package net.thumbtack.school.hospital.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.request.*;
import net.thumbtack.school.hospital.response.*;

import java.util.*;
import java.util.regex.Pattern;

public class DoctorService {
    private final Gson gson = new Gson();
    private final DoctorDao doctorDao = new DoctorDaoImpl();

    //DOCTOR
    public String registerDoctor(String request) {
        try {
            RegistrationDoctorDtoRequest regDtoDoc = getClassFromJson(request, RegistrationDoctorDtoRequest.class);
            validateRegDoctor(regDtoDoc);

            Doctor newDoctor = new Doctor(regDtoDoc.getFirstName(), regDtoDoc.getLastName(), regDtoDoc.getSpeciality(), regDtoDoc.getLogin(), regDtoDoc.getPassword());
            return gson.toJson(new GetTokenDtoResponse(doctorDao.registerDoctor(newDoctor)));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateRegDoctor(RegistrationDoctorDtoRequest doctor) throws ServerException {
        if (!Pattern.matches("([a-zA-Z]{2,})", doctor.getFirstName()) || !Pattern.matches("([a-zA-Z]{2,})", doctor.getLastName()) || !Pattern.matches("([A-Za-z]{4,})", doctor.getSpeciality())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA);
        }

        if (!(Pattern.matches("[A-Za-z0-9]{3,20}@[a-z]{2,6}[.][a-z]{2,4}", doctor.getLogin()))) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN);
        }

        if (!(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", doctor.getPassword()))) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
    }

    public String delDoctor(String tokenJson) {
        try {
            DelDoctorDtoRequest delDto = getClassFromJson(tokenJson, DelDoctorDtoRequest.class);
            validateDelDoctorRequest(delDto);

            Doctor doctor = getDoctorByToken(delDto.getToken());

            if ( doctor.getPatients().size() != 0) {
                //Доктор той же специальностью
                List<Doctor> listDoc = (ArrayList<Doctor>) doctorDao.getDoctorsBySpeciality(doctor.getSpeciality());
                listDoc.remove(doctor);
                int randomDoc = (int) (Math.random() * listDoc.size());

                //with the same speciality
                if (listDoc.size() != 0) {
                    listDoc.get(randomDoc).setPatients(doctor.getPatients());
                }

                else {
                    for (String speciality : doctorDao.getKeySpeciality()) {
                        listDoc.addAll(doctorDao.getDoctorsBySpeciality(speciality));
                    }
                    listDoc.remove(doctor);

                    if (listDoc.size() != 0) {
                        int randomDocTwo = (int) (Math.random() * listDoc.size());
                        listDoc.get(randomDocTwo).setPatients(doctor.getPatients());
                    } else {
                        throw new ServerException(AnswerErrorCode.ERROR_DELETE_DOCTOR);
                    }
                }
            }

            doctorDao.deleteDoctor(doctor);
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDelDoctorRequest(DelDoctorDtoRequest delDto) throws ServerException {
        if (delDto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String addDestination(String jsonText) {
        try {
            AddDestinationFromDoctorDtoRequest addDestDto = getClassFromJson(jsonText, AddDestinationFromDoctorDtoRequest.class);
            validateAddDestinationRequest(addDestDto);

            Doctor doctor = getDoctorByToken(addDestDto.getTokenDoc());
            Patient patient = getPatientById(addDestDto.getIdPatient());


            if (patient.getDoctor() != doctor) {
                throw new ServerException(AnswerErrorCode.ERROR_DOCTOR_DID_NOT_PATIENT);
            }

            doctorDao.addDestination(patient, addDestDto.getDestination());
            return gson.toJson(new EmptyResponse());
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }


    private void validateAddDestinationRequest(AddDestinationFromDoctorDtoRequest addDestDto) throws ServerException {
        if (addDestDto.getTokenDoc() == null || addDestDto.getDestination() == null || addDestDto.getIdPatient() == -1) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String delDestination(String jsonText) {
        try {
            DelDestinationFromDoctorDtoRequest dto = getClassFromJson(jsonText, DelDestinationFromDoctorDtoRequest.class);
            validateDelDestinationRequest(dto);
            //if such a doctor exists
            getDoctorByToken(dto.getTokenDoc());
            Patient patient = getPatientById(dto.getIdPatient());

            doctorDao.deleteDestination(patient, dto.getDestination());
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDelDestinationRequest(DelDestinationFromDoctorDtoRequest dto) throws ServerException {
        if (dto.getTokenDoc() != null && dto.getDestination() == null || dto.getIdPatient() == -1) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String addDrugFrom(String jsonText) {
        try {
            AddDrugDtoRequest addDrugDto = getClassFromJson(jsonText, AddDrugDtoRequest.class);
            validateAddDrugRequest(addDrugDto);

            //if such a doctor exists
           getDoctorByToken(addDrugDto.getToken());

            Patient patient = getPatientById(addDrugDto.getIdPatient());

            doctorDao.addDrugFromDoctor(patient, addDrugDto.getDrug());
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateAddDrugRequest(AddDrugDtoRequest addDrugDto) throws ServerException {
        if (addDrugDto.getToken() == null || addDrugDto.getDrug() == null || addDrugDto.getIdPatient() == -1) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String delDrug(String jsonText) {
        try {
            DelDrugFromDoctorDtoRequest dto = getClassFromJson(jsonText, DelDrugFromDoctorDtoRequest.class);
            validateDelDrugRequest(dto);
            //if such a doctor exists
            getDoctorByToken(dto.getTokenDoc());
            Patient patient = getPatientById(dto.getIdPatient());

            if (!patient.getDrugList().contains(dto.getDrug())) {
                throw new ServerException(AnswerErrorCode.ERROR_DRUG);
            }

            doctorDao.delDrugFromDoctor(patient, dto.getDrug());
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDelDrugRequest(DelDrugFromDoctorDtoRequest dto) throws ServerException {
        if (dto.getTokenDoc() == null || dto.getDrug() == null || dto.getIdPatient() == -1) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    //PATIENT
    public String regPatient(String jsonText) {
        //Аналогично доктору
        try {
            RegistrationPatientDtoRequest regPatientDto = getClassFromJson(jsonText, RegistrationPatientDtoRequest.class);
            validateRegPatientRequest(regPatientDto);

            Patient newPatient = new Patient(regPatientDto.getFirstName(), regPatientDto.getLastName(), regPatientDto.getDiseaseName(), regPatientDto.getLogin(), regPatientDto.getPassword());
            Doctor doctor = getDoctorByToken(regPatientDto.getTokenDoc());

            doctorDao.registerPatient(doctor, newPatient);
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateRegPatientRequest(RegistrationPatientDtoRequest patient) throws ServerException {
        if (!Pattern.matches("([a-zA-Z]{2,})", patient.getFirstName()) || !Pattern.matches("([a-zA-Z]{2,})", patient.getLastName()) || !Pattern.matches("([A-Za-z]{4,})", patient.getDiseaseName())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA);
        }

        if (!(Pattern.matches("[A-Za-z0-9]{3,20}@[a-z]{2,6}[.][a-z]{2,4}", patient.getLogin()))) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN);
        }

        if (!(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", patient.getPassword()))) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
        if (patient.getTokenDoc() == null) {
            throw new ServerException(AnswerErrorCode.TOKEN_ERROR);
        }
    }

    public String delPatient(String jsonText) {
        try {
            DelPatientDtoRequest delPatientDtoRequest = getClassFromJson(jsonText, DelPatientDtoRequest.class);
            validateDelPatientRequest(delPatientDtoRequest);

            Doctor doctor = getDoctorByToken(delPatientDtoRequest.getTokenDoc());
            Patient patient = getPatientById(delPatientDtoRequest.getIdPatient());

            doctorDao.deletePatient(doctor, patient);
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDelPatientRequest(DelPatientDtoRequest delPatientDtoRequest) throws ServerException {
        if (delPatientDtoRequest.getTokenDoc() == null || delPatientDtoRequest.getIdPatient() == -1) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }


    public String getListPatientsFromDoctor(String textJson) {
        try {
            GetListPatientDtoRequest dto = getClassFromJson(textJson, GetListPatientDtoRequest.class);
            validateGetListPatientsFromDoctorRequest(dto);

            Doctor doctor = getDoctorByToken(dto.getToken());

            List<Patient> list = (ArrayList<Patient>) doctorDao.getPatientListByDoctor( doctor);
            List<PatientDto> dtoList = new ArrayList<>();
            for (Patient patient : list) {
                dtoList.add(new PatientDto(patient));
            }
            return gson.toJson(new GetListPatientResponse(dtoList));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetListPatientsFromDoctorRequest(GetListPatientDtoRequest dto) throws ServerException {
        if (dto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }

    }

    public String getDoctorToPatientWithDisease(String textJson) {
        try {
            GetPatientOnDiseaseDtoRequest dto = getClassFromJson(textJson, GetPatientOnDiseaseDtoRequest.class);
            validateGetDoctorToPatientWithDiseaseRequest(dto);

            Doctor doctor = getDoctorByToken(dto.getToken());

            List<Patient> list = doctorDao.getDoctorToPatientWithDisease( doctor, dto.getDisease());
            List<PatientDto> dtoList = new ArrayList<>();

            for (Patient patient : list) {
                dtoList.add(new PatientDto(patient));
            }
            return gson.toJson(new GetListPatientResponse(dtoList));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetDoctorToPatientWithDiseaseRequest(GetPatientOnDiseaseDtoRequest dto) throws ServerException {
        if (dto.getDisease() == null || dto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String getDoctorToPatientWithDirections(String textJson) {
        try {
            GetPatientOnDirectionsDtoRequest getPatientDto = getClassFromJson(textJson, GetPatientOnDirectionsDtoRequest.class);
            validateGetDoctorToPatientWithDirections(getPatientDto);

            Doctor doctor = getDoctorByToken(getPatientDto.getToken());

            List<Patient> list = doctorDao.getDoctorToPatientWithDirections(doctor, getPatientDto.getDirections());
            List<PatientDto> dtoList = new ArrayList<>();

            for (Patient patient : list) {
                dtoList.add(new PatientDto(patient));
            }
            return gson.toJson(new GetListPatientResponse(dtoList));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }

    }

    private void validateGetDoctorToPatientWithDirections(GetPatientOnDirectionsDtoRequest getPatientDto) throws ServerException {
        if (getPatientDto.getToken() == null || getPatientDto.getDirections() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }

    }

    public String doctorGetAllPatientAllDoctors(String textJson) {
        try {
            GetUserByTokenDtoRequest dto = getClassFromJson(textJson, GetUserByTokenDtoRequest.class);
            validateDoctorGetAllPatientAllDoctorsRequest(dto);

            //if such a doctor exists
            getDoctorByToken(dto.getToken());

            List<PatientInfoDtoRequest> listPatientInfo  = new ArrayList<>();
            for (Doctor doctorOne :  doctorDao.getAllPatientAllDoctors()) {
                for (Patient patient: doctorOne.getPatients()) {
                    listPatientInfo.add(new PatientInfoDtoRequest(new PatientDto(patient), doctorOne.getFirstName(), doctorOne.getLastName()));
                }
            }
            return gson.toJson(new DoctorGetAllPatientAllDoctorsResponse(listPatientInfo));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDoctorGetAllPatientAllDoctorsRequest(GetUserByTokenDtoRequest dto) throws ServerException {
        if (dto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String doctorGetAllPatientsWithDisease(String textJson) {
        try {
            DoctorGetAllPatientsWithDiseaseDtoRequest dto = getClassFromJson(textJson, DoctorGetAllPatientsWithDiseaseDtoRequest.class);
            validateDoctorGetAllPatientsWithDiseaseRequest(dto);
            //if such a doctor exists
            getDoctorByToken(dto.getToken());

            List<PatientInfoDtoRequest> patientInfo = new ArrayList<>();
            for (Doctor doctorOne : doctorDao.getAllPatientAllDoctors()) {
                for (Patient patient: doctorOne.getPatients()) {
                    if(patient.getDiseaseName().equals(dto.getDisease())) {
                        patientInfo.add(new PatientInfoDtoRequest(new PatientDto(patient), doctorOne.getFirstName(), doctorOne.getLastName()));
                    }
                }
            }

            return gson.toJson(new DoctorGetAllPatientAllDoctorsResponse(patientInfo));
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateDoctorGetAllPatientsWithDiseaseRequest(DoctorGetAllPatientsWithDiseaseDtoRequest dto) throws ServerException {
        if (dto.getToken() == null && dto.getDisease() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    //Распарсили строку Json с проверкой на синтаксическую ошибку
    public <T> T getClassFromJson(String jsonRequest, Class<T> tClass) throws ServerException {
        try {
            return gson.fromJson(jsonRequest, tClass);
        } catch (JsonSyntaxException e) {
            throw new ServerException(AnswerErrorCode.WRONG_JSON_SYNTAX);
        }
    }

    public void debugService() {
        doctorDao.clear();
    }

    public String getInfo(String jsonToken) {
        try {
            GetInfoUserDtoRequest dto = gson.fromJson(jsonToken, GetInfoUserDtoRequest.class);
            validateGetInfo(dto);

            User user = doctorDao.getByToken(dto.getToken());
            if (!user.getClass().equals(Doctor.class)) {
                throw new ServerException(AnswerErrorCode.TOKEN_ERROR);
            }
            return gson.toJson(new DoctorDto((Doctor) user));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetInfo(GetInfoUserDtoRequest dto) throws ServerException {
        if (dto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }


    private Patient getPatientById(int idPatient) throws ServerException {
        Patient patient;
        if (!(patient = (Patient) doctorDao.getUserById(idPatient)).getClass().equals(Patient.class)) {
            throw new ServerException(AnswerErrorCode.PATIENT_ID_ERROR);
        }
        return patient;
    }

    private Doctor getDoctorByToken(String tokenDoc) throws ServerException {
        Doctor doctor;
        if (!(doctor = (Doctor) doctorDao.getByToken(tokenDoc)).getClass().equals(Doctor.class) ) {
            throw new ServerException(AnswerErrorCode.TOKEN_ERROR);
        }
        return doctor;
    }
}
