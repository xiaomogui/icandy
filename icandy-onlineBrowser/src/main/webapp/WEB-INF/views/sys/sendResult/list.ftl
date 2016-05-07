<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">推送结果管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/sendResult" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <div class="form-group margin-top">
	        <label for="rn_type">状态：</label>
	        <select name="status" class="form-control">
	        	<option value="">请选择</option>
	        	<option value="1"<#if 1==status>selected</#if>>成功</option>
	        	<option value="2"<#if 2==status>selected</#if>>失败</option>
	        </select>
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">赛事ID：</label>
	        <input class="form-control" name="sportsEventsId" type="text" value="${sportsEventsId}" placeholder="期号">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">接收地址：</label>
	        <input class="form-control" name="host" type="text" value="${host}" placeholder="接收地址">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_fromTime">推送时间</label>
	        <div class='input-group date form_datetime' id="startTimeGroupId" data-minEle="#endTimeGroupId" data-maxDate="${endTime}">
                <input name="startTime" class="form-control" type="text" value="${startTime}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-time"></span>
                </span>
            </div>
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_toTime" class="sr-only">至</label>
	        <div class='input-group date form_datetime' id="endTimeGroupId" data-maxEle="#startTimeGroupId" data-minDate="${startTime}">
                <input name="endTime" class="form-control" type="text" value="${endTime}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-time"></span>
                </span>
            </div>
	      </div>
	      <button type="submit" class="btn btn-success margin-top"><i class="icon-search"></i>&nbsp;查询</button>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>接收地址</th>
	      <th>推送赛事ID</th>
	      <th>状态</th>
	      <th>推送时间</th>
	      <th>失败次数</th>
	      <th style="width:5em;"></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.host}</td>
	      <td>${x.sportsEventsId}</td>
	      <td><#if x.status==1>成功<#else><span style="color:red">失败</span></#if></td>
	      <td>${x.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      <td>${x.failedNum}</td>
	      <td>
			<a data-todoAjax="" href="${ctx!}/sendResult/delete/${x.id}" title="确定要删除该信息？"><i class="fa fa-trash-o"></i></a>
	      </td>
	    </tr>
	    </#list>
	  </tbody>
	</table>
	<#include "common/_paginate.ftl" />
	<@paginate currentPage=recordPage.pageNumber totalPage=recordPage.totalPage/>
</div>