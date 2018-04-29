<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>正在跳转...</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/hitef/wechat/res/css/base.css">
</head>
<body>
<div class="weui-loadmore bd-m-t">
    <i class="weui-loading"></i>
    <span class="weui-loadmore__tips">正在跳转...</span>
</div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var time = 3;
    autoJump();

    function autoJump() {
        setTimeout("autoJump()", 1000)
        time = time - 1;
        if (time == 0) {
            readyPay();
        }
    }

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

    function readyPay() {
        wx.chooseWXPay({
            timestamp: ${requestScope.payInfo.timeStamp},
            nonceStr: '${requestScope.payInfo.nonceStr}',
            package: '${requestScope.payInfo.packageStr}',
            signType: '${requestScope.payInfo.signType}',
            paySign: '${requestScope.payInfo.paySign}',
            success: function () {
                window.location.href = "${applicationScope.globalUrlPrefix}/hitef/wechat/test/?action=getDonatorFormPage&outTradeNo=${out_trade_no}"
            },
            cancel: function () {
                window.location.href = "${applicationScope.globalUrlPrefix}/hitef/wechat/items/${fundItemId}";
            }
        });
    }
</script>
</html>
