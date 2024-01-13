package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import com.sun.istack.internal.Nullable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public abstract class SearchableUpdateDto {
    @Nullable
    @Size(min = 2, max = 50)
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

    @ValidUuid
    private UUID id;

    private boolean visible = true;

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
