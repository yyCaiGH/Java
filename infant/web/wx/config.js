var configMod=angular.module('config',[]);
var api = "http://localhost:8080/";
//var api = "http://cyy.tunnel.qydev.com/";
configMod.constant("ENV",{
    "debug":false,
    "pageSize" : 10,/**一次加载几条**/
    "api": api,//本地环境
    "imgUploadApi":api+"file/uploadTemp",//临时图片默认上传接口
    "wangEditorImgUploadApi":api+"file/editUploadTemp",//临时图片默认上传接口
    'delayTime':20000,
    'token':"",
    'isLogin':false,//是否登陆
    'userName':'',
    'tip':''
});