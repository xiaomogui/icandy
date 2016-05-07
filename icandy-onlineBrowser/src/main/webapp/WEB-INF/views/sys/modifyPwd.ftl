<form class="form" id="resetPwdFormId">
  <div class="form-group">
    <label for="mp_password">原始密码：</label>
    <input class="form-control" id="mp_password" name="oldpassword" type="password">
  </div>
  <div class="form-group">
    <label for="mp_newpassword">新密码：</label>
    <input class="form-control" id="mp_newpassword" name="newpassword" type="password">
  </div>
  <div class="form-group">
    <label for="mp_confirmPassword">确认密码：</label>
    <input class="form-control" id="mp_confirmPassword" name="confirmPassword" type="password">
  </div>
  <div class="padding-top text-center">
    <input class="btn radius btn-warning" type="button" onclick="resetPwd(this)" value="修改密码">
    <button class="btn radius btn-warning dclose" type="button">关闭</button>
  </div>
</form>

<script type="text/javascript">
//修改密码方法
function resetPwd(obj){
  var form = $("#resetPwdFormId");
  var o = form.find("input[name='oldpassword']").val()
    , n = form.find("input[name='newpassword']").val()
    , cn = form.find("input[name='confirmPassword']").val();
  if(!o){Spider.errTips("请输入原密码");return false;}
  if(o.length<6){Spider.errTips("原密码至少6位");return false;}
  if(!n){Spider.errTips("请输入新密码");return false;}
  if(n.length<6){Spider.errTips("密码至少6位");return false;}
  if(cn!=n){Spider.errTips("两次输入密码不一样");return false;}
  var p = {oldpassword:o,newpassword:n}
  $.ajax({url:"${ctx!}/modifyPwdAction?"+new Date().getTime(),
    data:p,
    dataType:"json",
    method:"POST",
    error:function(xhr, textStatus, errThrow){
        Spider.errTips(errThrow||textStatus);
    },
    success:function(data, textStatus, xhr, headers){
	    if(data.statusCode=="200"){
		    var dialog = form.data("data-dialog");
		    if(dialog){
		      dialog.close();
		    }
	      	Spider.successTips("修改成功");
	    }else{
	      	Spider.errTips(data.message);
	    }
    }
  });
  return false;
}
</script>