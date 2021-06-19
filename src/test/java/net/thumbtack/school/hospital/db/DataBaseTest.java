package net.thumbtack.school.hospital.db;

import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class DataBaseTest {
    DataBase db = DataBase.getInstance();

    @Test
    public void registerDoctor() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","vaysV@mail.ru","123fsdfsdfgdfgdg");
        String token;
        try {
            token = db.registerDoctor(doctor);
           Assert.assertNotNull(token);
            Assert.assertEquals(doctor,db.getByTokenUser(token));
            Assert.assertEquals(doctor,db.getByIdUser(doctor.getId()));
        } catch (ServerException serverException) {
            fail();
        }
        try {
            db.registerDoctor(doctor);
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.REGISTRATION_THIS_USER_HAS_ALREADY_REGISTERED.getMsg());
        }

    }

    @Test
    public void logInUser() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","vvaysV@mail.ru","123fsdfsdfgdfgdg");
        Doctor doctorFalse = new Doctor("Vasily","Vays","Surgeon","vaysggV@mail.ru","1123fsdfsdfgdfgdg");
        try {
            String token = db.registerDoctor(doctor);
            db.logOutUser(doctor);
            String newToken = db.logInUser(doctor.getLogin(),doctor.getPassword());
            Assert.assertNotNull(newToken);
            Assert.assertNotEquals(token,newToken);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.logInUser(doctorFalse.getLogin(),doctorFalse.getPassword());
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.LOGIN_ERROR.getMsg());
        }
    }

    @Test
    public void updateDoctorPassword() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","RvaysV@mail.ru","123fsdfsdfgdfgdg");

        //Регестрируем
        try {
            String token = db.registerDoctor(doctor);
            Assert.assertNotNull(token);
            db.logOutUser(doctor);
        } catch (ServerException serverException) {
            fail();
        }

        //Ошибка входа
        try {
            db.updatePasswordUser(doctor,"sdsdfs78s8df7s");
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.TOKEN_ERROR.getMsg());
        }
        //Обнавляем
        try {
            db.logInUser(doctor.getLogin(),doctor.getPassword());
            db.updatePasswordUser(doctor,"sdsdfs78s8df7s");
            db.logOutUser(doctor);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.logInUser(doctor.getLogin(),"sdsdfs78s8df7s");
        } catch (ServerException serverException) {
            fail();
        }

    }

    @Test
    public void deleteDoctor() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","vvafsV@mail.ru","1233fsdfsdfgdfgdg");
        try {
            db.deleteDoctor(doctor);
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.ID_ERROR.getMsg());
        }
        try {
            db.registerDoctor(doctor);
           Assert.assertEquals(db.getByIdUser(doctor.getId()),doctor);
            db.deleteDoctor(doctor);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.getByIdUser(doctor.getId());
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.ID_ERROR.getMsg());
        }
    }

    @Test
    public void registerAndLogInAndLogOutPatient() {
        db.clear();
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","niiklayIvanovich@mail.ru","Nik228dgdgf");
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","vvabV@mail.ru","1233fsdfsdfgdfgdg");

        try {
            db.registerDoctor(doctor);
            db.logOutUser(doctor);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.registerPatient(doctor,patient);
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.TOKEN_ERROR.getMsg());
        }
        try {
            db.logInUser(doctor.getLogin(),doctor.getPassword());
            db.registerPatient(doctor,patient);
            db.registerPatient(doctor,patient);
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.REGISTRATION_THIS_USER_HAS_ALREADY_REGISTERED.getMsg());
        }
        try {
            Assert.assertEquals(db.getByIdUser(patient.getId()),patient);
        } catch (ServerException serverException) {
            fail();
        }

    }

    @Test
    public void deletePatient() {
        db.clear();
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","niklayIvanovich@mail.ru","Nik228dgdgf");
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","VaysAl@mail.ru","1233fsdfsdfgdfgdg");

        //Регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
            db.logOutUser(doctor);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.logInUser(doctor.getLogin(),doctor.getPassword());
            db.deletePatient(doctor,patient);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            db.getByIdUser(patient.getId());
        } catch (ServerException serverException) {
            Assert.assertEquals(serverException.getErrorMessage(),AnswerErrorCode.ID_ERROR.getMsg());
        }

    }

    @Test
    public void logOutPatient() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","ttt@mail.ru","1233fsdfsdfgdfgdg");
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","niggg@mail.ru","Nik228dgdgf");
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
            db.logOutUser(patient);
            String token = db.logInUser(patient.getLogin(),patient.getPassword());
            db.logOutUser(patient);
            String newToken = db.logInUser(patient.getLogin(),patient.getPassword());
            Assert.assertNotEquals(token,newToken);
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void addDirectionsFromDoctor() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","pavelDurov@mail.ru","1233fsdfsdfgdfgdg");
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","noizeMc@mail.ru","Nik228dgdgf");

        //Регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
            db.addDestinationFromDoctor(patient,"Sleep 8 hours a day");
        } catch (ServerException serverException) {
            fail();
        }


        try {
          Patient patientTwo = (Patient) db.getByIdUser(patient.getId());
          Assert.assertEquals(1,patientTwo.getDirectionsList().size());
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void addDrugPatinet() {
        db.clear();
        Doctor doctor = new Doctor("Vasily", "Vays", "Surgeon", "vcxdgh@mail.ru", "1233fsdfsdfgdfgdg");
        Patient patient = new Patient("Nikolay", "Ivanovich", "Covid", "man@mail.ru", "Nik228dgdgf");
        Drug drug = new Drug("laxative");
        //Регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor, patient);
            db.addDrugPatient(patient, drug);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            Patient patientTwo = (Patient) db.getByIdUser(patient.getId());
            Assert.assertEquals(1, db.getDrugForPatient(patientTwo).size());
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void updatePasswordPatient() {
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","jjjjj@mainl.ru","123fsdfsdfgdfgdg");
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","bigBuster@mail.ru","Nik228dgdgf");

        //Регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
        } catch (ServerException serverException) {
            fail();
        }

        //Обнавляем
        try {
            db.logInUser(patient.getLogin(),patient.getPassword());
            db.updatePasswordUser(patient,"123");
        } catch (ServerException serverException) {
            fail();
        }
        try {
            db.logInUser(patient.getLogin(),"123");
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void getDoctorToPatient() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","PigPepa@mainl.ru","123fsdfsdfgdfgdg");
        Patient patientOne = new Patient("Nikolay","Ivanovich","Covid","vv@mail.ru","Nik228dgdgf");
        Patient patientTwo = new Patient("Nikolay","Ivanovich","Covid","ssasd@mail.ru","Nik228dgdgf");
        Patient patientThree = new Patient("Nikolay","Ivanovich","Covid","fsdfgasd@mail.ru","Nik228dgdgf");

        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patientOne);
            db.registerPatient(doctor,patientTwo);
            db.registerPatient(doctor,patientThree);
        } catch (ServerException serverException) {
            fail();
        }
        Assert.assertEquals(3,db.getListPatientsByDoctor(doctor).size());
    }

    @Test
    public void getDoctorToPatientWithDisease() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","jjjjj@mail.ru","123fsdfsdfgdfgdg");
        Patient patientOne = new Patient("Nikolay","Ivanovich","Covid","patient1@mail.ru","Nik228dgdgf");
        Patient patientTwo = new Patient("Nikolay","Ivanovich","Spid","patient2@mail.ru","Nik228dgdgf");
        Patient patientThree = new Patient("Nikolay","Ivanovich","Covid","patient3@mail.ru","Nik228dgdgf");

        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patientOne);
            db.registerPatient(doctor,patientTwo);
            db.registerPatient(doctor,patientThree);
        } catch (ServerException serverException) {
            fail();
        }
        try {
            Assert.assertEquals(2,db.getPatientWithDiseaseByDoctor(doctor,"Covid").size());
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void getDoctorToPatientWithDirections() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","doctorHho@mainl.ru","123fsdfsdfgdfgdg");
        Patient patientOne = new Patient("Nikolay","Ivanovich","Covid","patient4@mail.ru","Nik228dgdgf");
        Patient patientTwo = new Patient("Nikolay","Ivanovich","Spid","patient5@mail.ru","Nik228dgdgf");
        Patient patientThree = new Patient("Nikolay","Ivanovich","Covid","patient6@mail.ru","Nik228dgdgf");

        //регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patientOne);
            db.registerPatient(doctor,patientTwo);
            db.registerPatient(doctor,patientThree);
        } catch (ServerException serverException) {
            fail();
        }

        //делаем направления
        try {
            db.addDestinationFromDoctor(patientOne,"drink beer");
            db.addDestinationFromDoctor(patientTwo,"sleep");
            db.addDestinationFromDoctor(patientThree,"drink beer");
        } catch (ServerException serverException) {
            serverException.printStackTrace();
        }

        try {
            Assert.assertEquals(2,db.getPatientWithDirectionsByDoctor(doctor,"drink beer").size());
        } catch (ServerException serverException) {
            fail();
        }
    }

    @Test
    public void doctorGetAllPatientAllDoctors() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","doc@mainl.ru","123fsdfsdfgdfgdg");
        Doctor doctorTwo = new Doctor("Pavlov","Ilya","Dermatolog","derm@mainl.ru","33334fsdf");

        Patient patientOne = new Patient("Nikolay","Ivanovich","Covid","patient7@mail.ru","Nik228dgdgf");
        Patient patientTwo = new Patient("Nikolay","Ivanovich","Spid","patient8@mail.ru","Nik228dgdgf");
        Patient patientThree = new Patient("Nikolay","Ivanovich","Covid","patient9@mail.ru","Nik228dgdgf");

        //регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerDoctor(doctorTwo);
            db.registerPatient(doctor,patientOne);
            db.registerPatient(doctorTwo,patientTwo);
            db.registerPatient(doctor,patientThree);
        } catch (ServerException serverException) {
            fail();
        }

        try {
            ArrayList<Doctor> map = db.getAllPatientAllDoctors();
            Assert.assertEquals(doctor.getLogin(),map.get(0).getLogin());
            Assert.assertEquals(2,map.get(0).getPatients().size());

            Assert.assertEquals(doctorTwo.getLogin(),map.get(1).getLogin());
            Assert.assertEquals(2,map.get(0).getPatients().size());
        } catch (ServerException serverException) {
            fail();
        }
    }


    @Test
    public void getPatientToDoctorAndDirections() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","docooo@mainl.ru","123fsdfsdfgdfgdg");
        Patient patient = new Patient("Pahsa","Ivanovich","Spid","patient12@mail.ru","Nik228dgdgf");
        //регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
        } catch (ServerException serverException) {
            fail();
        }
        //делаем направления
        try {
            db.addDestinationFromDoctor(patient,"drink beer");
            db.addDestinationFromDoctor(patient,"sleep");
            db.addDestinationFromDoctor(patient,"drink beer");
        } catch (ServerException serverException) {
            fail();
        }

        Doctor doctor1 = null;
        try {
            doctor1 =  db.getDoctorInfoFromPatient(patient);
        } catch (ServerException serverException) {
            fail();
        }
        Assert.assertEquals(doctor1,doctor);
    }

    @Test
    public void getPatientToDirections() {
        db.clear();
        Doctor doctor = new Doctor("Vasily","Vays","Surgeon","vvvasdasd@mainl.ru","123fsdfsdfgdfgdg");
        Patient patient = new Patient("Pahsa","Ivanovich","Spid","patient13@mail.ru","Nik228dgdgf");
        //регестрируем
        try {
            db.registerDoctor(doctor);
            db.registerPatient(doctor,patient);
        } catch (ServerException serverException) {
            fail();
        }
        //делаем направления
        try {
            db.addDestinationFromDoctor(patient,"drink beer");
            db.addDestinationFromDoctor(patient,"sleep");
            db.addDestinationFromDoctor(patient,"drink beer");
        } catch (ServerException serverException) {
            serverException.printStackTrace();
        }
        Assert.assertEquals(3,db.getPatientToDirections(patient).size());
        db.delDestinationFromDoctor(patient,"sleep");
        Assert.assertEquals(2,db.getPatientToDirections(patient).size());
    }

}