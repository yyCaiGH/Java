/* Controllers */
app.controller('HomeCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    var api_apply= $scope.host+"wx/apply";
    var api_apply_data= $scope.host+"wx/getApplyDataByPhone";
    var api_wx_share= $scope.host+"wxsdk/share";
    $scope.applyData = {};
    initData();
    function initData() {
        showLog("HomeCtrl init");
        getHomeData();
    }

    $scope.share = function () {
        wxShare();
    }
    /**
     * 微信分享
     */
    function wxShare() {
        alert(window.location.href);/***用于获得当前连接url用**/
        /***用户点击分享到微信圈后加载接口接口*******/
        var data={url:window.location.href};
        myHttp(
            api_wx_share,
            data,
            function (obj) {
                var data = obj.data;
                console.log(data.appid+" "+data.timestamp+" "+data.nonceStr+" "+data.signature);
                wx.config({
                    debug: true,
                    appId: data.appid,
                    timestamp:data.timestamp,
                    nonceStr:data.nonceStr,
                    signature:data.signature,
                    jsApiList: [
                        'checkJsApi',
                        'onMenuShareTimeline',
                        'hideOptionMenu',
                    ]
                });
                wx.error(function(res){
                    alert("error res="+res);
                });
                var shareTitle = "一起分享吧！";
                var shareImg = "http://imgsrc.baidu.com/baike/pic/item/509b9fcb7bf335ab52664fdb.jpg";
                wx.ready(function(){
                    alert("准备分享2222");
                    wx.onMenuShareTimeline({
                        title : shareTitle, // 分享标题
                        link : '', // 分享链接
                        imgUrl : shareImg, // 分享图标
                        success : function() {                      //没有进来。。。。。。。。。。。。。。。。。。。。。。。。。。。
                            // 用户确认分享后执行的回调函数
                            alert("分享成功");
                        },
                        fail:function(){
                            alert("分享失败");
                        },
                        complete:function () {
                            alert("分享完成");
                        },
                        cancel : function() {
                            // 用户取消分享后执行的回调函数
                            alert("分享取消");
                        }
                    });
                    //wx.hideOptionMenu();/***隐藏分享菜单****/
                });
            }
        );

        /*$.post(
            api_wx_share,
            {"url":window.location.href},
            function(data,status){
            data=eval("("+data+")");
            console.log(data.appid+" "+data.timestamp+" "+data.nonceStr+" "+data.signature);
            wx.config({
                debug: true,
                appId: data.appid,
                timestamp:data.timestamp,
                nonceStr:data.nonceStr,
                signature:data.signature,
                jsApiList: [
                    'checkJsApi',
                    'onMenuShareTimeline',
                    'hideOptionMenu',
                ]
            });
            var shareTitle = "一起分享吧！";
            var shareImg = "http://imgsrc.baidu.com/baike/pic/item/509b9fcb7bf335ab52664fdb.jpg";
            wx.ready(function(){
                alert("准备分享");
                wx.onMenuShareTimeline({
                    title : shareTitle, // 分享标题
                    link : '', // 分享链接
                    imgUrl : shareImg, // 分享图标
                    success : function() {
                        // 用户确认分享后执行的回调函数
                        alert("分享成功");
                    },
                    cancel : function() {
                        // 用户取消分享后执行的回调函数
                        alert("分享取消");
                    }
                });
                //wx.hideOptionMenu();/!***隐藏分享菜单****!/
            });
        });*/
    }

    function getHomeData() {
        var data={};
        myHttp(
            api_home_data,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                });
            }
        );
    }
    $scope.change = function(x){
        showLog("value="+x);
    }
    $scope.apply = function(){
        if(!checkMobile($scope.applyData.user_phone)){
            showMsg("请输入正确的手机号");
            return;
        }
        if (isEmpty2($scope.applyData.org_id)){
            showMsg("请选择培训机构");
            return;
        }
        var data=$scope.applyData;
        yHttp(
            api_apply,
            data,
            function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $.toast("报名成功");
                    $scope.applyData = obj.data;
                    $state.go("access.apply",{id:$scope.applyData.id});
                }
                else{//已经报名了
                    showMsg(obj.msg);
                }

            }
        );
    }
    $scope.look = function () {
        $.prompt("请输入您报名的电话号码，查看报名情况", function(text) {
            //点击确认后的回调函数
            //text 是用户输入的内容
            gotoLook(text);
        }, function() {
            //点击取消后的回调函数
        });


    }
    function gotoLook(text) {
        if(!checkMobile(text)){
            showMsg("请输入正确的手机号");
            return;
        }
        var data={phone:text};
        myHttp(
            api_apply_data,
            data,
            function (obj) {
                if(obj.data==null){
                    showMsg("您还未报名，请先报名");
                }
                else{
                    if(obj.data.length>1){
                        $state.go("access.applylist",{phone:text});
                    }
                    else{
                        $scope.applyData = obj.data[0];
                        showLog($scope.applyData);
                        $state.go("access.apply",{id:$scope.applyData.id});
                    }
                }
            }
        );
    }

  }])
;