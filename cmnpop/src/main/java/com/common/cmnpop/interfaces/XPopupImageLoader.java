package com.common.cmnpop.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.cmnpop.core.ImageViewerPopupView;
import com.common.cmnpop.photoview.PhotoView;

import java.io.File;

public interface XPopupImageLoader {

    void loadSnapshot(@NonNull Object uri, @NonNull PhotoView snapshot, @Nullable ImageView srcView);


    View loadImage(int position, @NonNull Object uri, @NonNull ImageViewerPopupView popupView, @NonNull PhotoView snapshot, @NonNull ProgressBar progressBar);

    /**
     * 获取图片对应的文件
     */
    File getImageFile(@NonNull Context context, @NonNull Object uri);
}
