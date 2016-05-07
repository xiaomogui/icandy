<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">接收地址管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/host" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <div class="form-group margin-top">
	        <label for="rn_type">接收地址：</label>
	        <input class="form-control" name="host" type="text" value="${host}" placeholder="接收地址">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">状态：</label>
	        <select class="form-control" name="status">
	            <option value="0">请选择</option>
	            <option value="1"<#if status?has_content && status==1>selected</#if>>启用</option>
	            <option value="2"<#if status?has_content && status==2>selected</#if>>失效</option>
	          </select>
	      </div>
	      <button type="submit" class="btn btn-success margin-top"><i class="icon-search"></i>&nbsp;查询</button>
	    	&nbsp; &nbsp;
	      <a class="btn btn-primary margin-top" href="${ctx!}/host/add" data-dialog="addHost"><i class="fa fa-plus"></i> 添加</a>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>接收地址</th>
	      <th>状态</th>
	      <th style="width: 3.5em;"></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.host}</td>
	      <td><#if x.status==1>启用<a data-todoAjax="" href="${ctx!}/host/resetStatus/${x.id}-2" title="确定要关闭【${x.host}】？">(关闭)</a>
				<#else>失效<a data-todoAjax="" href="${ctx!}/host/resetStatus/${x.id}-1" title="确定要启用【${x.host}】？">(启用)</a></#if></td>
	      <td>
	          <a data-dialog="editHost" href="${ctx!}/host/edit/${x.id}"><i class="fa fa-pencil"></i></a>
	          <a data-todoAjax="" href="${ctx!}/host/delete/${x.id}" title="确定要删除IP【${x.host}】？"><i class="fa fa-trash-o"></i></a>
	      </td>
	    </tr>
	    </#list>
	  </tbody>
	</table>
	<#include "common/_paginate.ftl" />
	<@paginate currentPage=recordPage.pageNumber totalPage=recordPage.totalPage/>
</div>