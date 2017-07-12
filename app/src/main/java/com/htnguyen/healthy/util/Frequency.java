//package com.htnguyen.healthy.util;
//
//public class Frequency {
//    public double calculateFFT(byte[] signal)
//    {
//        final int mNumberOfFFTPoints =1024;
//        double mMaxFFTSample;
//        final float sampleRate=44100;
//
//        double temp;
//        Complex[] y;
//        Complex[] complexSignal = new Complex[mNumberOfFFTPoints];
//        double[] absSignal = new double[mNumberOfFFTPoints/2];
//
//        for(int i = 0; i < mNumberOfFFTPoints; i++){
//            temp = (double)((signal[2*i] & 0xFF) | (signal[2*i+1] << 8)) / 32768.0F;
//            complexSignal[i] = new Complex(temp,0.0);
//        }
//
//        y = FFT.fft(complexSignal);
//
//        mMaxFFTSample = 0.0;
//        int mPeakPos = 0;
//        for(int i = 0; i < (mNumberOfFFTPoints/2); i++) {
//            absSignal[i] = Math.sqrt(Math.pow(y[i].re(), 2) + Math.pow(y[i].im(), 2));
//
//            if (absSignal[i] > mMaxFFTSample) {
//                mMaxFFTSample = absSignal[i];
//                mPeakPos = i;
//            }
//        }
//
//        return ((1.0 * sampleRate) / (1.0 * mNumberOfFFTPoints)) * mPeakPos;
//
//    }
//}
