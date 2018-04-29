package site.liuming.hitef.constant;


/**
 * 捐助项目类型枚举类
 */
public enum FundItemTypeEnum {
    /**
     * 校级捐助项目（特别推荐）
     */
    SCHOOL(1),
    /**
     * 院系捐助项目
     */
    ACADEMY(2),;

    private Integer type;

    FundItemTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
