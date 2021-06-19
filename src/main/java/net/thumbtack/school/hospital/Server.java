package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.ServerService;
import net.thumbtack.school.hospital.service.UserService;

import java.io.Serializable;

public class Server implements Serializable {
    private final DoctorService docService = new DoctorService();
    private final PatientService patientService = new PatientService();
    private final UserService userService = new UserService();
    private final ServerService serverService = new ServerService();

    public String startServer(String savedDataFileName) {
        return serverService.start(savedDataFileName);
    }

    public String stopServer(String savedDataFileName) {
        return serverService.stop(savedDataFileName);
    }

    //Doctor
    //регистрация доктора
    public String regDoctorServer(String jsonText) {
        return docService.registerDoctor(jsonText);
    }

    //Удаление доктора
    public String delDoctorServer(String jsonToken) {
        return docService.delDoctor(jsonToken);
    }

    //Регестрация клиента через доктора
    public String regPatientServer(String jsonText) {
        return docService.regPatient(jsonText);
    }

    //Доктор добавляет направление для клиента
    public String addDestinationForPatient(String jsonText){ return docService.addDestination(jsonText);}

    //Доктор удаляет сове назначение
    public String delDestinationForPatient(String jsonText) {return docService.delDestination(jsonText);}

    //Доктор добавляет лекарство для клиента
    public String addDrugFromDoctor(String jsonText){ return docService.addDrugFrom(jsonText);}

    public String getDrugForPatient(String jsonText){return patientService.getDrug(jsonText);}

    public String delDrugForPatient(String jsonText){return  docService.delDrug(jsonText);}

    //Доктор получает всех своих клиентов с указанием болезни
    public String getPatientWithDiseaseServerFromDoctorServer(String textJson) { return docService.getDoctorToPatientWithDisease(textJson); }

    //Доктор получает всех своих клиентов получивших некоторое направление
    public String getPatientWithDirectionsServerFromDoctorServer(String textJson) { return docService.getDoctorToPatientWithDirections(textJson); }

    //Удаление клиента через доктора
    public String delPatientServer(String jsonText) {
        return docService.delPatient(jsonText);
    }

    //Доктор получает всех своих пациентов
    public String gePatientFromDoctorServer(String textJson) {
        return docService.getListPatientsFromDoctor(textJson);
    }

    //Доктор получает список всех пациентов, получающих то или иное назначение, в том числе и не своих, с указанием, кто является врачом этого пациента
    public String doctorGetAllPatientAllDoctors(String textJson){ return docService.doctorGetAllPatientAllDoctors(textJson); }

    //список всех пациентов, больных той или иной болезнью, в том числе и не своих, с указанием, кто является врачом этого пациента
    public String doctorGetAllPatientsWithDisease(String textJson){ return docService.doctorGetAllPatientsWithDisease(textJson); }

    //Patient
    //Клиент просматривает своего доктора
    public String getDoctorInfoFromPatient(String JsonToken) { return patientService.getDoctorInfoFromPatient(JsonToken); }

    //Клиет просматривает все свои направления
    public String getPatientToDirections(String JsonToken) {
        return patientService.getDirections(JsonToken);
    }

    //User
    //Обновление пароля у пользователя
    public String updatePasswordUserServer(String jsonText){return userService.updatePassUser(jsonText);}

    //Вход пользователя
    public String logInUser(String jsonText) {
        return userService.logInUser(jsonText);
    }

    //Выход доктора
    public String logOutUser(String jsonToken) {
        return userService.logOutUser(jsonToken);
    }

    public String getInfoDoctorByToken(String jsonToken){return docService.getInfo(jsonToken);}

    public String getInfoPatientByToken(String jsonToken){return patientService.getInfo(jsonToken);}


    public void  clear(){
        docService.debugService();
    }

}
