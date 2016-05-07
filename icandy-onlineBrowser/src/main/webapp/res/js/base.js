var Spider={
	statusCode: {ok:200, error:300, timeout:301},
	pageInfo: {pageNum:"page", pageSize:"pageSize"},
	_set:{
		loginUrl:vrctx+"/loginDialog", //session timeout
		loginTitle:"超时重新登录" //if loginTitle open a login dialog
	},
	jsonEval:function(data) {
		try{
			if ($.type(data) == 'string'){
				data = eval('(' + data + ')');
			}
			if(data.errCode){
				data.statusCode = data.statusCode || Spider.statusCode.error;
				data.message= data.errMsg;
			}else{
				data.statusCode = data.statusCode ||Spider.statusCode.ok;
			}
			return data;
		} catch (e){
			return {statusCode:Spider.statusCode.ok,message:data};
		}
	},
	loadLogin:function(){
		Spider.ajaxDialog(Spider._set.loginUrl,Spider._set.loginTitle,{id: "loginDialog"});
	},
	ajaxError:function(xhr, ajaxOptions, thrownError){
		Spider.errTips("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" 
			+ "<div>ajaxOptions: "+ajaxOptions + "</div>"
			+ "<div>thrownError: "+thrownError + "</div>"
			+ "<div>"+xhr.responseText+"</div>");
	},
	ajaxDone:function(json){
		if (json.statusCode==Spider.statusCode.error){
			Spider.errTips(json.message || "系统发生错误");
		}else if (json.statusCode==Spider.statusCode.timeout){
			Spider.tips(json.message || "登录超时","error", function(){
				Spider.loadLogin();
			});
		}
	},
	msg:function(key, args){
		var _format = function(str,args) {
			args = args || [];
			var result = str || "";
			for (var i = 0; i < args.length; i++){
				result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
			}
			return result;
		}
		return _format(this._msg[key], args);
	},
	alert:function(msg,callback){
		BootstrapDialog.alert(msg, callback);
	},
	confirm:function(msg,successFn,cancelFn){
		BootstrapDialog.confirm(msg, function(result){
            if(result) {
            	if($.isFunction(successFn))return successFn();
            }else {
            	if($.isFunction(cancelFn))return cancelFn();
            }
        });
	},
	successTips:function(msg){
		Spider.tips(msg,"success");
	},
	errTips:function(msg){
		Spider.tips(msg,"error");
	},
	tips:function(msg,type,hiddenFn){
		switch(type){
			case "success":
				type=BootstrapDialog.TYPE_SUCCESS;
			break;
			case "warning":
				type=BootstrapDialog.TYPE_WARNING;
			break;
			case "error":
				type=BootstrapDialog.TYPE_DANGER;
			break;
			default: type=BootstrapDialog.TYPE_INFO;
		}
		var dialog = new BootstrapDialog({
            message:msg,
            type:type,
            onshown: function(dialogRef){
                setTimeout(function(){dialogRef.close()},2000);
            },
            onhidden:function(dialogRef){
            	if($.isFunction(hiddenFn))hiddenFn();
            }
        });
        dialog.realize();
        dialog.getModalHeader().hide();
        dialog.getModalFooter().hide();
        dialog.open();
	},
	ajaxDialog:function(url,title,options){
		var op = $.extend({}, options);
		op.id = op.id||"dl_"+new Date().getTime();
		op.size=op.size||BootstrapDialog.SIZE_NORMAL;
		op.zIndex=op.zIndex || 20000;
		op.type=op.type||BootstrapDialog.TYPE_PRIMARY;
		$.get(url,function(html){
        	var json = Spider.jsonEval(html);
			Spider.ajaxDone(json);
			if (json.statusCode==Spider.statusCode.ok){
					BootstrapDialog.show({
						id: op.id,
			            title: title,
			            message: html,
			            zIndex:op.zIndex,
			            size:op.size,
			            type:op.type,
			            closable: false,
			            closeByBackdrop: false,
			            closeByKeyboard: false,
			            nl2br:false,
			            onshow:function(dialog){
			              if(op.dclass){
			                dialog.getModal().addClass(op.dclass);
			              }
			            },
			            onshown: function(dialog) {
			              var obj = dialog.getModalBody();
			              obj.find(".dclose").click(function(){
			                dialog.close();
			              });
			              obj.find(".btn,form").data("data-dialog",dialog);
			              initUI(obj);
			            }
			          });
			}
        },"html");
	}
};
/**
 * 普通ajax表单提交
 * @param {Object} form
 * @param {Object} callback
 * @param {String} confirmMsg 提示确认信息
 */
