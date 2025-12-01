package com.notification.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.notification.config.RabbitConfig;
import com.notification.model.EmailPayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingEmailListener {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void listen(EmailPayload payload) {
        try {
            System.out.println("Received email event: " + payload);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(payload.getEmail());
            msg.setSubject("Booking Confirmed - PNR: " + payload.getPnr());
            msg.setText("Hi " + payload.getName() + ",\n\n"
                    + "Your booking for flight " + payload.getFlightId() + " has been confirmed.\n"
                    + "PNR: " + payload.getPnr() + "\n\n"
                    + "Thanks, Enjoy your flight!");

            mailSender.send(msg);
            System.out.println("Email sent to " + payload.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
