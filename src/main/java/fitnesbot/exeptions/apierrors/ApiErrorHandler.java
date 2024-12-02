package fitnesbot.exeptions.apierrors;


public class ApiErrorHandler {
    private static final int NOT_MODIFIED = 304;
    private static final int NOT_FOUND = 404;
    private static final int E_TAG_TOKEN = 409;
    private static final int UNPROCESSABLE_ENTITY = 422;
    private static final int INVALID_RECIPE = 555;

    public String errorHandler(int errorCode) {
        return switch (errorCode) {
            case NOT_MODIFIED -> "Not Modified";
            case NOT_FOUND -> "Not Found - The specified URL was not found or couldn't be retrieved";
            case E_TAG_TOKEN -> "The provided ETag token does not match the input data";
            case UNPROCESSABLE_ENTITY ->
                    "Unprocessable Entity - Couldn't parse the recipe or extract the nutritional info";
            case INVALID_RECIPE -> "Recipe with insufficient quality to process correctly";
            default -> "Unknown error";
        };
    }
}
