package net.i2it.hit.hitef.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FormatDateTag extends TagSupport {

    private Long value;

    private String pattern;

    public void setValue(Long value) {
        this.value = value;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public int doStartTag() throws JspException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String result = dateFormat.format(value * 1000);
        try {
            pageContext.getOut().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doStartTag();
    }

}
