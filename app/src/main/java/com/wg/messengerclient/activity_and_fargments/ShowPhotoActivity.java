package com.wg.messengerclient.activity_and_fargments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.wg.messengerclient.R;

public class ShowPhotoActivity extends AppCompatActivity {
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo_activity);

        String url = getIntent().getStringExtra("url");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);

        photoView = findViewById(R.id.photo_view);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
        .into(new SimpleTarget<Drawable>() {
                  @Override
                  public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                      photoView.setImageDrawable(resource);
                  }
              });
    }
}
