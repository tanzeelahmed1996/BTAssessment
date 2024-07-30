package LogFileProcessor;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class LogProcessor {
    public Map<String, SessionManager> userSessions;
    private LocalTime earliestTime;
    private LocalTime latestTime;

    public LogProcessor() {
        userSessions = new HashMap<>();
        earliestTime = null;
        latestTime = null;
    }

    public void processLine(String line) {
        String[] parts = line.split(" ");
        if (parts.length != 3) return; // Ignore invalid lines

        try {
            LocalTime time = LocalTime.parse(parts[0]);
            String user = parts[1];
            String action = parts[2];

            if (earliestTime == null || time.isBefore(earliestTime)) {
                earliestTime = time;
            }
            if (latestTime == null || time.isAfter(latestTime)) {
                latestTime = time;
            }

            userSessions.putIfAbsent(user, new SessionManager());
            if ("Start".equals(action)) {
                userSessions.get(user).startSession(time);
            } else if ("End".equals(action)) {
                userSessions.get(user).endSession(time, earliestTime);
            }
        } catch (Exception e) {
            // Ignore invalid lines
        }
    }

    public void finaliseSessions() {
        for (SessionManager manager : userSessions.values()) {
            manager.finaliseSessions(latestTime);
        }
    }

    public void printReport() {
        for (Map.Entry<String, SessionManager> entry : userSessions.entrySet()) {
            String user = entry.getKey();
            SessionManager manager = entry.getValue();
            System.out.println(user + " " + manager.getSessionCount() + " " + manager.getTotalDuration());
        }
    }
}
