package ar.edu.itba.paw.webapp.forms.note;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.Min;

public class SearchReviewForm {

    @Min(1)
    private int pageNumber = 1;

    @Range(min = 4, max = 15)
    private int pageSize = 5;

    public int getPageNumber() { return pageNumber;}

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
