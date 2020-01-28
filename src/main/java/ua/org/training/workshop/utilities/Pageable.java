package ua.org.training.workshop.utilities;

/**
 * @author kissik
 */
public class Pageable {
    private Long page;
    private Long size;
    private String sorting;
    private String search;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
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
        return size*page;
    }

    @Override
    public String toString(){
        return "page: " + page + ", size: " + size + ", search : " +
                search + ", sorting by " + sorting;
    }
}
