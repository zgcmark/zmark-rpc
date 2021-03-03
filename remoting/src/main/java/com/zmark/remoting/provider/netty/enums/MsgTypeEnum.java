package com.zmark.remoting.provider.netty.enums;

/**
 * 商品状态
 */
public enum MsgTypeEnum implements EnumInterface {
    request(1, "请求"),
    response(2, "响应"),

    ;

    private int value;
    private String description;

    MsgTypeEnum(int _value, String _desc) {
        this.description = _desc;
        this.value = _value;
    }

    public static MsgTypeEnum valueOfType(int value) {
        for (MsgTypeEnum type : MsgTypeEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
