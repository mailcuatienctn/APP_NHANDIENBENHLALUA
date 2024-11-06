package com.example.app_nhan_dien_benh_la_lua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_nhan_dien_benh_la_lua.adapter.ChatRecyclerAdapter;
import com.example.app_nhan_dien_benh_la_lua.fire_base.FirebaseUtil;
import com.example.app_nhan_dien_benh_la_lua.model.user;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.example.app_nhan_dien_benh_la_lua.model.chatroom_model;
import com.example.app_nhan_dien_benh_la_lua.model.ChatMessageModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class Activity_ChatRoom extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ImageButton sendMessageBtn;
    EditText messageInput;
    chatroom_model chatroomModel;
//    String chatroomId;
    String userIds;
    Timestamp lastmessTimestamp;
    FirebaseUtil firebaseUtil;
    ChatRecyclerAdapter adapter;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_room);
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        messageInput = findViewById(R.id.chat_message_input);
        recyclerView = findViewById(R.id.chat_recycler_view);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phoneuser");
        Log.d("phone7", phone);
        setUserID(phone);
        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString();
            if(message.isEmpty())
                return;
            sendMessageToUser(message);
        }));
        getOrCreateChatroomModel();
        setupChatRecyclerView();

//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(item.getItemId()==R.id.menu_profile){
//                    Intent i = new Intent(getApplicationContext(), Profile_Fragment.class);
//                    startActivity(i);
//                }
//                return true;
//            }
//        });

    }



//    public void setMess(){
//           chatroom_model chatroom_model = new chatroom_model("1", userIds, lastmessTimestamp);
//            Log.d("model", chatroom_model.toString());
//            firebaseUtil.currentChatRoom().set(chatroom_model);
//            Log.d("dungroi", "oke");
//    }

    void sendMessageToUser(String message){
        ChatMessageModel chatMessageModel = new ChatMessageModel(message,userIds,Timestamp.now());
        FirebaseUtil.getChatroomMessageReference("2").add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                        }
                    }
                });
    }

    void setupChatRecyclerView(){
//        Log.d("chayoke", "oke");

        Query query = FirebaseUtil.getChatroomMessageReference("2")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });


    }

    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference("2").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("chayoke", "oke");
                chatroomModel = task.getResult().toObject(chatroom_model.class);
                if(chatroomModel==null){
                    //first time chat
                    chatroomModel = new chatroom_model(
                            FirebaseUtil.currentUserId(),
                            Timestamp.now()
                    );
                    FirebaseUtil.getChatroomReference("2").set(chatroomModel);
                }
//                Log.d("chayoke", "oke");
            }
        });
    }

    public void setUserID(String phonee){
        db.collection("users")  // Tên của collection chứa dữ liệu người dùng
                .whereEqualTo("phone", phonee)  // Truy vấn theo số điện thoại
                .get()  // Lấy kết quả
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Nếu có kết quả
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String documentID = document.getId();  // Lấy documentID
                            Log.d("DocumentID", "Document ID: " + documentID);
                            userIds = documentID;
                            // Lưu giá trị vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("documentID", userIds);  // Lưu một chuỗi documentID
                            editor.apply();  // Áp dụng thay đổi
                        }
                    } else {
                        Log.d("DocumentID", "No document found with this phone number");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents", e);
                });
    }

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