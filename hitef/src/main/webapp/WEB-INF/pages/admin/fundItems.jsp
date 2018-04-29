<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="my" uri="/WEB-INF/tld/fmt.tld" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>哈尔滨工业大学教育发展基金会微信服务号管理后台</title>
    <style type="text/css" rel="stylesheet"></style>
</head>
<fundItemName>
    <%--<jsp:include page="nav_base.jsp" flush="true"/>--%>
    <div style="padding-left: 20px;">
        <h2>正在进行的基金项目</h2>
        <hr>
        <p>
            <a href="/hitef/wechat/items?opt=add">添加新的基金项目</a>
        </p>
        <c:forEach items="${fundItems}" var="item" varStatus="itemStatus">
            <hr>
            <div>
                <p>#${itemStatus.count}　|　
                    <c:choose>
                        <c:when test="${item.type==0}">特别推荐（校级基金）</c:when>
                        <c:when test="${item.type==1}">院系基金</c:when>
                    </c:choose>
                    　|　${item.name}</p>
                <p>${item.desc}</p>
                <p>
                    <my:formatDate value="${item.ctime}" pattern="yyyy年MM月dd日 HH:mm"/>　
                    <c:if test="${not empty item.pictureName}">|　<a href="/hitef/wechat/pic/${item.pictureName}" target="_blank">查看配图</a>　</c:if>
                    |　<a href="/hitef/wechat/items/${item.id}?opt=update">修改</a>　
                    |　<a href="/hitef/wechat/items/${item.id}?opt=stop"
                         onClick="return confirm('确认要终止编号为${itemStatus.count}的基金项目吗?');">终止</a>
                </p>
            </div>
        </c:forEach>
    </div>
</fundItemName>
</html>
