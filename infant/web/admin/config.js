/*var configMod=angular.module('config',[]);
configMod.constant("ENV",{
    "debug":false,
    "pageSize" : 5,/!**一次加载几条**!/
    // "api": "http://localhost:8080/",
    "api": "http://localhost:8080/",
    // "api": "http://cyy.tunnel.qydev.com/",
    // "api": "http://192.168.113.2:40001/",
    "imgUploadApi":"http://localhost:8080/file/uploadTemp",//临时图片默认上传接口
    "layeditImgUploadApi":"http://localhost:8082/file/editUploadTemp",//临时图片默认上传接口
    // "imgUploadApi":"http://cyy.tunnel.qydev.com/file/uploadTemp",//临时图片默认上传接口
    'delayTime':20000,
    'token':'',
    'isLogin':false,//是否登陆
    'userName':'',
    'tip':''
});*/

var configMod=angular.module('config',[]);
//var api = "http://localhost:8080/";
var api = "http://139.224.192.136:8080/";
configMod.constant("ENV",{
    "debug":false,
    "pageSize" : 10,/**一次加载几条**/
    // "api": "http://localhost:8080/",
    "api": api,//本地环境
    // "api": "http://cyy.tunnel.qydev.com/",//本地映射
    //"api": "http://139.224.192.136:8080/",//正式环境
    "imgUploadApi":api+"file/uploadTemp",//临时图片默认上传接口
    "wangEditorImgUploadApi":api+"file/editUploadTemp",//临时图片默认上传接口
    'delayTime':20000,
    'token':"",
    'isLogin':false,//是否登陆
    'userName':'',
    'tip':''
});