<form class="form required-validate" method="post" action="${ctx!}/loginAction?from=dialog" onsubmit="return validateCallback(this, dialogAjaxDone)">
<table class="table table-bordered">
	<tr>
		<th class="text-center">用户名</th>
		<td><input class="form-control" name="username" type="text" required></td>
	</tr>
	<tr>
		<th class="text-center">登录密码</th>
		<td><input class="form-control" name="password" type="password" required></td>
	</tr>
</table>
  <div class="text-center">
    <input class="btn radius btn-warning" type="submit" value="登录">
  </div>
</form>