package com.ndroidpro.customviewdemo;

/**
 * Created by Nikhil Vashistha on 08-12-2016 for CustomViewDemo.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ndroidpro.customviewdemo.Listener.TimeData;

import java.math.BigDecimal;

public class RecordTocoEcgView extends View {
    private static final int NONE = 0;
    private static final int SCROLL = 3;
    private static final int ZOOM = 1;
    private static final int ZOOM_FINISH = 2;
    private float START_X_NUM;
    private Bitmap beatZdbmp;
    private Bitmap bgBitmap;
    private Bitmap bgBitmap2;
    private final int breakValue;
    private Context context;
    private TimeData[] datas;
    TimeData[] demoData;
    private float fhrBottom;
    private final int fhrMax;
    private final int fhrMin;
    private float fhrTop;
    private float fhrVer;
    private final float hNum;
    private boolean isToastMax;
    private boolean isToastMin;
    int last;
    private int lineWidth;
    private Paint mFhr1Paint;
    private notifyScrolledListener mNotifyListener;
    private Paint mTocoPaint;
    private Paint mVerticalLine;
    private final int maxSize;
    private MediaPlayer mediaPlayer;
    private int mode;
    private float oldDist;
    private float oneHeight;
    private float oneWidth;
    private boolean reloadData;
    private int screenHeight;
    private int screenWidth;
    private float stepWidth;
    private float tocoBottom;
    private final int tocoMax;
    private final int tocoMin;
    private Bitmap tocoResetBmp;
    private float tocoTop;
    private float tocoVer;
    private int wNum;
    private float wide;

    public interface notifyScrolledListener {
        void notifyScrolled(int i);
    }

    private void initDemoData() {
        this.demoData = new TimeData[ZOOM];
        this.demoData[NONE] = new TimeData();
        this.demoData[NONE].heartRate = NONE;
        this.demoData[NONE].tocoWave = NONE;
        this.demoData[NONE].afmWave = NONE;
        this.demoData[NONE].status1 = NONE;
        this.demoData[NONE].status2 = NONE;
        this.demoData[NONE].beatZd = NONE;
    }

    public RecordTocoEcgView(Context context) {
        this(context, null, NONE);
        this.context = context;
        initBitmap();
    }

    public RecordTocoEcgView(Context context, AttributeSet attrs) {
        this(context, attrs, NONE);
        this.context = context;
        initBitmap();
    }

    public RecordTocoEcgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.oldDist = 1.0f;
        this.mode = NONE;
        this.reloadData = false;
        this.START_X_NUM = 3.0f;
        this.hNum = 17.6f;
        this.wNum = 9;
        this.breakValue = 30;
        this.fhrMax = 210;
        this.fhrMin = 50;
        this.tocoMax = 100;
        this.tocoMin = NONE;
        this.wide = 2.0f;
        this.maxSize = 360;
        this.demoData = null;
        this.context = context;
        float width = context.getResources().getDimension(R.dimen.line_width);
        initDemoData();
        this.mFhr1Paint = new Paint(ZOOM);
        this.mFhr1Paint.setColor(context.getResources().getColor(R.color.black));
        this.mFhr1Paint.setStrokeWidth(width);
        this.mTocoPaint = new Paint(ZOOM);
        this.mTocoPaint.setColor(context.getResources().getColor(R.color.line_dark_green));
        this.mTocoPaint.setStrokeWidth(width);
        float width1 = context.getResources().getDimension(R.dimen.line_width1);
        this.mVerticalLine = new Paint(ZOOM);
        this.mVerticalLine.setColor(context.getResources().getColor(R.color.line_blue));
        this.mVerticalLine.setStrokeWidth(width1);
        this.beatZdbmp = BitmapFactory.decodeResource(getResources(), R.drawable.beat_zd);
        this.tocoResetBmp = BitmapFactory.decodeResource(getResources(), R.drawable.toco_reset_mark);
        initBitmap();
    }

    private void initBitmap() {
        this.bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fhr_monitor_toco1);
        this.bgBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.fhr_monitor_toco1);
    }

    public void setDatas(TimeData[] datas) {
        this.reloadData = true;
        if (datas == null) {
            this.datas = this.demoData;
        } else {
            this.datas = datas;
        }
        invalidate();
        super.requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || this.reloadData) {
            this.screenWidth = right - left;
            this.screenHeight = bottom - top;
            this.stepWidth = ((float) this.screenWidth) / 3.0f;
            this.fhrTop = (float) ((this.screenHeight * 20) / 760);
            this.fhrBottom = (float) ((this.screenHeight * 502) / 760);
            this.fhrVer = (this.fhrBottom - this.fhrTop) / 160.0f;
            this.tocoTop = (float) ((this.screenHeight * 546) / 760);
            this.tocoBottom = (float) ((this.screenHeight * 746) / 760);
            this.tocoVer = (this.tocoBottom - this.tocoTop) / 100.0f;
            this.oneHeight = ((float) this.screenHeight) / 17.6f;
            this.oneWidth = ((float) this.screenWidth) / ((float) this.wNum);
            this.wide = (1.0f * ((float) this.screenWidth)) / 360.0f;
            if (this.datas != null) {
                this.lineWidth = (int) (((float) (this.datas.length - 1)) * this.wide);
            }
        }
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        int bgBitmapOffsetX = getScrollX();
        canvas.drawBitmap(this.bgBitmap, null, new Rect((bgBitmapOffsetX / this.screenWidth) * this.screenWidth, NONE, ((bgBitmapOffsetX / this.screenWidth) + ZOOM) * this.screenWidth, this.screenHeight), null);
        canvas.drawBitmap(this.bgBitmap2, null, new Rect(((bgBitmapOffsetX / this.screenWidth) + ZOOM) * this.screenWidth, NONE, ((bgBitmapOffsetX / this.screenWidth) + ZOOM_FINISH) * this.screenWidth, this.screenHeight), null);
        int offX = getScrollX();
        canvas.drawLine(((float) offX) + (((float) this.screenWidth) / 3.0f), 0.0f, ((float) offX) + (((float) this.screenWidth) / 3.0f), (float) this.screenHeight, this.mVerticalLine);
        Paint textPaint = new Paint();
        textPaint.setTextSize(22.0f);
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setStrokeWidth(2.0f);
        int startIndex = (int) (((float) offX) / this.stepWidth);
        int num = ((int) (((float) (this.screenWidth * ZOOM_FINISH)) / this.stepWidth)) / ZOOM_FINISH;
        int multiple = (int) (((float) (this.screenWidth / SCROLL)) / this.stepWidth);
        int i = startIndex - num < 0 ? NONE : startIndex - num;
        while (i < startIndex + num) {
            if (i % multiple == 0) {
                canvas.drawText(String.valueOf(i) + "'", (((float) this.screenWidth) / 3.0f) + (this.stepWidth * ((float) i)), this.tocoTop - 10.0f, textPaint);
            }
            i += ZOOM;
        }
        if (this.datas != null) {
            int length = this.datas.length;
            for (i = ZOOM; i < length; i += ZOOM) {
                int lastRate = this.datas[i - 1].heartRate;
                int currRate = this.datas[i].heartRate;
                int lastToco = this.datas[i - 1].tocoWave;
                int currToco = this.datas[i].tocoWave;
                int status1 = this.datas[i].status1;
                float startX = (((float) (i - 1)) * this.wide) + (this.oneWidth * this.START_X_NUM);
                float stopX = (((float) i) * this.wide) + (this.oneWidth * this.START_X_NUM);
                float fhrStartY = fhrToValue(lastRate);
                float fhrStopY = fhrToValue(currRate);
                float tocoStartY = tocoToValue(lastToco);
                float tocoStopY = tocoToValue(currToco);
                boolean breakFlag = new BigDecimal(lastRate - currRate).abs().intValue() <= 30;
                if (lastRate >= 50 && lastRate <= 210 && currRate >= 50 && currRate <= 210) {
                    if (breakFlag) {
                        canvas.drawLine(startX, fhrStartY, stopX, fhrStopY, this.mFhr1Paint);
                    } else {
                        canvas.drawPoint(stopX, fhrStopY, this.mFhr1Paint);
                    }
                }
                canvas.drawLine(startX, tocoStartY, stopX, tocoStopY, this.mTocoPaint);
                if (this.datas[i].beatZd == ZOOM) {
                    canvas.drawBitmap(this.beatZdbmp, stopX - (this.wide / 2.0f), fhrToValue(211), null);
                }
                if ((status1 & 16) != 0) {
                    canvas.drawBitmap(this.tocoResetBmp, stopX - (this.wide / 2.0f), fhrToValue(200), null);
                }
            }
        }
    }

    private float fhrToValue(int fhr) {
        return this.fhrTop + (this.fhrVer * ((float) (210 - fhr)));
    }

    private float tocoToValue(int toco) {
        return this.tocoTop + (this.tocoVer * ((float) (100 - toco)));
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case NONE /*0*/:
                this.mode = NONE;
                this.last = (int) event.getX();
                break;
            case ZOOM /*1*/:
                break;
            case ZOOM_FINISH /*2*/:
                if (this.mode != ZOOM) {
                    this.mode = Math.abs(event.getX() - ((float) this.last)) > 10.0f ? SCROLL : NONE;
                    break;
                }
                this.isToastMin = false;
                this.isToastMax = false;
                float newDist = spacing(event);
                float offset = newDist - this.oldDist;
                if (Math.abs(offset) > 33.0f) {
                    this.oldDist = newDist;
                    if (offset < 0.0f) {
                        this.wide /= 2.0f;
                        float minWide = ((float) (this.screenWidth / 360)) / 4.0f;
                        if (this.wide >= minWide) {
                            minWide = this.wide;
                        }
                        this.wide = minWide;
                        this.stepWidth /= 2.0f;
                        float minWidth = ((float) this.screenWidth) / 12.0f;
                        if (this.stepWidth < minWidth) {
                            this.stepWidth = minWidth;
                            this.isToastMin = true;
                        }
                    } else {
                        this.wide *= 2.0f;
                        float maxWide = (1.0f * ((float) this.screenWidth)) / 360.0f;
                        if (this.wide <= maxWide) {
                            maxWide = this.wide;
                        }
                        this.wide = maxWide;
                        this.stepWidth *= 2.0f;
                        float maxWidth = ((float) this.screenWidth) / 3.0f;
                        if (this.stepWidth > maxWidth) {
                            this.stepWidth = maxWidth;
                            this.isToastMax = true;
                        }
                    }
                    String toastString = "X" + ((int) ((this.stepWidth * 12.0f) / ((float) this.screenWidth)));
                    if (this.isToastMax) {
                        toastString = getResources().getString(R.string.max);
                    } else if (this.isToastMin) {
                        toastString = getResources().getString(R.string.min);
                    }
                    Toast mToast = Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT);
                    mToast.setGravity(17, NONE, NONE);
                    mToast.show();
                    this.mode = ZOOM_FINISH;
                    invalidate();
                    break;
                }
                break;
