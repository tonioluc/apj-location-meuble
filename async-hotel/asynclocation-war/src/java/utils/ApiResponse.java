package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilitaire standard pour formater les réponses JSON des Web Services.
 *
 * Exemple de structure:
 * {
 *   "success": true,
 *   "message": "...",
 *   "data": { ... },
 *   "code": "OPTIONAL",
 *   "timestamp": 1699999999999
 * }
 */
public class ApiResponse<T> {

    private static final Gson GSON = new GsonBuilder().create();

    private final boolean success;
    private final String message;
    private final T data;
    private final String code; // optionnel (ex: code d'erreur/metier)
    private final long timestamp;

    private ApiResponse(boolean success, String message, T data, String code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    // Factories succès
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(true, "OK", data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, message, data, null);
    }

    // Factories erreur
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<T>(false, message, null, code);
    }

    // Ecriture pratique dans la réponse HTTP
    public void write(HttpServletResponse resp, int httpStatus) throws IOException {
        resp.setStatus(httpStatus);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(GSON.toJson(this));
    }

    // Getters nécessaires pour la sérialisation/lecture éventuelle
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getCode() { return code; }
    public long getTimestamp() { return timestamp; }
}
