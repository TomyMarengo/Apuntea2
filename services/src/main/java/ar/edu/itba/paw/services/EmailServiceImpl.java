package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.user.VerificationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.ISpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ISpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Environment env;

    private	static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Async
    @Override
    public void sendReviewEmail(Review review) {
        final Locale ownerLocale = review.getNote().getUser().getLocale();
        final String to = review.getNote().getUser().getEmail();
        final String subject = messageSource.getMessage("email.review.new", new Object[]{review.getNote().getName()}, ownerLocale);
        HashMap<String, Object> data = createReviewEmailMap(review);
        data.put("url", env.getProperty("base.url"));
        try {
            LOGGER.info("Sending review email to {}", review.getNote().getUser().getEmail());
            sendMessageUsingThymeleafTemplate(to,subject,"new-review.html", data, ownerLocale);
            LOGGER.info("Review email sent to {}", review.getNote().getUser().getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Review email could not be sent to {}",review.getNote().getUser().getEmail());
        }

    }

    private HashMap<String, Object> createReviewEmailMap(Review review) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", review.getNote().getName());
        data.put("score", review.getScore());
        data.put("content", review.getContent());
        data.put("url", env.getProperty("base.url"));
        data.put("urlNote", env.getProperty("base.url") + "/notes/" + review.getNote().getId());
        data.put("reviewer", review.getUser().getDisplayName());
        return data;
    }

    @Async
    @Override
    public void sendDeleteReviewEmail(Review review, String reason) {
        final Locale ownerLocale = review.getUser().getLocale();
        final String to = review.getUser().getEmail();
        final String subject = messageSource.getMessage("email.review.delete", new Object[]{review.getNote().getName()}, ownerLocale);
        HashMap<String, Object> data = createReviewEmailMap(review);
        data.put("reason", reason);
        data.put("url", env.getProperty("base.url"));
        try {
            LOGGER.info("Sending delete review email to {}", review.getUser().getEmail());
            sendMessageUsingThymeleafTemplate(to,subject,"deleted-review.html", data, ownerLocale);
            LOGGER.info("Delete review email sent to {}", review.getUser().getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Delete review email could not be sent to {}",review.getUser().getEmail());
        }
    }

    @Async
    @Override
    public void sendDeleteNoteEmail(Note note, String reason) {
        final Locale ownerLocale = note.getUser().getLocale();
        final String to = note.getUser().getEmail();
        final String subject = messageSource.getMessage("email.note.hasBeenDeleted", new Object[]{note.getName()}, ownerLocale);
        final Map<String, Object> data = new HashMap<>();
        data.put("name", note.getName());
        data.put("url", env.getProperty("base.url"));
        data.put("reason", reason);
        try {
            LOGGER.info("Sending delete note email to {} for note {}", note.getUser().getEmail(), note.getName());
            sendMessageUsingThymeleafTemplate(to,subject,"deleted-note.html", data, ownerLocale);
            LOGGER.info("Delete note email sent to {}", note.getUser().getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Delete note email could not be sent to {}",note.getUser().getEmail());
        }
    }

    @Async
    @Override
    public void sendDeleteDirectoryEmail(Directory directory, String reason) {
        final Locale ownerLocale = directory.getUser().getLocale();
        final String to = directory.getUser().getEmail();
        final String subject = messageSource.getMessage("email.directory.hasBeenDeleted", new Object[]{directory.getName()}, ownerLocale);
        final Map<String, Object> data = new HashMap<>();
        data.put("name", directory.getName());
        data.put("url", env.getProperty("base.url"));
        data.put("reason", reason);
        try {
            LOGGER.info("Sending delete directory email to {} for directory {}", directory.getUser().getEmail(), directory.getName());
            sendMessageUsingThymeleafTemplate(to,subject,"deleted-directory.html", data, ownerLocale);
            LOGGER.info("Delete directory email sent to {}", directory.getUser().getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Delete directory email could not be sent to {}",directory.getUser().getEmail());
        }
    }

    @Async
    @Override
    public void sendForgotPasswordEmail(VerificationCode verificationCode, Locale locale) {
        final String subject = messageSource.getMessage("email.forgotPassword.title", null, locale);
        final Map<String, Object> data = new HashMap<>();
        data.put("code", verificationCode.getCode());
        data.put("url", env.getProperty("base.url"));
        try {
            LOGGER.info("Sending forgot password email to {}", verificationCode.getEmail());
            sendMessageUsingThymeleafTemplate(verificationCode.getEmail(), subject,"forgot-password.html", data, locale);
            LOGGER.info("Forgot password email sent to {}", verificationCode.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Forgot password email could not be sent to {}", verificationCode.getEmail());
        }
    }

    @Async
    @Override
    public void sendBanEmail(User user, String reason, int duration) {
        final Locale ownerLocale = user.getLocale();
        final String to = user.getEmail();
        final String subject = messageSource.getMessage("email.ban.title", null, ownerLocale);
        final Map<String, Object> data = new HashMap<>();
        data.put("url", env.getProperty("base.url"));
        data.put("reason", reason);
        data.put("duration", duration);
        try {
            LOGGER.info("Sending ban user email to {}", user.getEmail());
            sendMessageUsingThymeleafTemplate(to,subject,"ban-user", data, ownerLocale);
            LOGGER.info("Ban user email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Ban user email could not be sent to {}",user.getEmail());
        }
    }

    @Async
    @Override
    public void sendUnbanEmail(User user) {
        final Locale ownerLocale = user.getLocale();
        final String to = user.getEmail();
        final String subject = messageSource.getMessage("email.unban.title", null, ownerLocale);
        final Map<String, Object> data = new HashMap<>();
        data.put("url", env.getProperty("base.url"));
        try {
            LOGGER.info("Sending unban user email to {}", user.getEmail());
            sendMessageUsingThymeleafTemplate(to,subject,"unban-user", data, ownerLocale);
            LOGGER.info("Unban user email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Unban user email could not be sent to {}",user.getEmail());
        }
    }

    /* https://www.baeldung.com/spring-email-templates */
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        final MimeMessage message = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

    private void sendMessageUsingThymeleafTemplate(String to, String subject, String template, Map<String, Object> templateModel, Locale locale) throws MessagingException {
        final Context thymeleafContext = new Context(locale);
        thymeleafContext.setVariables(templateModel);
        final String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }
}