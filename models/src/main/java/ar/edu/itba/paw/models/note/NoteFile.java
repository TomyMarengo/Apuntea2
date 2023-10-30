package ar.edu.itba.paw.models.note;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "note_files")
public class NoteFile {
    @Id
    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "file", nullable = false)
    private byte[] file;

    @OneToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    private static final Map<String, String> mimeTypes = new HashMap<String, String>(){{
        put("pdf", "application/pdf");
        put("png", "image/png");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");
        put("mp3", "audio/mp3");
        put("mp4",  "video/mp4");
    }};

    /* package-private */ NoteFile() {}

    @PrePersist
    private void prePersist() {
        noteId = note.getId();
    }

    public NoteFile(byte[] file, Note note) {
        this.file = file;
        this.note = note;
    }


    public String getMimeType()  {
        return mimeTypes.get(note.getFileType());
    }

    public byte[] getContent() {
        return file;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
