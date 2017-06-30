package aero.sita.asl.tasks.service;

import static aero.sita.asl.tasks.util.Constants.MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY;
import static aero.sita.asl.tasks.util.Constants.MSG_BUILDING_OUTPUT_FILE;
import static aero.sita.asl.tasks.util.Constants.MSG_ERROR_PROCESSING_FILE_MESSAGE;
import static aero.sita.asl.tasks.util.Constants.MSG_PROCESS_FILE;
import static aero.sita.asl.tasks.util.Constants.MSG_RECEIVED_FILE;

import org.apache.log4j.Logger;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * Prepare content for the output message and build message with new content.
 * 
 * @author prashant.soam
 * 
 */
public class FileProcessService {

    /** LOGGER. **/
    private static final Logger LOGGER = Logger.getLogger(FileProcessService.class);

    /**
     * Method to process the input message (file) and generate resulting message (file).
     * 
     * @param message {@link Message<?>}
     * @return {@link Object}
     */
    public Object processMessage(Message<?> message) {
        String content;
        String fileName = (String) message.getHeaders().get(FileHeaders.FILENAME);

        LOGGER.debug(String.format(MSG_RECEIVED_FILE, fileName));

        try {
            LOGGER.info(MSG_PROCESS_FILE);
            content = processContent(message.getPayload().toString());
        }
        catch (Exception e) {
            LOGGER.error(MSG_ERROR_PROCESSING_FILE_MESSAGE);
            // There was error in processing the file return original message to mark it as ERROR.
            return message;
        }

        LOGGER.info(MSG_BUILDING_OUTPUT_FILE);

        // Building new message with processed result and also original message (file) is being retained in the header
        // to mark it as PROCESSED.
        Message<String> output = MessageBuilder.withPayload(content).copyHeaders(message.getHeaders())
                .setHeader(MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY, message).build();

        return output;
    }

    /**
     * Read the content line by line from payload and sum each line to produce result.
     * 
     * @param payload {@link String}
     * @return {@link String} - the sum of the numbers on each line.
     */
    private String processContent(String payload) throws Exception {
        long sum = 0;
        String[] lines = payload.split("\n");
        for (String line : lines) {
            // Try to sum all the numbers in the file.
            sum = sum + Long.valueOf(line.trim());
        }

        return String.valueOf(sum);
    }
}
