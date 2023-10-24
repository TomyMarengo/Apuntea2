package ar.edu.itba.paw.models.user;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID imageId;

    @Column(name = "image")
    private byte[] picture;

    /* package-private */ Image() {
    }

    public Image(byte[] picture) {
        this.picture = picture;
    }

    public byte[] getPicture() {
        return picture;
    }

    public UUID getImageId() {
        return imageId;
    }
}
