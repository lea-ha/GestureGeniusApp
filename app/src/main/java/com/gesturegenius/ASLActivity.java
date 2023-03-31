package com.gesturegenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import com.gesturegenius.ml.Aslmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class ASLActivity extends AppCompatActivity {

    public TextureView textureView;
    public Handler handler;
    public CameraDevice camDevice;
    public CameraManager camManager;
    public ImageView imageView;
    public Bitmap bitmap;
    public Context ctxt;
    public ImageProcessor imageProcessor;
    @NonNull Aslmodel model;
    public Paint paint;
    public List<String> labels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aslactivity);
        ctxt = this;
        getPermission();

        try {
            labels = FileUtil.loadLabels(this,"asllabels.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR))
                .build();


        model = null;
        try {
            model = Aslmodel.newInstance(ctxt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HandlerThread handlerThread = new HandlerThread("videoThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        imageView = findViewById(R.id.imageView);

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                // Implement onSurfaceTextureAvailable() method here
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                // Implement onSurfaceTextureSizeChanged() method here
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                // Implement onSurfaceTextureDestroyed() method here
                ASLActivity.super.onDestroy();
                model.close();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                // Implement onSurfaceTextureUpdated() method here
                bitmap = textureView.getBitmap();

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                inputFeature0.loadBuffer(getBitmapByteBuffer(resizedBitmap));


                Aslmodel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


                // Gets the prediction as a string.
                int maxIndex = 0;
                float maxValue = outputFeature0.getFloatArray()[0];
                for (int i = 1; i < outputFeature0.getFloatArray().length; i++) {
                    if (outputFeature0.getFloatArray()[i] > maxValue) {
                        maxIndex = i;
                        maxValue = outputFeature0.getFloatArray()[i];
                    }
                }
                String prediction = labels.get(maxIndex);

                // Draws prediction on the image.
                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutable);

                paint = new Paint();
                paint.setTextSize(mutable.getHeight() / 15f);
                paint.setStrokeWidth(mutable.getHeight() / 85f);
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(prediction, 10f, mutable.getHeight() / 2f, paint);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(new RectF(mutable.getWidth(),mutable.getHeight(),mutable.getWidth(),mutable.getHeight()), paint);
                paint.setStyle(Paint.Style.FILL);

                imageView.setImageBitmap(mutable);

                // Releases model resources if no longer used.
                //model.close();
            }

            private ByteBuffer getBitmapByteBuffer(Bitmap bitmap) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
                byteBuffer.order(ByteOrder.nativeOrder());
                int[] intValues = new int[224 * 224];

                bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

                int pixel = 0;
                for (int i = 0; i < 224; ++i) {
                    for (int j = 0; j < 224; ++j) {
                        int value = intValues[pixel++];

                        byteBuffer.putFloat(((value >> 16) & 0xFF) / 255.0f);
                        byteBuffer.putFloat(((value >> 8) & 0xFF) / 255.0f);
                        byteBuffer.putFloat((value & 0xFF) / 255.0f);
                    }
                }
                return byteBuffer;
            }


        });

        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    }



    void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 101);
                return;
            }
            camManager.openCamera(camManager.getCameraIdList()[0], new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    // Do something when the camera is opened successfully
                    camDevice = camera;

                    SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                    Surface surface = new Surface(surfaceTexture);

                    try {
                        CaptureRequest.Builder capRequest = camDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        capRequest.addTarget(surface);
                        camDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {
                                // Handle camera capture session configuration here
                                try {
                                    session.setRepeatingRequest(capRequest.build(), null,null);
                                } catch (CameraAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {
                                // Handle camera capture session configuration failure here
                            }
                        }, handler);

                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    // Do something when the camera is disconnected
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    // Do something when there is an error opening the camera
                }
            }, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    void getPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            getPermission();
        }
    }
}