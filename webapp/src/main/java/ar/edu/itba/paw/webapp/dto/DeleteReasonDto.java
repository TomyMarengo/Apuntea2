package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Size;

public class DeleteReasonDto {
    @Size(max = 255, message = "{error.param.length}")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
