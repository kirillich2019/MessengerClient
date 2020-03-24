package com.wg.messengerclient.presenters.messageSystem.interfaces;

import com.wg.messengerclient.presenters.messageSystem.events.DataLoadingSatusChangeEvent;
import com.wg.messengerclient.presenters.messageSystem.events.DialogStatusChangeEvent;

public interface MessageSystemEventsListner {
    void dialogListLoadingStatusChange(DataLoadingSatusChangeEvent event);

    void dialogStatusChange(DialogStatusChangeEvent event);
}
