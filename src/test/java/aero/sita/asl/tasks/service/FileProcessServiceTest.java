package aero.sita.asl.tasks.service;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Test class for FileProcessService.
 * 
 * @author prashant.soam
 * 
 */
public class FileProcessServiceTest {

    private FileProcessService fileProcessService;

    /**
     * Setting the resources ready
     */
    @Before
    public void setUp() {
        fileProcessService = new FileProcessService();
    }

    /**
     * Tidying resources
     */
    @After
    public void tearDown() {
        fileProcessService = null;
    }

    /**
     * Test to check valid message processing.
     */
    @Test
    public void processValidMessage() {
        Map<String, Object> headers = new HashMap<String, Object>();

        headers.put("file_name", "valid-file-1.txt");
        headers.put("correlation_id", "1234567890");
        MessageHeaders messageHeaders = new MessageHeaders(headers);

        Message<?> message = MessageBuilder.createMessage("100\n200\n300", messageHeaders);
        Message<?> outputMessage = (Message<?>) fileProcessService.processMessage(message);

        Assert.assertEquals("Incoreect result!!!", "600", outputMessage.getPayload().toString());
        Assert.assertEquals("Filename Modified!!!", "valid-file-1.txt", outputMessage.getHeaders()
                .get("file_name"));
        Assert.assertEquals("Correlation ID Modified!!! ", "1234567890", outputMessage.getHeaders()
                .get("correlation_id"));
        Assert.assertNotNull("Original Message (file) could not be retained!!!", outputMessage
                .getHeaders().get("original_message"));
    }

    /**
     * Test to check invalid message processing.
     */
    @Test
    public void processInValidMessage() {
        Map<String, Object> headers = new HashMap<String, Object>();

        headers.put("file_name", "invalid-file-1.txt");
        headers.put("correlation_id", "1234567890");
        MessageHeaders messageHeaders = new MessageHeaders(headers);

        Message<?> message = MessageBuilder.createMessage("100\n200abc\n300", messageHeaders);
        Message<?> outputMessage = (Message<?>) fileProcessService.processMessage(message);

        Assert.assertEquals("Correlation ID Modified!!! ", "1234567890", outputMessage.getHeaders()
                .get("correlation_id"));
        Assert.assertNull("Original Message (file) retained!!! Not an invlid message",
                outputMessage.getHeaders().get("original_message"));
    }

}
