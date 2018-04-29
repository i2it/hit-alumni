<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>${item.name}</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/hitef/wechat/res/css/base.css">
    <style type="text/css">
        .div-area {
            width: 95%;
            color: #333;
            background: #fff;
            margin: 16px auto 20px;
            border: 1px solid #c5c5c5;
            border-radius: 8px;
            display: table;
            box-sizing: border-box;
        }

        .fund-content {
            padding: 10px;
        }

        .donate-area {
            padding: 0px 10px;
        }

        .hd-content {
            width: 100%;
            border-bottom: 1px dotted #c3c3c3;
            text-align: center;
        }

        .hd-content h3 {
            font-size: 1rem;
            line-height: 30px;
        }

        .fund-fundItemName {
            padding: 10px;
            box-sizing: border-box;
            font-size: 14px;
            line-height: 30px;
            color: #333;
        }
    </style>
</head>
<body>
<div class="container js_container">
    <div><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/top_img.jpg" width="100%"></div>
    <div class="div-area fund-content">
        <div class="hd-content"><h3>${item.name}</h3></div>
        <div class="fund-fundItemName">
            <p>${item.desc}</p>
            <c:if test="${not empty item.pictureName}"><img src="/hitef/wechat/pictures/${item.pictureName}" width="100%"></c:if>
        </div>
    </div>
    <div class="weui-cells weui-cells_form div-area donate-area">
        <div class="weui-cell" id="donateMoneyDiv">
            <div class="weui-cell__hd"><label class="weui-label">捐款金额</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" id="donateMoney" type="number" placeholder="单位元，最低1元"/>
            </div>
        </div>
    </div>
    <div class="weui-btn-area" style="text-align: center;margin-bottom:20px;">
        <button id="btn-sbt" class="weui-btn weui-btn_mini weui-btn_primary btn">捐助</button>
    </div>
</div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script src="http://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
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

    $("#donateMoney").focus();

    $("#btn-sbt").click(function () {
        var param = $("#donateMoney").val();
        if (checkMoneyFormat(param)) {
            var targetUrl = "${targetUrl}";
            var redirectUrl = "${redirectUrl}";
            redirectUrl = redirectUrl + "?payInfo=${item.id}__${item.name}__" + $("#donateMoney").val();
            redirectUrl = encodeURI(redirectUrl);
            targetUrl = targetUrl.replace("REDIRECT_URI", redirectUrl);
            window.location.href = targetUrl;
        }
    });

    function checkMoneyFormat(param) {
        var reg = /^[0-9]+\.?[0-9]{0,2}$/;
        var element = $("#donateMoneyDiv");
        if (!reg.test(param) || param < 1) {
            element.addClass("weui-cell_warn");
            if ($("#donateMoneyWarn").length > 0) {
            } else {
                element.append("<div id='donateMoneyWarn' class='weui-cell__ft'><i class='weui-icon-warn'></i></div>");
            }
            return false;
        } else {
            element.removeClass("weui-cell_warn");
            $("#donateMoneyWarn").remove();
            return true;
        }
    }
</script>
</html>