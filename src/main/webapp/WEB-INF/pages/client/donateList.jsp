<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>捐款名录</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/wechat/hitef/res/css/base.css">
    <style type="text/css">
        #top-area {
            margin-bottom: 20px;
        }

        .weui-form-preview {
            margin: 8px;
        }

        .weui-form-preview__value {
            text-align: left;
        }

        #bottom-area a {
            display: inline-block;
            padding-left: 20px;
        }
    </style>
</head>
<body>
<div class="container js_container">
    <div id="top-area"><img src="${applicationScope.globalUrlPrefix}/wechat/hitef/res/img/top_img.jpg" width="100%">
    </div>
    <c:forEach var="item" items="${donateList}">
        <div class="weui-form-preview">
            <div class="weui-form-preview__bd">
                <p>
                    <label class="weui-form-preview__label">捐助者：</label>
                    <span class="weui-form-preview__value">
                        <c:choose>
                            <c:when test="${item.donatorName==null}">未知（捐助者未填写）</c:when>
                            <c:otherwise>${item.donatorName}</c:otherwise>
                        </c:choose>
                    </span>
                </p>
                <p>
                    <label class="weui-form-preview__label">金　额：</label>
                    <span class="weui-form-preview__value">￥<fmt:formatNumber pattern="#0.00"
                                                                              value="${item.donateMoney}"/></span>
                </p>
                <p>
                    <label class="weui-form-preview__label">项　目：</label>
                    <span class="weui-form-preview__value">${item.fundItemName}</span>
                </p>
                <p>
                    <label class="weui-form-preview__label">时　间：</label>
                    <span class="weui-form-preview__value">
                        <fmt:formatDate value="${item.donateDate}" pattern="yyyy-MM-dd HH:mm"/>
                        <c:set var="dateObj" value='${item.donateDate}'/>
                    </span>
                </p>
            </div>
        </div>
    </c:forEach>
</div>
<div id="bottom-area" style="margin:15px 30px 20px auto;text-align:right;">
    <%--暂时不提供上一页功能，用户可通过返回键查看上一页内容--%>
    <%--<a href="">上一页</a>--%>
    <c:if test="${donateList.size()==20}">
        <a href="${applicationScope.globalUrlPrefix}/wechat/hitef/donate/list?date=<fmt:formatDate value="${dateObj}" pattern="yyyy-MM-dd HH:mm:ss"/>">下一页</a>
    </c:if>
</div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    wx.config({
        debug: ${requestScope.jsSdkConfig.debug},
        appId: '${requestScope.jsSdkConfig.appId}',
        timestamp:${requestScope.jsSdkConfig.timestamp},
        nonceStr: '${requestScope.jsSdkConfig.nonceStr}',
        signature: '${requestScope.jsSdkConfig.signature}',
        jsApiList: ${requestScope.jsSdkConfig.jsApiList}
    });
    wx.ready(function () {
        wx.hideAllNonBaseMenuItem();
    });
</script>
</html>
