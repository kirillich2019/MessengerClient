package com.wg.messengerclient.models.server_answers;

public class AnswerWithError {
    private int error = 0;

    private String error_text = "";

    public int getError() {
        return error;
    }

    public String getError_text() { return error_text; }
}
