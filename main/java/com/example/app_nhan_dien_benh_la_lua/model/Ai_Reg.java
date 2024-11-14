package com.example.app_nhan_dien_benh_la_lua.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.app_nhan_dien_benh_la_lua.ml.Model;  // Import model TensorFlow Lite đã chuyển đổi

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Ai_Reg {

    private Model model;  // Biến để lưu đối tượng Model TensorFlow Lite
    private Context context;

    // Constructor
    public Ai_Reg(Context context) {
        this.context = context;
        try {
            // Khởi tạo mô hình khi tạo đối tượng AiModel
            this.model = Model.newInstance(context);
        } catch (IOException e) {
            Log.e("AiModel", "Error loading model", e);
        }
    }

    // Phương thức phân loại ảnh
    public String classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[256 * 256];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < 256; i ++){
                for(int j = 0; j < 256; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Cháy Bìa Lá", "Đốm Nâu", "Đạo Ôn"};



            // Releases model resources if no longer used.
            model.close();
            return classes[maxPos];
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return "";
    }
}
