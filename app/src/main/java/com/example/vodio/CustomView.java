package com.example.vodio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class CustomView extends View {
    String pathName = "/mnt/sdcard/DCIM/Camera/IMG_20200110_104110.jpg";
    Paint paint = new Paint();
    Bitmap bitmap;
    public CustomView(Context context) {
        super(context);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        bitmap = BitmapFactory.decodeFile(pathName);  // 获取bitmap
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 不建议在onDraw做任何分配内存的操作
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }
}
