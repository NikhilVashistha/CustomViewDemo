package com.ndroidpro.customviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements RecordTocoEcgView.notifyScrolledListener {

    private Handler handler;
    RecordTocoEcgView monitorTocoEcgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        monitorTocoEcgView = (RecordTocoEcgView) findViewById(R.id.graph);
        for (int i = 0; i < 1000; i++) {
            final int temp = i;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {// we add 100 new entries



                    addEntry(temp);
                }



            }, 1000);
        }

    }
    int max = 200;
    int min = 100;
    Random r = new Random();
    private void addEntry(int i) {
         Listener.TimeData timeDataLinkedList[]= new Listener.TimeData[1000];
        int Result = r.nextInt(max-min) + min;
            timeDataLinkedList[i] = new Listener.TimeData(Result,Result);
            monitorTocoEcgView.setDatas(timeDataLinkedList);
        }


    @Override
    public void notifyScrolled(int time) {
        setRate(time);
    }

    private void setRate(int milliseconds) {
        this.monitorTocoEcgView.setTime(milliseconds);
        setRate(milliseconds / Listener.PRE, Listener.formatTime(milliseconds / 1000));
    }

    private void setRate(int index, String currStr) {
        Log.d(index+"nikhil",currStr+"\n");
//        this.timingTv.setText(new StringBuilder(String.valueOf(currStr)).append("/").append(this.toatTimeStr).toString());
//        if (index < this.datas.length && index >= 0) {
//            TimeData data = this.datas[index];
//            if (data.heartRate < 50 || data.heartRate > 210) {
//                this.rateTv.setText(getString(C0228R.string.data_none));
//            } else {
//                this.rateTv.setText(String.valueOf(data.heartRate));
//            }
//            this.currRate = data.heartRate;
//            this.tocoTv.setText(String.valueOf(data.tocoWave));
//        }
    }
}
