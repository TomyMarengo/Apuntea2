package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.SubjectCareer;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

public class SubjectCareerDto {
    private int year;

    private URI self;
    private URI career;
    private URI subject;

    public SubjectCareerDto() {
    }

    public SubjectCareerDto(int year, UUID institutionId, UUID careerId, UUID subjectId, UriInfo uriInfo) {
        this.year = year;
        UriBuilder ub = uriInfo.getBaseUriBuilder().path("institutions").path(institutionId.toString())
                .path("careers").path(careerId.toString());
        career = ub.build();
        self = ub.path("subjectcareers").path(subjectId.toString()).build();
        subject = uriInfo.getBaseUriBuilder().path("subjects").path(subjectId.toString()).build();
    }


    public static SubjectCareerDto fromSubjectCareer(SubjectCareer sc, UriInfo uriInfo) {
        SubjectCareerDto scDto = new SubjectCareerDto();
        scDto.year = sc.getYear();

        UUID subjectId = sc.getSubject().getSubjectId();
        Career c = sc.getCareer();
        UUID institutionId = c.getInstitutionId();
        UriBuilder ub = uriInfo.getBaseUriBuilder().path("institutions").path(institutionId.toString())
                                                    .path("careers").path(c.getCareerId().toString());
        scDto.career = ub.build();
        scDto.self = ub.path("subjectcareers").path(subjectId.toString()).build();
        scDto.subject = uriInfo.getBaseUriBuilder().path("subjects").path(subjectId.toString()).build();

        return scDto;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getCareer() {
        return career;
    }

    public void setCareer(URI career) {
        this.career = career;
    }

    public URI getSubject() {
        return subject;
    }

    public void setSubject(URI subject) {
        this.subject = subject;
    }
}
