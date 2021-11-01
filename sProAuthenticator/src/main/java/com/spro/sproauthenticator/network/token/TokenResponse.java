package com.spro.sproauthenticator.network.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokenResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;

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


    @Override
    public String toString() {
        return "TokenResponse{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("msisdn")
        @Expose
        private Object msisdn;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;
        @SerializedName("last_logout")
        @Expose
        private String lastLogout;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("role")
        @Expose
        private List<String> role = null;
        @SerializedName("token_type")
        @Expose
        private String tokenType;
        @SerializedName("access_token")
        @Expose
        private String accessToken;
        @SerializedName("expires_in")
        @Expose
        private Integer expiresIn;
        @SerializedName("refresh_token")
        @Expose
        private String refreshToken;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public Object getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(Object msisdn) {
            this.msisdn = msisdn;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getLastLogout() {
            return lastLogout;
        }

        public void setLastLogout(String lastLogout) {
            this.lastLogout = lastLogout;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", fullname='" + fullname + '\'' +
                    ", msisdn=" + msisdn +
                    ", email='" + email + '\'' +
                    ", status=" + status +
                    ", lastLogin='" + lastLogin + '\'' +
                    ", lastLogout='" + lastLogout + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", role=" + role +
                    ", tokenType='" + tokenType + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", expiresIn=" + expiresIn +
                    ", refreshToken='" + refreshToken + '\'' +
                    '}';
        }
    }

}
