package com.afei.gpuimagedemo.activity;

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.afei.gpuimagedemo.R;
import com.afei.gpuimagedemo.util.FileUtils;
import com.afei.gpuimagedemo.util.GPUImageFilterTools;
import com.afei.gpuimagedemo.util.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import com.afei.gpuimagedemo.util.ImageUtils;

import java.io.IOException;
import java.io.InputStream;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class GalleryActivity extends BaseActivity implements View.OnClickListener {

    private GPUImageView mGPUImageView;
    private SeekBar mSeekBar;
    private TextView mFilterNameTv;

    private GPUImageFilter mNoImageFilter = new GPUImageFilter();
    private GPUImageFilter mCurrentImageFilter = mNoImageFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private ActivityResultLauncher<String> mGalleryLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initView();
        openGallery();
    }

    private void initView() {
        mGPUImageView = findViewById(R.id.gpuimage);
        mSeekBar = findViewById(R.id.tone_seekbar);
        mFilterNameTv = findViewById(R.id.filter_name_tv);
        mGPUImageView.setOnClickListener(this);
        mFilterNameTv.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        findViewById(R.id.compare_iv).setOnTouchListener(mOnTouchListener);
        findViewById(R.id.close_iv).setOnClickListener(this);
        findViewById(R.id.save_iv).setOnClickListener(this);
        mGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::handleGalleryResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gpuimage:
                openGallery();
                break;
            case R.id.filter_name_tv:
                GPUImageFilterTools.showDialog(this, mOnGpuImageFilterChosenListener);
                break;
            case R.id.close_iv:
                finish();
                break;
            case R.id.save_iv:
                saveImage();
                break;
        }
    }

    private void handleGalleryResult(Uri imageUri) {
        if (imageUri != null) {
            try {
                Log.w(TAG, "imageUri path: " + imageUri.getPath());
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Log.w(TAG, "selectedBitmap: " + selectedBitmap.getWidth() + ", " + selectedBitmap.getHeight());
                int rotation = ImageUtils.getRotation(this, imageUri);
                Bitmap rotatedBitmap = ImageUtils.rotateBitmap(selectedBitmap, rotation);
                Log.w(TAG, "rotatedBitmap: " + rotatedBitmap.getWidth() + ", " + rotatedBitmap.getHeight());

                mGPUImageView.setRatio(rotatedBitmap.getWidth() * 1f / rotatedBitmap.getHeight()); // 设置正确的比例才能更好的显示
                mGPUImageView.setImage(rotatedBitmap);
                mGPUImageView.setFilter(mCurrentImageFilter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void openGallery() {
        if (mGalleryLauncher != null) {
            mGalleryLauncher.launch("image/*");
        }
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        mGPUImageView.saveToPictures("GPUImage", fileName, mOnPictureSavedListener);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.compare_iv) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mGPUImageView.setFilter(mNoImageFilter);
                        break;
                    case MotionEvent.ACTION_UP:
                        mGPUImageView.setFilter(mCurrentImageFilter);
                        break;
                }
            }
            return true;
        }
    };

    private OnGpuImageFilterChosenListener mOnGpuImageFilterChosenListener = new OnGpuImageFilterChosenListener() {
        @Override
        public void onGpuImageFilterChosenListener(GPUImageFilter filter, String filterName) {
            switchFilterTo(filter);
            mFilterNameTv.setText(filterName);
        }
    };

    private void switchFilterTo(GPUImageFilter filter) {
        if (mCurrentImageFilter == null
                || (filter != null && !mCurrentImageFilter.getClass().equals(filter.getClass()))) {
            mCurrentImageFilter = filter;
            mGPUImageView.setFilter(mCurrentImageFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mCurrentImageFilter);
            mSeekBar.setVisibility(mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
        } else {
            mSeekBar.setVisibility(View.GONE);
        }
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mFilterAdjuster != null) {
                mFilterAdjuster.adjust(progress);
            }
            mGPUImageView.requestRender();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private GPUImageView.OnPictureSavedListener mOnPictureSavedListener = new GPUImageView.OnPictureSavedListener() {
        @Override
        public void onPictureSaved(Uri uri) {
            String filePath = FileUtils.getRealFilePath(GalleryActivity.this, uri);
            Log.d(TAG, "save to " + filePath);
            Toast.makeText(GalleryActivity.this, "Saved: " + filePath, Toast.LENGTH_SHORT).show();
        }
    };
}
