<form class="form required-validate" method="post" action="${ctx!}/host/update" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered"><input type="hidden" name="id" value="${item.id}">
	<tr>
		<th>接收地址</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="host" type="text" required value="${item.host}">
			<span class="input-group-addon mytips" title="支持以下格式：192.15.12.1 、 192.15.12.1:8080 、 http://192.15.12.1:8080 、 http://192.15.12.1:8080/ 、 ht.aaa.com 、 http://ht.aaa.com/">?</span>
		</div></td>
	</tr>
	<tr>
		<th>状态</th>
		<td><select class="form-control" name="status">
	            <option value="1"<#if item?has_content && item.status==1>selected</#if>>启用</option>
	            <option value="2"<#if item?has_content && item.status==2>selected</#if>>失效</option>
	          </select></td>
	</tr>
</table>
  <div class="text-center">
    <input class="btn radius btn-warning" type="submit" value="确定">
    <button class="btn radius btn-warning dclose" type="button">取消</button>
  </div>
</form>
