package net.thumbtack.school.hospital;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.Drug;
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
        Assert.assertNotNull(gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken());
        Assert.assertEquals("{}",server.stopServer("src/main/java/net/thumbtack/school/hospital/fileDB/file1.txt"));

        server.startServer("src/main/java/net/thumbtack/school/hospital/fileDB/file1.txt");
        LogInUserDtoRequest request = new LogInUserDtoRequest("tima@mail.ru","Sdfsf32334");
        String jsonRespLogIn = server.logInUser(gson.toJson(request));
        String tokenLogIn = gson.fromJson(jsonRespLogIn, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenLogIn);

    }


    @Test
    public void regDoctorServer() {
        server.clear();
        RegistrationDoctorDtoRequest request = new RegistrationDoctorDtoRequest("Shramov","Maksim","Therapist","shram@mail.ru","gsgdf4243DFvc");
        String jsonRequst = gson.toJson(request);
        String jsonResponse = server.regDoctorServer(jsonRequst);
        GetUserByTokenDtoRequest response = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class);
       Assert.assertNotNull(response.getToken());
    }

    @Test
    public void logInAndLogOutDoctorServer() {
        server.clear();
        //Регестрируем
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Victor","Malcev","Therapist","tima@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        Assert.assertNotNull(gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken());

        LogInUserDtoRequest requestOne = new LogInUserDtoRequest("","123");
        ErrorResponse responseFalseOne = gson.fromJson(server.logInUser(gson.toJson(requestOne)), ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN.getMsg(),responseFalseOne.getError());
        LogInUserDtoRequest request = new LogInUserDtoRequest("tima@mail.ru","Sdfsf32334");
        String jsonRespLogIn = server.logInUser(gson.toJson(request));
        String tokenLogIn = gson.fromJson(jsonRespLogIn, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenLogIn);

        String jsonRespLogOut = server.logOutUser(gson.toJson(new GetUserByTokenDtoRequest(tokenLogIn)));
        Assert.assertEquals("{}",jsonRespLogOut);
    }


    @Test
    public void updatePasswordDoctorServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Miheev","Pavel","Telepat","telepat@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String token = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
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
        String tokenDoc = gson.fromJson(respRegDoc, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //True request
        String reqRegPatOne = gson.toJson(new RegistrationPatientDtoRequest("Misha","Pavlin","Prostuda","pati20@mail.ru","123HJHbjh43",tokenDoc));
        String respRegPatOne = server.regPatientServer(reqRegPatOne);
        Assert.assertEquals("{}",respRegPatOne);

        //Error token
        String reqRegPatOTwo = gson.toJson(new RegistrationPatientDtoRequest("Nikita","Pavlin","Prostuda","pati21@mail.ru","123HJHbjh43",tokenDoc+"2"));
        String respRegPatTwo = server.regPatientServer(reqRegPatOTwo);
        ErrorResponse errorResponse = gson.fromJson(respRegPatTwo,ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(),AnswerErrorCode.TOKEN_ERROR.getMsg());

        //Test add patient about doctors
        List<PatientDto> list = gson.fromJson(server.gePatientFromDoctorServer(gson.toJson(new GetUserByTokenDtoRequest(tokenDoc))),GetListPatientResponse.class).getPatient();
        Assert.assertEquals(list.size(),1);
    }


    @Test
    public void delDoctorServer() {
        server.clear();
        //Register a doctors
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Alena","psychologist","psih1@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Poleshuk","Roman","psychologist","psih23@mail.ru","Wdsfsc232Df"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        String jsonRequstThree = gson.toJson(new RegistrationDoctorDtoRequest("Nikitin","Nikita","Terapevt","psih3@mail.ru","Sdfsf32334"));
        String jsonResponseTree = server.regDoctorServer(jsonRequstThree);
        String tokenDocThree = gson.fromJson(jsonResponseTree, GetUserByTokenDtoRequest.class).getToken();
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

        //delete doctorOne
        String requestDelTwo = gson.toJson(new DelDoctorDtoRequest(tokenDocOne));
        String responseDelTwo = server.delDoctorServer(requestDelTwo);
        Assert.assertEquals("{}",responseDelTwo);


        //Test add patients
        String jsonReq  = gson.toJson(new GetUserByTokenDtoRequest(tokenDocTwo));
        String jsonResp = server.gePatientFromDoctorServer(jsonReq);
        GetListPatientResponse list = gson.fromJson(jsonResp,GetListPatientResponse.class);
        Assert.assertEquals(2,list.getPatient().size());

        // delete doctorTwo
        String requestDelThree = gson.toJson(new DelDoctorDtoRequest(tokenDocTwo));
        String responseDelThree = server.delDoctorServer(requestDelThree);
        Assert.assertEquals("{}", responseDelThree);

        //Test add patients
        String jsonReqThree = gson.toJson(new GetUserByTokenDtoRequest(tokenDocThree));
        String jsonRespThree = server.gePatientFromDoctorServer(jsonReqThree);
        GetListPatientResponse listThree = gson.fromJson(jsonRespThree,GetListPatientResponse.class);
        Assert.assertEquals(3,listThree.getPatient().size());

        // delete doctorThree error
        String requestDelErr = gson.toJson(new DelDoctorDtoRequest(tokenDocThree));
        String responseDelErr = server.delDoctorServer(requestDelErr);
        ErrorResponse errorResponse = gson.fromJson(responseDelErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ERROR_DELETE_DOCTOR.getMsg(), errorResponse.getError());
    }

    @Test
    public void addAndDelAndGetDestinationFromDoctor() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Alena","psychologist","psih24@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Spid","pat25@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pat25@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne,GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull("{}", jsonResponseLogInPatientOne);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDocOne));
        String jsonResponseGetPatient = server.gePatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patients.size());

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

        //Checking destination for patients
        String reqGetDesti = gson.toJson( new GetUserByTokenDtoRequest(tokenPatientOne));
        String respGetDrsti = server.getPatientToDirections(reqGetDesti);
        List<String> list = gson.fromJson(respGetDrsti,GetDirectionsOnePatient.class).getDirections();
        Assert.assertEquals(list.get(0),"Sleep 8 hours");
        Assert.assertEquals(list.get(1),"Drink beer");

        //del destination
        String reqDel = gson.toJson(new DelDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),"Drink beer"));
        String respDel = server.delDestinationForPatient(reqDel);
        Assert.assertEquals("{}",respDel);

        //del error id
        String reqDelTwo = gson.toJson(new DelDestinationFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId() + 5,"Drink beer"));
        ErrorResponse errDel = gson.fromJson(server.delDestinationForPatient(reqDelTwo),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ID_ERROR.getMsg(),errDel.getError());

        //Checking destination for patients
        String reqGetDestiTwo = gson.toJson( new GetUserByTokenDtoRequest(tokenPatientOne));
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
        String tokenDocOne = gson.fromJson(jsonResponse, GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenDocOne);

        //Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Bunin","Misha","Spid","pat30@mail.ru","Sdfsf32334",tokenDocOne));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        //get id patient by doctors
        String jsonRequestGetPatient = gson.toJson(new GetListPatientDtoRequest(tokenDocOne));
        String jsonResponseGetPatient = server.gePatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patients.size());

        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pat30@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne,GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull("{}", jsonResponseLogInPatientOne);


        String reqNewDest = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(0).getId(),new Drug("Supradin")));
        String resp = server.addDrugFromDoctor(reqNewDest);
        Assert.assertEquals("{}",resp);

        String reqNewDestTwo = gson.toJson(new AddDrugDtoRequest(tokenDocOne,patients.get(0).getId(),new Drug("laxative")));
        String respTwo = server.addDrugFromDoctor(reqNewDestTwo);
        Assert.assertEquals("{}",respTwo);

        //Checking drug for patient
        String reqGetDrug = gson.toJson( new GetUserByTokenDtoRequest(tokenPatientOne));
        String respGetDrug = server.getDrugForPatient(reqGetDrug);
        List<net.thumbtack.school.hospital.response.DrugDto> list = gson.fromJson(respGetDrug, GetDrugListDtoResponse.class).getList();
        Assert.assertEquals(list.get(0).getName(),"Supradin");
        Assert.assertEquals(list.get(1).getName(),"laxative");

        //del drug patient
        String reqDel = gson.toJson(new DelDrugFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),new Drug("Supradin")));
        String respDel = server.delDrugForPatient(reqDel);
        Assert.assertEquals("{}",respDel);

        //del error drug
        String reqDelErr = gson.toJson(new DelDrugFromDoctorDtoRequest(tokenDocOne,patients.get(0).getId(),new Drug("Supradins")));
        String respDelErr = server.delDrugForPatient(reqDelErr);
        ErrorResponse response = gson.fromJson(respDelErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ERROR_DRUG.getMsg(),response.getError());

        //Checking drug for patient
        String reqGetDrugTwo = gson.toJson( new GetUserByTokenDtoRequest(tokenPatientOne));
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
        String tokenDocOne = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
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
    }

    @Test
    public void getPatientWithDirectionsServerFromDoctorServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc73@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);


        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc74@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetUserByTokenDtoRequest.class).getToken();
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
        String jsonResponseGetPatient = server.gePatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patientsDocOne = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(1,patientsDocOne.size());

        String jsonRequestGetPatientTwo = gson.toJson(new GetListPatientDtoRequest(tokenDocTwo));
        String jsonResponseGetPatientTwo = server.gePatientFromDoctorServer(jsonRequestGetPatientTwo);
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
    }

    @Test
    public void delPatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Vays","Billi","psychologist","psih38@mail.ru","Ddfghk3535"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDocOne = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
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
        String jsonResponseGetPatient = server.gePatientFromDoctorServer(jsonRequestGetPatient);
        List<PatientDto> patients = gson.fromJson(jsonResponseGetPatient,GetListPatientResponse.class).getPatient();
        Assert.assertEquals(2,patients.size());


        //Delete patient
        String reqDelPati = gson.toJson(new DelPatientDtoRequest(tokenDocOne,patients.get(0).getId()));
        String respDelPati = server.delPatientServer(reqDelPati);
        Assert.assertEquals("{}",respDelPati);

        //Delete patient err id
        String reqDelPatiErr = gson.toJson(new DelPatientDtoRequest(tokenDocOne,patients.get(1).getId() + 10));
        String respDelPatiErr = server.delPatientServer(reqDelPatiErr);
        ErrorResponse errorResponse = gson.fromJson(respDelPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.ID_ERROR.getMsg(),errorResponse.getError());

    }

    @Test
    public void getDoctorToPatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","dent@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);

    //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati11@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Ptushkin","Nurlan","Spid","pati12@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        String req = server.gePatientFromDoctorServer(gson.toJson(new GetUserByTokenDtoRequest(tokenDoc)));
        GetListPatientResponse resp = gson.fromJson(req,GetListPatientResponse.class);
        Assert.assertEquals(2,resp.getPatient().size());
    }

    @Test
    public void doctorGetAllPatientAllDoctors() {
      server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc70@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);

        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc71@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Covid","pati40@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);

        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati41@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        //get all patients and doctors
       String reqGetAll =  gson.toJson(new GetUserByTokenDtoRequest(tokenDoc));
       String respGetAll = server.doctorGetAllPatientAllDoctors(reqGetAll);
       List<PatientInfoDtoRequest> list = gson.fromJson(respGetAll,DoctorGetAllPatientAllDoctorsResponse.class).getPatientInfo();
       Assert.assertEquals(2,list.size());
       Assert.assertEquals(list.get(0).getPatient().getLogin(),"pati40@mail.ru");
       Assert.assertEquals(list.get(0).getFirstNameDoctor(),"Petrushkin");

        Assert.assertEquals(list.get(1).getPatient().getLogin(),"pati41@mail.ru");
        Assert.assertEquals(list.get(1).getFirstNameDoctor(),"Bims");

        String reqGetAllErr =  gson.toJson(new GetUserByTokenDtoRequest(tokenDoc + "1"));
        String respGetAllErr = server.doctorGetAllPatientAllDoctors(reqGetAllErr);
        ErrorResponse errorResponse = gson.fromJson(respGetAllErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());
    }


    @Test
    public void logInAndOutAndUpdatePatientServer() {
        server.clear();
        //Register a doctor
        String jsonRequest = gson.toJson(new RegistrationDoctorDtoRequest("Petunin","Lesha","Dentist","dent1@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequest);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //    Register a patient
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Killer","Mamin","Cold","pati31@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);



        //Patient get Drug error logIn
        String errResp  = server.getDrugForPatient(gson.toJson(new GetUserByTokenDtoRequest("3453453-34534gdgdg-365456745gdgrt")));
        ErrorResponse response = gson.fromJson(errResp,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),response.getError());

        //LogIn error
        String reqLogInErr = gson.toJson(new LogInUserDtoRequest("pati3221@mail.ru","Sdfsf32334"));
        ErrorResponse respLogInErr = gson.fromJson(server.logInUser(reqLogInErr),ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.LOGIN_ERROR.getMsg(),respLogInErr.getError());

        //LogIn
        String reqLogIn = gson.toJson(new LogInUserDtoRequest("pati31@mail.ru","Sdfsf32334"));
        GetUserByTokenDtoRequest respLogIn = gson.fromJson(server.logInUser(reqLogIn), GetUserByTokenDtoRequest.class);
        String newTokenPatientOne = respLogIn.getToken();
        Assert.assertNotNull(respLogIn.getToken());


        //Update patient err pass
        String reqUpPassErr = gson.toJson(new UpdatePassUserDtoRequest(tokenDoc,"123"));
        String respUpPassErr = server.updatePasswordUserServer(reqUpPassErr);
        ErrorResponse errUpPass = gson.fromJson(respUpPassErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD.getMsg(),errUpPass.getError());

        //update password patient
        String reqUpPass = gson.toJson(new UpdatePassUserDtoRequest(newTokenPatientOne,"123KHJfhkjrwkjr"));
        String respUpPass = server.updatePasswordUserServer(reqUpPass);
        Assert.assertEquals("{}",respUpPass);

        //LogOut
        String logOut = gson.toJson(new GetUserByTokenDtoRequest(newTokenPatientOne));
        String respLogOutTwo = server.logOutUser(logOut);
        Assert.assertEquals("{}",respLogOutTwo);

        //LogIn new Pass
        String reqLogInNewPass = gson.toJson(new LogInUserDtoRequest("pati31@mail.ru","123KHJfhkjrwkjr"));
        GetUserByTokenDtoRequest respLogInNewPass = gson.fromJson(server.logInUser(reqLogInNewPass), GetUserByTokenDtoRequest.class);
        String newTokenPatientOneNewPass = respLogInNewPass.getToken();
        Assert.assertNotNull(newTokenPatientOneNewPass);

    }

    @Test
    public void getPatientToDoctorsServer() {
        server.clear();
        //Register a doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc76@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);

        //Register a doctor
        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc75@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetUserByTokenDtoRequest.class).getToken();
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
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne,GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientOne);

        //Get doc info
        String reqGetAllDoc = gson.toJson(new GetUserByTokenDtoRequest(tokenPatientOne));
        String respGetAllDoc = server.getDoctorInfoFromPatient(reqGetAllDoc);
        GetDoctorInfoFromPatientResponse doctorInfo = gson.fromJson(respGetAllDoc, GetDoctorInfoFromPatientResponse.class);
        Assert.assertEquals("Petrushkin",doctorInfo.getFirstName());
        Assert.assertEquals("Petya",doctorInfo.getLastName());
        Assert.assertEquals("Dentist",doctorInfo.getSpeciality());

        //LogIn patiTwo
        String jsonRequestLogInPatientTwo = gson.toJson(new LogInUserDtoRequest("pati47@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientTwo = server.logInUser(jsonRequestLogInPatientTwo);
        String tokenPatientTwo = gson.fromJson(jsonResponseLogInPatientTwo,GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientTwo);

        //Get doc info err
        String reqGetAllDocErr = gson.toJson(new GetUserByTokenDtoRequest(tokenPatientTwo + "1"));
        String respGetAllDocErr = server.getDoctorInfoFromPatient(reqGetAllDocErr);
        ErrorResponse errorResponse = gson.fromJson(respGetAllDocErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());
    }

    @Test
    public void doctorGetAllPatientsWithDisease() {
        server.clear();
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc80@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDoc);


        String jsonRequstTwo = gson.toJson(new RegistrationDoctorDtoRequest("Bims","Petya","Dentist","doc81@mail.ru","Sdfsf32334"));
        String jsonResponseTwo = server.regDoctorServer(jsonRequstTwo);
        String tokenDocTwo = gson.fromJson(jsonResponseTwo, GetUserByTokenDtoRequest.class).getToken();
        Assert.assertNotNull(tokenDocTwo);

        //    Register a patients
        String jsonRequestPatient = gson.toJson(new RegistrationPatientDtoRequest("Mironov","Mitya","Spid","pati82@mail.ru","Sdfsf32334",tokenDoc));
        String jsonResponsePatientOne = server.regPatientServer(jsonRequestPatient);
        Assert.assertEquals("{}",jsonResponsePatientOne);


        String jsonRequestPatientTwo = gson.toJson(new RegistrationPatientDtoRequest("Yoda","Nurlan","Spid","pati83@mail.ru","Sdfsf32334",tokenDocTwo));
        String jsonResponsePatientTwo = server.regPatientServer(jsonRequestPatientTwo);
        Assert.assertEquals("{}",jsonResponsePatientTwo);

        //get all
        String reqGetPati = gson.toJson(new DoctorGetAllPatientsWithDiseaseDtoRequest(tokenDoc,"Spid"));
        String respGetPati = server.doctorGetAllPatientsWithDisease(reqGetPati);
        List<PatientInfoDtoRequest> list =  gson.fromJson(respGetPati,DoctorGetAllPatientAllDoctorsResponse.class).getPatientInfo();
        Assert.assertEquals(2,list.size());

        Assert.assertEquals("Petrushkin",list.get(0).getFirstNameDoctor());
        Assert.assertEquals("Petya",list.get(0).getLastNameDoctor());

        Assert.assertEquals("Bims",list.get(1).getFirstNameDoctor());
        Assert.assertEquals("Petya",list.get(1).getLastNameDoctor());

        //get all error Token doc
        String reqGetPatiErr = gson.toJson(new DoctorGetAllPatientsWithDiseaseDtoRequest(tokenDoc + "1","Spid"));
        String respGetPatiErr = server.doctorGetAllPatientsWithDisease(reqGetPatiErr);
        ErrorResponse errorResponse = gson.fromJson(respGetPatiErr,ErrorResponse.class);
        Assert.assertEquals(AnswerErrorCode.TOKEN_ERROR.getMsg(),errorResponse.getError());
    }
    @Test
    public void getInfoByToken(){
        server.clear();
        //Registration doctor
        String jsonRequst = gson.toJson(new RegistrationDoctorDtoRequest("Petrushkin","Petya","Dentist","doc80@mail.ru","Sdfsf32334"));
        String jsonResponse = server.regDoctorServer(jsonRequst);
        String tokenDoc = gson.fromJson(jsonResponse, GetUserByTokenDtoRequest.class).getToken();
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

        //LogIn patient
        String jsonRequestLogInPatient = gson.toJson(new LogInUserDtoRequest("pati82@mail.ru","Sdfsf32334"));
        String jsonResponseLogInPatientOne = server.logInUser(jsonRequestLogInPatient);
        String tokenPatientOne = gson.fromJson(jsonResponseLogInPatientOne,GetTokenDtoResponse.class).getToken();
        Assert.assertNotNull(tokenPatientOne);

        //Get info Patient
        String reqGetPati = gson.toJson(new GetInfoUserDtoRequest(tokenPatientOne));
        String respGetInfoPati = server.getInfoPatientByToken(reqGetPati);
        PatientDto patientDto = gson.fromJson(respGetInfoPati,PatientDto.class);

        Assert.assertEquals("Mironov",patientDto.getFirstName());
        Assert.assertEquals("Mitya",patientDto.getLastName());
        Assert.assertEquals("Spid",patientDto.getDiseaseName());
        Assert.assertEquals("pati82@mail.ru",patientDto.getLogin());

    }

}