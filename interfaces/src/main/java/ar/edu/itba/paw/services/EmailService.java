package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.VerificationCode;

import java.util.Locale;

public interface EmailService {
    void sendReviewEmail(Review review);
    void sendDeleteReviewEmail(Review review, String reason);
    void sendDeleteNoteEmail(Note note, String reason);
    void sendDeleteDirectoryEmail(Directory directory, String reason);
    void sendForgotPasswordEmail(VerificationCode verificationCode, Locale locale);
}
