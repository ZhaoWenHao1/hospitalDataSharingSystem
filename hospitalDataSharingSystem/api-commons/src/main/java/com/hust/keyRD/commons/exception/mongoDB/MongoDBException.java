package com.hust.keyRD.commons.exception.mongoDB;

/**
 * @program: system
 * @description: mongoDB异常
 * @author: zwh
 * @create: 2021-01-14 19:09
 **/
public class MongoDBException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final String message;
    
    public MongoDBException(){
        this.message = "mongoDB exception";
    }

    public MongoDBException(String message) {
        this.message = message;
    }

    public MongoDBException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
