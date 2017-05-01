var dabug = true;
function showLog(note,obj) {
	if(dabug){
		if(note!=null){
			console.log(note);
		}
		if(obj!=null){
			console.log(obj);
		}
	}
}
//提示
function showMsg(cont){
	layer.msg(cont);
}
//控件旁提示
function tipMsg(msg,pid){
	layer.tips(msg,pid, {
		tips: 2
		
	});
}
//加载提示
function loadingTip(){
	var loadIndex = layer.load(2, {shade: false}); //0代表加载的风格，支持0-2
	return loadIndex;
}
//询问框
function confirm(){
	layer.confirm('确定删除吗？', {
		title:"提示",
		btn: ['确定','取消'] //按钮
	}, function(){
		showMsg("确定");
	}, function(){
		showMsg("取消");
	});
}



function tipDialog(msg){
	//墨绿深蓝风
	layer.alert(msg, {
	  skin: 'layui-layer-molv' //样式类名
	  ,closeBtn: 0
	});
}

function tipDialog2(msg){
	//捕获页
	layer.open({
	  type: 1,
	  shade: false,
	  title: false, //不显示标题
	  content: msg //捕获的元素
	});
}

function showHtml(html){
	layer.open({
		  type: 1,
		  title: false,
		  area: ['620px','630px'],
		  closeBtn: 2,
		  shadeClose: true,
		  skin: 'html_css',
		  content: html
		});
}
function changeF(){
    document.getElementById('addr').value=
            document.getElementById('makeupCoSe').options[document.getElementById('makeupCoSe').selectedIndex].value;
}

function isEmpty(str){
	if (str==undefined||str==null||str.replace(/(^s*)|(s*$)/g, "").length ==0) {
		return true;
	}
	return false;
}
function isEmpty2(str){
	if (str==undefined||str==null||str=="") {
		return true;
	}
	return false;
}
//手机号格式
function checkMobile(str) {
	var re = /^1(3|4|5|7|8)\d{9}$/
	return re.test(str);
}
function getUnit(standard){
	if(isEmpty(standard)){
		return "";
	}
	else{
		return standard.substring(standard.length-1,standard.length);
	}
}

function createLayerEditor(layedit) {
	var index = layedit.build('LAY_demo1',{
		tool: [
			'strong' //加粗
			,'italic' //斜体
			,'underline' //下划线
			,'del' //删除线

			,'|' //分割线

			,'left' //左对齐
			,'center' //居中对齐
			,'right' //右对齐
			,'link' //超链接
			,'unlink' //清除链接
		]
	});
	return index;
}
/**
 * 重新渲染id为LAY_demo1的layedit
 */
function renderLayedit() {
	layui.use(['layedit'], function(){
		var layedit = layui.layedit
			,$ = layui.jquery;
		//构建一个默认的编辑器
		var index = layedit.build('LAY_demo1');
	});
}
/**
 * 重新渲染Layui的from，需要导入layui.js
 */
function renderLayuiForm() {
	layui.use('form', function(){
		var form = layui.form();
		form.render(); //更新全部
	});
}

/**
 * 重新渲染Layui的from，需要导入layui.js
 */
function renderLayuiFormSelect() {
	layui.use('form', function(){
		var form = layui.form();
		form.render('select');//更新select
	});
}

/**
 * 图片预览
 */
function imgChange() {
	var pic = document.getElementById("preview"),
		file = document.getElementById("file");

	var ext=file.value.substring(file.value.lastIndexOf(".")+1).toLowerCase();

	// gif在IE浏览器暂时无法显示
	if(ext!='png'&&ext!='jpg'&&ext!='jpeg'){
		alert("图片的格式必须为png或者jpg或者jpeg格式！");
		return;
	}
	var isIE = navigator.userAgent.match(/MSIE/)!= null,
		isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;

	if(isIE) {
		file.select();
		var reallocalpath = document.selection.createRange().text;

		// IE6浏览器设置img的src为本地路径可以直接显示图片
		if (isIE6) {
			pic.src = reallocalpath;
		}else {
			// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
			pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
			// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
			pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
		}
	}else {
		html5Reader(file);
	}
}

function html5Reader(file){
	var file = file.files[0];
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		var pic = document.getElementById("preview");
		pic.src=this.result;
	}
}
/*
function loginOut(msg){
	if(msg == "登录信息失效!"){
		console.log("清除登陆信息");
		ENV.AuthStatus = 0;
		ENV.isLogin = false;
		ENV.token = "";
		localStorage.clear();
	}

}*/

/**
 * 封装ajax请求
 * @param url
 * @param data
 * @param sucCallBack 请求成功回调
 */
function yHttp(url,data,sucCallBack) {
	$.ajax({
		type:"post",
		url:url,
		data:data,
		dataType:"json",
		success: sucCallBack,
		error: function (obj) {
			console.log(obj);
			showMsg("请求失败！")
		},
		beforeSend: function () {
			loadIndex = loadingTip();
		},
		complete: function () {
			layer.close(loadIndex);
		}
	});
}
/**
 * 母婴ajax封装
 * @param url
 * @param data
 * @param sucCallBack
 *
 使用demo
 myHttp(
 api_get_kd_config,
 data,
 function (obj) {
     $scope.$apply(function () {
          $scope.kdList = obj.data;
      });
      renderLayuiForm();
  }
 );
 */
function myHttp(url,data,sucCallBack) {
	$.ajax({
		type:"post",
		url:url,
		data:data,
		dataType:"json",
		success: function (obj) {
			console.log(obj);
			if(obj.code==0){
				sucCallBack(obj);
			}
			else{
				showMsg(obj.msg);
			}
		},
		error: function (obj) {
			console.log(obj);
			showMsg("请求失败！")
		},
		beforeSend: function () {
			loadIndex = loadingTip();
		},
		complete: function () {
			layer.close(loadIndex);
		}
	});
}

function createEditor(imgApi) {
	var editor = new wangEditor('wangEditor');
	// 普通的自定义菜单
	editor.config.menus = [
		'source',
		'|',
		'bold',
		'underline',
		'italic',
		'strikethrough',
		'eraser',
		'forecolor',
		'bgcolor',
		'|',
		'quote',
		'fontfamily',
		'fontsize',
		'head',
		'unorderlist',
		'orderlist',
		'alignleft',
		'aligncenter',
		'alignright',
		'|',
		'link',
		'unlink',
		'table',
		'|',
		'img',
		'|',
		'undo',
		'redo',
		'fullscreen'
	];
	// 上传图片
	editor.config.uploadImgUrl = imgApi;
	// 关闭菜单栏fixed
	editor.config.menuFixed = false;
	editor.create();
	return editor;
}