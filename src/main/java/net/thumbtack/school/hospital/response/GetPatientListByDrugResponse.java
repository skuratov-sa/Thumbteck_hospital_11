package net.thumbtack.school.hospital.response;

import java.util.List;

public class GetPatientListByDrugResponse {
    private final List<PatientDto> dtoList;

    public GetPatientListByDrugResponse(List<PatientDto> dtoList) {
        this.dtoList = dtoList;
    }

    public List<PatientDto> getDtoList() {
        return dtoList;
    }

}
