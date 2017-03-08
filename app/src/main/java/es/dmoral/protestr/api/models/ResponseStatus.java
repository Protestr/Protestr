package es.dmoral.protestr.api.models;

/**
 * Created by grender on 13/02/17.
 */

public class ResponseStatus {
    public static final int STATUS_OK = 200;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int STATUS_NOT_FOUND = 404;

    private int status;
    private String message;

    public ResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
