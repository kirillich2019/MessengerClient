package com.wg.messengerclient.presenters.messageSystem.events;

import java.util.EventObject;

public class DataLoadingSatusChangeEvent extends EventObject {
    private DataLoadingStatus dataLoadingStatus;

    public DataLoadingStatus getDataLoadingStatus() {
        return dataLoadingStatus;
    }

    public DataLoadingSatusChangeEvent(Object source, DataLoadingStatus dataLoadingStatus) {
        super(source);
        this.dataLoadingStatus = dataLoadingStatus;
    }

    public DataLoadingSatusChangeEvent(DataLoadingStatus dataLoadingStatus){
        super(null);
    }
}

