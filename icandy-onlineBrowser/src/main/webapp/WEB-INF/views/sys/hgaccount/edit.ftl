<form class="form required-validate" method="post" action="${ctx!}/hgaccount/update" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered"><input type="hidden" name="id" value="${item.id}">
	<tr>
		<th class="col-sm-3">皇冠账号</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="hg_username" type="text" required value="${item.hgUsername}">
			<span class="input-group-addon mytips" title="皇冠体育官方账号">?</span>
		</div></td>
	</tr>
	<tr>
		<th class="col-sm-3">皇冠密码</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="hg_password" type="password" required value="${item.hgPassword}">
			<span class="input-group-addon mytips" title="皇冠体育官方账号对应的密码">?</span>
		</div></td>
	</tr>
	<tr>
		<th>状态</th>
		<td><select class="form-control" name="status">
	            <option value="1" <#if item?has_content && item.status=="1">selected</#if>>启用</option>
	            <option value="2" <#if item?has_content && item.status=="2">selected</#if>>失效</option>
	          </select></td>
	</tr>
</table>
  <div class="text-center">
    <input class="btn radius btn-warning" type="submit" value="确定">
    <button class="btn radius btn-warning dclose" type="button">取消</button>
  </div>
</form>
