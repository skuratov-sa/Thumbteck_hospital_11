package net.thumbtack.school.hospital.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PatientTest  {

    @Test
    void TestTruCreatePatient() {
        //Создадим клиента
        Patient patient = new Patient("Nikolay","Ivanovich","Covid","niklayIvanovich@mail.ru","Nik228dgdgf");
        Patient newPatientTwo = new Patient("Nikolay","Ivanovich","Covid","niklayIvanovich@mail.ru","Nik228dgdgf");


        Assertions.assertEquals("Nikolay", patient.getFirstName());
        Assertions.assertEquals("Ivanovich", patient.getLastName());
        Assertions.assertEquals("Covid", patient.getDiseaseName());
        Assertions.assertEquals("niklayIvanovich@mail.ru", patient.getLogin());
        Assertions.assertEquals("Nik228dgdgf", patient.getPassword());

        Assertions.assertEquals(patient, newPatientTwo);
    }
}