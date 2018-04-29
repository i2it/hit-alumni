package site.liuming.hitef.tld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class DateFormatterTag extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatterTag.class);

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
            logger.error("自定义时间格式化Tag解析异常，信息：{}", e.getMessage());
        }
        return super.doStartTag();
    }

}
