<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>我的捐助证书</title>
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
            margin-top: 32px;
            padding: 10px;
        }

        .hd-content {
            width: 100%;
            padding-bottom: 10px;
            border-bottom: 1px dotted #c3c3c3;
            text-align: center;
        }

        .hd-content h3 {
            font-size: 1rem;
            line-height: 30px;
        }

        .fund-body {
            padding: 20px 8px 10px 8px;
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
        <div class="hd-content"><h3>捐助证书</h3></div>
        <div class="fund-body"><img style="width: 100%;height: auto;display: block;" alt="捐助证书"
                                    src="${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg">
        </div>
    </div>
    <div style="margin-left: 25px;"><a href="/hitef/wechat/items">我也要去捐助，助母校腾飞！</a></div>
</div>
</body>
</html>


