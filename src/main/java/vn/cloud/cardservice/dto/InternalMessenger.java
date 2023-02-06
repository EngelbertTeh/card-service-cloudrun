package vn.cloud.cardservice.dto;

import lombok.Data;

@Data
public class InternalMessenger<T> {
    private T data;
    private boolean success;
    private String errorMessage;

    public InternalMessenger(T data, boolean success) {
        this.data = data;
        this.success = success;
    }
    public InternalMessenger(T data, boolean success, String errorMessage) {
        this.data = data;
        this.success = success;
        this.errorMessage = errorMessage;
    }

}

