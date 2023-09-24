package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.lang.reflect.InvocationTargetException;
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


    private	static	final Logger LOGGER	=	LoggerFactory.getLogger(EmailServiceImpl.class);

    @Async
    @Override
    public void sendReviewEmail(Review review) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String to = review.getNote().getUser().getEmail();
        final String subject = messageSource.getMessage("email.review.new", null, locale) + " " + review.getNote().getName();
        final Map<String, Object> data = new HashMap<>();
        data.put("score", review.getScore());
        data.put("content", review.getContent());
        data.put("reviewer", review.getUser().getEmail());

        try {
            LOGGER.info("Sending review email to {}", review.getNote().getUser().getEmail());
            sendMessageUsingThymeleafTemplate(to,subject,"new-review.html", data, locale);
            LOGGER.info("Review email sent to {}", review.getNote().getUser().getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Review email could not be sent to {}",review.getNote().getUser().getEmail());
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