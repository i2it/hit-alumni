<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes"/>
    <title>捐助者信息完善</title>
    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css">
    <link rel="stylesheet" type="text/css" href="${applicationScope.globalUrlPrefix}/hitef/wechat/res/css/base.css">
</head>
<body>
<form class="container js_container"
      action="${applicationScope.globalUrlPrefix}/hitef/wechat/donate/?action=updateDonatorInfo" method="post"
      id="donator-info" onsubmit="return checkTrueName();">
    <div><img src="${applicationScope.globalUrlPrefix}/hitef/wechat/res/img/top_img.jpg" width="100%"></div>
    <input type="hidden" name="outTradeNo" value="${out_trade_no}">
    <div class="weui-cells__title" style="margin-top: 30px;">做好事记得留名奥</div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell" id="div0">
            <div class="weui-cell__hd"><label class="weui-label">姓　　名</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" name="trueName" id="trueName" required="required"
                       placeholder="请输入您的真实姓名" value="${donateInfo.trueName}"/>
            </div>
        </div>
        <div class="weui-cell weui-cell_switch">
            <div class="weui-cell__bd">选择匿名</div>
            <div class="weui-cell__ft">
                <input class="weui-switch" type="checkbox" id="anonymous" value="0"/>
            </div>
        </div>
        <div class="weui-cell" id="div1">
            <div class="weui-cell__hd"><label class="weui-label">入学年份</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" pattern="[0-9]*" name="entryYear" placeholder="请输入您的入学年份"
                       value="${donateInfo.entryYear}"/>
            </div>
        </div>
        <div class="weui-cell" id="div2">
            <div class="weui-cell__hd"><label class="weui-label">专业名称</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" name="major" placeholder="请输入您的专业名称"
                       value="${donateInfo.major}"/>
            </div>
        </div>
        <div class="weui-cell" id="div3">
            <div class="weui-cell__hd"><label class="weui-label">手机号码</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" pattern="[0-9]*" name="phone" placeholder="请输入您的手机号码"
                       value="${donateInfo.entryYear}"/>
            </div>
        </div>
        <div class="weui-cell" id="div4">
            <div class="weui-cell__hd"><label class="weui-label">工作单位</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" name="company" placeholder="请输入您的工作单位"
                       value="${donateInfo.phone}"/>
            </div>
        </div>
        <div class="weui-cell" id="div5">
            <div class="weui-cell__hd"><label class="weui-label">职　　位</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" name="job" placeholder="请输入您的职务"
                       value="${donateInfo.job}"/>
            </div>
        </div>
        <div class="weui-cell" id="div6">
            <div class="weui-cell__hd"><label class="weui-label">邮寄地址</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" name="mailAddr" placeholder="请输入您的邮寄地址"
                       value="${donateInfo.mailAddr}"/>
            </div>
        </div>
    </div>
    <div class="weui-cells__title">捐赠留言</div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <textarea class="weui-textarea" placeholder="匿名了也是可以填的奥" rows="3"
                          id="comment_">${donateInfo.comment}</textarea>
                <input type="hidden" name="comment" id="comment" value="${donateInfo.comment}">
            </div>
        </div>
    </div>
    <div class="weui-btn-area" style="text-align: center;margin-bottom: 20px;">
        <input id="sbt-btn" class="weui-btn weui-btn_mini weui-btn_primary btn" type="submit" value="提交">
    </div>
</form>
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

    var truenameObj = $("#trueName");
    var reloadFlag = false;
    $(function () {
        if (truenameObj.val() != "") {
            reloadFlag = true;
            $("#donator-info input").each(function () {
                $(this).attr("readonly", "readonly");
            });
            for (var i = 0; i < 7; i++) {
                $("#div" + i).addClass("input-disabled")
            }

            $("#sbt-btn").attr("type", "hidden");
        }
    });

    $("#anonymous").click(function () {
        if (reloadFlag == false) {
            var tmp = $("#anonymous").val();
            if (tmp == 0) {
                $("#anonymous").val(1);
                truenameObj.val("匿名");
                $("#donator-info input").each(function () {
                    $(this).attr("readonly", "readonly");
                });
                for (var i = 0; i < 7; i++) {
                    $("#div" + i).addClass("input-disabled")
                }
            } else {
                $("#anonymous").val(0);
                truenameObj.val("");
                $("#donator-info input").each(function () {
                    $(this).removeAttr("readonly");
                });
                for (var i = 0; i < 7; i++) {
                    $("#div" + i).removeClass("input-disabled")
                }
            }
        }
    });

    $("#comment_").change(function () {
        $("#comment").val($(this).val());
    });

    function checkTrueName() {
        if ($.trim($("#trueName").val()) == "") {
            return false;
        }
        $("#trueName").val($.trim($("#trueName").val()));
    }
</script>
</html>