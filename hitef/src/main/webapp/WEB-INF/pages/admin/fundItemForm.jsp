<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>哈尔滨工业大学教育发展基金会微信服务号管理后台</title>
    <style type="text/css" rel="stylesheet">
        input, select {
            width: 200px;
            height: 30px;
        }

        .sbt-btn {
            width: 100px;
        }
    </style>
</head>
<fundItemName>
    <%--<jsp:include page="nav_base.jsp" flush="true"/>--%>
    <div style="padding-left: 20px;">
        <h2>
            <c:choose>
                <c:when test="${opt=='update'}">修改基金信息
                    <c:set var="actionUrl" value="/hitef/wechat/items/${fundItem.id}"/>
                </c:when>
                <c:when test="${opt=='add'}">新增基金信息
                    <c:set var="actionUrl" value="/hitef/wechat/items"/>
                </c:when>
                <c:otherwise>基金项目信息</c:otherwise>
            </c:choose>
        </h2>
        <hr>
        <form action="${actionUrl}" method="post" enctype="multipart/form-data">
            <input type="hidden" name="desc" value="">
            基金名称：<input type="text" name="name" value="${fundItem.name}" required="required"><br><br>
            基金类型：<select name="type" required="required" style="text-align: center;">
            <option value="0">特别推荐（校级基金）</option>
            <option value="1">院系基金</option>
        </select><br><br>
            上传图片：<input type="file" name="picture">　<c:if test="${not empty fundItem.pictureName}"><a href="/hitef/wechat/pic/${fundItem.pictureName}" target="_blank">上传过的配图</a></c:if><br><br>
            基金描述：<textarea rows="8" cols="100" required="required">${fundItem.desc}</textarea><br><br>
            <c:choose>
                <c:when test="${opt=='update'}">
                    <input class="sbt-btn" type="submit" value="修改">
                </c:when>
                <c:when test="${opt=='add'}">
                    <input class="sbt-btn" type="submit" value="添加">
                </c:when>
            </c:choose>
        </form>
    </div>
</fundItemName>
<script src="http://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script>
    $(function () {
        var type = "${fundItem.type}";
        if (type == "1") {
            $("select").val(type);
        }
    });
    $("form").submit(function () {
        $("input[name='desc']").val($("textarea").val());
    });
</script>
</html>
