package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ExistingDirectory;
import ar.edu.itba.paw.webapp.validation.NonExistingCreateSearchable;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.NotEmpty;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@NonExistingCreateSearchable()
public abstract class SearchableCreationDto {
    @NotEmpty(message = "{error.param.empty}")
    @Size(min = 2, max = 50, message = "{error.param.length}")
    @Pattern(regexp = RegexUtils.FILE_REGEX, message = "{validation.searchable.name}")
    @FormDataParam("name")
    private String name;

    @ExistingDirectory
    @ValidUuid
    @FormDataParam("parentId")
    private UUID parentId;

    @FormDataParam("visible")
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
