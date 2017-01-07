package ch.christofbuechi.delaywarner.network;

import ch.christofbuechi.delaywarner.network.APIError;
import retrofit2.Response;

/**
 * Created by Christof on 07.01.2017.
 */
public class APIException extends Exception {


    private final int code;
    private final Response<?> response;
    private final APIError apiError;

    public APIException(Response<?> response, APIError apiError) {
        super("HTTP " + response.code() + " " + response.message());
        this.code = response.code();
        this.response = response;
        this.apiError = apiError;
    }

    public <T> APIException(Response<T> response) {
        super("HTTP " + response.code() + " " + response.message());
        this.code = response.code();
        this.response = response;
        this.apiError = null;

    }

    public int code() {
        return code;
    }

    public Response<?> response() {
        return response;
    }

    public APIError getAPIError() {
        return apiError;
    }
}