function validateCallback(form, callback, confirmMsg) {
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
	
	var _submitFn = function(){
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"text",
			cache: false,
			success: function(json){
				json =Spider.jsonEval(json);
				Spider.ajaxDone(json);
				if(json.statusCode==Spider.statusCode.ok){
					if($.isFunction(callback))callback(json,$form);
				}
			},
			error: Spider.ajaxError
		});
	}
	
	if (confirmMsg) {
		Spider.confirm(confirmMsg, {okCall: _submitFn});
	} else {
		_submitFn();
	}
	return false;
}
function dialogAjaxDone(json,dialogForm){
	Spider.ajaxDone(json);
	if (json.statusCode == Spider.statusCode.ok){
		var dialog = dialogForm.data("data-dialog");
	    if(dialog){
	      dialog.close();
	    }
		var $pagerForm = $("#mainContent").find("form.pagerForm");
		if($pagerForm.size()){
			$pagerForm.find("input[name='"+Spider.pageInfo.pageNum+"']").val(1);
			$pagerForm.submit();
		}
	}
}
function ajaxTodo(url, callback,type){
	var $callback = callback || function(json){
		var $pagerForm = $("#mainContent").find("form.pagerForm");
		if($pagerForm.size()){
			$pagerForm.submit();
		}
	};
	if (! $.isFunction($callback)) $callback = eval('(' + callback + ')');
	$.ajax({
		type:type||'POST',
		url:url,
		dataType:"text",
		cache: false,
		success: function(html){
			var json = Spider.jsonEval(html);
			Spider.ajaxDone(json);
			if (json.statusCode == Spider.statusCode.ok){
				$callback(json);
			}
		},
		error: Spider.ajaxError
	});
}
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = '提示信息';
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = '提示信息';
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = '提示信息';
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = '成功';
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = '警告';
BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = '危险';
BootstrapDialog.DEFAULT_TEXTS['OK'] = '提交';
BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = '取消';
BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = '确定';
function initUI(_box){
  var $p = $(_box || document);
  //a标签中有data-aim这个属性（应该是某个容器的id）的，点击后在这个属性对应的容器中显示连接结果
  $("a[data-aim]", $p).each(function(){
	$(this).click(function(event){
		var $this = $(this), url = $this.attr("href");
		if(url && url!="#" && $this.attr("data-aim")){
			$("#"+$this.attr("data-aim")).loadUrl(url);
		}
		event.preventDefault();
		return false;
	});
  });
  $("form.required-validate", $p).each(function(){
	var $form =$(this);
	$form.validate({
		onsubmit: false,
		focusInvalid: false,
		focusCleanup: true,
		errorElement: "span",
		ignore:".ignore",
		showErrors: function(errorMap, errorList) {
			var i, error;
			for ( i = 0; errorList[ i ]; i++ ) {
				error = errorList[ i ];
				layer.tips(error.message, error.element, {
				    tipsMore: true
				});
			}
	    }
	});
  });
  $("form[data-aim]", $p).each(function(){
    $(this).submit(function(event){
      var $this = $(this), url = $this.attr("action");
      if(url){
    	  $("#"+$this.attr("data-aim")).ajaxUrl({url:url,type:$this.attr("method")|| "POST",data:$this.serialize()});
      }
      event.preventDefault();
      return false;
    });
  });
  $(".pagination a",$p).each(function(){
    $(this).click(function(event){
      var $this = $(this),baseUnit = $this.parents(".baseUnit");
      if(!baseUnit.length){
    	  Spider.errTips("分页与查询表单必须同个.baseUnit父级下");return;
      }
      var form = $this.parents(".baseUnit").find("form");
      if(form.length){
    	form.find("input[name='page']").val($this.attr("page"));
        form.submit();
      }else{
    	  Spider.errTips("分页与查询表单必须在同个.baseUnit父级下");
      }
      event.preventDefault();
      return false;
    });
  });
  
  $(".form_datetime",$p).each(function(){
    var $this = $(this), format = $this.attr("data-format")|| "YYYY-MM-DD",sideBySide=false,disabledHours=false;//YYYY-MM-DD HH:mm:ss
    if(format.length > 10){
    	sideBySide = true;
    	disabledHours=true;
    }
    $this.datetimepicker({
    	useCurrent:false,
    	locale:"zh-cn",
    	sideBySide:sideBySide,
    	showTodayButton: true,
        format: format,
        minDate:$this.attr("data-minDate")||false,
        maxDate:$this.attr("data-maxDate")||false
      });
    var minEle = $this.attr("data-minEle"),maxEle = $this.attr("data-maxEle");
    if(minEle){
    	$this.on("dp.change", function (e) {
    		$(minEle).data("DateTimePicker").minDate(e.date);
        });
    }
    if(maxEle){
    	$this.on("dp.change", function (e) {
    		$(maxEle).data("DateTimePicker").maxDate(e.date);
        });
    }
    
  });

  $("a[data-dialog]", $p).each(function(){
    $(this).click(function(event){
    	$(this).openByDialog();
    	event.preventDefault();
     // return false;
    });
  });
  $("a[data-todoAjax]", $p).each(function(){
    $(this).click(function(event){
      var $this = $(this), url = $this.attr("href");
      if(url && url!="#"){
        var title = $this.attr("title"),$callback=$this.attr("data-todoAjax");
        if($callback && ! $.isFunction($callback)){
            $callback = eval('(' + $callback + ')');
        }
        if(title){
        	Spider.confirm(title,function(){
        		ajaxTodo(url,$callback, "get");
        	});
        }else{
        	ajaxTodo(url,$callback, "get");
        }
      }
      event.preventDefault();
      return false;
    });
  });
  
  $(".mytips").each(function(){
	 var $it = $(this);
	 if($it.attr("title")){
		 $it.click(function(){
			 layer.tips($it.attr("title"), $it, {tips: 3});
		 });
	 }
  });
}
$(function(){
	$.fn.extend({
		/**
		 * @param {Object} op: {type:GET/POST, url:ajax请求地址, data:ajax请求参数列表, callback:回调函数 ,openDialog:是否使用dialog打开(true,false)}
		 */
		ajaxUrl: function(op){
			var $this = $(this);
			$.ajax({
				type: op.type || 'GET',
				url: op.url,
				data: op.data,
				cache: false,
				dataType:"html",
				success: function(response){
					var json = Spider.jsonEval(response);
					Spider.ajaxDone(json);
					if (json.statusCode==Spider.statusCode.ok){
						$this.html(response);
						initUI($this);
						if ($.isFunction(op.callback)) op.callback(response,$this);
					}
				},
				error: Spider.ajaxError,
				statusCode: {
					503: function(xhr, ajaxOptions, thrownError) {
						alert(thrownError||"系统发生错误");
					}
				}
			});
		},
		loadUrl: function(url,data,callback,openDialog){
			$(this).ajaxUrl({url:url, data:data, callback:callback,openDialog:openDialog});
		},
		openByDialog:function(){
			var $this = $(this), url = $this.attr("data-url") || $this.attr("href");
			if(url && url!="#"){
				Spider.ajaxDialog(url,($this.attr("title")|| $this.html()),{id:$this.attr("data-dialog"),size:$this.attr("data-dsize"),dclass:$this.attr("data-dclass")});
			}
		}
	});
	$.extend($.validator.messages, {
		required: "必填字段",
		remote: "请修正该字段",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("长度最多是 {0} 的字符串"),
		minlength: $.validator.format("长度最少是 {0} 的字符串"),
		rangelength: $.validator.format("长度介于 {0} 和 {1} 之间的字符串"),
		range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: $.validator.format("请输入一个最大为 {0} 的值"),
		min: $.validator.format("请输入一个最小为 {0} 的值"),
		
		alphanumeric: "字母、数字、下划线",
		lettersonly: "必须是字母",
		phone: "数字、空格、括号"
	});
  initUI();
  var $win = $(window);
  $("#mainContent").height($win.height()-65);
  $win.resize(function(){
	  $("#mainContent").height($win.height()-65);
  });
});