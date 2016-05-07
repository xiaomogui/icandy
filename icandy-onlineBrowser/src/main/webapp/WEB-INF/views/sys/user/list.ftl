<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">用户管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/user" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <div class="form-group margin-top">
	        <label for="rn_type">账户名：</label>
	        <input class="form-control" name="username1" type="text" value="${username1}" placeholder="账户名">
	      </div>
	      <button type="submit" class="btn btn-success margin-top"><i class="icon-search"></i>&nbsp;查询</button>
	    	&nbsp; &nbsp;
	      <a class="btn btn-primary margin-top" href="${ctx!}/user/add" data-dialog="addSysSetting"><i class="fa fa-plus"></i> 添加</a>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>帐户名</th>
	      <th style="width: 3.5em;"></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.username}</td>
	      <td>
	          <a data-todoAjax="" href="${ctx!}/user/resetPwd/${x.id}" title="重置用户【${x.username}】的密码为123456"><i class="fa fa-lock"></i></a>
	          <a data-todoAjax="" href="${ctx!}/user/delete/${x.id}" title="确定要删除用户【${x.username}】"><i class="fa fa-trash-o"></i></a>
	      </td>
	    </tr>
	    </#list>
	  </tbody>
	</table>
	<#include "common/_paginate.ftl" />
	<@paginate currentPage=recordPage.pageNumber totalPage=recordPage.totalPage/>
</div>