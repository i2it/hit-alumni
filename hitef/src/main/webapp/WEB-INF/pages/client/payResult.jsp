<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>捐助结果</title>
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

        .hd-content {
            width: 100%;
            border-bottom: 1px dotted #c3c3c3;
            text-align: center;
        }

        .hd-content h3 {
            font-size: 1rem;
            line-height: 30px;
        }

        .fund-body {
            padding: 10px;
            box-sizing: border-box;
            font-size: 14px;
            line-height: 30px;
            color: #333;
        }
    </style>
</head>
<body>
<div class="weui-msg bd-m-t" style="margin-top: 20px;padding-top: 20px;">
    <div class="weui-msg__icon-area" style="margin-bottom: 20px;"><i class="weui-icon-success weui-icon_msg"></i></div>
    <div class="weui-msg__text-area">
        <h2 class="weui-msg__title">完成捐赠</h2>
        <c:if test="${empty out_trade_no}">
            <p class="weui-msg__desc">哈工大基金会感谢您的捐赠</p>
        </c:if>
        <c:if test="${!empty out_trade_no}">
            <p class="weui-msg__desc">感谢您的捐赠,下方领取您的捐赠证书吧。</p>
        </c:if>
    </div>
</div>
<c:if test="${!empty out_trade_no}">
    <div>
        <img style="width: 100%;height: auto;display: block;"
             src="${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg">
        <div style="margin-top: 5px;margin-left: 16px;text-align:left;font-size: 14px;color: #999;">
            温馨提示：点击右上角中分享按钮可实现分享。
        </div>
    </div>
</c:if>
<div class="div-area fund-content">
    <jsp:useBean id="currentDate" class="java.util.Date"/>
    <div class="hd-content"><h3>感谢函</h3></div>
    <div class="fund-body">尊敬的${donatorName}：<br>　　您好！<br>　　感谢您长期以来对哈尔滨工业大学的关心和支持！对您捐资助学的慈心善举表示最诚挚的感谢和最崇高的敬意！<br>　　哈工大教育发展基金会自2009年12月成立以来，受到了社会各界、广大校友和师生员工的关心和支持。基金会始终把公信力建设放在工作首位，坚持公平正义，公开透明，努力维护公益形象，不断提高工作的规范化、科学化和专业化水平。基金会于2013年在黑龙江省社会组织等级评估中，被授予5A级基金会荣誉称号。<br>　　基金会立足实际，开拓创新，打造和开展了一系列如“李昌教育基金”、“春晖创新成果奖励基金”、“困难不怕，哈工大是家”等深入人心的品牌项目，多方面助力了学校的发展建设。<br>　　涓涓细流，汇成爱的大海。您无私的爱心给学校师生员工带来了无限温暖，哈工大的发展历程中将会永远地镌刻着您的名字。再次感谢您对哈工大的支持与厚爱！<br>
        <div align="right">
            <div style="margin-top: 10px;width:auto; display:inline-block !important; display:inline;text-align: center;">
                哈尔滨工业大学教育发展基金会<br><fmt:formatDate value="${currentDate}" pattern="yyyy年MM月dd日"/></div>
        </div>
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
        //隐藏全部菜单项
        wx.hideAllNonBaseMenuItem();
        //显示指定的菜单项
        wx.showMenuItems({
            menuList: ['menuItem:share:appMessage', 'menuItem:share:timeline', 'menuItem:share:qq', 'menuItem:share:QZone']
        });
        //分享给微信朋友
        wx.onMenuShareAppMessage({
            title: '我参与了为母校哈工大捐款的活动，你有我捐得多吗？→ →',
            desc: '尽一份微薄之力，助力母校腾飞！',
            link: '${applicationScope.globalUrlPrefix}/hitef/wechat/donate/certification/${out_trade_no}',
            imgUrl: '${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg',
        });
        //分享到微信朋友圈
        wx.onMenuShareTimeline({
            title: '我参与了为母校哈工大捐款的活动，你有我捐得多吗？→ →',
            link: '${applicationScope.globalUrlPrefix}/hitef/wechat/donate/certification/${out_trade_no}',
            imgUrl: '${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg',
        });
        //分享给QQ朋友
        wx.onMenuShareQQ({
            title: '我参与了为母校哈工大捐款的活动，你有我捐得多吗？→ →',
            desc: '尽一份微薄之力，助力母校腾飞！',
            link: '${applicationScope.globalUrlPrefix}/hitef/wechat/donate/certification/${out_trade_no}',
            imgUrl: '${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg',
        });
        //分享到QQ空间
        wx.onMenuShareQZone({
            title: '我参与了为母校哈工大捐款的活动，你有我捐得多吗？→ →',
            desc: '尽一份微薄之力，助力母校腾飞！',
            link: '${applicationScope.globalUrlPrefix}/hitef/wechat/donate/certification/${out_trade_no}',
            imgUrl: '${applicationScope.globalUrlPrefix}/hitef/wechat/certifications/${out_trade_no}.jpg',
        });
    });
</script>
</html>
