package com.springReact.DocumentManager.domain;

public class RequestContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private RequestContext(){}

    public static Long getUserId(){
        return USER_ID.get();
    }
    public static void setUserId(Long userId){
        USER_ID.set(userId);
    }
}
