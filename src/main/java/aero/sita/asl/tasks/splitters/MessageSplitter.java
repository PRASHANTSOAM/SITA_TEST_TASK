package aero.sita.asl.tasks.splitters;

import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY;
import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_STATUS_ERROR;
import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_STATUS_KEY;
import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_STATUS_OUTPUT;
import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_STATUS_PROCESSED;
import static aero.sita.asl.tasks.util.Constants.MSG_SPLITTING_OUTPUT_MESSAGE;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * Splitter class to split original and processed to route to appropriate channel.
 * 
 * @author prashant.soam
 * 
 */
public class MessageSplitter {

    /** LOGGER. **/
    private static final Logger LOGGER = Logger.getLogger(MessageSplitter.class);

    /**
     * Splits original and output messages and sets appropriate header attribute to route to appropriate channel.
     * 
     * @param message {@link Message<?>} - incoming message (file)
     * @return {@link List<Message>} - List of messages
     */
    public List<Message<?>> splitFileMessage(Message<?> message) {
        List<Message<?>> messageList = new ArrayList<Message<?>>();

        // Check if original message exists in header of sent message.
        if (message.getHeaders().containsKey(MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY)) {
            LOGGER.info(MSG_SPLITTING_OUTPUT_MESSAGE);

            // Message successfully processed now splitting output and original messages to set appropriate header
            // attribute (status) value so that, each may be routed to their respective channel for file creation.
            Message<?> originalMessage = (Message<?>) message.getHeaders().get(MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY);
            originalMessage = MessageBuilder.fromMessage(originalMessage)
                    .setHeader(MESSAGE_HEADER_STATUS_KEY, MESSAGE_HEADER_STATUS_PROCESSED).build();
            message = MessageBuilder.fromMessage(message)
                    .setHeader(MESSAGE_HEADER_STATUS_KEY, MESSAGE_HEADER_STATUS_OUTPUT).build();

            messageList.add(message);
            messageList.add(originalMessage);
        }
        else {
            // Message was not processed successfully so setting status=ERROR to route to error channel.
            message = MessageBuilder.fromMessage(message)
                    .setHeader(MESSAGE_HEADER_STATUS_KEY, MESSAGE_HEADER_STATUS_ERROR).build();
            messageList.add(message);
        }

        return messageList;
    }
}