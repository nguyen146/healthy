package com.htnguyen.healthy.util;

import com.htnguyen.healthy.model.Chat;

import java.util.List;

public class ListUtil {

    public static boolean equalChatKey(List<Chat> chats, String key){
        for (Chat mChat: chats
             ) {
            if(mChat.getKey().equals(key)) return true;
        }
        return false;
    }
}
