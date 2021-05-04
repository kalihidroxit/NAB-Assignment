package com.lozado.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ApiResponse {

    private HttpStatus status;
    private String message;
    private Object data;
    private String error;
    private long timestamp = new Date().getTime();
    private int statusCode;

    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.statusCode = status.value();
    }

    public ApiResponse(HttpStatus status, Object data) {
        this.status = status;
        this.data = data;
        this.statusCode = status.value();
    }

    public ApiResponse(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = status.value();
    }

    public ApiResponse(HttpStatus status, String error, String message, Object resultObject) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = resultObject;
        this.timestamp = new Timestamp(new Date().getTime()).getTime();
        this.statusCode = status.value();
    }

    private ApiResponse(ApiResponseBuilder responseBuilder) {
        this.setStatus(responseBuilder.status);
        this.setMessage(responseBuilder.message);
        this.setData(responseBuilder.data);
        this.setError(responseBuilder.error);
        this.setTimestamp(responseBuilder.timestamp);
        this.setStatusCode(responseBuilder.status.value());
    }

    public static ApiResponse getFailureResponse(){
        return new ApiResponse(HttpStatus.BAD_REQUEST , "Something Went wrong!");
    }

    public static ApiResponse getSuccessResponse(){
        return new ApiResponse(HttpStatus.OK , "");
    }

    public static ApiResponse getFailureResponse(String message) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, message);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ApiResponse))
            return false;
        final ApiResponse other = (ApiResponse) o;
        if (!other.canEqual((Object) this))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (!Objects.equals(this$status, other$status))
            return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (!Objects.equals(this$message, other$message))
            return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (!Objects.equals(this$data, other$data))
            return false;
        final Object this$error = this.getError();
        final Object other$error = other.getError();
        if (!Objects.equals(this$error, other$error))
            return false;
        if (this.getTimestamp() != other.getTimestamp())
            return false;
        if (this.getStatusCode() != other.getStatusCode())
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        final Object $error = this.getError();
        result = result * PRIME + ($error == null ? 43 : $error.hashCode());
        final long $timestamp = this.getTimestamp();
        result = result * PRIME + (int) ($timestamp >>> 32 ^ $timestamp);
        result = result * PRIME + this.getStatusCode();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ApiResponse;
    }

    public String toString() {
        return "ApiResponse(status=" + this.getStatus() + ", message=" + this.getMessage() + ", data=" + this.getData()
                + ", error=" + this.getError() + ", timestamp=" + this.getTimestamp() + ", statusCode="
                + this.getStatusCode() + ")";
    }

    public static class ApiResponseBuilder {
        private HttpStatus status;
        private String message;
        private Object data;
        private String error;
        private long timestamp = System.currentTimeMillis();

        public ApiResponse build() {
            return new ApiResponse(this);
        }

        public ApiResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder setData(Object data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder setError(String error) {
            this.error = error;
            return this;
        }

        public ApiResponseBuilder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }
    }
}
