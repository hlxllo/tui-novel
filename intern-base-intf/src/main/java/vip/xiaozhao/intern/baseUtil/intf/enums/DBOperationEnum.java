package vip.xiaozhao.intern.baseUtil.intf.enums;

// 数据库操作枚举
public enum DBOperationEnum {
    INSERT(1, "insert"),
    UPDATE(2, "update"),
    DELETE(3, "delete");

    private int number;
    private String type;

    DBOperationEnum(int number, String type) {
        this.number = number;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public static String getType(int number) {
        for (DBOperationEnum dBOperationEnum : DBOperationEnum.values()) {
            if (dBOperationEnum.getNumber() == number) {
                return dBOperationEnum.getType();
            }
        }
        return "未知方法";
    }
}
