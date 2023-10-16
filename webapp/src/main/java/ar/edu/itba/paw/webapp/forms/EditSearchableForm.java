package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.NonExistingEditSearchable;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@NonExistingEditSearchable
public abstract class EditSearchableForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

    @ValidUuid
    private UUID id;

    @ValidUuid
    private UUID parentId;

    private boolean visible = true;

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

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
