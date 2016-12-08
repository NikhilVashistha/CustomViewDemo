package com.ndroidpro.customviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.util.LinkedList;
import com.ndroidpro.customviewdemo.Listener.TimeData;

/**
 * Created by Nikhil Vashistha on 08-12-2016 for CustomViewDemo.
 */

public class MonitorTocoEcgView extends View {
    private Bitmap beatZdbmp;
    private Bitmap bgBitmap;
    private final int breakValue;
    private Context context;
    private Listener.TimeData currData;
    private LinkedList<Listener.TimeData> dataList;
    private int dataListRemoveCount;
    private float fhrBottom;
    private final int fhrMax;
    private final int fhrMin;
    private float fhrTop;
    private float fhrVer;
    private Paint mFhr1Paint;
    private Paint mTocoPaint;
    private final int maxSize;
    private final int num;
    private float oneWidth;
    private int screenHeight;
    private int screenWidth;
    private float tocoBottom;
    private final int tocoMax;
    private final int tocoMin;
    private Bitmap tocoResetBmp;
    private float tocoTop;
    private float tocoVer;
    private int wNum;
    private float wide;

    public MonitorTocoEcgView(Context context) {
        this(context, null, 0);
        this.context = context;
        initBitmap();
    }

    public MonitorTocoEcgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        initBitmap();
    }

    public MonitorTocoEcgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.num = 17;
        this.wNum = 9;
        this.breakValue = 30;
        this.fhrMax = 210;
        this.fhrMin = 50;
        this.tocoMax = 100;
        this.tocoMin = 0;
        this.dataListRemoveCount = 0;
        this.wide = 2.0f;
        this.maxSize = 360;
        this.context = context;
        float width = context.getResources().getDimension(R.dimen.line_width);
        this.mFhr1Paint = new Paint(1);
        this.mFhr1Paint.setColor(context.getResources().getColor(android.R.color.black));
        this.mFhr1Paint.setStrokeWidth(width);
        this.mTocoPaint = new Paint(1);
        this.mTocoPaint.setColor(context.getResources().getColor(R.color.line_dark_green));
        this.mTocoPaint.setStrokeWidth(width);
        this.beatZdbmp = BitmapFactory.decodeResource(getResources(), R.drawable.beat_zd);
        this.tocoResetBmp = BitmapFactory.decodeResource(getResources(), R.drawable.toco_reset_mark);
        initBitmap();
    }

    private void initBitmap() {
        this.bgBitmap = BitmapLinUtils.readBitmap(this.context, R.drawable.fhr_monitor_toco1);
    }

    public void setDataList(LinkedList<Listener.TimeData> dataList) {
        this.dataList = dataList;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.screenHeight = bottom - top;
            this.screenWidth = right - left;
            this.oneWidth = ((float) this.screenWidth) / ((float) this.wNum);
            this.wide = (1.0f * ((float) this.screenWidth)) / 360.0f;
            this.fhrTop = (float) ((this.screenHeight * 20) / 760);
            this.fhrBottom = (float) ((this.screenHeight * 502) / 760);
            this.fhrVer = (this.fhrBottom - this.fhrTop) / 160.0f;
            this.tocoTop = (float) ((this.screenHeight * 546) / 760);
            this.tocoBottom = (float) ((this.screenHeight * 746) / 760);
            this.tocoVer = (this.tocoBottom - this.tocoTop) / 100.0f;
        }
    }

    public void addBeat(Listener.TimeData currData) {
        this.currData = currData;
        try {
            if (this.dataList.size() != 0 && this.dataList.size() >= 360) {
                this.dataList.pollFirst();
                this.dataListRemoveCount++;
            }
            this.dataList.add(currData);
        } catch (Exception e) {
        }
        postInvalidate();
    }

    public void addSelfBeat() {
        if (this.currData != null) {
            this.currData.beatZd = 1;
            Listener.TimeData timeData = this.currData;
            timeData.status1 |= 8;
        }
    }

    public void addTocoReset() {
        if (this.currData != null) {
            Listener.TimeData timeData = this.currData;
            timeData.status1 |= 16;
        }
    }

    public void clear() {
        this.dataList.clear();
        invalidate();
        this.dataListRemoveCount = 0;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bgBitmapOffsetX = this.dataListRemoveCount * ((int) this.wide);
        canvas.drawBitmap(this.bgBitmap, null, new Rect(0 - (bgBitmapOffsetX % this.screenWidth), 0, this.screenWidth - (bgBitmapOffsetX % this.screenWidth), this.screenHeight), null);
        canvas.drawBitmap(this.bgBitmap, null, new Rect(this.screenWidth - (bgBitmapOffsetX % this.screenWidth), 0, (this.screenWidth * 2) - (bgBitmapOffsetX % this.screenWidth), this.screenHeight), null);
        Paint textPaint = new Paint();
        textPaint.setTextSize(22.0f);
        textPaint.setColor(-16777216);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(2.0f);
        int offX = this.dataListRemoveCount * ((int) this.wide);
        int startIndex = (offX / this.screenWidth) * 3;
        float minStep = ((float) this.screenWidth) / 3.0f;
        canvas.drawText(String.valueOf(startIndex) + "'", (((float) (startIndex % 3)) * minStep) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        startIndex++;
        canvas.drawText(String.valueOf(startIndex) + "'", (((float) (startIndex % 3)) * minStep) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        startIndex++;
        canvas.drawText(String.valueOf(startIndex) + "'", (((float) (startIndex % 3)) * minStep) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        startIndex++;
        canvas.drawText(String.valueOf(startIndex) + "'", ((((float) (startIndex % 3)) * minStep) + ((float) this.screenWidth)) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        startIndex++;
        canvas.drawText(String.valueOf(startIndex) + "'", ((((float) (startIndex % 3)) * minStep) + ((float) this.screenWidth)) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        startIndex++;
        canvas.drawText(String.valueOf(startIndex) + "'", ((((float) (startIndex % 3)) * minStep) + ((float) this.screenWidth)) - ((float) (offX % this.screenWidth)), this.tocoTop - 8.0f, textPaint);
        int length = this.dataList.size();
        for (int i = 1; i < length; i++) {
            int lastRate = ((Listener.TimeData) this.dataList.get(i - 1)).heartRate;
            int currRate = ((Listener.TimeData) this.dataList.get(i)).heartRate;
            int lastToco = ((TimeData) this.dataList.get(i - 1)).tocoWave;
            int currToco = ((TimeData) this.dataList.get(i)).tocoWave;
            int statu1 = ((TimeData) this.dataList.get(i)).status1;
            float startX = ((float) (i - 1)) * this.wide;
            float stopX = ((float) i) * this.wide;
            float fhrStartY = fhrToValue(lastRate);
            float fhrStopY = fhrToValue(currRate);
            float tocoStartY = tocoToValue(lastToco);
            float tocoStopY = tocoToValue(currToco);
            boolean breakflag = new BigDecimal(lastRate - currRate).abs().intValue() <= 30;
            if (lastRate >= 50 && lastRate <= 210 && currRate >= 50 && currRate <= 210) {
                if (breakflag) {
                    canvas.drawLine(startX, fhrStartY, stopX, fhrStopY, this.mFhr1Paint);
                } else {
                    canvas.drawPoint(stopX, fhrStopY, this.mFhr1Paint);
                }
            }
            canvas.drawLine(startX, tocoStartY, stopX, tocoStopY, this.mTocoPaint);
            if ((((TimeData) this.dataList.get(i)).status1 & 8) != 0) {
                canvas.drawBitmap(this.beatZdbmp, stopX - (this.wide / 2.0f), fhrToValue(211), null);
            }
            if ((statu1 & 16) != 0) {
                canvas.drawBitmap(this.tocoResetBmp, stopX - (this.wide / 2.0f), fhrToValue(200), null);
            }
        }
    }

    private float fhrToValue(int fhr) {
        return this.fhrTop + (this.fhrVer * ((float) (210 - fhr)));
    }

    private float tocoToValue(int toco) {
        return this.tocoTop + (this.tocoVer * ((float) (100 - toco)));
    }

    public static int getRatioSize(int bitWidth, int bitHeight) {
        int ratio = 1;
        if (bitWidth > bitHeight && bitWidth > 960) {
            ratio = bitWidth / 960;
        } else if (bitWidth < bitHeight && bitHeight > 1280) {
            ratio = bitHeight / 1280;
        }
        if (ratio <= 0) {
            return 1;
        }
        return ratio;
    }
}