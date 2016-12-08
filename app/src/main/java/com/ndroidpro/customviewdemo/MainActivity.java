package com.ndroidpro.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Listener.TimeData timeDataLinkedList[]= new Listener.TimeData[10];
        Listener.TimeData timeData = new Listener.TimeData(100,120);
        Listener.TimeData timeData1 = new Listener.TimeData(110,130);
        Listener.TimeData timeData12 = new Listener.TimeData(120,140);
        Listener.TimeData timeData13 = new Listener.TimeData(130,150);
        Listener.TimeData timeData14 = new Listener.TimeData(140,160);
        Listener.TimeData timeData15 = new Listener.TimeData(150,170);
        Listener.TimeData timeData16 = new Listener.TimeData(160,180);
        Listener.TimeData timeData17 = new Listener.TimeData(170,190);
        Listener.TimeData timeData18 = new Listener.TimeData(180,200);
        Listener.TimeData timeData19 = new Listener.TimeData(190,210);

        timeDataLinkedList[0] = timeData;
        timeDataLinkedList[1] = timeData1;
        timeDataLinkedList[2] = timeData12;
        timeDataLinkedList[3] = timeData13;
        timeDataLinkedList[4] = timeData14;
        timeDataLinkedList[5] = timeData15;
        timeDataLinkedList[6] = timeData16;
        timeDataLinkedList[7] = timeData17;
        timeDataLinkedList[8] = timeData18;
        timeDataLinkedList[9] = timeData19;
        RecordTocoEcgView monitorTocoEcgView = (RecordTocoEcgView) findViewById(R.id.graph);
        monitorTocoEcgView.setDatas(timeDataLinkedList);
    }
}
