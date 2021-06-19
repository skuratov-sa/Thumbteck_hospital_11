package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.db.DataBase;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Drug;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.exeptions.ServerException;

import java.util.List;

public class PatientDaoImpl implements PatientDao {

    public Doctor getDoctorInfoFromPatient(Patient patient) throws ServerException {
        return DataBase.getInstance().getDoctorInfoFromPatient(patient);
    }
    @Override
    public List<String> getDirections(Patient token) {
        return DataBase.getInstance().getPatientToDirections(token);
    }

    @Override
    public List<Drug> getDrug(Patient patient) {
        return DataBase.getInstance().getDrugForPatient(patient);
    }

    @Override
    public User getInfo(String token) throws ServerException {
        return DataBase.getInstance().getByTokenUser(token);
    }

    @Override
    public User getByLoginPatient(int id) throws ServerException {
        return DataBase.getInstance().getByIdUser(id);
    }

    @Override
    public User getByTokenPatient(String token) throws ServerException {
        return DataBase.getInstance().getByTokenUser(token);
    }

}
