package fitnesbot.services;

import fitnesbot.models.TrainingSession;

import java.util.List;
import java.util.Map;

public interface TrainingRepository {
    void save(long chatId, String date, TrainingSession trainingSession);

    List<TrainingSession> findByChatId(long chatId);

    List<TrainingSession> findByDate(long chatId, String date);

    Map<String, List<TrainingSession>> getTrainingByChatId(long chatId);

    void deleteSession(long chatId, String date, String sessionName);
}
