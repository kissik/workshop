package ua.org.training.workshop.utility;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageService<T> {

    private static final Long PAGEABLE_PAGE_DEFAULT_VALUE = 0L;
    private static final String PAGEABLE_PAGE_ATTRIBUTE = "page";
    private static final String PAGEABLE_SEARCH_ATTRIBUTE = "search";
    private static final Long PAGEABLE_SIZE_DEFAULT_VALUE = 5L;
    private static final String PAGEABLE_SIZE_ATTRIBUTE = "size";
    private static final String PAGEABLE_SORTING_ATTRIBUTE = "sorting";

    public String getPage(Page<T> page) {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = jsonMapper.writeValueAsString(makeMap(page));
        } catch (IOException ignored) {

        }

        return jsonString;
    }

    private Map<String, Object> makeMap(Page<T> page) {

        Map<String, Object> mappedObject = new HashMap<>();

        mappedObject.put("content", page.getContent().orElse(new ArrayList<>()));
        mappedObject.put("size", page.getSize());
        mappedObject.put("totalElements", page.getTotalElements());
        mappedObject.put("language", page.getLanguage());

        return mappedObject;
    }

    public Page<T> createPage(HttpServletRequest request) {
        Page<T> page;
        page = new Page<>();
        page.setPageNumber(
                Utility.tryParseLong(
                        request.getParameter(PAGEABLE_PAGE_ATTRIBUTE),
                        PAGEABLE_PAGE_DEFAULT_VALUE));
        page.setSize(
                Utility.tryParseLong(
                        request.getParameter(PAGEABLE_SIZE_ATTRIBUTE),
                        PAGEABLE_SIZE_DEFAULT_VALUE));
        page.setSearch(
                Utility.getParameterString(
                        request.getParameter(PAGEABLE_SEARCH_ATTRIBUTE),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        page.setSorting(
                Utility.getParameterString(
                        request.getParameter(PAGEABLE_SORTING_ATTRIBUTE),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        page.setLanguage(
                Utility.getCurrentLanguage(request)
        );
        return page;
    }
}
