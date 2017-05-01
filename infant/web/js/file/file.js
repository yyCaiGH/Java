var BASEPATH="http://localhost:8082/";
/**
 * 绑定图片上传时间
 */
function bindPicUpload(select) {

	//obj.each(function() {
		jQuery('.com_input').bind('change', function(e) {
			var path = jQuery(this).val();
			var actionUrl = BASEPATH + 'admin/imgTest';
			console.log("cyy--1---path="+path+",actionUrl="+actionUrl);
			try {
				uploadImage(this, path,select,actionUrl);
			} catch (error) {
				console.log("cyy-----2");
				alert(error.message);
			}
			e.preventDefault();
			e.stopPropagation();
		});

	//});
	
}

/**
 * 绑定附件上传时间
 */
function bindFileUpload(select) {
	obj.each(function() {
		jQuery(this).on('change', function(e) {
			var fileSize = this.files[0].size;
			if (fileSize / 1048576 > 10) {
				layer.alert("文件最大不能超过10M");
				return;
			}
			var path = jQuery(this).val();
			uploadFile(this, path,select);
			e.preventDefault();
		});
	});
}

function uploadImage(object, filePath,select,actionUrl) {
	console.log("cyy-----3");
	var filepath = filePath;
	filepath = (filepath + '').toLowerCase();
	var regex = new RegExp(
			'\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$', 'gi');
	/** 上传图片文件格式验证 */
	if (!filepath || !filepath.match(regex)) {
		layer.alert('请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp).');
		jQuery(object).value = "";
		return;
	}
	var callback = function(data, status) {
		console.log("cyy-----4");
		console.log(data);
		/** 上传成功，隐藏上传组件，并显示该图片 */
		if (data.success) {
			var fileName=data.obj.fileName;
			var index = $(object).attr('index');
			jQuery("#imgUrl"+index).val(data.obj.url);
			$("#image" + index).attr("src", BASEPATH+data.obj.url);
			//layer.alert(data.msg);
//			$("#picVfsId" + index).val(data.map.vfsId);
//			$("#picVfsId" + index).attr('picName', data.map.imageName);
//			$("#picVfsId" + index).attr('mediaRtype', '2');
//			$('#picVfsId' + index).attr('mediaType', '1');
		} else {
			console.log("cyy-----7");
			alert(data.msg);
		}
		bindPicUpload(select);
	};

	ajaxFileUpload(actionUrl, false, jQuery(object).attr('id'), "POST","json", callback);
}
function uploadFile(object1, path,select) {
	var filepath = path;
	filepath = (filepath + '').toLowerCase();
	// var regex = new RegExp(
	// '\\.(pdf)$|\\.(doc)$|\\.(txt)$', 'gi');
	// /** 上传文件格式验证 */
	// if (!filepath || !filepath.match(regex)) {
	// eDialog.alert('请选择文件(.pdf,).');
	// $(object).value = "";
	// return;
	// }
	var url = BASEPATH + '/goods/uploadfile';
	var propId = jQuery(object1).attr('propId');
	var callback = function(data, status) {
		/** 上传成功，隐藏上传组件，并显示该图片 */
		if (data.success == "ok") {
//			var _this = $("#file" + propId);
//			_this.val(data.resultMap.vfsId);
//			_this.attr('disabled', true);
//			_this.parent().find('.file-wrap').hide();
//			_this.parent().find('button').show();
		} else {
			layer.error(data.message);
		}
		bindFileUpload(select);
	};
	ajaxFileUpload(url, false, jQuery(object1).attr('id'), "POST","json", callback);
}
function ajaxFileUpload(url, secureuri, fileElementId, type, dataType, callback) {
	console.log("cyy-----5---url="+url+",fileElementId="+fileElementId);
	jQuery.ajaxFileUpload({
		url : url, // 用于文件上传的服务器端请求地址
		secureuri : false, // 一般设置为false
		fileElementId : fileElementId, // 文件上传空间的id属性 <input
		// type="file" id="imageFile"
		// name="imageFile" />
		type : "post", // get 或 post
		dataType : "json", // 返回值类型
		success : callback, // 服务器成功响应处理函数
		error : function(data, status, e) // 服务器响应失败处理函数
		{
			console.log("cyy-----6--"+e);
			alert(e);
		}
	});
}
