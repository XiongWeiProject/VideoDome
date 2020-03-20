package com.example.vodio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.example.vodio.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private String TAG = "MainActivity";
    String pathName = "/mnt/sdcard/DCIM/Camera/IMG_20200110_104110.jpg";
    SurfaceView surfaceView;

    String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mainBinding.testView.setText("视图绑定");
        imageView = mainBinding.ivPic;
        surfaceView = mainBinding.sfPic;
        setContentView(mainBinding.getRoot());
        //安卓动态权限申请
        //权限确认
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (!mPermissionList.isEmpty()) {
            //请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        //IMageVIew绘制图片
        IVViewPic();
        //Swif画图片
        SFViewPic();
        //自义定画图
        CustomView();

    }

    private void CustomView() {

    }

    private void SFViewPic() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (holder==null)
                    return;
                Paint paint = new Paint();
                paint.setAntiAlias(true);//设置画笔锯齿效果
                paint.setStyle(Paint.Style.STROKE);
                Bitmap bitmap = BitmapFactory.decodeFile(pathName);
                Canvas canvas = holder.lockCanvas();// 先锁定当前surfaceView的画布
                canvas.drawBitmap(bitmap,0,10,paint);//画图
                holder.unlockCanvasAndPost(canvas);//解锁
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    private void IVViewPic() {
        //width: 263,ImageView实际比较小
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,加载图片时不会获取到bitmap对象,但是可以拿到图片的宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        //计算采样率,对图片进行相应的缩放
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Log.d(TAG, "outWidth: " + outWidth + ",outHeight:" + outHeight);
        float widthRatio = outWidth * 1.0f / width;
        float heightRatio = outHeight * 1.0f / height;
        Log.d(TAG, "widthRatio: " + widthRatio + ",heightRatio:" + heightRatio);
        float max = Math.max(widthRatio, heightRatio);
        //向上舍入
        int inSampleSize = (int) Math.ceil(max);
        Log.d(TAG, "inSampleSize: " + inSampleSize);
        //改为false,因为要获取采样后的图片了
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap1 = BitmapFactory.decodeFile(pathName, options);
        //采样后图片大小:144000,是采样前图片的inSampleSize*inSampleSize分之一(1/64)
        imageView.setImageBitmap(bitmap);
    }
    //
}
