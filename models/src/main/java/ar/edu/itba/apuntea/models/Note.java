package ar.edu.itba.apuntea.models;

import java.time.LocalDateTime;
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

    public Note(UUID noteId, String name) {
        this.noteId = noteId;
        this.name = name;
    }

    public Note(UUID noteId, String name, Category category, LocalDateTime createdAt) {
        this.noteId = noteId;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
    }

    public UUID getNoteId() {
        return noteId;
    }
    public String getName() {
        return name;
    }
}
