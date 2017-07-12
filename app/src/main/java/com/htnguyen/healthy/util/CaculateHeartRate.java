package com.htnguyen.healthy.util;

import com.htnguyen.healthy.model.HeartRateModel;

import java.util.ArrayList;
import java.util.List;

public class CaculateHeartRate {

    public CaculateHeartRate() {
    }

    public int getBpm(List<HeartRateModel> heartRateModels) {
        List<HeartRateModel> perToPer = new ArrayList<>();
        int lengh = heartRateModels.size();
        long avg = avgSignal(heartRateModels);
        if (lengh < 10) return 0;
        for (int i = 1; i < lengh - 1; i++) {
            if (heartRateModels.get(i).getSinal() < heartRateModels.get(i - 1).getSinal() &&
                    heartRateModels.get(i).getSinal() < heartRateModels.get(i + 1).getSinal()) {
                if (perToPer.size() == 2)
                    return (int) ((1000f / (float) (perToPer.get(1).getTime() - perToPer.get(0).getTime())) * 60f);
                if (heartRateModels.get(i).getSinal() < avg) {
                    if (perToPer.size() == 0) {
                        perToPer.add(heartRateModels.get(i));
                    } else {
                        int milisecond = (int) (heartRateModels.get(i).getTime() - perToPer.get(perToPer.size() - 1).getTime());
                        //Khi <300 thì bpm > 200 không nằm trong khoản bpm, >2000 bmp <30
                        if (milisecond < 300 || milisecond > 2000) {
                            perToPer.clear();
                        } else {
                            perToPer.add(heartRateModels.get(i));
                        }
                    }

                }
            }
        }
        float avgtime = 0;
        if (perToPer.size() <= 1) return 0;
        for (int i = 1; i < perToPer.size(); i++) {
            avgtime += (perToPer.get(i).getTime() - perToPer.get(i - 1).getTime());
        }
        return (int) ((1000f / avgtime) * 60f);
    }

    private long avgSignal(List<HeartRateModel> heartRateModels) {
        int lengh = heartRateModels.size();
        long totalSignal = 0;
        for (int i = 0; i < lengh; i++) {
            totalSignal += heartRateModels.get(i).getSinal();
        }
        return totalSignal / lengh;
    }

    private double avgTime(List<HeartRateModel> perToPer) {
        int lengh = perToPer.size();
        List<Integer> listTime = new ArrayList<>();
        if (lengh < 2) return 0;
        for (int i = 0; i < lengh - 1; i++) {
            listTime.add((int) (perToPer.get(i + 1).getTime() - perToPer.get(i).getTime()));
        }
        List<Double> avgTimeList = new ArrayList<>();
        for (int j = 0; j < listTime.size() - 1; j++) {
            int time1 = Math.abs(listTime.get(j) - listTime.get(j + 1));
            if (time1 > 100) {
                if (j + 2 < listTime.size()) {
                    int time2 = Math.abs(listTime.get(j) - listTime.get(j + 2));
                    if (time2 > 100) {
                        int time3 = Math.abs(listTime.get(j + 1) - listTime.get(j + 2));
                        if (time3 < 100) {
                            avgTimeList.add((double) (1000 / listTime.get(j + 2)));
                        }
                    } else {
                        avgTimeList.add((double) (1000 / listTime.get(j + 1)));
                    }

                }
            } else {
                avgTimeList.add((double) (1000 / listTime.get(j)));
            }
        }
        if (avgTimeList.size() <= 0) return 0;
        double avgTotal = 0d;
        for (int i = 0; i < avgTimeList.size(); i++) {
            avgTotal += avgTimeList.get(i);
        }
        return avgTotal / avgTimeList.size();
    }


    public int HeartRate(List<HeartRateModel> heartRateModels) {
        int j = 0;
        int f = heartRateModels.size();
        int g = 0;
        do {
            g++;
            if (heartRateModels.get(g).getSinal() < heartRateModels.get(g + -1).getSinal()) {
                j++;
            }
            if (g > f - 10) {
                return 1;
            }
        } while (j < 2 && g < f - 1);

        long startTime2 = heartRateModels.get(g).getTime();
        long signal = heartRateModels.get(g).getSinal();

        do {
            g++;
            if (heartRateModels.get(g).getSinal() <= signal) {
                if (heartRateModels.get(g).getTime() - startTime2 > 350) {
                    return (int) ((1000 / (double) (heartRateModels.get(g).getTime() - startTime2)) * 60);
                }
            }
        } while (g < f - 1);

//        for (int i = 1; i < heartRateModels.size(); i++) {
//            if (heartRateModels.get(i).getSinal() < heartRateModels.get(i + -1).getSinal() && !hasChange) {
//                hasChange = true;
//                if(count == 2){
//                    count = 0;
//                    hasChange = false;
//                    return (int) ((1000/(double)(heartRateModels.get(i).getTime() - startTime))*60);
//                }
//                startTime = heartRateModels.get(i).getTime();
//            }else if (heartRateModels.get(i).getSinal() > heartRateModels.get(i - 1).getSinal() && hasChange){
//                if((heartRateModels.get(i).getTime() -  startTime) <300){
//
//                }else {
//                    count ++;
//                    hasChange = false;
//                }
//            }
//        }
        return 1;
    }
}
