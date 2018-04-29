<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>捐款统计</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/hitef/wechat/res/css/base.css">
    <style type="text/css">
        #top-area {
            margin-bottom: 20px;
        }

        table {
            margin-bottom: 30px;
        }

        thead tr {
            background-color: #cccccc;
        }

        td, th {
            border: 1px solid #cccccc;
            text-align: center;
            padding: 4px 8px;
        }

        .col-2 {
            min-width: 4em;
        }

        .col-3 {
            min-width: 4em;
        }
    </style>
</head>
<body>
<div class="container js_container" align="center">
    <div id="top-area"><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/top_img.jpg" width="100%">
    </div>
    <table width="98%">
        <thead>
        <tr>
            <th>总捐款次数</th>
            <th colspan="2">${stat.donateCount}</th>
        </tr>
        <tr>
            <th>基金项目</th>
            <th class="col-2">捐款次数</th>
            <th class="col-3">捐款总额<br>(￥)</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${stat.fundItemStat}">
            <tr>
                <td>
                    <a href="/hitef/wechat/items/${item.fundItemId}">${item.fundItemName}</a>
                </td>
                <td>${item.totalCount}</td>
                <td><fmt:formatNumber pattern="#0.00" value="${item.totalMoney}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
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

