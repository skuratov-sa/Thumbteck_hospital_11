package net.thumbtack.school.hospital;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.request.GetTokenDtoResponse;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.request.*;
import net.thumbtack.school.hospital.response.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ServerTest {
    Gson gson = new Gson();
    Server server = new Server();


    @Test
    public void startAndStopServer() {
        server.clear();
        ErrorResponse response = gson.fromJson(server.startServer("src/main/java/net/thumbtack/school/hospital/fileDB/file.txt"),ErrorResponse.class);
        Assert.assertNotNull(response.getError());
        //Регестрируем
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Victor","Malcev","Therapist","tima@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        Assert.assertNotNull(gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken());
        Assert.assertEquals("{}",server.stopServer("src/main/java/net/thumbtack/school/hospital/fileDB/file1.txt"));

        server.startServer("src/main/java/net/thumbtack/school/hospital/fileDB/file1.txt");
        LogInUserDtoRequest request = new LogInUserDtoRequest("tima@mail.ru","Sdfsf32334");
        String jsonRespLogIn = server.logInUser(gson.toJson(request));
        String tokenLogIn = gson.fromJson(jsonRespLogIn, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenLogIn);


        //Stop ERROR
        ErrorResponse response1 = gson.fromJson(server.stopServer(""),ErrorResponse.class);
        Assert.assertNotNull(response1);
    }


    @Test
    public void regDoctorServer() {
        server.clear();

        //Registration doctor
        RegistrationDoctorDtoRequest request = new RegistrationDoctorDtoRequest("Shramov","Maksim","Therapist","shram@mail.ru","gsgdf4243DFvc");
        String jsonRequst = gson.toJson(request);
        String jsonResponse = server.regDoctorServer(jsonRequst);
        GetTokenDtoResponse response = gson.fromJson(jsonResponse, GetTokenDtoResponse.class);
       Assert.assertNotNull(response.getToken());


        RegistrationDoctorDtoRequest requestSet = new RegistrationDoctorDtoRequest("","","","","");
        requestSet.setFirstName("Betty");
        requestSet.setLastName("Hellygnova");
        requestSet.setSpeciality("Therapist");
        requestSet.setLogin("therBetty@gmail.ru");
        requestSet.setPassword("2123dsnKHSABDKsdfs");

        String jsonRequstSet = gson.toJson(requestSet);
        String jsonResponseSet = server.regDoctorServer(jsonRequstSet);
        GetTokenDtoResponse responseSet = gson.fromJson(jsonResponseSet, GetTokenDtoResponse.class);
        Assert.assertNotNull(responseSet.getToken());

       //error SYNTAX json
        String requestErr =  "Hahahahaha";
        String jsonRequstErr = gson.toJson(requestErr);
        String jsonResponseErr = server.regDoctorServer(jsonRequstErr);
        ErrorResponse err = gson.fromJson(jsonResponseErr, ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.WRONG_JSON_SYNTAX.getMsg(),err.getError());
    }

    @Test
    public void logInAndLogOutDoctorServer() {
    	// REVU вынесите server.clear(); в @BeforeEach 
        server.clear();
        //Регестрируем
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Victor","Malcev","Therapist","tima@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        Assert.assertNotNull(gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken());

        LogInUserDtoRequest requestOne = new LogInUserDtoRequest("","123");
        ErrorResponse responseFalseOne = gson.fromJson(server.logInUser(gson.toJson(requestOne)), ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN.getMsg(),responseFalseOne.getError());
        LogInUserDtoRequest request = new LogInUserDtoRequest("tima@mail.ru","Sdfsf32334");
        String jsonRespLogIn = server.logInUser(gson.toJson(request));
        String tokenLogIn = gson.fromJson(jsonRespLogIn, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenLogIn);

        String jsonRespLogOut = server.logOutUser(gson.toJson(new GetTokenDtoResponse(tokenLogIn)));
        Assert.assertEquals("{}",jsonRespLogOut);
    }


    @Test
    public void updatePasswordDoctorServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Miheev","Pavel","Telepat","telepat@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String token = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(token);

        //error password is zero
        String jsonUpdateFalse = server.updatePasswordUserServer(gson.toJson(new UpdatePassUserDtoRequest(token,"")));
        ErrorResponse errorResponse = gson.fromJson(jsonUpdateFalse,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD.getMsg(),errorResponse.getError());

        //error token
        String jsonUpdateFalseTwo = server.updatePasswordUserServer(gson.toJson(new UpdatePassUserDtoRequest(token + "1","Skdfhk345345345khsdkfsdjfkh")));
        ErrorResponse errorResponseTwo = gson.fromJson(jsonUpdateFalseTwo,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponseTwo.getError());

        //true request
        String jsonUpdateTrue = server.updatePasswordUserServer(gson.toJson(new UpdatePassUserDtoRequest(token,"Sdfsf32334")));
        Assert.assertEquals("{}",jsonUpdateTrue);
    }
    @Test
    public void regPatientServer() {
        server.clear();
        //Register Doctor
        String reqRegDoc = gson.toJson(new RegistrationDoctorDtoRequest("Skuratov","Stanislav","Pediatrician","stas@mail.ru","sdf234234JKh3"));
        String respRegDoc = server.regDoctorServer(reqRegDoc);
        String tokenDoc = gson.fromJson(respRegDoc, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //True request
        String reqRegPatOne = gson.toJson(new RegistrationPatientDtoRequest("Misha","Pavlin","Prostuda","pati20@mail.ru","123HJHbjh43",tokenDoc));
        String respRegPatOne = server.regPatientServer(reqRegPatOne);
        Assert.assertEquals("{}",respRegPatOne);

        //Registration patient (TRUE SET)
        RegistrationPatientDtoRequest regPashaSet = new RegistrationPatientDtoRequest("","","","","","");
        regPashaSet.setFirstName("Pasha");
        regPashaSet.setLastName("Korolev");
        regPashaSet.setDiseaseName("stupid");
        regPashaSet.setLogin("Pasha@mail.ru");
        regPashaSet.setPassword("23982IHHiudhrfjskh");
        regPashaSet.setTokenDoc(tokenDoc);
        String jsonRegPashaDrugRespSet = server.regPatientServer(gson.toJson(regPashaSet));
        Assert.assertEquals("{}",jsonRegPashaDrugRespSet);

        //Error token
        String reqRegPatOTwo = gson.toJson(new RegistrationPatientDtoRequest("Nikita","Pavlin","Prostuda","pati21@mail.ru","123HJHbjh43",tokenDoc+"2"));
        String respRegPatTwo = server.regPatientServer(reqRegPatOTwo);
        ErrorResponse errorResponse = gson.fromJson(respRegPatTwo,ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(),AnswerErrorCode.TOKEN_ERROR.getMsg());

        //Error null
        String reqRegPatNull = gson.toJson(new RegistrationPatientDtoRequest("Nikita","Pavlin","Prostuda","pati21@mail.ru","123HJHbjh43",null));
        String respRegPatNull = server.regPatientServer(reqRegPatNull);
        ErrorResponse errorResponseNull = gson.fromJson(respRegPatNull,ErrorResponse.class);
        Assert.assertEquals(errorResponseNull.getError(),AnswerErrorCode.NULL_REQUEST.getMsg());

        //Registration patient with drug (TRUE)
        String jsonRegPatiDrugReq = gson.toJson(new RegistrationPatientWithDrugDtoRequest("Billy","Robbin","cough","loginPati@mail.ru","123DASdjosmdsflksdfjo1","Best drug",tokenDoc));
        String jsonRegPatiDrugResp = server.regPatientWithDrug(jsonRegPatiDrugReq);
        Assert.assertEquals("{}",jsonRegPatiDrugResp);

        //Registration patient with drug (TRUE SET)
        RegistrationPatientWithDrugDtoRequest regPatiSet = new RegistrationPatientWithDrugDtoRequest("","","","","","","");
        regPatiSet.setFirstName("Jon");
        regPatiSet.setLastName("Getcbi");
        regPatiSet.setDiseaseName("stupid");
        regPatiSet.setLogin("Nick@mail.ru");
        regPatiSet.setPassword("23982IHHiudhrfjskh");
        regPatiSet.setDrugDto(new DrugDto("drugs"));
        regPatiSet.setTokenDoc(tokenDoc);
        String jsonRegPatiDrugRespSet = server.regPatientWithDrug(gson.toJson(regPatiSet));
        Assert.assertEquals("{}",jsonRegPatiDrugRespSet);

        //Registration patient with drug (ERROR DRUG)
        String jsonRegPatiDrugReqErr = gson.toJson(new RegistrationPatientWithDrugDtoRequest("Billy","Robbin","cough","loginPati1@mail.ru","123DASdjosmdsflksdfjo1",null,tokenDoc));
        String jsonRegPatiDrugRespErr = server.regPatientWithDrug(jsonRegPatiDrugReqErr);
        ErrorResponse errorResponseDrug = gson.fromJson(jsonRegPatiDrugRespErr,ErrorResponse.class);
        Assert.assertEquals(errorResponseDrug.getError(),AnswerErrorCode.NULL_REQUEST.getMsg());


        //Test add patient about doctors
        List<PatientDto> list = gson.fromJson(server.getPatientFromDoctorServer(gson.toJson(new GetTokenDtoResponse(tokenDoc))),GetListPatientResponse.class).getPatient();
        Assert.assertEquals(list.size(),4);
    }


    @Test
    public void delDoctorServer() {
        server.clear();
        //Register a doctors
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Alena","psychologist","psih1@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Poleshuk","Roman","psychologist","psih23@mail.ru","Wdsfsc232Df"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        String jsonRequstThree = gson.toJson(new RegistrationDoctorDtoRequest("Nikitin","Nikita","Terapevt","psih3@mail.ru","Sdfsf32334"));
        String jsonResponseTree = server.regDoctorServer(jsonRequstThree);
        String tokenDocThree = gson.fromJson(jsonResponseTree, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocThree);

        //Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Hachapuri","Misha","Spid","pat1@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Mishin","Misha","Spid","psih2@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        String jsonRequestPatientThree = gson.toJson(new RegistrationPatientDtoRequest("Nikita","Gara","Spid","pat111@mail.ru","Sdfsf32334",tokenDocThree));
        String jsonResponsePatientThree = server.regPatientServer(jsonRequestPatientThree);
        Assert.assertEquals("{}",jsonResponsePatientThree);

        //error token
        String requestDel = gson.toJson(new DelDoctorDtoRequest(tokenDocOne + "1"));
        String responseDel = server.delDoctorServer(requestDel);
        ErrorResponse errorTokenDoc = gson.fromJson(responseDel,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorTokenDoc.getError());

        //error null
        String requestDelErr = gson.toJson(new DelDoctorDtoRequest(null));
        String responseDelErr = server.delDoctorServer(requestDelErr);
        ErrorResponse errorTokenDocTwo = gson.fromJson(responseDelErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorTokenDocTwo.getError());

        //delete doctorOne
        String requestDelTwo = gson.toJson(new DelDoctorDtoRequest(tokenDocOne));
        String responseDelTwo = server.delDoctorServer(requestDelTwo);
        Assert.assertEquals("{}",responseDelTwo);


        //Test add patients
        String jsonReq  = gson.toJson(new GetTokenDtoResponse(tokenDocTwo));
        String jsonResp = server.getPatientFromDoctorServer(jsonReq);
        GetListPatientResponse list = gson.fromJson(jsonResp,GetListPatientResponse.class);
        Assert.assertEquals(2,list.getPatient().size());

        // delete doctorTwo
        String requestDelThree = gson.toJson(new DelDoctorDtoRequest(tokenDocTwo));
        String responseDelThree = server.delDoctorServer(requestDelThree);
        Assert.assertEquals("{}", responseDelThree);

        //Test add patients
        String jsonReqThree = gson.toJson(new GetTokenDtoResponse(tokenDocThree));
        String jsonRespThree = server.getPatientFromDoctorServer(jsonReqThree);
        GetListPatientResponse listThree = gson.fromJson(jsonRespThree,GetListPatientResponse.class);
        Assert.assertEquals(3,listThree.getPatient().size());

        // delete doctorThree error
        String requestDelError = gson.toJson(new DelDoctorDtoRequest(tokenDocThree));
        String responseDelError = server.delDoctorServer(requestDelError);
        ErrorResponse errorResponse = gson.fromJson(responseDelError,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ERROR_DELETE_DOCTOR.getMsg(), errorResponse.getError());
    }

    @Test
    public void addAndDelAndGetDestinationFromDoctor() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Alena","psychologist","psih24@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a doctor
        String jsonRequstDocTwo = gson.toJson(new RegistrationDoctorDtoRequest("Skuratov","Nikita","psychologist","psihfff24@mail.ru","Sdfsf32334"));
        String jsonResponseDocTwo = server.regDoctorServer(jsonRequstDocTwo);
        String tokenDocDocTwo = gson.fromJson(jsonResponseDocTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocDocTwo);

        //Register a patient
        String jsonRequestPati = gson.toJson(new RegistrationPatientDtoRequest("Merlow","Slava","Spid","slava@mail.ru","Sdfsf32334",tokenDocDocTwo));
        String jsonResponsePatiOne = server.regPatientServer(jsonRequestPati);
        Assert.assertEquals("{}",jsonResponsePatiOne);

        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Spid","pat25@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pat25@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull("{}", jsonResponseLogInPatientOne);

        //get id patient by doctor one
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDocOne));
        String jsonResponseGetPatient = server.getPatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patients.size());

        //get id patient by doctor two
        String jsonReqGetPatiTwo = gson.toJson(new GetListPatientDtoRequest(tokenDocDocTwo));
        String jsonRespGetPatiTwo = server.getPatientFromDoctorServer(jsonReqGetPatiTwo);
        List<PatientDto> patientsTwo = gson.fromJson(jsonRespGetPatiTwo,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patientsTwo.size());

        Assert.assertNotEquals(patients.get(0).getId(),patients.get(0).getDoctorId());

        //new destination
        String reqNewDest = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),"Sleep 8 hours"));
        String resp = server.addDestinationForPatient(reqNewDest);
        Assert.assertEquals("{}",resp);



        String reqNewDestTwo = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),"Drink beer"));
        String respTwo = server.addDestinationForPatient(reqNewDestTwo);
        Assert.assertEquals("{}",respTwo);

        //error token
        String reqErrTok = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDocOne+"22",patients.get(0).getId() + 9,"Drink beer"));
        String respErr = server.addDestinationForPatient(reqErrTok);
        ErrorResponse errorTokenAdd = gson.fromJson(respErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorTokenAdd.getError());

        //error id
        String reqErrPati = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDocOne,patientsTwo.get(0).getId(),"Drink beer"));
        String respErrPati = server.addDestinationForPatient(reqErrPati);
        ErrorResponse errorPati = gson.fromJson(respErrPati,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ERROR_DOCTOR_DID_NOT_PATIENT.getMsg(),errorPati.getError());

        //error token null
        String reqErrNull = gson.toJson(new AddDestinationFromDoctorDtoRequest(null,patients.get(0).getId(),"Drink beer"));
        String respErrNull = server.addDestinationForPatient(reqErrNull);
        ErrorResponse errorNull = gson.fromJson(respErrNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorNull.getError());

        //Checking destination for patients
        String reqGetDesti = gson.toJson( new GetTokenDtoResponse(tokenPatientOne));
        String respGetDrsti = server.getPatientToDirections(reqGetDesti);
        List<String> list = gson.fromJson(respGetDrsti,GetDirectionsOnePatient.class).getDirections();
        Assert.assertEquals(list.get(0),"Sleep 8 hours");
        Assert.assertEquals(list.get(1),"Drink beer");

        //ERROR NULL token
        String reqGetDestiTokenErr = gson.toJson( new GetTokenDtoResponse(null));
        String respGetDrstiTokenErr = server.getPatientToDirections(reqGetDestiTokenErr);
        ErrorResponse errorResponse = gson.fromJson(respGetDrstiTokenErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResponse.getError());


        //del destination
        String reqDel = gson.toJson(new DelDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),"Drink beer"));
        String respDel = server.delDestinationForPatient(reqDel);
        Assert.assertEquals("{}",respDel);

        //del error id
        String reqDelTwo = gson.toJson(new DelDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId() + 5,"Drink beer"));
        ErrorResponse errDel = gson.fromJson(server.delDestinationForPatient(reqDelTwo),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ID_ERROR.getMsg(),errDel.getError());

        //del err null
        String reqDelNull = gson.toJson(new DelDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),null));
        ErrorResponse errNull = gson.fromJson(server.delDestinationForPatient(reqDelNull),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errNull.getError());

        //Checking destination for patients
        String reqGetDestiTwo = gson.toJson( new GetTokenDtoResponse(tokenPatientOne));
        String respGetDrstiTwo = server.getPatientToDirections(reqGetDestiTwo);
        List<String> listTwo = gson.fromJson(respGetDrstiTwo,GetDirectionsOnePatient.class).getDirections();
        Assert.assertEquals(1,listTwo.size());
        Assert.assertEquals(listTwo.get(0),"Sleep 8 hours");
    }

    @Test
    public void addAndGetAndDelDrugFromDoctor() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Alena","psychologist","psih26@mail.ru","Ddfghk3535"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Spid","pat30@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDocOne));
        String jsonResponseGetPatient = server.getPatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patients.size());

        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pat30@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull("{}", jsonResponseLogInPatientOne);

        //ERROR PASSWORD
        String jsonRequestLogInPatientErr = gson.toJson(new LogInUserDtoRequest("pat31@mail.ru","24"));
        String jsonResponseLogInPatientOneErr = server.logInUser(jsonRequestLogInPatientErr);
        ErrorResponse errorResponse = gson.fromJson(jsonResponseLogInPatientOneErr, ErrorResponse.class);
        Assert.assertNotNull(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN.getMsg(), errorResponse.getError());

        String reqNewDest = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(0).getId(),new DrugDto("Supradin")));
        String resp = server.addDrugFromDoctor(reqNewDest);
        Assert.assertEquals("{}",resp);

        String reqNewDestTwo = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(0).getId(),new DrugDto("laxative")));
        String respTwo = server.addDrugFromDoctor(reqNewDestTwo);
        Assert.assertEquals("{}",respTwo);

        //Checking drug for patient
        String reqGetDrug = gson.toJson( new GetTokenDtoResponse(tokenPatientOne));
        String respGetDrug = server.getDrugForPatient(reqGetDrug);
        List<net.thumbtack.school.hospital.response.DrugDto> list = gson.fromJson(respGetDrug, GetDrugListDtoResponse.class).getList();
        Assert.assertEquals(list.get(0).getName(),"Supradin");
        Assert.assertEquals(list.get(1).getName(),"laxative");

        //Checking drug for patient ERROR token
        String reqGetDrugNull = gson.toJson( new GetTokenDtoResponse(null));
        String respGetDrugNull = server.getDrugForPatient(reqGetDrugNull);
        ErrorResponse errNull = gson.fromJson(respGetDrugNull , ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errNull.getError());

        //del error null
        String reqDelNull = gson.toJson(new DelDrugFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),null));
        String respDelNull = server.delDrugForPatient(reqDelNull);
        ErrorResponse responseNull = gson.fromJson(respDelNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),responseNull.getError());

        //del error drug
        String reqDelErr = gson.toJson(new DelDrugFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),new DrugDto("Supradins")));
        String respDelErr = server.delDrugForPatient(reqDelErr);
        ErrorResponse response = gson.fromJson(respDelErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ERROR_DRUG.getMsg(),response.getError());

        //del drug patient
        String reqDel = gson.toJson(new DelDrugFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),new DrugDto("Supradin")));
        String respDel = server.delDrugForPatient(reqDel);
        Assert.assertEquals("{}",respDel);

        //Checking drug for patient
        String reqGetDrugTwo = gson.toJson( new GetTokenDtoResponse(tokenPatientOne));
        String respGetDrugTwo = server.getDrugForPatient(reqGetDrugTwo);
        List<net.thumbtack.school.hospital.response.DrugDto> listTwo = gson.fromJson(respGetDrugTwo, GetDrugListDtoResponse.class).getList();
        Assert.assertEquals(1,listTwo.size());
        Assert.assertEquals(listTwo.get(0).getName(),"laxative");
    }

    @Test
    public void getPatientWithDiseaseServerFromDoctorServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Billi","psychologist","psih36@mail.ru","Ddfghk3535"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Covid","pat34@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Lesha","Lee","Spid","pat35@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOneTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientOneTwo);

        //Get disease
        String reqGetPati = gson.toJson(new GetPatientOnDiseaseDtoRequest(tokenDocOne,"Spid"));
        String respGetPati = server.getPatientWithDiseaseServerFromDoctorServer(reqGetPati);
        List<PatientDto> list = gson.fromJson(respGetPati,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,list.size());
        Assert.assertEquals("pat35@mail.ru",list.get(0).getLogin());

        // Get disease zero
        String reqGetPatiZero = gson.toJson(new GetPatientOnDiseaseDtoRequest(tokenDocOne,"Spides"));
        String respGetPatiZero = server.getPatientWithDiseaseServerFromDoctorServer(reqGetPatiZero);
        List<PatientDto> listZero = gson.fromJson(respGetPatiZero,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(0,listZero.size());

        //Get disease err null
        String reqGetPatiNull = gson.toJson(new GetPatientOnDiseaseDtoRequest(null,"Spides"));
        String respGetPatiNull = server.getPatientWithDiseaseServerFromDoctorServer(reqGetPatiNull);
        String msqErr = gson.fromJson(respGetPatiNull,ErrorResponse.class).getError();
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),msqErr);
    }

    @Test
    public void getPatientWithDirectionsServerFromDoctorServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc73@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);


        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc74@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati51@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati52@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDoc));
        String jsonResponseGetPatient = server.getPatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patientsDocOne = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patientsDocOne.size());

        String jsonRequestGetPatientTwo = gson.toJson(new GetListPatientDtoRequest(tokenDocTwo));
        String jsonResponseGetPatientTwo = server.getPatientFromDoctorServer(jsonRequestGetPatientTwo);
        List<PatientDto> patientsDocTwo = gson.fromJson(jsonResponseGetPatientTwo,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1, patientsDocTwo.size());

        //new Directions
        String reqNewDest = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDoc,patientsDocOne.get(0).getId(),"Sleep 8 hours"));
        String resp = server.addDestinationForPatient(reqNewDest);
        Assert.assertEquals("{}",resp);

        String reqNewDestTwo = gson.toJson(new AddDestinationFromDoctorDtoRequest(tokenDocTwo,patientsDocTwo.get(0).getId(),"Sleep 8 hours"));
        String respTwo = server.addDestinationForPatient(reqNewDestTwo);
        Assert.assertEquals("{}",respTwo);

        //get all
        String reqGetPati = gson.toJson(new GetPatientOnDirectionsDtoRequest(tokenDoc,"Sleep 8 hours"));
        String respGetPati = server.getPatientWithDirectionsServerFromDoctorServer(reqGetPati);
        ArrayList<PatientDto> list = (ArrayList<PatientDto>) gson.fromJson(respGetPati,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,list.size());

        //get all error Token doc
        String reqGetPatiErr = gson.toJson(new GetPatientOnDirectionsDtoRequest(tokenDoc + "1","Sleep 8 hours"));
        String respGetPatiErr = server.getPatientWithDirectionsServerFromDoctorServer(reqGetPatiErr);
        ErrorResponse errorResponse = gson.fromJson(respGetPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());

        //get err null
        String reqGetPatiNull = gson.toJson(new GetPatientOnDirectionsDtoRequest(null,"Sleep 8 hours"));
        String respGetPatiNull = server.getPatientWithDirectionsServerFromDoctorServer(reqGetPatiNull);
        ErrorResponse errorResponseNull = gson.fromJson(respGetPatiNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResponseNull.getError());
    }

    @Test
    public void delPatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Billi","psychologist","psih38@mail.ru","Ddfghk3535"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Covid","pat36@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Lesha","Lee","Spid","pat37@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOneTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientOneTwo);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDocOne));
        String jsonResponseGetPatient = server.getPatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(2,patients.size());

        //Add drug
        String jsonRequestAddDrugPatiOne = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(0).getId(),new DrugDto("gemotogenka")));
        String jsonResponseAddDrugPatiOne = server.addDrugFromDoctor(jsonRequestAddDrugPatiOne);
        Assert.assertEquals("{}",jsonResponseAddDrugPatiOne);

        String jsonRequestAddDrugPatiTwo = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(1).getId(),new DrugDto("chocolate")));
        String jsonResponseAddDrugPatiTwo = server.addDrugFromDoctor(jsonRequestAddDrugPatiTwo);
        Assert.assertEquals("{}",jsonResponseAddDrugPatiTwo);

        //LogIn
        String reqLogIn = gson.toJson(new LogInUserDtoRequest("pat36@mail.ru","Sdfsf32334"));
        GetTokenDtoResponse respLogIn = gson.fromJson(server.logInUser(reqLogIn), GetTokenDtoResponse.class);
        String newTokenPatientOne = respLogIn.getToken();
        Assert.assertNotNull(respLogIn.getToken());

        //Delete patient
        String reqDelPati = gson.toJson(new DelPatientDtoRequest(tokenDocOne,patients.get(0).getId()));
        String respDelPati = server.delPatientServer(reqDelPati);
        Assert.assertEquals("{}",respDelPati);

        //Delete err null
        String reqDelPatiErrNull = gson.toJson(new DelPatientDtoRequest(null,patients.get(1).getId()));
        String respDelPatiErrNull = server.delPatientServer(reqDelPatiErrNull);
        ErrorResponse errorResponseNull = gson.fromJson(respDelPatiErrNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResponseNull.getError());

        //Delete err token
        String reqDelPatiErrToken = gson.toJson(new DelPatientDtoRequest(newTokenPatientOne,patients.get(1).getId()));
        String respDelPatiErrToken = server.delPatientServer(reqDelPatiErrToken);
        ErrorResponse errorResponseToken = gson.fromJson(respDelPatiErrToken,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponseToken.getError());


        //Delete patient err id
        String reqDelPatiErr = gson.toJson(new DelPatientDtoRequest(tokenDocOne,patients.get(1).getId() + 10));
        String respDelPatiErr = server.delPatientServer(reqDelPatiErr);
        ErrorResponse errorResponse = gson.fromJson(respDelPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ID_ERROR.getMsg(),errorResponse.getError());

        //Delete patient err id doctor
        String reqDelPatiErrDocId = gson.toJson(new DelPatientDtoRequest(tokenDocOne,patients.get(1).getDoctorId()));
        String respDelPatiErrDocId = server.delPatientServer(reqDelPatiErrDocId);
        ErrorResponse errorResponseDocId = gson.fromJson(respDelPatiErrDocId,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.PATIENT_ID_ERROR.getMsg(),errorResponseDocId.getError());


    }

    @Test
    public void getDoctorToPatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","dent@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

    //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati11@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Ptushkin","Nurlan","Spid","pati12@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        String req = server.getPatientFromDoctorServer(gson.toJson(new GetTokenDtoResponse(tokenDoc)));
        GetListPatientResponse resp = gson.fromJson(req,GetListPatientResponse.class);
        Assert.assertEquals(2,resp.getPatient().size());
    }

    @Test
    public void doctorGetAllPatientAllDoctors() {
      server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc70@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc71@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati40@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati41@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        //get all patients and doctors
       String reqGetAll =  gson.toJson(new GetTokenDtoResponse(tokenDoc));
       String respGetAll = server.doctorGetAllPatientAllDoctors(reqGetAll);
       List<PatientDto> list = gson.fromJson(respGetAll,DoctorGetAllPatientAllDoctorsResponse.class).getPatientInfo();
       Assert.assertEquals(2,list.size());
       Assert.assertEquals(list.get(0).getLogin(),"pati40@mail.ru");
        Assert.assertEquals(list.get(1).getLogin(),"pati41@mail.ru");

        //error token
        String reqGetAllErr =  gson.toJson(new GetTokenDtoResponse(tokenDoc + "1"));
        String respGetAllErr = server.doctorGetAllPatientAllDoctors(reqGetAllErr);
        ErrorResponse errorResponse = gson.fromJson(respGetAllErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());

        //Error null
        String reqGetAllErrNull =  gson.toJson(new GetTokenDtoResponse(null));
        String respGetAllErrNull = server.doctorGetAllPatientAllDoctors(reqGetAllErrNull);
        ErrorResponse errorResponseNull = gson.fromJson(respGetAllErrNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResponseNull.getError());
    }


    @Test
    public void logInAndOutAndUpdatePatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequest = gson.toJson(new RegistrationDoctorDtoRequest("Petunin","Lesha","Dentist","dent1@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequest);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //    Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Killer","Mamin","Cold","pati31@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        //Patient get Drug error logIn
        String errResp  = server.getDrugForPatient(gson.toJson(new GetTokenDtoResponse("3453453-34534gdgdg-365456745gdgrt")));
        ErrorResponse response = gson.fromJson(errResp,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),response.getError());

        //LogIn error
        String reqLogInErr = gson.toJson(new LogInUserDtoRequest("pati3221@mail.ru","Sdfsf32334"));
        ErrorResponse respLogInErr = gson.fromJson(server.logInUser(reqLogInErr),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.LOGIN_ERROR.getMsg(),respLogInErr.getError());

        //Password error
        String reqPassErr = gson.toJson(new LogInUserDtoRequest("pati31@mail.ru","S1sdf4r4Dwdw"));
        ErrorResponse respPassErr = gson.fromJson(server.logInUser(reqPassErr),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.PASSWORD_ERROR.getMsg(),respPassErr.getError());
        //LogIn
        String reqLogIn = gson.toJson(new LogInUserDtoRequest("pati31@mail.ru","Sdfsf32334"));
        GetTokenDtoResponse respLogIn = gson.fromJson(server.logInUser(reqLogIn), GetTokenDtoResponse.class);
        String newTokenPatientOne = respLogIn.getToken();
        Assert.assertNotNull(respLogIn.getToken());


        //Update patient err pass
        String reqUpPassErr = gson.toJson(new UpdatePassUserDtoRequest(tokenDoc,"123"));
        String respUpPassErr = server.updatePasswordUserServer(reqUpPassErr);
        ErrorResponse errUpPass = gson.fromJson(respUpPassErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD.getMsg(),errUpPass.getError());

        //ERROR class Dto
        String reqDtoErr = gson.toJson("sdfhnskjfhsdkjfhskjfhsdkjfhskdjfhskdjhf");
        ErrorResponse errDto = gson.fromJson(server.updatePasswordUserServer(reqDtoErr),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.WRONG_JSON_SYNTAX.getMsg(),errDto.getError());


        //Update patient err token
        String reqTokenErr = gson.toJson(new UpdatePassUserDtoRequest(tokenDoc + 1,"DAKFJnsmsdnfkjn123"));
        String respTokenErr = server.updatePasswordUserServer(reqTokenErr);
        ErrorResponse errUpToken = gson.fromJson(respTokenErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errUpToken.getError());

        //Update patient err null token
        String reqTokenErrNull = gson.toJson(new UpdatePassUserDtoRequest(null,"DAKFJnsmsdnfkjn123"));
        String respTokenErrNull = server.updatePasswordUserServer(reqTokenErrNull);
        ErrorResponse errUpTokenNull = gson.fromJson(respTokenErrNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errUpTokenNull.getError());


        //update password patient
        String reqUpPass = gson.toJson(new UpdatePassUserDtoRequest(newTokenPatientOne,"123KHJfhkjrwkjr"));
        String respUpPass = server.updatePasswordUserServer(reqUpPass);
        Assert.assertEquals("{}",respUpPass);

        //LogOut
        String logOut = gson.toJson(new GetTokenDtoResponse(newTokenPatientOne));
        String respLogOutTwo = server.logOutUser(logOut);
        Assert.assertEquals("{}",respLogOutTwo);

        //LogOut Error token
        String logOutErr = gson.toJson(new GetTokenDtoResponse(null));
        ErrorResponse errorResp = gson.fromJson(server.logOutUser(logOutErr),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResp.getError());

        //LogIn new Pass
        String reqLogInNewPass = gson.toJson(new LogInUserDtoRequest("pati31@mail.ru","123KHJfhkjrwkjr"));
        GetTokenDtoResponse respLogInNewPass = gson.fromJson(server.logInUser(reqLogInNewPass), GetTokenDtoResponse.class);
        String newTokenPatientOneNewPass = respLogInNewPass.getToken();
        Assert.assertNotNull(newTokenPatientOneNewPass);

    }

    @Test
    public void getPatientToDoctorsServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc76@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //Register a doctor
        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc75@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati425@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati47@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        //LogIn patiOne
        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pati425@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientOne);

        //Get doc info
        String reqGetAllDoc = gson.toJson(new GetTokenDtoResponse(tokenPatientOne));
        String respGetAllDoc = server.getDoctorInfoFromPatient(reqGetAllDoc);
        GetDoctorInfoFromPatientResponse doctorInfo = gson.fromJson(respGetAllDoc, GetDoctorInfoFromPatientResponse.class);
        Assert.assertEquals("Petrushkin",doctorInfo.getFirstName());
        Assert.assertEquals("Petya",doctorInfo.getLastName());
        Assert.assertEquals("Dentist",doctorInfo.getSpeciality());

        //Get doc ERROR token
        String reqGetAllDocErr = gson.toJson(new GetTokenDtoResponse(null));
        String respGetAllDocErr = server.getDoctorInfoFromPatient(reqGetAllDocErr);
        ErrorResponse doctorInfoErr = gson.fromJson(respGetAllDocErr, ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),doctorInfoErr.getError());

        //LogIn patiTwo
        String jsonRequestLogInPatientTwo = gson.toJson(new LogInUserDtoRequest("pati47@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientTwo = server.logInUser(jsonRequestLogInPatientTwo);
        String tokenPatientTwo = gson.fromJson(jsonResponseLogInPatientTwo, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientTwo);

        //Get doc info err
        String reqGetAllDocError = gson.toJson(new GetTokenDtoResponse(tokenPatientTwo + "1"));
        String respGetAllDocError = server.getDoctorInfoFromPatient(reqGetAllDocError);
        ErrorResponse errorResponseError = gson.fromJson(respGetAllDocError,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponseError.getError());
    }

    @Test
    public void doctorGetAllPatientsWithDisease() {
        server.clear();
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc80@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);


        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc81@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Spid","pati82@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);


        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati83@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);


        //get all error Token doc
        String reqGetPatiErr = gson.toJson(new DoctorGetAllPatientsWithDiseaseDtoRequest(tokenDoc + "1","Spid"));
        String respGetPatiErr = server.doctorGetAllPatientsWithDisease(reqGetPatiErr);
        ErrorResponse errorResponse = gson.fromJson(respGetPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());

        //err null
        String reqGetPatiErrNull = gson.toJson(new DoctorGetAllPatientsWithDiseaseDtoRequest(tokenDoc,null));
        String respGetPatiErrNull = server.doctorGetAllPatientsWithDisease(reqGetPatiErrNull);
        ErrorResponse errorResponseNull = gson.fromJson(respGetPatiErrNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),errorResponseNull.getError());

        //True version
        String reqGetPatient = gson.toJson(new DoctorGetAllPatientsWithDiseaseDtoRequest(tokenDoc,"Spid"));
        String respGetPatient = server.doctorGetAllPatientsWithDisease(reqGetPatient);
        DoctorGetAllPatientAllDoctorsResponse dto = gson.fromJson(respGetPatient,DoctorGetAllPatientAllDoctorsResponse.class);
        Assert.assertEquals(2,dto.getPatientInfo().size());
    }
    @Test
    public void getInfoByToken(){
        server.clear();
        //Registration doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc80@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //    Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Spid","pati82@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);


        //Get info Doc
        String reqGetDoct = gson.toJson(new GetInfoUserDtoRequest(tokenDoc));
        String respGetInfoDoc = server.getInfoDoctorByToken(reqGetDoct);
        DoctorDto doctorDto = gson.fromJson(respGetInfoDoc,DoctorDto.class);

        Assert.assertEquals("Petrushkin",doctorDto.getFirstName());
        Assert.assertEquals("Petya",doctorDto.getLastName());
        Assert.assertEquals("Dentist",doctorDto.getSpeciality());
        Assert.assertEquals("doc80@mail.ru",doctorDto.getLogin());


        //Get info Doc ERROR token null
        String reqGetDoctTokNull = gson.toJson(new GetInfoUserDtoRequest(null));
        String respGetInfoDocTokNull = server.getInfoDoctorByToken(reqGetDoctTokNull);
        ErrorResponse doctorDtoTokNull = gson.fromJson(respGetInfoDocTokNull,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),doctorDtoTokNull.getError());

        //Get info Doc ERROR token
        String reqGetDoctTok = gson.toJson(new GetInfoUserDtoRequest(tokenDoc+1));
        String respGetInfoDocTok = server.getInfoDoctorByToken(reqGetDoctTok);
        ErrorResponse doctorDtoTok = gson.fromJson(respGetInfoDocTok,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),doctorDtoTok.getError());

        //LogIn patient
        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pati82@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientOne);

        //Get info Patient
        String reqGetPati = gson.toJson(new GetInfoUserDtoRequest(tokenPatientOne));
        String respGetInfoPati = server.getInfoPatientByToken(reqGetPati);
        PatientDto patientDto = gson.fromJson(respGetInfoPati,PatientDto.class);

        //Get info Patient err null
        String reqGetPatiErr = gson.toJson(new GetInfoUserDtoRequest(null));
        String respGetInfoPatiErr = server.getInfoPatientByToken(reqGetPatiErr);
        ErrorResponse patientDtoErr = gson.fromJson(respGetInfoPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),patientDtoErr.getError());

        //Get info Patient ERROR SYNTAX
        String reqGetPatiErrSin = "fsjkdfhksjdhfkjsdhfkjsdhfkjshfjsfkjhs";
        String respGetInfoPatiErrSin = server.getInfoPatientByToken(reqGetPatiErrSin);
        ErrorResponse patientDtoErrSin = gson.fromJson(respGetInfoPatiErrSin,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.WRONG_JSON_SYNTAX.getMsg(),patientDtoErrSin.getError());

        //Get info Patient err token
        String reqGetPatiErrToken = gson.toJson(new GetInfoUserDtoRequest(tokenDoc));
        String respGetInfoPatiErrToken = server.getInfoPatientByToken(reqGetPatiErrToken);
        ErrorResponse patientDtoErrToken = gson.fromJson(respGetInfoPatiErrToken,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),patientDtoErrToken.getError());

        Assert.assertEquals("Mironov",patientDto.getFirstName());
        Assert.assertEquals("Mitya",patientDto.getLastName());
        Assert.assertEquals("Spid",patientDto.getDiseaseName());
        Assert.assertEquals("pati82@mail.ru",patientDto.getLogin());

    }

    @Test
    public void getListPatientWithDrug(){
        server.clear();
        //Registration doctor
        String jsonReqD = gson.toJson(new RegistrationDoctorDtoRequest("Mihail","Kovilchinco","Dentst","mihail@gmail.ru","Dddd4456ksdflJH3"));
        String jsonRespD = server.regDoctorServer(jsonReqD);
        String tokenDoc = gson.fromJson(jsonRespD, net.thumbtack.school.hospital.response.GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //Registration patient
        String jsonReqPOne = gson.toJson(new RegistrationPatientDtoRequest("Mitay","Bethoven","Snuffle","Mitay@mail.ru","2FJLH23kjhdkjkjh",tokenDoc));
        String jsonRespPOne = server.regPatientServer(jsonReqPOne);
        Assert.assertEquals("{}",jsonRespPOne);

        String jsonReqPatiTwo = gson.toJson(new RegistrationPatientDtoRequest("Anton","Titov","COVID","titOf@mail.ru","2FJLH23kjhdkjkjh",tokenDoc));
        String jsonRespPatiTwo = server.regPatientServer(jsonReqPatiTwo);
        Assert.assertEquals("{}",jsonRespPatiTwo);

        String jsonReqPatiThree = gson.toJson(new RegistrationPatientDtoRequest("Grisha","Nanana","Happy","Mit@mail.ru","2FJLH23kjhdkjkjh",tokenDoc));
        String jsonRespPatiThree = server.regPatientServer(jsonReqPatiThree);
        Assert.assertEquals("{}",jsonRespPatiThree);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDoc));
        String jsonResponseGetPatient = server.getPatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(3,patients.size());

        //get id patient err null
        String jsonReqNull = gson.toJson(new GetListPatientDtoRequest(null));
        String jsonRespNull = server.getPatientFromDoctorServer(jsonReqNull);
        String patientsNull = gson.fromJson(jsonRespNull,ErrorResponse.class).getError();
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),patientsNull);


        //Add drug
        String jsonReqDrugOne = gson.toJson(new AddDrugDtoRequest(tokenDoc,patients.get(0).getId(),new DrugDto("laxative")));
        String respDrugOne = server.addDrugFromDoctor(jsonReqDrugOne);
        Assert.assertEquals("{}",respDrugOne);

        String jsonReqDrugTwo = gson.toJson(new AddDrugDtoRequest(tokenDoc,patients.get(1).getId(),new DrugDto("antidepressants")));
        String respDrugTwo = server.addDrugFromDoctor(jsonReqDrugTwo);
        Assert.assertEquals("{}",respDrugTwo);

        String jsonReqDrugThree = gson.toJson(new AddDrugDtoRequest(tokenDoc,patients.get(2).getId(),new DrugDto("laxative")));
        String respDrugThree = server.addDrugFromDoctor(jsonReqDrugThree);
        Assert.assertEquals("{}",respDrugThree);

        //Add drug ERROR Token
        String jsonReqDrugOneToken = gson.toJson(new AddDrugDtoRequest(null,patients.get(0).getId(),new DrugDto("laxative")));
        ErrorResponse respDrugOneToken = gson.fromJson(server.addDrugFromDoctor(jsonReqDrugOneToken),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),respDrugOneToken.getError());

        String jsonGetListPatient = gson.toJson(new GetPatientListByDrugDtoRequest(tokenDoc,"laxative"));
        String jsonGetResp = server.getPatientListByDrug(jsonGetListPatient);
        List<PatientDto> list = gson.fromJson(jsonGetResp,GetPatientListByDrugResponse.class).getDtoList();
        Assert.assertEquals(2,list.size());

        String jsonGetListPatientErr = gson.toJson(new GetPatientListByDrugDtoRequest(tokenDoc,"None"));
        String jsonGetRespErr = server.getPatientListByDrug(jsonGetListPatientErr);
        List<PatientDto> listErr = gson.fromJson(jsonGetRespErr,GetPatientListByDrugResponse.class).getDtoList();
        Assert.assertEquals(0,listErr.size());

        //error null
        String jsonGetListPatientErrNull = gson.toJson(new GetPatientListByDrugDtoRequest(tokenDoc,null));
        String jsonGetRespErrNull = server.getPatientListByDrug(jsonGetListPatientErrNull);
        String listErrNull = gson.fromJson(jsonGetRespErrNull,ErrorResponse.class).getError();
        Assert.assertEquals(AnswerErrorCode.NULL_REQUEST.getMsg(),listErrNull);
    }

}