package com.example.app_nhan_dien_benh_la_lua.fire_base;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {


    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserIdDetail(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

}
