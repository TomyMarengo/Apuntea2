package ar.edu.itba.paw.webapp.api;

public class ApunteaMediaType {
    public static final String ERROR = "application/vnd.apuntea.error-v1.0+json";

    public static final String USER = "application/vnd.apuntea.user-v1.0+json";
    public static final String USER_WITH_EMAIL = "application/vnd.apuntea.user-with-email-v1.0+json";
    public static final String USER_STATUS_REASON = "application/vnd.apuntea.status-reason-v1.0+json";
    public static final String USER_PASSWORD = "application/vnd.apuntea.user-password-v1.0+json";
    public static final String USER_REQUEST_PASSWORD_CHANGE = "application/vnd.apuntea.request-password-change-v1.0+json";
    public static final String USER_COLLECTION = "application/vnd.apuntea.user-collection-v1.0+json";
    public static final String USER_COLLECTION_WITH_EMAIL = "application/vnd.apuntea.user-with-email-collection-v1.0+json";
    public static final String INSTITUTION = "application/vnd.apuntea.institution-v1.0+json";
    public static final String INSTITUTION_COLLECTION = "application/vnd.apuntea.institution-collection-v1.0+json";
    public static final String CAREER = "application/vnd.apuntea.career-v1.0+json";
    public static final String CAREER_COLLECTION = "application/vnd.apuntea.career-collection-v1.0+json";
    public static final String SUBJECT = "application/vnd.apuntea.subject-v1.0+json";
    public static final String SUBJECT_COLLECTION = "application/vnd.apuntea.subject-collection-v1.0+json";
    public static final String SUBJECT_CAREER = "application/vnd.apuntea.subjectcareer-v1.0+json";
    public static final String SUBJECT_CAREER_COLLECTION = "application/vnd.apuntea.subjectcareer-collection-v1.0+json";
    public static final String DIRECTORY = "application/vnd.apuntea.directory-v1.0+json";
    public static final String DIRECTORY_COLLECTION = "application/vnd.apuntea.directory-collection-v1.0+json";
    public static final String NOTE = "application/vnd.apuntea.note-v1.0+json";
    public static final String NOTE_COLLECTION = "application/vnd.apuntea.note-collection-v1.0+json";
    public static final String DELETE_REASON = "application/vnd.apuntea.delete-reason-v1.0+json";

    public static final String REVIEW = "application/vnd.apuntea.review-v1.0+json";
    public static final String REVIEW_COLLECTION = "application/vnd.apuntea.review-collection-v1.0+json";

    public static final String FORM_DATA = "multipart/form-data";

    private ApunteaMediaType() {
    }
}
