package com.hust.keyRD.commons.exception.fabric;

/**
 * @program: fabricPlatform
 * @description: Fabric异常
 * @author: zwh
 * @create: 2021-01-06 20:14
 **/
public class FabricException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final String message;

    public FabricException(String message) {
        this.message = message;
    }

    public FabricException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
