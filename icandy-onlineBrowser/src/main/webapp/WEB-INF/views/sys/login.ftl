<!doctype html>
<html lang="en"><head>
    <meta charset="utf-8">
    <title>登录</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx!}/res/css/theme.css">
</head>
<body class=" theme-blue">
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <![endif]-->

  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->

<div class="dialog">
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">管理员登录</p>
        <div class="panel-body">
            <form action="${ctx!}/loginAction" method="post">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" class="form-control span12" name="username" value="${username}">
                </div>
                <div class="form-group">
                <label>密  码</label>
                    <input type="password" class="form-controlspan12 form-control" name="password">
                </div>
                <button class="btn btn-primary pull-right">登录</button>
                <label class="remember-me"><#if errMsg?has_content>${errMsg}</#if></label>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>
</body></html>
