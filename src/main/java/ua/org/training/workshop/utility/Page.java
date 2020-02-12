package ua.org.training.workshop.utility;

import java.util.List;
import java.util.Optional;

/**
 * @author kissik
 */
public class Page<T> {
    private Long pageNumber;
    private Long size;
    private String sorting;
    private String search;
    private Long totalElements;
    private String language;
    private Optional<List<T>> content;

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public long getOffset() {
        return size * pageNumber;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Optional<List<T>> getContent() {
        return content;
    }

    public void setContent(Optional<List<T>> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "page: " + pageNumber + ", size: " + size + ", search : " +
                search + ", sorting by " + sorting;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
