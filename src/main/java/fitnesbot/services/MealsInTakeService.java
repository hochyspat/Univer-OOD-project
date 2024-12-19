package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.Meal;
import fitnesbot.models.MealsInTake;
import fitnesbot.models.Nutrient;
import fitnesbot.models.ParsedMeal;
import fitnesbot.services.enums.MealType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class MealsInTakeService {
    private final MealsInTakeRepository mealsIntakeRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final WaterInTakeRepository waterInTakeRepository;

    public MealsInTakeService(MealsInTakeRepository mealsIntakeRepository,
                              WaterInTakeRepository waterInTakeRepository) {
        this.mealsIntakeRepository = mealsIntakeRepository;
        this.waterInTakeRepository = waterInTakeRepository;
    }

    public MessageOutputData saveMealIntake(MealsInTake mealInTake, long chatId, MealType mealType) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);

        mealsIntakeRepository.save(mealInTake, chatId, date, mealType);

        return new MessageOutputData("Отлично! Запись в дневник осуществлена."
                + " Введи /help для справки или /menu для выбора команд", chatId);
    }

    public MessageOutputData saveWaterInTake(long chatId) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        waterInTakeRepository.save(chatId, date);
        return new MessageOutputData("Прием воды добавлен", chatId);
    }

    public int getWaterInTake(long chatId, String date) {
        date = date.formatted(formatter);
        return waterInTakeRepository.findWaterInTakeByDate(chatId, date);

    }

    public MealsInTake getMealsInTake(String date, MealType mealType, long chatId) {
        date = date.formatted(formatter);
        return mealsIntakeRepository.findByMealsInTakeType(mealType, date, chatId);
    }

    public void deleteMealInTake(long chatId, String date, MealType mealType) {
        mealsIntakeRepository.deleteMealType(mealType, date, chatId);
    }

    public String getMealPFC(long chatId) {
        Map<String, List<MealsInTake>> mealPFCdata = mealsIntakeRepository.getDataById(chatId);

        if (mealPFCdata.isEmpty()) {
            return null;
        }
        try {
            JFreeChart sleepChart = createMealPFCChart(mealPFCdata, "", "");
            String imagePath = "C:\\Users\\USVER\\IdeaProjects"
                    + "\\Univer-OOD-project\\src\\main\\charts/MealImage_" + chatId + ".jpeg";
            File lineChart = new File(imagePath);
            ChartUtils.saveChartAsJPEG(lineChart, sleepChart, 480, 640);
            return imagePath;
        } catch (Exception e) {
            System.out.println("Не удалось сохранить изоражение SleepImage");
            return null;
        }
    }

    private JFreeChart createMealPFCChart(Map<String, List<MealsInTake>> mealPDCdata, String start, String end) {
        double proteins = 0;
        double fat = 0;
        double carbs = 0;
        for (Map.Entry<String, List<MealsInTake>> entry : mealPDCdata.entrySet()) {
            try {
                for (MealsInTake mealsInTake : entry.getValue()) {
                    for (ParsedMeal parsedMeal : mealsInTake.getMeals()) {
                        for (Meal meal : parsedMeal.getParsedMeals()) {
                            proteins += meal.nutrients().get("PROCNT").getQuantity();
                            fat += meal.nutrients().get("FAT").getQuantity();
                            carbs += meal.nutrients().get("CHOCDF").getQuantity();
                        }
                    }
                }
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка парсинга даты: " + entry.getKey());
            }
        }
        proteins = Math.round(proteins * 100.0) / 100.0;
        fat = Math.round(fat * 100.0) / 100.0;
        carbs = Math.round(carbs * 100.0) / 100.0;
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("БЕЛКИ", proteins);
        dataset.setValue("ЖИРЫ", fat);
        dataset.setValue("УГЛЕВОДЫ", carbs);


        JFreeChart chart = ChartFactory.createPieChart(
                "Статистика БЖУ",
                dataset,
                false,
                true,
                false
        );
        TextTitle subtitle = new TextTitle(
                String.format(
                        "Всего:%n%.2f г белков%n%.2f г жиров%n%.2f г углеводов",
                        proteins, fat, carbs
                ),
                new Font("Arial", Font.PLAIN, 20)
        );
        subtitle.setPaint(Color.BLACK);
        subtitle.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(subtitle);

        chart.setBackgroundPaint(new Color(49, 76, 221));
        return chart;
    }


    public String getWaterChart(long chatId, double waterGoal) {
        Map<String, Integer> waterData = waterInTakeRepository.getWaterByChatId(chatId);
        if (waterData.isEmpty()) {
            return null;
        }
        try {
            JFreeChart sleepChart = createWaterChart(waterData, "", "", waterGoal);
            String imagePath = "C:\\Users\\USVER\\IdeaProjects\\"
                    + "Univer-OOD-project\\src\\main\\charts/WaterImage_" + chatId + ".jpeg";
            File lineChart = new File(imagePath);
            ChartUtils.saveChartAsJPEG(lineChart, sleepChart, 480, 640);
            return imagePath;
        } catch (Exception e) {
            System.out.println("Не удалось сохранить изоражение SleepImage");
            return null;
        }
    }

    private JFreeChart createWaterChart(Map<String, Integer> waterData, String start, String end, double waterGoal) {
        TimeSeries series = new TimeSeries("Вода");
        TimeSeries seriesGoal = new TimeSeries("Цель");
        for (Map.Entry<String, Integer> entry : waterData.entrySet()) {
            try {
                LocalDate localDate = LocalDate.parse(entry.getKey(), formatter);
                System.out.println(localDate);
                Date date = java.sql.Date.valueOf(localDate);
                System.out.println(new Day(date));
                series.add(new Day(date), entry.getValue() * 200);
                seriesGoal.add(new Day(date), waterGoal);
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка парсинга даты: " + entry.getKey());
            }
        }

        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        dataSet.addSeries(seriesGoal);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Статистика воды",
                "Дата",
                "Количество воды в мл",
                dataSet,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(new Color(103, 246, 98));
        XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainGridlinePaint(Color.YELLOW);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        renderer.setSeriesPaint(0, Color.BLACK);
        plot.setRenderer(renderer);
        return chart;
    }

}
