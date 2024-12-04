package fitnesbot.models;

public class TrainingSession {
    private final String name;
    private final double trainingTime;
    private final double caloriesBurned;
    private boolean deleted;

    public TrainingSession(String name, double trainingTime, double caloriesBurned) {
        this.name = name;
        this.trainingTime = trainingTime;
        this.caloriesBurned = caloriesBurned;
        this.deleted = false;
    }

    public String getName() {
        return name;
    }

    public double getTrainingTime() {
        return trainingTime;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }
    public String getInfo() {
        return String.format(
                "Тренировка: %s%nПродолжительность: %.2f ч%nСожжено калорий: %.2f",
                name, trainingTime, caloriesBurned
        );
    }
}
