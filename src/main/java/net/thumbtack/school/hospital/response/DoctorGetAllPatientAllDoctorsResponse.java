package net.thumbtack.school.hospital.response;
import java.util.List;

public class DoctorGetAllPatientAllDoctorsResponse {
    private final List<PatientDto> patientInfo;

    public DoctorGetAllPatientAllDoctorsResponse(List<PatientDto> patientInfo) {
        this.patientInfo = patientInfo;
    }

    public List<PatientDto> getPatientInfo() {
        return patientInfo;
    }
}
