package net.thumbtack.school.hospital.service;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.response.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorServiceTest {
    private DoctorService doctorService = new DoctorService();
    private Gson gson = new Gson();

    @Test
    void TestValidateRegDoctor() {
        DoctorService doctorService = new DoctorService();
        String doctor = "{firstName:Nikolay, lastName:Ivanovich, speciality:Therapist, login:niklayIvanovich@mail.ru,password:Nik228dgdgf}";
        String doctorTwo = "{firstName:f , lastName:Ivanovich, speciality:Therapist, login:nikolayD@mail.ru,password :Nik228dgdgf}";
        String doctorThree = "{firstName:Nikolay , lastName:f, speciality:Therapist, login:nikolayD@mail.ru,password :Nik228dgdgf}";
        String doctorFour = "{firstName:Nikolay , lastName:Ivanovich, speciality:f, login:nikolayD@mail.ru,password :Nik228dgdgf}";
        String doctorFive = "{firstName:Nikolay , lastName:Ivanovich, speciality:Therapist, login:ffff,password :Nik228dgdgf}";
        String doctorSix = "{firstName:Nikolay , lastName:Ivanovich, speciality:Therapist, login:nikolayD@mail.ru,password :ffff}";

        assertNotNull(doctorService.registerDoctor(doctor));
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.registerDoctor(doctorTwo), ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.registerDoctor(doctorThree),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.registerDoctor(doctorFour),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN.getMsg(),gson.fromJson(doctorService.registerDoctor(doctorFive),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD.getMsg(),gson.fromJson(doctorService.registerDoctor(doctorSix),ErrorResponse.class).getError());
    }

    @Test
    void TestValidateRegPatient() {
        String patient = "{firstName:Nikolay, lastName:Ivanovich, diseaseName:Spid, login:niklayIvanovich@mail.ru,password:Nik228dgdgf, tokenDoc: 4353534ffdsf34tgdg54tdft34t5}";
        String patientTwo = "{firstName:c, lastName:Ivanovich, diseaseName:Spid, login:niklayIvanovich@mail.ru,password:Nik228dgdgf, tokenDoc: 4353534ffdsf34tgdg54tdft34t5}";
        String patientThree = "{firstName:Nikolay, lastName:c, diseaseName:Spid, login:niklayIvanovich@mail.ru,password:Nik228dgdgf, tokenDoc: 4353534ffdsf34tgdg54tdft34t5}";
        String patientFour = "{firstName:Nikolay, lastName:Ivanovich, diseaseName:6, login:niklayIvanovich@mail.ru,password:Nik228dgdgf, tokenDoc: 4353534ffdsf34tgdg54tdft34t5}";
        String patientFive ="{firstName:Nikolay, lastName:Ivanovich, diseaseName:Spid, login:1, tokenDoc: 4353534ffdsf34tgdg54tdft34t5}";
        String patientSix = "{firstName:Nikolay, lastName:Ivanovich, diseaseName:Spid, login:niklayIvanovich@mail.ru,password:1, tokenDoc: 4werwerwfg3245324rfsdf}";

        assertNotNull(doctorService.regPatient(patient));
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.regPatient(patientTwo),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.regPatient(patientThree),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA.getMsg(),gson.fromJson(doctorService.regPatient(patientFour),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN.getMsg(),gson.fromJson(doctorService.regPatient(patientFive),ErrorResponse.class).getError());
        assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD.getMsg(),gson.fromJson(doctorService.regPatient(patientSix),ErrorResponse.class).getError());
    }
}