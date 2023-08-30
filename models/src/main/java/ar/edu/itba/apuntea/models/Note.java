package ar.edu.itba.apuntea.models;

public class Note {
    private String filePath; //TODO: change for BLOB when we have database
    private String university;
    private String career;
    private String subject;
    private String type;
    private float score = 0f;

    public Note(String filePath, String university, String career, String subject, String type) {
        this.filePath = filePath;
        this.university = university;
        this.career = career;
        this.subject = subject;
        this.type = type;
    }
}
