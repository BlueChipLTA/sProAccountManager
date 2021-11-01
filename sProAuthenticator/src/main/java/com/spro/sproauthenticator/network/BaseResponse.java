package com.spro.sproauthenticator.network;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code" + code +
                ", message='" + message +
                ", data='" + data + '\'' +
                '}';
    }
}
