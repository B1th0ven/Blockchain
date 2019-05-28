package com.scor.TableUpdateReport.model;

import com.scor.dataProcessing.models.NotExecutedDto;

public class NotExecutedList {

    NotExecutedDto[] notExecutedDtos;

    public NotExecutedDto[] getNotExecutedDtos() {
        return notExecutedDtos;
    }

    public NotExecutedList() {
    }

    public NotExecutedList(NotExecutedDto[] notExecutedDtos) {
        this.notExecutedDtos = notExecutedDtos;
    }

    public void setNotExecutedDtos(NotExecutedDto[] notExecutedDtos) {
        this.notExecutedDtos = notExecutedDtos;
    }
}
