package com.wg.messengerclient.models.server_answers;

import com.google.gson.annotations.SerializedName;

public class AnswerWithError {
    private int error = 0;

    @SerializedName("error_text")
    private String errorText = "";

    public int getError() {
        return error;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
