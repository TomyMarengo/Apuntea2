package ar.edu.itba.apuntea.models;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Note {
    private UUID noteId;
    private String name;
    private User owner;
    private Institution institution; //TODO: change institution to model?
    private Career career;
    private Subject subject;
    private Category category;
    private LocalDateTime createdAt;
    private byte[] file;
    private Float avgScore;

    public Note(UUID noteId, String name) {
        this.noteId = noteId;
        this.name = name;
    }

    public Note(UUID noteId, String name, Category category, LocalDateTime createdAt, Float avgScore) {
        this.noteId = noteId;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
        this.avgScore = avgScore;
    }

    public UUID getNoteId() {
        return noteId;
    }
    public String getName() {
        return name;
    }
    public Optional<Float> getAvgScore(){
        return Optional.ofNullable(avgScore);
    }

}
