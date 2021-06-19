package net.thumbtack.school.hospital.response;
import java.util.List;

public class DoctorGetAllPatientAllDoctorsResponse {
    private final List<PatientInfoDtoRequest> patientInfo;

    public DoctorGetAllPatientAllDoctorsResponse(List<PatientInfoDtoRequest> patientInfo) {
        this.patientInfo = patientInfo;
    }

    public List<PatientInfoDtoRequest> getPatientInfo() {
        return patientInfo;
    }
}
