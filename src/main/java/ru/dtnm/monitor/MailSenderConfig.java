package ru.dtnm.monitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.PasswordAuthentication;
import javax.mail.URLName;
import java.util.Properties;

@Configuration
public class MailSenderConfig {

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private Integer smtpPort;

    @Value("${mail.smtp.username}")
    private String userName;

    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String enableTtls;

    @Value("${mail.debug}")
    private String debug;


    @Bean
    public JavaMailSender javaMailService() {
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(smtpHost);
        javaMailSender.setPort(smtpPort);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.getSession().setPasswordAuthentication(new URLName(smtpHost), new PasswordAuthentication(userName, password));

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", enableTtls);
        properties.setProperty("mail.debug", debug);
        return properties;
    }

}
