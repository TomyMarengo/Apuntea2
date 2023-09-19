package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;

public interface EmailService {
    void sendReviewEmail(Review review);
}
