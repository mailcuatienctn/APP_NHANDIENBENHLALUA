package com.example.app_nhan_dien_benh_la_lua;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.app_nhan_dien_benh_la_lua.Img_Util.Model_Solution;
import com.example.app_nhan_dien_benh_la_lua.model.Ai_Reg;

public class Activity_Select_Camera extends AppCompatActivity {
    ImageView img_cr;
    Button btn_chuan_doan, btn_nnvct, btn_chonlai;
    EditText edt_rs;
//    Ai_Reg ai_reg;
    Uri uri_img;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_camera);

        img_cr = findViewById(R.id.img_rs);
//        btn_chuan_doan = findViewById(R.id.btn_result);
        edt_rs = findViewById(R.id.edt_rs);
        btn_nnvct = findViewById(R.id.btn_nnvct);
        btn_chonlai = findViewById(R.id.btn_chonlai);
        // Tạo đối tượng của lớp Ai_Reg
        openCamera();


//        btn_chuan_doan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edt_rs.setText(result);
//            }
//        });

        btn_nnvct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model_Solution modelSolution = new Model_Solution();
                startActivity(modelSolution.Solution(result));
            }
        });

        btn_chonlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Lấy ảnh đã chụp từ Intent
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Ai_Reg ai_reg = new Ai_Reg(getApplicationContext());
            result = ai_reg.classifyImage(photo);
            // Xử lý ảnh, ví dụ: hiển thị lên ImageView
            img_cr.setImageBitmap(photo); // Hiển thị ảnh từ camera
            edt_rs.setText(result);
        }
    }

}