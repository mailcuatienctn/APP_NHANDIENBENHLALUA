package com.example.app_nhan_dien_benh_la_lua.Img_Util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class Process_Img {




    //hàm uriToBitmap
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
