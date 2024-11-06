package com.example.app_nhan_dien_benh_la_lua.utils;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;
public class AndroidUtil {
    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
