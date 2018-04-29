package site.liuming.hitef.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.liuming.hitef.AppWebPageJsSdkConfigGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截所有请求，为每一个请求生成不同的js签名，并包装成微信网页页面的js-sdk配置对象
 */
@Aspect
@Component
public class JsSdkConfigAspect {

    @Autowired
    private AppWebPageJsSdkConfigGenerator generator;

    @Pointcut("execution(public String site.liuming.hitef.controller.*.*(..,org.springframework.ui.ModelMap))")
    public void request() {
    }


    @Before("request()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof ModelMap) {
                ModelMap map = (ModelMap) arg;
                //调用微信页面js sdk功能需要的配置信息
                map.put("jsSdkConfig", generator.generateJsSdkConfig(request));
            }
        }
    }

}
