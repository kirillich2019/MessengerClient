package com.wg.messengerclient.activity_and_fargments;

import android.content.Context;
import android.content.Intent;

public class ShowingPhoto {
    public static void ShowPhoto(Context context, String url){
        Intent intent = new Intent(context, ShowPhotoActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
