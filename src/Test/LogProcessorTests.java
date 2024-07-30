package Test;
import org.junit.Before;
import org.junit.Test;
import LogFileProcessor.LogProcessor;
import static org.junit.Assert.assertEquals;

public class LogProcessorTests {

    private LogProcessor logProcessor;

    @Before
    public void setUp() {
        logProcessor = new LogProcessor();
    }

    @Test
    public void testProcessLineAndFinalizeSessions() {
        logProcessor.processLine("14:02:03 ALICE99 Start");
        logProcessor.processLine("14:02:34 ALICE99 End");
        logProcessor.finaliseSessions();
        
        assertEquals(1, logProcessor.userSessions.get("ALICE99").getSessionCount());
        assertEquals(31, logProcessor.userSessions.get("ALICE99").getTotalDuration());
    }

    @Test
    public void testProcessLineWithMissingStart() {
        logProcessor.processLine("14:02:05 CHARLIE End");
        logProcessor.finaliseSessions();

        assertEquals(1, logProcessor.userSessions.get("CHARLIE").getSessionCount());
        assertEquals(0, logProcessor.userSessions.get("CHARLIE").getTotalDuration());
    }

    @Test
    public void testProcessLineWithMissingEnd() {
        logProcessor.processLine("14:02:03 ALICE99 Start");
        logProcessor.finaliseSessions();

        assertEquals(1, logProcessor.userSessions.get("ALICE99").getSessionCount());
        assertEquals(0, logProcessor.userSessions.get("ALICE99").getTotalDuration());
    }

    @Test
    public void testMultipleSessions() {
        logProcessor.processLine("14:02:03 ALICE99 Start");
        logProcessor.processLine("14:02:05 CHARLIE End");
        logProcessor.processLine("14:02:34 ALICE99 End");
        logProcessor.processLine("14:02:58 ALICE99 Start");
        logProcessor.processLine("14:03:02 CHARLIE Start");
        logProcessor.processLine("14:03:33 ALICE99 Start");
        logProcessor.processLine("14:03:35 ALICE99 End");
        logProcessor.processLine("14:03:37 CHARLIE End");
        logProcessor.processLine("14:04:05 ALICE99 End");
        logProcessor.processLine("14:04:23 ALICE99 End");
        logProcessor.processLine("14:04:41 CHARLIE Start");
        logProcessor.finaliseSessions();

        assertEquals(4, logProcessor.userSessions.get("ALICE99").getSessionCount());
        assertEquals(240, logProcessor.userSessions.get("ALICE99").getTotalDuration());
        assertEquals(3, logProcessor.userSessions.get("CHARLIE").getSessionCount());
        assertEquals(37, logProcessor.userSessions.get("CHARLIE").getTotalDuration());
    }
}
