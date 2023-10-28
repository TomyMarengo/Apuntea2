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

    @Column(name = "file")
    private byte[] file;

    @OneToOne(mappedBy = "noteFile")
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


    public String getMimeType()  {
        return mimeTypes.get(note.getFileType());
    }

    public byte[] getContent() {
        return file;
    }
}
