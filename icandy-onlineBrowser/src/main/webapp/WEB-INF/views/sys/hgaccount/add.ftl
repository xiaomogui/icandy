<form class="form required-validate" method="post" action="${ctx!}/hgaccount/addAction" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered">
	<tr>
		<th class="col-sm-3">皇冠账号</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="hgusername" type="text" required>
			<span class="input-group-addon mytips" title="皇冠体育官方账号">?</span>
		</div></td>
	</tr>
	<tr>
		<th class="col-sm-3">皇冠密码</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="hgpassword" type="password" required>
			<span class="input-group-addon mytips" title="皇冠体育官方账号对应的密码">?</span>
		</div></td>
	</tr>
	<tr>
		<th>状态</th>
		<td><select class="form-control" name="status">
	            <option value="1">启用</option>
	            <option value="2">失效</option>
	          </select></td>
	</tr>
</table>
  <div class="text-center">
    <input class="btn radius btn-warning" type="submit" value="确定">
    <button class="btn radius btn-warning dclose" type="button">取消</button>
  </div>
</form>
