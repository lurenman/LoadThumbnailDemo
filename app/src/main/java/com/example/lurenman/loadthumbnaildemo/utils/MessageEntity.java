package com.example.lurenman.loadthumbnaildemo.utils;

/**
 * @author ChenYe
 */

public class MessageEntity {

    /**
     * 标识
     */
    public int mWhat;
    /**
     * 数据
     */
    public Object mMsg;

    private MessageEntity() {

    }

    private static class Holder {
        private static final MessageEntity INSTANCE = new MessageEntity();
    }

    public static MessageEntity obtianMessage() {
        return Holder.INSTANCE;
    }
}
