package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Async
    @Override
    public void sendReviewEmail(Review review) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String to = review.getNote().getUser().getEmail();
        final String subject = messageSource.getMessage("email.review.new", null, locale);
        final Map<String, Object> data = new HashMap<>();
        data.put("score", review.getScore());
        try {
            sendMessageUsingThymeleafTemplate(to,subject,"new-review.html", data, locale);
//            LOGGER.info("Verification email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            System.err.println(e.getMessage()); // TODO: Fix with logger
//            LOGGER.warn("Verification email could not be sent to {}",user.getEmail());
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