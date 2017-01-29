package eu.dorsum.webtrader.services.mailbox.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.dorsum.webtrader.domain.mailbox.CreateGroupMailboxMessageRq;
import eu.dorsum.webtrader.domain.mailbox.CreateUserMailboxMessageRq;
import eu.dorsum.webtrader.domain.mailbox.DeleteMailboxMessageRq;
import eu.dorsum.webtrader.domain.mailbox.FindMailboxMessageRp;
import eu.dorsum.webtrader.domain.mailbox.FindMailboxMessageRq;
import eu.dorsum.webtrader.domain.mailbox.GetMailboxMessageRp;
import eu.dorsum.webtrader.domain.mailbox.GetMailboxMessageRq;
import eu.dorsum.webtrader.domain.mailbox.GetRecipientsListRp;
import eu.dorsum.webtrader.domain.mailbox.GetRecipientsListRq;
import eu.dorsum.webtrader.domain.mailbox.MailboxMessageAck;
import eu.dorsum.webtrader.domain.mailbox.ReadMailboxMessageRq;
import eu.dorsum.webtrader.services.mailbox.MailboxService;


public class MailboxServiceMyBatisImpl implements MailboxService{

	@Autowired
	@Qualifier("mailboxMapper")
	private MailboxMapper mailboxMapper;
	
		
	@Override
	public MailboxMessageAck createGroupMailboxMessage(CreateGroupMailboxMessageRq request) {
		return mailboxMapper.createGroupMailboxMessage(request);
	}

	@Override
	public MailboxMessageAck createUserMailboxMessage(CreateUserMailboxMessageRq request) {
		return mailboxMapper.createUserMailboxMessage(request);
	}

	@Override
	public MailboxMessageAck deleteMailboxMessage(DeleteMailboxMessageRq request) {
		return mailboxMapper.deleteMailboxMessage(request);
	}

	@Override
	public FindMailboxMessageRp findMailboxMessage(FindMailboxMessageRq request) {
		return mailboxMapper.findMailboxMessage(request);
	}

	@Override
	public GetMailboxMessageRp getMailboxMessage(GetMailboxMessageRq request) {
		return mailboxMapper.getMailboxMessage(request);
	}

	@Override
	public MailboxMessageAck readMailboxMessage(ReadMailboxMessageRq request) {
		return mailboxMapper.readMailboxMessage(request);
	}

	@Override
	public GetRecipientsListRp getRecipientsList(GetRecipientsListRq request) {
		return mailboxMapper.getRecipientsList(request);
	}

}
