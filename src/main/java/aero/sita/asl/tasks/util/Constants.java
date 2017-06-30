package aero.sita.asl.tasks.util;

/**
 * Constant class to keep application wide constants.
 * 
 * @author prashant.soam
 * 
 */
public class Constants {

    /** Message header status key. **/
    public static final String MESSAGE_HEADER_STATUS_KEY = "status";
    /** Message header status output. **/
    public static final String MESSAGE_HEADER_STATUS_OUTPUT = "OUTPUT";
    /** Message header status processed. **/
    public static final String MESSAGE_HEADER_STATUS_PROCESSED = "PROCESSED";
    /** Message header status error. **/
    public static final String MESSAGE_HEADER_STATUS_ERROR = "ERROR";
    /** Message header original message key. **/
    public static final String MESSAGE_HEADER_ORIGINAL_MESSAGE_KEY = "original_message";
    /** File received Message. **/
    public static final String MSG_RECEIVED_FILE = "File: '%s' received for procesing!!!";
    /** Process file Message. **/
    public static final String MSG_PROCESS_FILE = "Processing Message (file)";
    /** Building output file Message. **/
    public static final String MSG_BUILDING_OUTPUT_FILE = "Building output Message (file)";
    /** Splitting output file Message. **/
    public static final String MSG_SPLITTING_OUTPUT_MESSAGE = "Splitting output Message into output and original messages (files) for further routing";
    /** Error in processing file Message. **/
    public static final String MSG_ERROR_PROCESSING_FILE_MESSAGE = "Error in processing file";
}
