package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validation.MaxFileSize;
import ar.edu.itba.paw.webapp.validation.UnusedEmail;
import ar.edu.itba.paw.webapp.validation.UnusedUsername;
import ar.edu.itba.paw.webapp.validation.ValidFileName;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditUserForm {

    @Pattern(regexp = "([a-zA-Z]+[ ]?)*")
    @Size(max = 20)
    private String firstName;
    @Pattern(regexp = "([a-zA-Z]+[ ]?)*")
    @Size(max = 20)
    private String lastName;

    @UnusedUsername
    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    //TODO add more restrictions
    private String username;

//    @ValidFileName(allowedExtensions = {".jpeg", ".png", ".jpg"}) // TODO: Add more extensions
//    @MaxFileSize(megabytes = 500)
//    private MultipartFile file;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getUsername() {
        return username;
    }

//    public MultipartFile getFile() {
//        return file;
//    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public void setFile(MultipartFile file) {
//        this.file = file;
//    }
}
