package site.liuming.hitef.domain;

public class FundItemDO extends BaseInfo {

    private String desc;//描述
    private String pictureName;//图片文件名
    private int type;////基金所属的类别id，0-特别推荐基金（校级基金）,1-院系基金
    private int status;//状态，是否终止：0-终止，1-正常
    private int ctime;//创建时间，秒，（1970年1月1日0时起的秒数）
    private int utime;//修改时间

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "FundItemDO{" +
                "desc='" + desc + '\'' +
                ", pictureName='" + pictureName + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", ctime=" + ctime +
                ", utime=" + utime +
                "} " + super.toString();
    }

}
