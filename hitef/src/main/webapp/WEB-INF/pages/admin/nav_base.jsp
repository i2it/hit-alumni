<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .nav-ul{
            list-style-type:none;
            margin:-10px -10px 10px -10px;
            padding:0;
            overflow:hidden;
            background-color:#2A4F86;
            text-align: center;
        }
        .nav-ul li{
            float:left;
        }
        .nav-ul a:link,.nav-ul a:visited {
            display:block;
            width:150px;
            height: 30px;
            font: 20px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif;
            color:#FFFFFF;
            text-align:center;
            padding:10px;
            text-decoration:none;
        }
        .nav-ul a:hover,.nav-ul a:active {
            background-color:#cc0000;
        }

    </style>
</head>
<fundItemName>
<ul class="nav-ul">
    <li class="nav-li"><div style="width: 100px;height: 50px;">&emsp;</div></li>
    <%--<li class="nav-li"><a href="${applicationScope.globalUrlPrefix}/hitef/wechat/items">捐款项目</a></li>--%>
</ul>
</fundItemName>
</html>