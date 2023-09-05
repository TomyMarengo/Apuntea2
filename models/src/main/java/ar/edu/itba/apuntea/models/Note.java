package ar.edu.itba.apuntea.models;

import java.util.UUID;

public class Note {
    private UUID noteId;
    private String name;
    private String institution; //TODO: change institution to model?
    private String career;
    private String subject;
    private String type;
    private byte[] file;

    public Note(UUID noteId, String name) {
        this.noteId = noteId;
        this.name = name;
    }

    public Note(UUID noteId, String institution, String career, String subject, String type, byte[] file) {
        this.noteId = noteId;
        this.institution = institution;
        this.career = career;
        this.subject = subject;
        this.type = type;
        this.file = file;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
