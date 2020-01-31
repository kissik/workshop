package ua.org.training.workshop.utilities;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author kissik
 */
public class Pageable {
    private Long pageNumber;
    private Long size;
    private String sorting;
    private String search;
    private Long totalElements;
    private Object content;

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
        return size*pageNumber;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getPage() {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = "";

        try{
            jsonString = jsonMapper.writeValueAsString(makeMap());
        } catch (IOException ignored) {

        }

        return jsonString;
    }

    private Map<String, Object> makeMap() {

        Map<String, Object> mappedObject = new HashMap<>();

        mappedObject.put("content", ((Optional)content).orElse(
                UtilitiesClass.APP_STRING_DEFAULT_VALUE
        ));
        mappedObject.put("size", size);
        mappedObject.put("totalElements", totalElements);

        return mappedObject;
   }

    @Override
    public String toString(){
        return "page: " + pageNumber + ", size: " + size + ", search : " +
                search + ", sorting by " + sorting;
    }
}
