package fitnesbot.exeptions.apierrors;

public class InputIngredientsError extends ApiErrors {

    public InputIngredientsError() {
        super("Невозможно рассчитать питательную ценность некоторых ингредиентов. "
                + "Пожалуйста, проверьте написание ингредиентов и укажите их количество.");
    }
}
