package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.Note;
import ar.edu.itba.paw.models.Review;

public interface EmailService {
    void sendReviewEmail(Review review);
    void sendDeleteNoteEmail(Note note);
    void sendDeleteDirectoryEmail(Directory directory);
}
