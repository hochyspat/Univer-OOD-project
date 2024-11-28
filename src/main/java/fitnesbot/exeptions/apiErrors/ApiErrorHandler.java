package fitnesbot.exeptions.apiErrors;


public class ApiErrorHandler {
    private final int NOT_MODIFIED = 304;
    private final int NOT_FOUND = 404;
    private final int E_TAG_TOKEN = 409;
    private final int UNPROCESSABLE_ENTITY = 422;
    private final int INVALID_RECIPE = 555;

    public String errorHandler(int errorCode) {
        switch (errorCode) {
            case NOT_MODIFIED:
                return "Not Modified";
            case NOT_FOUND:
                return "Not Found - The specified URL was not found or couldn't be retrieved";
            case E_TAG_TOKEN:
                return "The provided ETag token does not match the input data";
            case UNPROCESSABLE_ENTITY:
                return "Unprocessable Entity - Couldn't parse the recipe or extract the nutritional info";
            case INVALID_RECIPE:
                return "Recipe with insufficient quality to process correctly";
            default:
                return "Unknown error";
        }
    }
}
