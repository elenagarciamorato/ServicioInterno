package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sun.mail.smtp.SMTPTransport;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



@RestController
public class MailController {

	
	private final String username = "healthsportinfo@gmail.com";
	private final String password= "AES2019:)";
	
	
	
	@SuppressWarnings("restriction")
	@GetMapping(value = "/correo/{nombre}/{email}/{rol}")
	public ResponseEntity<Boolean> sendMail(@PathVariable String nombre, @PathVariable String email, @PathVariable String rol) {
		
		System.out.println("Datos recibidos!");
		System.out.println("Nombre: " + nombre + "  Email: " + email + "  Rol: " + rol);
		
		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

			
			Properties props = System.getProperties();
			props.setProperty("mail.smtps.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.setProperty("mail.smtps.auth", "true");
			props.put("mail.smtps.quitwait", "false");

			
			Session session = Session.getInstance(props, null);
			final MimeMessage msg = new MimeMessage(session);
			
			
			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
			msg.setSubject("Bienvenido");
			
			//Se envia un mensaje diferente dependiendo si te registras como entrenador o deportista
			if(rol.equals("deportista")){
				msg.setText(" Hola " + nombre.toUpperCase() + ", \n \n Bienvenido a HealthSport, la herramienta que te permitirá controlar tus progresos y los de tu equipo! \n Al registrarte como deportista, un entrenador te seleccionará proximamente y pasarás a formar parte de su equipo. \n \n Quedamos a tu disposición para lo que necesites. \n \n Gracias por confiar en nosotros. \n \n El equipo de HealthSport", "utf-8");
			}else{
				msg.setText(" Hola " + nombre.toUpperCase() + ", \n \n Bienvenido a HealthSport, la herramienta que te permitirá controlar tus progresos y los de tu equipo! \n Al registrarte como entrenador, obtienes acceso a una lista de jugadores que puedes seleccionar para que formen parte de tu equipo. \n \n Quedamos a tu disposición para lo que necesites. \n \n Gracias por confiar en nosotros. \n \n El equipo de HealthSport", "utf-8");
			}
			msg.setSentDate(new Date());

			
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", username, password);
			t.sendMessage(msg, msg.getAllRecipients());
			t.close();

			
		} catch (MessagingException ex) {
			System.out.println(ex);
		}	
		
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		
	}
	
}
