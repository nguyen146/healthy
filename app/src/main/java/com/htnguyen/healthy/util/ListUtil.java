package com.htnguyen.healthy.util;

import com.htnguyen.healthy.model.Chat;
import com.htnguyen.healthy.model.User;

import java.util.List;

public class ListUtil {

    public static boolean equalChatKey(List<Chat> chats, String key){
        for (Chat mChat: chats
             ) {
            if(mChat.getKey().equals(key)) return true;
        }
        return false;
    }

    public static boolean equalUserKey(List<User> user, String key){
        for (User mUser: user
                ) {
            if(mUser.getUserId().equals(key)) return true;
        }
        return false;
    }
}
