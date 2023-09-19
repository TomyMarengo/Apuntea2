package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note {
    private UUID noteId;
    private String name;
    private User user;
    private Institution institution;
    private Career career;
    private Subject subject;
    private Category category;
    private LocalDateTime createdAt;
    private byte[] file;
    private Float avgScore;

    public Note(UUID noteId, String name, User user) {
        this.noteId = noteId;
        this.name = name;
        this.user = user;
    }

    public Note(UUID noteId, String name, Category category, LocalDateTime createdAt, Float avgScore, Subject subject ) {
        this.noteId = noteId;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
        this.avgScore = avgScore;
        this.subject = subject;
    }

    public Note(UUID noteId, String name, Category category, LocalDateTime createdAt, Float avgScore, Subject subject, byte[] file) {
        this(noteId, name, category, createdAt, avgScore, subject);
        this.file = file;
    }


    public UUID getNoteId() {
        return noteId;
    }
    public String getName() {
        return name;
    }
    public Float getAvgScore(){
        return avgScore == null ? 0 : avgScore;
    }


    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", owner=" + user +
                '}';
    }
    
    public byte[] getFile() {
        return file;
    }

    public User getUser() {
        return user;
    }
}
