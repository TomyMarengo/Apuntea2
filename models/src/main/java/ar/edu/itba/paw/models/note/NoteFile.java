package ar.edu.itba.paw.models.note;

import java.util.HashMap;
import java.util.Map;

public class NoteFile {
    private String type;
    private byte[] content;

    private static final Map<String, String> mimeTypes = new HashMap<String, String>(){{
        put("pdf", "application/pdf");
        put("png", "image/png");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");
        put("mp3", "audio/mp3");
        put("mp4",  "video/mp4");
    }};


    public NoteFile(String type, byte[] content) {
        this.type = type;
        this.content = content;
    }

    public String getMimeType()  {
        return mimeTypes.get(type);
    }

    public byte[] getContent() {
        return content;
    }
}
