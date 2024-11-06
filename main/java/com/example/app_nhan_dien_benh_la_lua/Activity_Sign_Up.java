package com.example.app_nhan_dien_benh_la_lua;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_nhan_dien_benh_la_lua.fire_base.FirebaseUtil;
import com.example.app_nhan_dien_benh_la_lua.model.user;
import com.google.firebase.Timestamp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class Activity_Sign_Up extends AppCompatActivity {
    TextView txt_main;
    EditText edt_user, edt_cf_ps;
    ImageView img_avata;
    Button btn_sign_up;
    String encodedImage;

    Uri rs_uri_img;
    user users;
    FirebaseUtil firebaseUtil;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        txt_main = findViewById(R.id.txt_main);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        edt_user = findViewById(R.id.edt_user);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        phone = "0" + phone.substring(phone.length() - 9);
        Log.d("phone_lastNineCharacters", phone);
        img_avata = findViewById(R.id.img_avata);

        txt_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isValidSignUpDetails()){
                    setUsesr();
                    Intent i = new Intent(getApplicationContext(), Activity_Sign_In.class);
                    i.putExtra("phone", phone);
                    startActivity(i);
//                }
            }
        });

        img_avata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

    }

    public void setUsesr(){
        users = new user(edt_user.getText().toString(), phone, encodedImage, Timestamp.now());
        Log.d("model", users.toString());
        firebaseUtil.currentUserIdDetail().set(users);
        Log.d("dungroi", "oke");
    }



    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            img_avata.setImageURI(selectedImageUri); // Hiển thị ảnh lên img_rs
            rs_uri_img = selectedImageUri;
            encodedImage = BitMapToString(uriToBitmap(rs_uri_img, getContentResolver(), 256, 256));
            Log.d("chuoianh", encodedImage);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    private Boolean isValidSignUpDetails() {
//        if (encodedImage == null) {
//            showToast("Chọn ảnh đại diện");
//            return false;
//        } else if (edt_user.getText().toString().trim().isEmpty()) {
//            showToast("Nhập tên");
//            return false;
//        }else if (edt_cf_ps.getText().toString().trim().isEmpty()) {
//            showToast("Nhập lại mật khẩu");
//            return false;
//        }else {
//            return true;
//        }
//    }

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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }




}