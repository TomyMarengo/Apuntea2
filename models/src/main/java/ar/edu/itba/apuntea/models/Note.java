package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class Note {
    private UUID noteId;
    private String institution; //TODO: change institution to model?
    private String career;
    private String subject;
    private String type;
    private byte[] file;

    public Note(String institution, String career, String subject, String type, byte[] file) {
        this.institution = institution;
        this.career = career;
        this.subject = subject;
        this.type = type;
        this.file = file;
    }
}
