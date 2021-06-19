package net.thumbtack.school.hospital.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DoctorTest {

    @Test
    void TestTruCreateDoctor() {
        //Создадим клиента
        Doctor doctor = new Doctor("Nikolay","Ivanovich","Surgeon","niklayIvanovich@mail.ru","Nik228dgdgf");
        Doctor doctorTwo = new Doctor("Nikolay","Ivanovich","Surgeon","niklayIvanovich@mail.ru","Nik228dgdgf");



        Assertions.assertEquals("Nikolay", doctor.getFirstName());
        Assertions.assertEquals("Ivanovich", doctor.getLastName());
        Assertions.assertEquals("Surgeon", doctor.getSpeciality());
        Assertions.assertEquals("niklayIvanovich@mail.ru", doctor.getLogin());
        Assertions.assertEquals("Nik228dgdgf", doctor.getPassword());

        Assertions.assertEquals(doctor, doctorTwo);
    }
}