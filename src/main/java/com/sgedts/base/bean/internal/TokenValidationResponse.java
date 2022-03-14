package com.sgedts.base.bean.internal;

import java.io.Serializable;

public class TokenValidationResponse implements Serializable {
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
