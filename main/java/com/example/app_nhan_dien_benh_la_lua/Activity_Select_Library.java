package com.example.app_nhan_dien_benh_la_lua;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


//import com.example.test_ai.ml.Model;
import com.example.app_nhan_dien_benh_la_lua.ml.Model;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.example.app_nhan_dien_benh_la_lua.model.Ai_Reg;
import com.example.app_nhan_dien_benh_la_lua.Img_Util.Model_Solution;




public class Activity_Select_Library extends AppCompatActivity {
    ImageView img_rs;
    Button btn_result, btn_nnvdt, btn_chonlai;
    EditText edt_rs;
    int imageSize = 256;
    String result;
    Uri uri_img_rs;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_library);
        img_rs = findViewById(R.id.img_rs);
//        btn_result = findViewById(R.id.btn_result);
        edt_rs = findViewById(R.id.edt_rs);
        btn_chonlai = findViewById(R.id.btn_chonlai);
        btn_nnvdt = findViewById(R.id.btn_nnvct);
//        btn_nnvdt.setEnabled(false); // Để vô hiệu hóa button điều trị
        openImagePicker();

//        btn_result.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                classifyImage(uriToBitmap(uri_img_rs, getContentResolver(), 256, 256));
//                Ai_Reg ai_reg = new Ai_Reg(getApplicationContext());
//                result = ai_reg.classifyImage(uriToBitmap(uri_img_rs, getContentResolver(), 256, 256));
////                result = classifyImage(uriToBitmap(uri_img_rs, getContentResolver(), 256, 256));
//                Log.d("ketqua", result);
//                edt_rs.setText(result);
//                btn_nnvdt.setEnabled(true);
//                Log.d("ketqa", ""+flag);
//            }
//        });

        btn_nnvdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model_Solution modelSolution = new Model_Solution();
                startActivity(modelSolution.Solution(result));
            }
        });

        btn_chonlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            img_rs.setImageURI(selectedImageUri); // Hiển thị ảnh lên img_rs
            uri_img_rs = selectedImageUri; //gan bien toan cuc
            Ai_Reg ai_reg = new Ai_Reg(getApplicationContext());
            result = ai_reg.classifyImage(uriToBitmap(uri_img_rs, getContentResolver(), 256, 256));
            edt_rs.setText(result);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 400);
    }

    public Bitmap uriToBitmap(Uri uri, ContentResolver contentResolver, int desiredWidth, int desiredHeight) {
        try {
            // Lấy Bitmap từ URI
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);

            // Scale Bitmap
            return Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Xử lý lỗi nếu có
        }
    }

}