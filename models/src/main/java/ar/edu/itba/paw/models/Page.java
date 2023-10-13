package ar.edu.itba.paw.models;

import java.util.List;

public class Page<T>{

    private final List<T> content;
    private final int currentPage;
    private final int pageSize;
    private final int totalResults;

    public Page(List<T> content, int currentPage, int pageSize, int totalResults) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalResults = totalResults;
    }

    public List<T> getContent() {
        return content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return Page.getTotalPages(totalResults, pageSize);
    }

    private static int getTotalPages(int totalResults, int pageSize) {
        if(totalResults == 0)
            return 1;
        if (totalResults % pageSize == 0)
            return totalResults / pageSize;
        return totalResults / pageSize + 1;
    }

    public static int getSafePagePosition(int page, int totalResults, int pageSize) {
        int result = Math.min(page, Page.getTotalPages(totalResults, pageSize));
        return Math.max(result, 1);
    }

    public int getTotalResults() {
        return totalResults;
    }
}
