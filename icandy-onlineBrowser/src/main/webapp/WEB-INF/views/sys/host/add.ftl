<form class="form required-validate" method="post" action="${ctx!}/host/addAction" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered">
	<tr>
		<th class="col-sm-3">接收地址</th>
		<td class="col-sm-9"><div class="input-group">
			<input class="form-control" name="host" type="text" required>
			<span class="input-group-addon mytips" title="支持以下格式：192.15.12.1 、 192.15.12.1:8080 、 http://192.15.12.1:8080 、 http://192.15.12.1:8080/ 、 ht.aaa.com 、 http://ht.aaa.com/">?</span>
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
