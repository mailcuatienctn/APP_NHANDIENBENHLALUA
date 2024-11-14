package com.example.app_nhan_dien_benh_la_lua.Img_Util;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class Model_Solution {
    public Model_Solution() {
    }

    //    private String
    public Intent Solution(String solution){
                String url = "";
                if (solution.equals("Cháy Bìa Lá")){
                     url = "https://vnfarm.com.vn/benh-chay-bia-la-lua";
                } else if (solution.equals("Đốm Nâu")) {
                    url = "https://tanixa.com/benh-dom-nau-tren-lua/";
                }else {
                    url = "https://vnfarm.com.vn/benh-dao-on-tren-lua";
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                return browserIntent;
            }

    }

