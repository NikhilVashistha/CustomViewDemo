package com.ndroidpro.customviewdemo;

/**
 * Created by Nikhil Vashistha on 08-12-2016 for CustomViewDemo.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Listener {
    public static final int BEAT = 1;
    public static final int NONE = 0;
    public static final int PRE = 500;
    public static final String SUFFIX = ".mtb";
    private final int MAX = 0;
    public int beatTimes;
    public LinkedList<TimeData> dataList;
    public int day;
    private File file;
    private final String filePath = "";
    public int secTime;
    public long startT;
    public String startTime;
    public String timing;
    public int week;

    public static class TimeData {
        public int afmWave;
        public int beatBd;
        public int beatZd;
        public int heartRate;
        public int status1;
        public int status2;
        public int tocoWave;

        public TimeData() {
            this.beatZd = Listener.NONE;
            this.beatBd = Listener.NONE;
        }

        public TimeData(int heartRate, int beatBd) {
            this.beatZd = Listener.NONE;
            this.beatBd = Listener.NONE;
            this.heartRate = heartRate;
            this.beatBd = beatBd;
        }

        public TimeData(int heartRate, int tocoWave, int afmWave, int status1, int status2, int beatBd) {
            this.beatZd = Listener.NONE;
            this.beatBd = Listener.NONE;
            this.heartRate = heartRate;
            this.tocoWave = tocoWave;
            this.afmWave = afmWave;
            this.status1 = status1;
            this.status2 = status2;
            this.beatBd = beatBd;
            if (beatBd != 0) {
                this.status1 = (this.status1 & -5) | 4;
            }
        }
    }

//    public Listener() {
//        this.filePath = "shiyuebaobei/data";
//        this.MAX = 1000;
//        this.secTime = -1;
//        this.dataList = new LinkedList();
//        this.startT = System.currentTimeMillis();
//        int off = (int) ((BabyApplication.pregnant.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / RefreshableView.ONE_DAY);
//        String fileName = (off / 7) + "_" + (off % 7) + "_" + this.startT + SUFFIX;
//        File root = new File(Environment.getExternalStorageDirectory(), "shiyuebaobei/data");
//        if (!root.exists()) {
//            root.mkdirs();
//        }
//        this.file = new File(root, fileName);
//    }

    public void addBeat(TimeData timeData) {
        setEndT(System.currentTimeMillis());
        if (this.dataList.size() >= 1000) {
            save();
        }
        this.dataList.add(timeData);
    }

    public void save() {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        if (this.file != null) {
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(this.file, true);
                try {
                    int length = this.dataList.size();
                    for (int i = NONE; i < length; i += BEAT) {
                        TimeData data = (TimeData) this.dataList.get(i);
                        fos2.write(data.heartRate);
                        fos2.write(data.tocoWave);
                        fos2.write(data.afmWave);
                        fos2.write(data.status1);
                        fos2.write(data.status2);
                    }
                    this.dataList.clear();
                    if (fos2 != null) {
                        try {
                            fos2.close();
                            fos = fos2;
                            return;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    fos = fos2;
                } catch (FileNotFoundException e4) {
                    e2 = e4;
                    fos = fos2;
                } catch (IOException e5) {
                    e5 = e5;
                    fos = fos2;
                } catch (Throwable th2) {
                    th = th2;
                    fos = fos2;
                }
            } catch (FileNotFoundException e6) {
                e2 = e6;
                try {
                    e2.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                    }
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } catch (IOException e7) {
                e7 = e7;
                e7.printStackTrace();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                    }
                }
            }
        }
    }

    public void setEndT(long endT) {
        int secTime = (int) ((endT - this.startT) / 1000);
        if (this.secTime != secTime) {
            this.secTime = secTime;
            this.timing = formatTime(secTime);
        }
    }

    public static String formatTime(int s) {
        String t;
        int m = s / 60;
        s %= 60;
        if (m < 10) {
            t = "0" + m;
        } else {
            t = Integer.toString(m);
        }
        if (s < 10) {
            return new StringBuilder(String.valueOf(t)).append(":0").append(s).toString();
        }
        return new StringBuilder(String.valueOf(t)).append(":").append(s).toString();
    }
}