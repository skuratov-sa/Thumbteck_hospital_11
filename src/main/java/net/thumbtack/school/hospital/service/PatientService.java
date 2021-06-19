package net.thumbtack.school.hospital.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.request.GetInfoUserDtoRequest;
import net.thumbtack.school.hospital.response.*;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.request.GetUserByTokenDtoRequest;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private final Gson gson = new Gson();
    private final PatientDaoImpl patientDao = new PatientDaoImpl();

    public String getDoctorInfoFromPatient(String jsonText) {
        try {
            GetUserByTokenDtoRequest tokenDto = getClassFromJson(jsonText, GetUserByTokenDtoRequest.class);
            validateGetDoctorInfoFromPatientRequest(tokenDto);

            Patient patient = getPatientByToken(tokenDto.getToken());
            Doctor doctor = patientDao.getDoctorInfoFromPatient(patient);

            return gson.toJson(new GetDoctorInfoFromPatientResponse(doctor.getFirstName(), doctor.getLastName(), doctor.getSpeciality()));
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }


    private void validateGetDoctorInfoFromPatientRequest(GetUserByTokenDtoRequest tokenDto) throws ServerException {
        if (tokenDto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }


    public String getDirections(String jsonText) {
        try {
            GetUserByTokenDtoRequest tokenDto = getClassFromJson(jsonText, GetUserByTokenDtoRequest.class);
            validateGetDirectionsRequest(tokenDto);
            Patient patient = getPatientByToken(tokenDto.getToken());
            return gson.toJson(new GetDirectionsOnePatient(patientDao.getDirections( patient)));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetDirectionsRequest(GetUserByTokenDtoRequest tokenDto) throws ServerException {
        if (tokenDto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }


    public String getDrug(String jsonText) {
        try {
            GetUserByTokenDtoRequest tokenDto = getClassFromJson(jsonText, GetUserByTokenDtoRequest.class);
            validateGetDrugRequest(tokenDto);

            Patient patient = getPatientByToken(tokenDto.getToken());

            List<Drug> list =  patientDao.getDrug(patient);
            List<DrugDto> listDto = new ArrayList<>();

            for (Drug drug : list) {
                listDto.add(new DrugDto(drug.getName()));
            }
            return gson.toJson(new GetDrugListDtoResponse(listDto));

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetDrugRequest(GetUserByTokenDtoRequest tokenDto) throws ServerException {
        if (tokenDto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String getInfo(String jsonToken) {
        try {
            GetInfoUserDtoRequest dto = getClassFromJson(jsonToken, GetInfoUserDtoRequest.class);
            validateGetInfo(dto);

            Patient patient =  getPatientByToken(dto.getToken());

           return gson.toJson(new PatientDto(patient));
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateGetInfo(GetInfoUserDtoRequest patient) throws ServerException {
        if (patient.getToken() == null) {
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

    private Patient getPatientByToken(String token) throws ServerException {
        Patient patient;
        if (!(patient = (Patient) patientDao.getByTokenPatient(token)).getClass().equals(Patient.class)) {
            throw new ServerException(AnswerErrorCode.TOKEN_ERROR);
        }
        return patient;
    }

}
