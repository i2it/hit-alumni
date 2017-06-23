package net.i2it.hit.hit_alumni.entity.vo.api.request;

/**
 * 微信菜单创建需要构建出的中的菜单类，转为对应的json格式字符串后直接提交给接口创建菜单
 * Created by liuming on 2017/4/21.
 */
public class MenuVO {

    private Button[] button;

    public Button[] getButton() {
        return button;
    }

    public void setButton(Button[] button) {
        this.button = button;
    }

    /**
     * click类型的按钮
     */
    public class ClickButton extends Button {

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    /**
     * view类型的按钮
     */
    public class ViewButton extends Button {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    /**
     * 每一个按钮的对应实体类
     */
    public class Button {

        private String type;
        private String name;
        private Button[] sub_button;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Button[] getSub_button() {
            return sub_button;
        }

        public void setSub_button(Button[] sub_button) {
            this.sub_button = sub_button;
        }

    }

}