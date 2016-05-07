<!doctype html>
<html lang="en"><head>
    <meta charset="utf-8">
    <title>管理后台</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link href="//cdn.bootcss.com/bootstrap3-dialog/1.34.9/css/bootstrap-dialog.min.css" rel="stylesheet" media="screen">
	<link href="//cdn.bootcss.com/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
	
    <script src="//cdn.bootcss.com/jquery/1.12.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/res/css/theme.css">

</head>
<body class=" theme-blue">
  <style type="text/css">
      .navbar-default .navbar-brand, .navbar-default .navbar-brand:hover{color: #fff;}
  </style>
  <script type="text/javascript">
    $(function() {
          var uls = $('.sidebar-nav > ul > *').clone();
          uls.addClass('visible-xs');
          $('#main-menu').append(uls.clone());
      });
  </script>
  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <![endif]-->
  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <div class="navbar navbar-default" role="navigation">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">导航切换</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="" href="index.html"><span class="navbar-brand"><span class="fa fa-paper-plane"></span> 管理后台</span></a></div>

      <div class="navbar-collapse collapse" style="height: 1px;">
        <ul id="main-menu" class="nav navbar-nav navbar-right">
          <li class="dropdown hidden-xs">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <span class="glyphicon glyphicon-user padding-right-small" style="position:relative;top: 3px;"></span> ${session.manager.username}
                  <i class="fa fa-caret-down"></i>
              </a>

            <ul class="dropdown-menu">
              <li><a href="${ctx!}/modifyPwd" data-dialog="modifyPwd" data-dclass="small">修改密码</a></li>
              <li class="divider"></li>
              <li><a tabindex="-1" href="${ctx!}/logout">安全退出</a></li>
            </ul>
          </li>
        </ul>

      </div>
    </div>
  </div>
  

  <div class="sidebar-nav">
  <ul>
    <li><a href="#" data-target=".accounts-menu" class="nav-header" data-toggle="collapse"><i class="fa fa-fw fa-home"></i> 首页<i class="fa fa-collapse"></i></a></li>
    <li><ul class="accounts-menu nav nav-list">
    	<li><a href="${ctx!}/home" data-aim="mainContent"><i class="fa fa-fw fa-home"></i> 首页</a></li>
    	<li><a href="${ctx!}/sportsevents" data-aim="mainContent"><span class="fa fa-caret-right"></span> 体育赛事管理</a></li>
    	<li><a href="${ctx!}/sendResult" data-aim="mainContent"><span class="fa fa-caret-right"></span> 推送结果管理</a></li>
    	<li><a href="${ctx!}/host" data-aim="mainContent"><span class="fa fa-caret-right"></span> 接收地址管理</a></li>
        <li><a href="${ctx!}/user" data-aim="mainContent"><span class="fa fa-caret-right"></span> 用户管理</a></li>
        <li><a href="${ctx!}/hgurl" data-aim="mainContent"><span class="fa fa-caret-right"></span> 皇冠地址管理</a></li>
        <li><a href="${ctx!}/hgaccount" data-aim="mainContent"><span class="fa fa-caret-right"></span> 皇冠账号管理</a></li>
    </ul></li>
  </ul>
  </div>

  <div class="content" id="mainContent">
    <#include "sys/home.ftl">
  </div>
  <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="//cdn.bootcss.com/bootstrap3-dialog/1.34.9/js/bootstrap-dialog.min.js"></script>
  <script src="//cdn.bootcss.com/moment.js/2.11.2/moment.min.js"></script>
  <script src="//cdn.bootcss.com/moment.js/2.11.2/locale/zh-cn.js"></script>
  <script src="//cdn.bootcss.com/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
  <script src="//cdn.bootcss.com/jquery-validate/1.15.0/jquery.validate.min.js"></script>
  <script src="//cdn.bootcss.com/jquery-validate/1.15.0/additional-methods.min.js"></script>
  <script src="${ctx!}/res/js/layer-v2.1/layer.min.js"></script>
  <script>var vrctx="${ctx!}";</script>
 	<script src="${ctx!}/res/js/base.js"></script>
</body></html>