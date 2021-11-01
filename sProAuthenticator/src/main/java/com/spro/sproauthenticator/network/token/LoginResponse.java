package com.spro.sproauthenticator.network.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;

    public boolean isStatus() {
        return (code==0);
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("otp_expire_time")
        private String otp_expire_time;

        @SerializedName("otp")
        private String otp;

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getOtp_expire_time() {
            return otp_expire_time;
        }

        public void setOtp_expire_time(String otp_expire_time) {
            this.otp_expire_time = otp_expire_time;
        }

        @Override
        public String toString() {
            return "Data {" +
                    "otp" + otp +
                    ", otp_expire_time='" + otp_expire_time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success" + success +
                "code" + code +
                ", message='" + message +
                ", data='" + data.toString() + '\'' +
                '}';
    }
}