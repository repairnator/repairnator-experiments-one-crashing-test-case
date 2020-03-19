package com.jairosousa.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.jairosousa.cursomc.domain.Cliente;
import com.jairosousa.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPassword);

}
