package com.sgedts.base.bean.internal;

import java.io.Serializable;

public class TokenValidationRequest implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
