package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.AttributeDependence;
import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Range;

import javax.ws.rs.QueryParam;
import java.util.UUID;

@EitherAttribute(fieldGroup1 = {"careerId", "year"}, fieldGroup2 = {"notInCareer"}, allowNeither = false, message = "invalidAttributeCombination")
@AttributeDependence(baseField = "year", dependentField = "careerId", message = "invalidAttributeCombination")
public class SubjectQuery {
    @ValidUuid
    @QueryParam("careerId")
    private UUID careerId;

    @Range(min = 1, max = 10)
    @QueryParam("year")
    private Integer year;

    @ValidUuid
    @QueryParam("notInCareer")
    private UUID notInCareer;

    public UUID getCareerId() {
        return careerId;
    }

    public void setCareerId(UUID careerId) {
        this.careerId = careerId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public UUID getNotInCareer() {
        return notInCareer;
    }

    public void setNotInCareer(UUID notInCareer) {
        this.notInCareer = notInCareer;
    }
}
