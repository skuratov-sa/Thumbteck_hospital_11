package net.thumbtack.school.hospital.response;
import java.util.List;

public class GetListPatientResponse {
    private final List<PatientDto> patient;

    public GetListPatientResponse(List<PatientDto> patientList) {
        patient  = patientList;
    }
    public List<PatientDto> getPatient() {
        return patient;
    }
}
