<form class="form required-validate" method="post" action="${ctx!}/user/addAction" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered">
	<tr>
		<th>用户名</th>
		<td><input class="form-control" name="username" type="text" required></td>
	</tr>
	<tr>
		<th>登录密码</th>
		<td><input class="form-control" name="password" type="password" required></td>
	</tr>
</table>
  <div class="text-center">
    <input class="btn radius btn-warning" type="submit" value="确定">
    <button class="btn radius btn-warning dclose" type="button">取消</button>
  </div>
</form>
