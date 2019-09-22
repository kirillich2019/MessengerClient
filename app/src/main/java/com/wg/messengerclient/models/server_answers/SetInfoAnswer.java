package com.wg.messengerclient.models.server_answers;

import java.util.List;
import java.util.Map;

public class SetInfoAnswer extends AnswerWithError {
    private List<Integer> errors;

    private Map<Integer, String> error_texts;

    public List<Integer> getErrors() {
        return errors;
    }

    public Map<Integer, String> getError_texts() {
        return error_texts;
    }
}
