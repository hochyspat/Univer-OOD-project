package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.TrainingSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public MessageOutputData addTraining(long chatId,
                                         String name,
                                         double trainingTime,
                                         double caloriesBurned) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        TrainingSession session = new TrainingSession(name, trainingTime, caloriesBurned);
        trainingRepository.save(chatId, date, session);
        return new MessageOutputData("Тренировка \""
                + session.getName() + "\" успешно добавлена!", chatId);
    }

    public MessageOutputData getTrainingSessions(long chatId) {
        List<TrainingSession> sessions = trainingRepository.findByChatId(chatId);
        if (sessions.isEmpty()) {
            return new MessageOutputData("Тренировки не найдены.", chatId);
        }
        StringBuilder response = new StringBuilder("Ваши тренировки:\n");
        for (TrainingSession session : sessions) {
            response.append(session.getInfo());
            response.append("\n");
        }
        return new MessageOutputData(response.toString(), chatId);
    }

    public MessageOutputData getTrainingSessionsByDate(long chatId, String date) {
        date = date.formatted(formatter);
        List<TrainingSession> sessions = trainingRepository.findByDate(chatId, date);
        if (sessions.isEmpty()) {
            return new MessageOutputData("Тренировки на дату " + date + " не найдены.", chatId);
        }
        StringBuilder response = new StringBuilder("Тренировки на " + date + ":\n");
        for (TrainingSession session : sessions) {
            response.append(session.getInfo());
            response.append("\n");
        }
        return new MessageOutputData(response.toString(), chatId);
    }

    public MessageOutputData deleteTrainingSession(long chatId, String date, String sessionName) {
        date = date.formatted(formatter);
        if (trainingRepository.findByDate(
                chatId, date).stream().noneMatch(session -> session.getName().equals(sessionName))) {
            return new MessageOutputData("Тренировка \""
                    + sessionName + "\" на дату " + date + " не найдена или уже удалена.", chatId);
        }
        trainingRepository.deleteSession(chatId, date, sessionName);
        return new MessageOutputData("Тренировка \""
                + sessionName + "\" успешно удалена.", chatId);
    }
}
