package com.example.app_nhan_dien_benh_la_lua.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_nhan_dien_benh_la_lua.R;
import com.example.app_nhan_dien_benh_la_lua.model.ChatMessageModel;
import com.example.app_nhan_dien_benh_la_lua.fire_base.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    private Context context;
    private FirebaseFirestore db;
    private String documentID;
    private Map<String, String> userNameCache = new HashMap<>();

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.documentID = getID();  // Load documentID once in the constructor
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        String senderId = model.getSenderId();

        if (userNameCache.containsKey(senderId)) {
            // If username is cached, use it
            String userName = userNameCache.get(senderId);
            updateUI(holder, model, senderId, userName);
        } else {
            // If not cached, fetch the username from Firestore
            getUserNameByDocumentID(senderId, userName -> {
                userNameCache.put(senderId, userName);  // Cache the username
                updateUI(holder, model, senderId, userName);
            });
        }
    }

    private void updateUI(ChatModelViewHolder holder, ChatMessageModel model, String senderId, String userName) {
        if (senderId.equals(documentID)) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());
        } else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(userName + ": " + model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }

    // Lấy documentID từ SharedPreferences
    private String getID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("documentID", null); // Trả về null nếu không có dữ liệu
    }

    // Phương thức lấy tên người dùng từ documentID
    private void getUserNameByDocumentID(String documentIDD, OnUserNameFetchedListener listener) {
        db.collection("users").document(documentIDD).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = document.getString("user");
                            if (username != null) {
                                listener.onUserNameFetched(username);
                            } else {
                                Log.d("UserName", "Không tìm thấy trường 'username' trong document.");
                            }
                        } else {
                            Log.d("UserName", "Không tìm thấy tài liệu với document ID: " + documentIDD);
                        }
                    } else {
                        Log.e("UserName", "Lỗi khi truy vấn Firestore: " + task.getException().getMessage());
                    }
                });
    }

    // Callback interface for fetching username
    public interface OnUserNameFetchedListener {
        void onUserNameFetched(String userName);
    }
}
