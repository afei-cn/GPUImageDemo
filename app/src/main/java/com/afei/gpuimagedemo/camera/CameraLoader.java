package com.afei.gpuimagedemo.camera;

public abstract class CameraLoader {

    protected OnPreviewFrameListener mOnPreviewFrameListener;

    public abstract void onResume(int width, int height);

    public abstract void onPause();

    public abstract void switchCamera();

    public abstract int getCameraOrientation();

    public abstract boolean hasMultipleCamera();

    public abstract boolean isFrontCamera();

    public void setOnPreviewFrameListener(OnPreviewFrameListener onPreviewFrameListener) {
        mOnPreviewFrameListener = onPreviewFrameListener;
    }

    public interface OnPreviewFrameListener {
        void onPreviewFrame(byte[] data, int width, int height);
    }

}
