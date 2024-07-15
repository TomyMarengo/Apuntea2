package ar.edu.itba.paw.webapp.api;

public class ApunteaMediaType {
    public static final String ERROR = "application/vnd.apuntea.error-v1.0+json";

    public static final String USER = "application/vnd.apuntea.user-v1.0+json";
    public static final String USER_UPDATE_STATUS = "application/vnd.apuntea.update-status-v1.0+json";
    public static final String USER_UPDATE_PASSWORD = "application/vnd.apuntea.update-password-v1.0+json";
    public static final String USER_REQUEST_PASSWORD_CHANGE = "application/vnd.apuntea.request-password-change-v1.0+json";
    public static final String USER_COLLECTION = "application/vnd.apuntea.user-collection-v1.0+json";
    public static final String USER_CREATE = "application/vnd.apuntea.user-create-v1.0+json";
    public static final String USER_UPDATE = "application/vnd.apuntea.user-update-v1.0+json";
    public static final String PICTURE_UPDATE = "multipart/form-data";
    public static final String INSTITUTION = "application/vnd.apuntea.institution-v1.0+json";
    public static final String INSTITUTION_COLLECTION = "application/vnd.apuntea.institution-collection-v1.0+json";
    public static final String CAREER = "application/vnd.apuntea.career-v1.0+json";
    public static final String CAREER_COLLECTION = "application/vnd.apuntea.career-collection-v1.0+json";
    public static final String SUBJECT = "application/vnd.apuntea.subject-v1.0+json";
    public static final String SUBJECT_COLLECTION = "application/vnd.apuntea.subject-collection-v1.0+json";
    public static final String SUBJECT_CAREER = "application/vnd.apuntea.subject-career-v1.0+json";
    public static final String SUBJECT_CAREER_COLLECTION = "application/vnd.apuntea.subject-career-collection-v1.0+json";
    public static final String SUBJECT_CAREER_CREATE = "application/vnd.apuntea.subject-career-create-v1.0+json";
    public static final String SUBJECT_CAREER_UPDATE = "application/vnd.apuntea.subject-career-update-v1.0+json";

    public static final String DIRECTORY = "application/vnd.apuntea.directory-v1.0+json";
    public static final String DIRECTORY_COLLECTION = "application/vnd.apuntea.directory-collection-v1.0+json";
    public static final String DIRECTORY_CREATE = "application/vnd.apuntea.directory-create-v1.0+json";
    public static final String DIRECTORY_UPDATE = "application/vnd.apuntea.directory-update-v1.0+json";

    public static final String NOTE = "application/vnd.apuntea.note-v1.0+json";
    public static final String NOTE_COLLECTION = "application/vnd.apuntea.note-collection-v1.0+json";

    // NOTE_CREATE is multipart form data
//    public static final String NOTE_CREATE = "multipart/vnd.apuntea.note-create-v1.0";
    public static final String NOTE_CREATE = "multipart/form-data";
    public static final String NOTE_UPDATE = "application/vnd.apuntea.note-update-v1.0+json";
    public static final String DELETE_REASON = "application/vnd.apuntea.delete-reason-v1.0+plain";

    public static final String REVIEW = "application/vnd.apuntea.review-v1.0+json"; // ???
    public static final String REVIEW_COLLECTION = "application/vnd.apuntea.review-collection-v1.0+json";
    public static final String REVIEW_CREATE = "application/vnd.apuntea.review-create-v1.0+json";
    public static final String REVIEW_UPDATE = "application/vnd.apuntea.review-update-v1.0+json";


    public static final String EMAIL = "text/vnd.apuntea.email-v1.0+plain";

    private ApunteaMediaType() {
    }
}
