<div class="header">
  <ul class="breadcrumb">
    <li><a href="${ctx!}/">首页</a> </li>
    <li class="active">皇冠地址管理</li>
  </ul>
</div>
<div class="main-content baseUnit" data-aim="mainContent">
	<div class="well well-sm padding-top0">
	    <form action="${ctx!}/hgurl" data-aim="mainContent" method="Post" class="form form-inline pagerForm">
	      <input name="page" value="${recordPage.pageNumber}" type="hidden">
	      <div class="form-group margin-top">
	        <label for="rn_type">皇冠地址：</label>
	        <input class="form-control" name="hgurl" type="text" value="${hgurl}" placeholder="皇冠地址">
	      </div>
	      <div class="form-group margin-top">
	        <label for="rn_type">状态：</label>
	        <select class="form-control" name="status">
	            <option value="">请选择</option>
	            <option value="1"<#if status?has_content && status=="1">selected</#if>>启用</option>
	            <option value="2"<#if status?has_content && status=="2">selected</#if>>失效</option>
	          </select>
	      </div>
	      <button type="submit" class="btn btn-success margin-top"><i class="icon-search"></i>&nbsp;查询</button>
	    	&nbsp; &nbsp;
	      <a class="btn btn-primary margin-top" href="${ctx!}/hgurl/add" data-dialog="addHgurl"><i class="fa fa-plus"></i> 添加</a>
	    	&nbsp; &nbsp;
	      <a class="btn btn-primary margin-top" id="refreshHgurlBtn"><i class="fa fa-refresh"></i> 刷新皇冠网址</a>
	    </form>
	</div>
	<table class="table">
	  <thead>
	    <tr>
	      <th>皇冠地址</th>
	      <th style="width: 12em;">状态</th>
	      <th style="width: 52em;">线路测速</th>
	      <th style="width: 4.5em;"></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list recordPage.list as x>
	    <tr>
	      <td>${x.hgUrl}</td>
	      <td><#if x.status==1>启用<a data-todoAjax="" href="${ctx!}/hgurl/resetStatus/${x.id}-2" title="确定要关闭【${x.hgUrl}】？">(关闭)</a>
				<#else>失效<a data-todoAjax="" href="${ctx!}/hgurl/resetStatus/${x.id}-1" title="确定要启用【${x.hgUrl}】？">(启用)</a></#if></td>
          <td>
              <span id="showPingResult_${x.id}">－</span> <!-- 毫秒 -->
          </td>
	      <td>
	          <a data-dialog="editHgurl" href="${ctx!}/hgurl/edit/${x.id}"><i class="fa fa-pencil"></i></a>
	          <a data-todoAjax="" href="${ctx!}/hgurl/delete/${x.id}" title="确定要删除IP【${x.hgUrl}】？"><i class="fa fa-trash-o"></i></a>
	          <a href="javascript: void(0);" title="测试网速" name="pingHgUrlBtn" data-hgurlid="${x.id}"><i class="fa fa-signal"></i></i></a>
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
	/* 
	$("table.table a[name=pingHgUrlBtn]").on("click", function(){
		var $this = $(this);
		var hgurlid = $this.data("hgurlid");
		var $showPingResult_ = $("#showPingResult_" + hgurlid);
		$showPingResult_.html('<i class="fa fa-spinner fa-spin"></i>');
		$.ajax({
			type:"post",
			url:"${ctx!}/hgurl/ping",
			// contentType:"application/json",
			dataType:"json",
			data:{'id': hgurlid},
			error: function (request, message, ex) {
				$showPingResult_.html('－');
			},
			success: function (result) {
				// if(result && result.statusCode == "300"){
					// $showPingResult_.html('－');
				// }
				if(result && result.statusCode == "0"){
					$showPingResult_.html(result.delay);
				} else {
					$showPingResult_.html('－');
				}
			}
		});
	});
	*/

	$("table.table a[name=pingHgUrlBtn]").on("click", function(){
		var $this = $(this);
		var hgurlid = $this.data("hgurlid");
		var $showPingResult_ = $("#showPingResult_" + hgurlid);
		$showPingResult_.html('<i class="fa fa-spinner fa-spin"></i>');
		$.ajax({
			type:"post",
			url:"${ctx!}/hgurl/testHgurl",
			// contentType:"application/json",
			dataType:"json",
			data:{'id': hgurlid},
			error: function (request, message, ex) {
				$showPingResult_.html('－');
			},
			success: function (result) {
				/*
				if(result && result.statusCode == "300"){
					$showPingResult_.html('－');
				}
				if(result && result.statusCode == "0"){
					$showPingResult_.html(result.delay);
				} else {
					$showPingResult_.html('－');
				}
				*/
				$showPingResult_.html(result.testHgurlStatus);
			}
		});
	});

	$("#refreshHgurlBtn").on("click", function(){
		var $i = $(this).find("i");
		$i.addClass("fa-spin");
		$.ajax({
			type:"post",
			url:"${ctx!}/hgurl/refreshHgurl",
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