package LogFileProcessor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Stack;

public class SessionManager {
    private Stack<LocalTime> startTimes;
    private int totalDuration;
    private int sessionCount;

    public SessionManager() {
        startTimes = new Stack<>();
        totalDuration = 0;
        sessionCount = 0;
    }

    public void startSession(LocalTime startTime) {
        //add start time to stack
        startTimes.push(startTime);
    }

    public void endSession(LocalTime endTime, LocalTime fallbackStartTime) {
        if (startTimes.isEmpty()) {
            addSession(fallbackStartTime, endTime);
        } else {
            LocalTime startTime = startTimes.pop();
            addSession(startTime, endTime);
        }
    }

    public void finaliseSessions(LocalTime fallbackEndTime) {
        while (!startTimes.isEmpty()) {
            LocalTime startTime = startTimes.pop();
            addSession(startTime, fallbackEndTime);
        }
    }

    private void addSession(LocalTime start, LocalTime end) {
        int duration = (int) Duration.between(start, end).getSeconds();
        totalDuration += duration;
        sessionCount++;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getSessionCount() {
        return sessionCount;
    }
}
