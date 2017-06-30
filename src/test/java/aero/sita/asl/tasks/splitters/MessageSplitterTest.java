package aero.sita.asl.tasks.splitters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import aero.sita.asl.tasks.service.FileProcessService;

/**
 * Test class for MessageSplitter.
 * 
 * @author prashant.soam
 * 
 */
public class MessageSplitterTest {

    private MessageSplitter messageSplitter;
    private FileProcessService fileProcessService;

    /**
     * Setting the resources ready
     */
    @Before
    public void setUp() {
        messageSplitter = new MessageSplitter();
        fileProcessService = new FileProcessService();
    }

    /**
     * Tidying resources
     */
    @After
    public void tearDown() {
        messageSplitter = null;
        fileProcessService = null;
    }

    /**
     * Test to check message splitter splitting message correctly.
     */
    @Test
    public void checkOutputMessageSplitting() {
        Map<String, Object> headers = new HashMap<String, Object>();

        headers.put("file_name", "valid-file-1.txt");
        headers.put("correlation_id", "1234567890");
        MessageHeaders messageHeaders = new MessageHeaders(headers);

        Message<?> message = MessageBuilder.createMessage("100\n200\n300", messageHeaders);
        // Calling file process service to get input for message splitter.
        Message<?> outputMessage = (Message<?>) fileProcessService.processMessage(message);

        List<Message<?>> messageList = (List<Message<?>>) messageSplitter
                .splitFileMessage(outputMessage);

        Assert.assertEquals(
                "Messages couldn't split correctly, Incorrect number of messages received!!!", 2,
                messageList.size());
        Assert.assertEquals("Original Message not received!!!", "1234567890", messageList.get(1)
                .getHeaders().get("correlation_id"));
        Assert.assertEquals("Output Message was not marked as OUTPUT!!", "OUTPUT",
                messageList.get(0).getHeaders().get("status"));
        Assert.assertEquals("Original Message was not marked as PROCESSED!!", "PROCESSED",
                messageList.get(1).getHeaders().get("status"));
    }

    /**
     * Test to check message splitter splitting message correctly.
     */
    @Test
    public void checkErrorMessageSplitting() {
        Map<String, Object> headers = new HashMap<String, Object>();

        headers.put("file_name", "invalid-file-1.txt");
        headers.put("correlation_id", "1234567890");
        MessageHeaders messageHeaders = new MessageHeaders(headers);

        Message<?> message = MessageBuilder.createMessage("100\n200abc\n300", messageHeaders);
        // Calling file process service to get input for message splitter.
        Message<?> outputMessage = (Message<?>) fileProcessService.processMessage(message);

        List<Message<?>> messageList = (List<Message<?>>) messageSplitter
                .splitFileMessage(outputMessage);

        Assert.assertEquals(
                "Messages couldn't split correctly, Incorrect number of message(s) received!!!", 1,
                messageList.size());
        Assert.assertEquals("Original Message not received!!!", "1234567890", messageList.get(0)
                .getHeaders().get("correlation_id"));
        Assert.assertEquals("Original Message was not marked as ERROR!!", "ERROR",
                messageList.get(0).getHeaders().get("status"));

    }

}
