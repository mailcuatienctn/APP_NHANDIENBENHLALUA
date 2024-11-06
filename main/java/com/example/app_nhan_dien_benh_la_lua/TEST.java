package com.example.app_nhan_dien_benh_la_lua;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class TEST extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String phoneNumber = "0383299400";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
    }


    public interface UserIDCallback {
        void onCallback(String documentID);
    }

    public void getUserID(String phonee, UserIDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")  // Tên của collection chứa dữ liệu người dùng
                .whereEqualTo("phone", phonee)  // Truy vấn theo số điện thoại
                .get()  // Lấy kết quả
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Nếu có kết quả
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String documentID = document.getId();  // Lấy documentID
                            Log.d("DocumentID", "Document ID: " + documentID);
                            callback.onCallback(documentID);  // Trả kết quả qua callback
                        }
                    } else {
                        Log.d("DocumentID", "No document found with this phone number");
                        callback.onCallback(null);  // Trả null nếu không tìm thấy
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents", e);
                    callback.onCallback(null);  // Trả null nếu có lỗi
                });
    }




}