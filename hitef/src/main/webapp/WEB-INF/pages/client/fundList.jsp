<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>筹款基金项目列表</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/hitef/wechat/res/css/base.css">
    <style type="text/css">
        .funds {
            margin-top: 10px;
            margin-bottom: 10px;
            font-size: 14px;
        }

        .funds ul {
            margin: 0 8px;
        }

        .funds li {
            background: #e1e3e2;
            padding: 0px 0px 0 0px;
            margin: 7px 0;
            border-radius: 10px;
            border-bottom: 1px solid #cccccc;
        }

        .funds li a {
            display: block;
            padding: 15px 10px;
            text-decoration: none;
        }

        a:visited {
            color: inherit;
            text-decoration: none;
        }

        a:link {
            color: inherit;
            text-decoration: none;
        }

        .funds li span {
            float: right;
            margin-right: 8px;
            width: 20px;
            height: 20px;
            position: relative;
        }
    </style>
</head>
<body>
<div class="container js_container">
    <div><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/top_img.jpg" width="100%"></div>
    <div class="funds">
        <ul>
            <c:if test="${not empty fundItem}">
                <li><a href="/hitef/wechat/items/${fundItem.id}">
                        ${fundItem.name}
                    <span><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/arrow.png" width="125%"></span></a>
                </li>
            </c:if>
            <c:forEach items="${fundItems}" var="item">
                <li><a href="/hitef/wechat/items/${item.id}">
                        ${item.name}
                    <span><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/arrow.png"
                               width="125%"></span></a>
                </li>
            </c:forEach>
        </ul>
    </div>
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

