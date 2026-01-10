package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * Réponse JSON standardisée pour les APIs.
 * Format souhaité:
 * {
 *   "success": true,
 *   "data": {},
 *   "message": "Opération réussie",
 *   "errors": null
 * }
 */
public class ApiResponse<T> {

    private static final Gson GSON = new GsonBuilder().create();

    private final boolean success;
    private final T data;
    private final String message;
    private final Object errors; // null ou Map/List détaillant les erreurs

    private ApiResponse(boolean success, T data, String message, Object errors) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errors = errors;
    }

    // Factories succès
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(true, data, "Opération réussie", null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, data, message, null);
    }

    // Factories erreur
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, null, message, null);
    }

    public static <T> ApiResponse<T> error(String message, Object errors) {
        return new ApiResponse<T>(false, null, message, errors);
    }

    // Ecriture pratique dans la réponse HTTP
    public void write(HttpServletResponse resp, int httpStatus) throws IOException {
        resp.setStatus(httpStatus);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(GSON.toJson(this));
    }

    // Getters
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public Object getErrors() { return errors; }
}
