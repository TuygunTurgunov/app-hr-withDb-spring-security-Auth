package uz.pdp.online.m6l5apphr.service.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.LoginDto;
import uz.pdp.online.m6l5apphr.repository.TaskRepository;

import java.util.Optional;

@Service
public class CustomService {
    @Autowired
    JavaMailSender javaMailSender;//Mail bilan ishlash ga

    @Autowired
    TaskRepository taskRepository;


    public void  sendEmail (String sendingEmail, String taskCode,String linkName,String taskName,String taskDescription){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName);
            mailMessage.setText("<h2>"+taskName+"</h2>");
//            mailMessage.setText("<a href='http://localhost:8080/api/auth/"+verifyName+"?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlang</a>");
            mailMessage.setText("<button><a href='http://localhost:8080/api/task/"+linkName+"?taskCode=" + taskCode + "&email=" + sendingEmail + "'</a>>Tasdiqlang</button>");
            javaMailSender.send(mailMessage);
            System.out.println("message send ok");

              } catch (Exception e) {
            System.out.println("error in message send");
             }

    }

    public void  sendEmail (String sendingEmail,String taskName, String remember){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName+". This task "+remember);
            mailMessage.setText("<h2>"+taskName+"</h2>");
//            mailMessage.setText("<a href='http://localhost:8080/api/auth/"+verifyName+"?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlang</a>");
//            mailMessage.setText("<button><a href='http://localhost:8080/api/task/"+linkName+"?taskCode=" + taskCode + "&email=" + sendingEmail + "'</a>>Tasdiqlang</button>");
            javaMailSender.send(mailMessage);
            System.out.println("message send ok");

        } catch (Exception e) {
            System.out.println("error in message send");
        }

    }

    public void  sendEmail (String sendingEmail,String fromEmployee,String taskName,String statusName){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName+" status changed to"+statusName);
            mailMessage.setText("shu hodimga  ==>"+sendingEmail+" yuborildi "+fromEmployee+" shu hodimdan ");
            javaMailSender.send(mailMessage);
            System.out.println("message send ok");

        } catch (Exception e) {
            System.out.println("error in message send");
        }

    }



}
