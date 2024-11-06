package com.example.app_nhan_dien_benh_la_lua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.app_nhan_dien_benh_la_lua.adapter.ChatRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.app_nhan_dien_benh_la_lua.fire_base.FirebaseUtil;
//import com.google.firebase.auth;
import com.example.app_nhan_dien_benh_la_lua.fire_base.FirebaseUtil;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Activity_Sign_In extends AppCompatActivity {
    TextView txt_sign_up;
    EditText  edt_user;
    Button btn_dangnhap;
    // Khởi tạo FirebaseAuth
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        txt_sign_up = findViewById(R.id.txt_main);

        edt_user = findViewById(R.id.edt_user);
        btn_dangnhap = findViewById(R.id.btn_sign_up);


//        Log.d("checkkk", phonee);
        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Activity_Authetic.class);

                startActivity(i);
            }
        });

//        getUserName(FirebaseUtil.currentUserId());

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("sodienthoai", edt_user.getText().toString());
                checkRegister(edt_user.getText().toString().trim());
            }
        });

    }

    private void checkRegister(String phone_rs) {
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    Log.d("sodienthoai", "phone");
                    if (task.isSuccessful()) {
                        StringBuilder phones = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy giá trị của trường "phone"
                            String phone = document.getString("phone");
                            Log.d("sodienthoai", phone);
                            if (phone_rs.equals(phone)){
                                Intent i = new Intent(getApplicationContext(), Activity_ChatRoom.class);
                                i.putExtra("phoneuser", edt_user.getText().toString().trim());
                                startActivity(i);
                            }else {
                                // Thong bao số điện thoại chua dang ky
//                                Toast.makeText(Activity_Sign_In.this, edt_user.getText().toString().trim() + "chua dang ky", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        Toast.makeText(Activity_Sign_In.this, "Lỗi khi lấy số điện thoại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void checkPhoneNumber() {
//        Log.d("sodienthoai", db.collection("users").document("phone").get().toString());
////                .addOnCompleteListener(task -> {
////                    if (task.isSuccessful() && task.getResult() != null) {
////                        if (task.getResult().exists()) {
////                            Toast.makeText(Activity_Sign_In.this, "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(Activity_Sign_In.this, "Số điện thoại có thể đăng ký.", Toast.LENGTH_SHORT).show();
////                        }
////                    } else {
////                        Toast.makeText(Activity_Sign_In.this, "Lỗi kiểm tra số điện thoại.", Toast.LENGTH_SHORT).show();
////                    }
////                });
//    }



    public void getUserName(String userId) {
        db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.getString("user");
                            Log.d("Firestore123", "Tên người dùng: " + userName);
                        } else {
                            Log.d("Firestore123", "Không tìm thấy tài liệu");
                        }
                    } else {
                        Log.w("Firestore123", "Lấy tên người dùng thất bại.", task.getException());
                    }
                });
    }




}