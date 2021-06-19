//package net.thumbtack.school.hospital;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import net.thumbtack.school.hospital.request.RegistrationDoctorDtoRequest;
//import net.thumbtack.school.hospital.model.Doctor;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.IOException;
//
//public class TestServer {
//    private Gson gson;
//    private String fileName = "fileDB.txt";
//    @Test
//    public void TestStartServer() throws IOException{
//    Server server = new Server();
//        //Проверка на создание нового файла для базы данных
//        try{
//            new File("/Users/graf_stanislav/Desktop/Thumbtack/src/main/java/net/thumbtack/school/hospital/fileDB/" + fileName).delete();
//            server.startServer(fileName);
//            Assert.fail();
//        }catch (ServException ex){
//            //Тест на вывод ошибки создания файла
//            Assert.assertEquals(ex,ServerErrorCode.TRAINING_FILE_NOT_EXEPTION);
//        }
//
//        //Проверка на то что файл создан после неудачной попытки обратиться к нему
//        Assert.assertFalse(new File("/Users/graf_stanislav/Desktop/Thumbtack/src/main/java/net/thumbtack/school/hospital/fileDB/" + fileName).createNewFile());
//    }
//    @Test
//    public void TestInputFileInDataBase() throws IOException, ServException {
//        gson = new Gson();
//         Server server = new Server();
//         server.startServer(fileName);
//
//         //Отправляю запрос на регестрацию через класс сервер
//        JsonObject doctorOneStr = (JsonObject)new JsonParser().parse("{firstName:Nastya, lastName:Ivanovna, speciality:Therapist, login:nastyaIvanova@mail.ru,password :Nastya224}");
//        //Проверка на заполнение полей
//        Assert.assertTrue(server.registrationsNewDoctorServer(gson.fromJson(doctorOneStr, RegistrationDoctorDtoRequest.class)).validate());
//        server.stopServer(fileName);
//    }
//
//    @Test
//    public void TestReturnAllDoctors() throws ServException {
//        gson = new Gson();
//        Server server = new Server();
//        server.startServer(fileName);
//        //Очистить базу данных при запуске
//        server.getDb().deleteAll();
//        //Создадим три запроса json
//        JsonObject doctorOne = (JsonObject)new JsonParser().parse("{firstName:Nastya, lastName:Ivanovna, speciality:Therapist, login:nastyaIvanova@mail.ru,password :Nastya224}");
//        JsonObject doctorTwo = (JsonObject)new JsonParser().parse("{firstName:Petya, lastName:Mushkin, speciality:psiholog, login:mushIn@mail.ru,password :Vasili22323y}");
//        JsonObject doctorThree = (JsonObject)new JsonParser().parse("{firstName:Vasiliy, lastName:Petrov, speciality:Stomatolog, login:vasiliy@mail.ru,password :Petretr345345}");
//
//        //gпроверить на правильноть ввода 3 докторов
//        Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorOne,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();
//        Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorTwo,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorThree,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();
//
//
//        //Вытащить полученные экземпляры докторов значения через функцию getAllDoctors()
//        Doctor[] doc = server.getMyRequestClass().getDoctorDao().getAllDoctors().toArray(new Doctor[0]);
//
//        //проверка на вводимых докторов и добавленных
//        Assert.assertEquals(doc[0],gson.fromJson(doctorOne,Doctor.class));
//        Assert.assertEquals(doc[1],gson.fromJson(doctorTwo,Doctor.class));
//        Assert.assertEquals(doc[2],gson.fromJson(doctorThree,Doctor.class));
//
//        Assert.assertTrue(server.stopServer(fileName));
//        //Проверка созранненных данных первого сервера
//        Server server1 = new Server();
//        server1.startServer(fileName);
//        Doctor[] doctors = server1.getDb().getAllDoctors().toArray(new Doctor[0]);
//        Assert.assertEquals(3, doctors.length);
//        server1.stopServer(fileName);
//
//    }
//    @Test
//    public void TestLogInDoctor() throws ServException {
//        gson = new Gson();
//        Server<RegistrationDoctorDtoRequest> server = new Server();
//        server.startServer(fileName);
//        //Очистить базу данных при запуске
//        server.getDb().deleteAll();
//        //Создадим три запроса json
//        JsonObject doctorOne = (JsonObject)new JsonParser().parse("{firstName:Nastya, lastName:Ivanovna, speciality:Therapist, login:nastyaIvanova@mail.ru,password :Nastya224}");
//        JsonObject doctorTwo = (JsonObject)new JsonParser().parse("{firstName:Petya, lastName:Mushkin, speciality:psiholog, login:mushIn@mail.ru,password :Vasili22323y}");
//        JsonObject doctorThree = (JsonObject)new JsonParser().parse("{firstName:Vasiliy, lastName:Petrov, speciality:Stomatolog, login:vasiliy@mail.ru,password :Petretr345345}");
//
//        //gпроверить на правильноть ввода 3 докторов
//        Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorOne,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();
//        Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorTwo,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();
//        Assert.assertTrue(server.setMyRequestClass(gson.fromJson(doctorThree,RegistrationDoctorDtoRequest.class)).validate());
//        server.getMyRequestClass().insertNewDoctor();
//        server.stopServer(fileName);
//
//        Server<LogInDoctorDtoRequest> server1 = new Server<LogInDoctorDtoRequest>();
//        server1.startServer(fileName);
//        Assert.assertEquals("Error",server1.getMyRequestClass().whathInfoOnDoc());
//        Assert.assertTrue(server1.getMyRequestClass().validate("nastyaIvanova@mail.ru","Nastya224"));
//        Assert.assertNotNull(server1.getMyRequestClass().whathInfoOnDoc());
//        Assert.assertTrue(server1.getMyRequestClass().validate("mushIn@mail.ru","Vasili22323y"));
//        Assert.assertNotNull(server1.getMyRequestClass().whathInfoOnDoc());
//
//    }
//
//}