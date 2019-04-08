package com.gliesereum.mail.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final String RECEIVER = "spring.mail.from";
    private final String SUBJECT = "spring.mail.subject";

    @Autowired
    private Environment environment;

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put("text", text);
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
            Template template = freemarkerConfig.getTemplate("email.ftl");
            sendSingleMessage(to, subject, model, template);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendSingleVerificationMessage(String to, String code) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put("code", code);
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
            Template template = freemarkerConfig.getTemplate("email-verification.ftl");
            sendSingleMessage(to, environment.getProperty(SUBJECT), model, template);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void sendSingleMessage(String to, String subject, Map<String, String> model, Template template) {
        try {
            MimeMessagePreparator preparatory = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws MessagingException, IOException, TemplateException {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setFrom(environment.getProperty(RECEIVER));
                    String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                    message.setText(text, true);
                }
            };
            mailSender.send(preparatory);
            logger.info("\nSend email, date: [{}] to: {}", LocalDateTime.now(), to);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendEmailsMessages(List<String> listTo, String subject, String text) {
        List<MimeMessagePreparator> preparatoryList = new ArrayList<>();
        MimeMessagePreparator preparatory;
        for (String email : listTo) {
            preparatory = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());
                message.setSubject(subject);
                message.setTo(email);
                message.setFrom(environment.getRequiredProperty(RECEIVER));

                Map<String, String> model = new HashMap<>();
                model.put("body", text);

                freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
                Template template = freemarkerConfig.getTemplate("emails.ftl");
                String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

                message.setText(body, true);
            };
            preparatoryList.add(preparatory);
        }
        if (!preparatoryList.isEmpty()) {
            mailSender.send(preparatoryList.toArray(new MimeMessagePreparator[preparatoryList.size()]));
            logger.info("\nSend emails, date: [{}] to: {}", LocalDateTime.now(), listTo);
        }
    }
}
