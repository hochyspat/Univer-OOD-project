package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.TrainingSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public String getTrainingCaloriesChart(long chatId) {
        Map<String, List<TrainingSession>> trainingData = trainingRepository.getTrainingByChatId(chatId);
        if (trainingData.isEmpty()) {
            return null;
        }
        try {
            JFreeChart trainingCaloriesChart = createTrainingCaloriesChart(trainingData, "", "");
            String imagePath = DirectoryService.createFile("TrainingCalories_" + chatId + ".jpeg",
                    "src/main/charts");
            File lineChart = new File(imagePath);
            ChartUtils.saveChartAsJPEG(lineChart, trainingCaloriesChart, 480, 640);
            return imagePath;
        } catch (Exception e) {
            System.out.println("Не удалось сохранить изоражение TrainingCaloriesImage");
            return null;
        }
    }

    public JFreeChart createTrainingCaloriesChart(Map<String, List<TrainingSession>> trainingData,
                                                  String start, String end) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double sumCalories = 0;
        for (Map.Entry<String, List<TrainingSession>> entry : trainingData.entrySet()) {
            try {
                LocalDate localDate = LocalDate.parse(entry.getKey(), formatter);
                System.out.println(localDate);
                Date date = java.sql.Date.valueOf(localDate);
                System.out.println(new Day(date));
                sumCalories = 0;
                for (TrainingSession training : entry.getValue()) {
                    sumCalories += training.getCaloriesBurned();
                }
                dataset.addValue(sumCalories, "Калории", entry.getKey());
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка парсинга даты: " + entry.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Сожженные калории за текущий период",
                null,
                "Калории",
                dataset);
        chart.setBackgroundPaint(new Color(181, 146, 221, 210));
        return chart;
    }
}
