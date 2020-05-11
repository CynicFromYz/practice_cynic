package com.cynic.enums;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/5/6 9:03
 */
public enum ResultState {

    SUCCESS("1", "处理成功"),
    FAILED("0", "处理失败"),
    DEALING("2", "处理中");

    String code;
    String message;

    ResultState(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
