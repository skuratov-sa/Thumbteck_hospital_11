package net.thumbtack.school.hospital.response;

import java.util.List;

public class GetDrugListDtoResponse {
    private final List<DrugDto> list;

    public GetDrugListDtoResponse(List<DrugDto> list) {
        this.list = list;
    }

    public List<DrugDto> getList() {
        return list;
    }
}
