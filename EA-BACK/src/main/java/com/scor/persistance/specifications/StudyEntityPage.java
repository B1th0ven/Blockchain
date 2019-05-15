package com.scor.persistance.specifications;

import java.io.Serializable;
import java.util.List;

import com.scor.persistance.entities.StudyEntity;

public class StudyEntityPage implements Serializable {

    List<StudyEntity> content;
    long totalPages;

    public StudyEntityPage(List<StudyEntity> content, long totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }

    public List<StudyEntity> getContent() {
        return content;
    }

    public void setContent(List<StudyEntity> content) {
        this.content = content;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
