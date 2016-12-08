package com.ndroidpro.customviewdemo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * Created by Nikhil Vashistha on 08-12-2016 for CustomViewDemo.
 */

public class BitmapLinUtils {
    private BitmapLinUtils() {
    }

    public static Bitmap readBitmap(Context context, int id) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        opt.inInputShareable = true;
        opt.inPurgeable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(id), null, opt);
    }
}
