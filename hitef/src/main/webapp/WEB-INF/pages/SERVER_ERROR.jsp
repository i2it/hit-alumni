<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>出错啦...</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <style type="text/css">
        body {
            background-color: #F8F8F8;
        }

        img {
            max-width: 169px;
            width: 50%;
            margin-top: 64px;
        }
    </style>
</head>
<body>
<div class="container js_container">
    <div class="weui-msg">
        <img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/server_error.jpg">
    </div>
</div>
</body>
</html>

