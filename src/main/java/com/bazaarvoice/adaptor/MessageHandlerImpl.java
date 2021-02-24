package com.bazaarvoice.adaptor;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import io.interact.sqsdw.sqs.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Map;

public class MessageHandlerImpl extends MessageHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageHandlerImpl.class);
    public MessageHandlerImpl(String messageType){
        super(messageType);
    }


    public void handle(Message message) {
        LOGGER.info(message.getBody());
        Map<String, MessageAttributeValue> messageAttributes = message.getMessageAttributes();
        LOGGER.info(String.valueOf(messageAttributes));

    }
}
