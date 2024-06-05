package edu.tcu.cs.hogwarts_artifact_online.System;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private boolean flag;
    private int code;
    private String message;
    private Object data;

    public Result(){}
    public Result(boolean flag, int code, String message){
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
    public Result(boolean flag, int code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
