package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.NonExistingCreateSearchable;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@NonExistingCreateSearchable
public abstract class CreateSearchableForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Pattern(regexp = RegexUtils.FILE_REGEX)
    private String name;

    @ValidUuid
    private UUID parentId;

    private boolean visible = true;

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

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
}
