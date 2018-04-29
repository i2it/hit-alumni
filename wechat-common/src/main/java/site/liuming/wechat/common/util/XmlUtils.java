package site.liuming.wechat.common.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * xml（字符串格式）和实例对象之间的转换
 *
 * @author liuming
 * @date 2017/11/15 15:14
 */
public class XmlUtils {

    /**
     * 将实例对象转为xml格式的字符串内容
     *
     * @param obj 需要转换的对象
     * @return
     * @throws JAXBException
     * @throws UnsupportedEncodingException
     */
    public static String object2XmlStr(Object obj) throws JAXBException, UnsupportedEncodingException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());

        Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // 设置编码字符集
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(obj, baos);

        return new String(baos.toByteArray(), "UTF-8"); // 生成XML字符串
    }

    /**
     * 将xml格式的字符串内容转为对应类型的对象
     *
     * @param xmlStr xml格式的字符串信息
     * @param clazz  类类型
     * @param <T>    类型
     * @return 类型为T的对象
     * @throws JAXBException
     */
    public static <T> Object xmlStr2Object(String xmlStr, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        // 进行将Xml转成对象的核心接口
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlStr);
        return unmarshaller.unmarshal(reader);
    }

}
