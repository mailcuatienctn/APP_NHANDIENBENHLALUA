package com.example.app_nhan_dien_benh_la_lua.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class chatroom_model {

    String userIds;
    Timestamp lastmessTimestamp;

    public chatroom_model() {
    }

    public chatroom_model(String userIds, Timestamp lastmessTimestamp) {
        this.userIds = userIds;
        this.lastmessTimestamp = lastmessTimestamp;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastmessTimestamp() {
        return lastmessTimestamp;
    }

    public void setLastmessTimestamp(Timestamp lastmessTimestamp) {
        this.lastmessTimestamp = lastmessTimestamp;
    }



    @Override
    public String toString() {
        return "chatroom_model{" +
                "userIds='" + userIds + '\'' +
                ", lastmessTimestamp=" + lastmessTimestamp +
                '}';
    }
}
