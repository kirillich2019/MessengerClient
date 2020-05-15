package com.wg.messengerclient;

import androidx.annotation.NonNull;

public class AddingPortToUrl {
    public static String addPort(@NonNull String urlWithoutPort, @NonNull String port){
        if(urlWithoutPort == null)
            return urlWithoutPort;

        /*int i = urlWithoutPort.indexOf(":" + port);
        if(i != -1)
            return urlWithoutPort;*/

        int index = -1;
        int count = 0;

        while (true){
            int newIndex = urlWithoutPort.indexOf('/', index + 1);

            if(newIndex == -1)
                break;

            count++;
            index = newIndex;

            if(count >= 3)
                break;
        }

        if(index == -1)
            return urlWithoutPort;

        StringBuffer sb = new StringBuffer(urlWithoutPort);

        sb.insert(index, ":" + port);

        return sb.toString();
    }
}
