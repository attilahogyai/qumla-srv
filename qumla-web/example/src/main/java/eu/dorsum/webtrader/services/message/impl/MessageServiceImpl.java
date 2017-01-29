package eu.dorsum.webtrader.services.message.impl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import eu.dorsum.webtrader.domain.message.MessageBox;
import eu.dorsum.webtrader.domain.message.MessageBoxRq;
import eu.dorsum.webtrader.domain.message.SendMessageRp;
import eu.dorsum.webtrader.domain.message.SendMessageRq;
import eu.dorsum.webtrader.services.message.MessageService;

public abstract class MessageServiceImpl implements MessageService {
	
	private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
	
	private static final String msgText1 = "Tisztelt Németh Gábor Imre!<br/>" +
			"Ezúton értesítjük, hogy számlájára megérkezett a kívánt összeg.<br>" +
			"További kérdéseiért forduljon hozzánk, használja email címünket<br>" +
			"vagy telefonáljon be ügyfélszolgálatunkra.<br>" +
			"Üdvözlettel<br>" +
			"Magyar Államkincstár";
	private static final String msgText2 = "Tisztelt Németh Gábor Imre!<br/>" +
			"Ezúton értesítjük, a hogy számlájáról levonásra került<br>" +
			"összeg jóváírásra került.<br>" +
			"Kérdés esetén forduljon ügyfélszolgáltunkhoz.<br>" +
			"Üdvözlettel<br>" +
			"Magyar Államkincstár";
	
	public MessageServiceImpl() {
		logger.info("CREATED");
	}
	
	@Override
	public List<MessageBox> getMessageBoxMsg(MessageBoxRq request) {
		List<MessageBox> list = new ArrayList<>();
		MessageBox msg = new MessageBox();
		msg.setId("1");
		msg.setNewMessage(0);
		msg.setSendDate(new GregorianCalendar(2014,11,17).getTime());
		msg.setLastReadDate(new GregorianCalendar(2015,4,15).getTime());
		msg.setSubject("Tájékoztatás");
		msg.setText(msgText1);
		list.add(msg);
		msg = new MessageBox();
		msg.setId("2");
		msg.setNewMessage(0);
		msg.setSendDate(new GregorianCalendar(2014,11,31).getTime());
		msg.setLastReadDate(new GregorianCalendar(2015,5,16).getTime());
		msg.setSubject("Értesítés");
		msg.setText(msgText2);
		list.add(msg);
		return list;
	}
	
	public abstract SendMessageRp sendMessage(SendMessageRq request);
	
}
