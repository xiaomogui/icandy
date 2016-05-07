<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">皇冠账号管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/hgaccount" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <div class="form-group margin-top">
	        <label for="rn_type">皇冠账号：</label>
	        <input class="form-control" name="hgusername" type="text" value="${hgusername}" placeholder="皇冠账号">
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
	      <a class="btn btn-primary margin-top" href="${ctx!}/hgaccount/add" data-dialog="addHgaccount"><i class="fa fa-plus"></i> 添加</a>
	    	&nbsp; &nbsp;
	      <a class="btn btn-primary margin-top" id="refreshHgaccountBtn"><i class="fa fa-refresh"></i> 刷新体育数据中心账号</a>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>皇冠账号</th>
	      <th style="width: 12em;">状态</th>
	      <th style="width: 52em;">账号检测状态</th>
	      <th style="width: 4.5em;"></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.hgUsername}</td>
	      <td><#if x.status==1>启用<a data-todoAjax="" href="${ctx!}/hgaccount/resetStatus/${x.id}-2" title="确定要关闭【${x.hgUsername}】？">(关闭)</a>
				<#else>失效<a data-todoAjax="" href="${ctx!}/hgaccount/resetStatus/${x.id}-1" title="确定要启用【${x.hgUsername}】？">(启用)</a></#if></td>
          <td>
              <span id="showCheckResult_${x.id}">未检测</span>
          </td>
	      <td>
	          <a data-dialog="editHgaccount" href="${ctx!}/hgaccount/edit/${x.id}"><i class="fa fa-pencil"></i></a>
	          <a data-todoAjax="" href="${ctx!}/hgaccount/delete/${x.id}" title="确定要删除IP【${x.hgUsername}】？"><i class="fa fa-trash-o"></i></a>
	          <a href="javascript: void(0);" title="检测皇冠账号" name="checkHgAccountBtn" data-hgaccountid="${x.id}"><i class="fa fa-user"></i></i></a>
	      </td>
	    </tr>
	    </#list>
	  </tbody>
	</table>
	<#include "common/_paginate.ftl" />
	<@paginate currentPage=recordPage.pageNumber totalPage=recordPage.totalPage/>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h5 class="modal-title" id="myModalLabel">
               提示
            </h5>
         </div>
         <div class="modal-body" id="shslfjsldjsjdl">
            <!-- 在这里添加一些文本 -->
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭
            </button>
         </div>
      </div>
</div>

<script>
var vrctx="${ctx!}";
$(function (){
	$("table.table a[name=checkHgAccountBtn]").on("click", function(){
		var $this = $(this);
		var hgaccountid = $this.data("hgaccountid");
		var $showCheckResult_ = $("#showCheckResult_" + hgaccountid);
		$showCheckResult_.html('<i class="fa fa-spinner fa-spin"></i>');
		$.ajax({
			type:"post",
			url:"${ctx!}/hgaccount/checkHgLogin",
			// contentType:"application/json",
			dataType:"json",
			data:{'id': hgaccountid},
			error: function (request, message, ex) {
				$showCheckResult_.html('异常');
			},
			success: function (result) {
				/*
				if(result && result.statusCode == "300"){
					$showCheckResult_.html('异常');
				}
				if(result && result.statusCode == "0"){
					$showCheckResult_.html('<font color="#00ff00">可登陆</font>');
				} else {
					$showCheckResult_.html('<font color="#ff0000">不可登陆</font>');
				}
				*/
				$showCheckResult_.html(result ? result.testLoginStatus : "异常");
			}
		});
	});

	$("#refreshHgaccountBtn").on("click", function(){
		var $i = $(this).find("i");
		$i.addClass("fa-spin");
		$.ajax({
			type:"post",
			url:"${ctx!}/hgaccount/refreshHgaccount",
			dataType:"json",
			error: function (request, message, ex) {
				$("#shslfjsldjsjdl").html("操作失败！");
				$("#myModal").modal();
				$i.removeClass("fa-spin");
			},
			success: function (result) {
				$("#shslfjsldjsjdl").html("操作成功！");
				$("#myModal").modal();
				$i.removeClass("fa-spin");
			}
		});
	});
});
</script>