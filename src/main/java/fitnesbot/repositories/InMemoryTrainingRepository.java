package fitnesbot.repositories;

import fitnesbot.models.TrainingSession;
import fitnesbot.services.TrainingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTrainingRepository implements TrainingRepository {
    private final Map<Long, Map<String, List<TrainingSession>>> userTrainingDiary = new HashMap<>();

    @Override
    public void save(long chatId, String date, TrainingSession session) {
        userTrainingDiary
                .computeIfAbsent(chatId, k -> new HashMap<>())
                .computeIfAbsent(date, k -> new ArrayList<>())
                .add(session);
    }

    @Override
    public List<TrainingSession> findByChatId(long chatId) {
        Map<String, List<TrainingSession>> userSessions = userTrainingDiary.get(chatId);
        if (userSessions == null) {
            return new ArrayList<>();
        }
        List<TrainingSession> allSessions = new ArrayList<>();
        for (List<TrainingSession> sessions : userSessions.values()) {
            allSessions.addAll(sessions);
        }
        return allSessions;
    }

    @Override
    public List<TrainingSession> findByDate(long chatId, String date) {
        Map<String, List<TrainingSession>> userSessions = userTrainingDiary.get(chatId);
        if (userSessions == null) {
            return new ArrayList<>();
        }
        return userSessions.getOrDefault(date, new ArrayList<>());
    }

    @Override
    public void deleteSession(long chatId, String date, String sessionName) {
        Map<String, List<TrainingSession>> userSessions = userTrainingDiary.get(chatId);
        if (userSessions == null) {
            System.out.println("No training sessions found for the user.");
            return;
        }
        List<TrainingSession> sessions = userSessions.get(date);
        if (sessions == null) {
            System.out.println("No training sessions found for the specified date.");
            return;
        }
        sessions.remove(sessionName);
        System.out.println("Session not found or already deleted.");
    }
}
