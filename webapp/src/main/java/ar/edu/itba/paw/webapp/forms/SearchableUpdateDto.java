package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public abstract class SearchableUpdateDto {
    @Size(min = 2, max = 50, message = "{validation.searchable.name}")
    @Pattern(regexp = RegexUtils.FILE_REGEX, message = "{validation.searchable.name}")
    private String name;

    private boolean visible = true;

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
