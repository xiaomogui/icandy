<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">体育赛事管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/sportsevents" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <!-- 
	      <div class="form-group margin-top">
	        <label for="rn_type">状态：</label>
	        <select name="status" class="form-control">
	        	<option value="">请选择</option>
	        	<option value="1"<#if 1==status>selected</#if>>成功</option>
	        	<option value="2"<#if 2==status>selected</#if>>失败</option>
	        </select>
	      </div>
	       -->
	      <div class="form-group margin-top">
	        <label for="rn_type">赛事ID：</label>
	        <input class="form-control" name="sportsEventsId" type="text" value="${sportsEventsId}" placeholder="赛事ID">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">gid：</label>
	        <input class="form-control" name="gid" type="text" value="${gid}" placeholder="gid">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">联赛：</label>
	        <input class="form-control" name="league" type="text" value="${league}" placeholder="联赛">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">主场球队：</label>
	        <input class="form-control" name="teamH" type="text" value="${teamH}" placeholder="主场球队">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">客场球队：</label>
	        <input class="form-control" name="teamC" type="text" value="${teamC}" placeholder="客场球队">
	      </div>
	      <button type="submit" class="btn btn-success margin-top"><i class="icon-search"></i>&nbsp;查询</button>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>联赛id</th>
	      <th>gid</th>
	      <th>联赛</th>
	      <th>主场球队</th>
	      <th>客场球队</th>
	      <th>开赛时间</th>
	      <th>球赛类型</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.id}</td>
	      <td>${x.gid}</td>
	      <td>${x.league}</td>
	      <td>${x.teamH}</td>
	      <td>${x.teamC}</td>
	      <td>${x.runTime}</td>
	      <td><#if x.ballcode=="FT">足球<#elseif x.ballcode=="BK">篮球</#if></td>
	      <!-- <td>${x.beginTime?string("yyyy-MM-dd HH:mm:ss")}</td> -->
	    </tr>
	    </#list>
	  </tbody>
	</table>
	<#include "common/_paginate.ftl" />
	<@paginate currentPage=recordPage.pageNumber totalPage=recordPage.totalPage/>
</div>