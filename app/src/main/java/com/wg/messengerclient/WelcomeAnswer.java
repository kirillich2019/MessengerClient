package com.wg.messengerclient;

import com.wg.messengerclient.models.server_answers.AnswerWithError;

public class WelcomeAnswer extends AnswerWithError {
    private String text;

    public String getText() { return text; }
}
