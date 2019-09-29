package com.wg.messengerclient.models.server_answers;

import java.util.List;

public class FriendsRequestsAnswer extends AnswerWithError {
    private List<Integer> ids;


    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
