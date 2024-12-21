package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

public class SleepInTakeService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final SleepInTakeRepository sleepInTakeRepository;

    public SleepInTakeService(SleepInTakeRepository sleepInTakeRepository) {
        this.sleepInTakeRepository = sleepInTakeRepository;
    }

    public MessageOutputData saveSleepInTake(long chatId, double quantity) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        sleepInTakeRepository.save(chatId, date, quantity);
        return new MessageOutputData("Количество сна добавлено", chatId);
    }

    public MessageOutputData getWeekSleepStat(long chatId) {
        double result = sleepInTakeRepository.getWeekStat(chatId);
        if (result > 0) {
            return (new MessageOutputData("Среднее количество сна за неделю: "
                    + result, chatId));
        }
        System.out.println("Не удалось найти информацию о  сне за неделю");
        return new MessageOutputData("К сожалению у вас нет записей за эту неделю", chatId);
    }

    public MessageOutputData getDaySleepStat(long chatId, String day) {
        String date = day.formatted(formatter);
        double result = sleepInTakeRepository.getDayStat(chatId, date);
        if (result != 0.0) {
            return new MessageOutputData("В этот день вы спали: " + result, chatId);
        }
        return new MessageOutputData("Нет статистики за  этот день", chatId);
    }

    public String getSleepChart(long chatId, double sleepGoal) {
        Map<String, Double> sleepData = sleepInTakeRepository.getDataByChatId(chatId);
        if (sleepData.isEmpty()) {
            return null;
        }
        try {
            JFreeChart sleepChart = createSleepChart(sleepData, "", "", sleepGoal);
            String imagePath = DirectoryService.createFile("SLeepImage_" + chatId + ".jpeg","src/main/charts");
            File lineChart = new File(imagePath);
            ChartUtils.saveChartAsJPEG(lineChart, sleepChart, 480, 640);
            return imagePath;
        } catch (Exception e) {
            System.out.println("Не удалось сохранить изоражение SleepImage");
            return null;
        }
    }

    private JFreeChart createSleepChart(Map<String, Double> sleepData, String start, String end, double sleepGoal) {
        TimeSeries series = new TimeSeries("Сон");
        TimeSeries seriesGoal = new TimeSeries("Цель");
        for (Map.Entry<String, Double> entry : sleepData.entrySet()) {
            try {
                LocalDate localDate = LocalDate.parse(entry.getKey(), formatter);
                System.out.println(localDate);
                Date date = java.sql.Date.valueOf(localDate);
                System.out.println(new Day(date));
                series.add(new Day(date), entry.getValue());
                seriesGoal.add(new Day(date), sleepGoal);
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка парсинга даты: " + entry.getKey());
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(seriesGoal);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Статистика сна",
                "Дата",
                "Время сна(часы)",
                dataset,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainGridlinePaint(Color.YELLOW);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        plot.setRenderer(renderer);
        return chart;
    }
}
