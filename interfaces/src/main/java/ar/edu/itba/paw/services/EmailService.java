package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.VerificationCode;

import java.util.Locale;

public interface EmailService {
    void sendReviewEmail(Review review);
    void sendDeleteNoteEmail(Note note, String reason);
    void sendDeleteDirectoryEmail(Directory directory, String reason);
    void sendForgotPasswordEmail(VerificationCode verificationCode, Locale locale);
}