//            case FragmentManagerImpl.ANIM_STYLE_FADE_ENTER /*5*/:
//                this.oldDist = spacing(event);
//                if (this.oldDist > 10.0f) {
//                    this.mode = ZOOM;
//                    break;
//                }
//                break;
//            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT /*6*/:
//                if (this.mode == ZOOM) {
//                    this.mode = NONE;
//                    break;
//                }
//                break;
        }
        if (this.mode == SCROLL) {
            int scrollX = getScrollX();
            int cur = (int) event.getX();
            int offX = this.last - cur;
            this.last = cur;
            offX += scrollX;
            if (offX < 0) {
                offX = NONE;
            } else if (offX > this.lineWidth) {
                offX = this.lineWidth;
            }
            if (offX > 0) {
                setPostion(offX);
            }
        }
        return true;
    }

    private void setPostion(int offX) {
        scrollTo(offX, NONE);
        int time = (int) (((float) (offX * Listener.PRE)) / this.wide);
        if (this.mediaPlayer != null) {
            this.mediaPlayer.seekTo(time);
        } else if (this.mNotifyListener != null) {
            this.mNotifyListener.notifyScrolled(time);
        }
    }

    public void setTime(int time) {
        scrollTo((int) ((((float) time) * this.wide) / 500.0f), NONE);
    }

    public void scrollEnd() {
        scrollTo(this.lineWidth, NONE);
    }

    public void setMediaPlay(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setNotifycrolledListener(notifyScrolledListener listener) {
        this.mNotifyListener = listener;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(NONE) - event.getX(ZOOM);
        float y = event.getY(NONE) - event.getY(ZOOM);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }
}