package com.wg.messengerclient.presenters.messageSystem.interfaces;

import com.wg.messengerclient.presenters.messageSystem.events.DialogStatusChangeEvent;

public interface MessageSystemEventsListner {
    void dialogStatusChange(DialogStatusChangeEvent event);
}
