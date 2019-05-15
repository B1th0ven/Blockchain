package com.scor.persistance.filters;

import java.io.Serializable;
import java.util.List;

public class PageBase <T>  implements Serializable {

    List<T> content;
    long totalPages;

    public PageBase(List<T> content, long totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
