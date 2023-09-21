package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.UUID;

public class CreateDirectoryForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty
    @ValidUuid
    private UUID parentId;

    public String getName() {
        return name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

}
