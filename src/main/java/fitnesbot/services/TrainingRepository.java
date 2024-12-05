package fitnesbot.services;

import fitnesbot.models.TrainingSession;

import java.util.List;

public interface TrainingRepository {
    void save(long chatId, String date, TrainingSession trainingSession);

    List<TrainingSession> findByChatId(long chatId);

    List<TrainingSession> findByDate(long chatId, String date);

    void deleteSession(long chatId, String date, String sessionName);
}
