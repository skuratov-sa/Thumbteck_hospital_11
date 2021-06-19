package net.thumbtack.school.hospital.response;

import java.util.List;

public class GetDrugListDtoResponse {
    private List<DrugDto> list;

    public GetDrugListDtoResponse(List<DrugDto> list) {
        this.list = list;
    }

    public List<DrugDto> getList() {
        return list;
    }

    public void setList(List<DrugDto> list) {
        this.list = list;
    }
}
