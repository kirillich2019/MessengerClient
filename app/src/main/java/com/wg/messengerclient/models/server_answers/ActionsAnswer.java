package com.wg.messengerclient.models.server_answers;

import java.util.List;

public class ActionsAnswer extends AnswerWithError {
    private List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }
}
