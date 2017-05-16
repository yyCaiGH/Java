/* Controllers */
app.controller('HomeCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    var api_apply= $scope.host+"wx/apply";
    var api_apply_data= $scope.host+"wx/getApplyDataByPhone";
    var api_wx_share= $scope.host+"wxsdk/share";
    var api_get_wx_user_info= $scope.host+"wxsdk/getWxUserInfo";
    var wx_share_img = $scope.host+"img/baomingla.png";
    $scope.applyData = {};
    initData();
    function initData() {
        showLog("HomeCtrl init11");
        var url = window.location.href;
        var isCodeExist = url.toString().indexOf('type=2');//用type=2来作为需要弹出授权框的标志,比较俗气
        if(isCodeExist!=-1){
            wxLogin();
        }
        else{
            wxJmLogin();
        }
    }
    /**
     * 微信登陆(静默授权)
     */
    function wxJmLogin() {
        var url = window.location;
        console.log("yy-url="+url);
        var isCodeExist = url.toString().indexOf('code=');
        console.log("yy-isCodeExist="+isCodeExist);
        if(isCodeExist!=-1){
            console.log("yy-授权获取code值1");
            var code=url.toString().substr(url.toString().indexOf('code=')+5,32);
            getWxUserInfo(code,1);
            getHomeData();//授权后再获取数据
        }
        else{
            console.log("yy-微信静默授权开始");
            var redirect = encodeURI("http://cyy.tunnel.qydev.com/index-wx.html");
            var url1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc93df0671af5918e&redirect_uri=" + redirect + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
            location.href = url1;
            return;//结束

        }
    }
    /**
     * 微信登陆(弹出授权界面)
     */
    function wxLogin() {
        var url = window.location;
        console.log("yy-url="+url);
        var isCodeExist = url.toString().indexOf('code=');
        console.log("yy-isCodeExist="+isCodeExist);
        if(isCodeExist!=-1){
            console.log("yy-授权获取code值2");
            var code=url.toString().substr(url.toString().indexOf('code=')+5,32);
            getWxUserInfo(code,2);
            getHomeData();//授权后再获取数据
        }
        else{
            console.log("yy-微信授权开始");
            var redirect = encodeURI("http://cyy.tunnel.qydev.com/index-wx.html");
            var url1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc93df0671af5918e&redirect_uri=" + redirect + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            location.href = url1;
            return;//结束

        }
    }

    /**
     *
     * @param code
     * @param type 授权方式：1：静默授权，2：动态授权
     */
    function getWxUserInfo(code,type) {
        var data={code:code,type:type};
        yHttp(
            api_get_wx_user_info,
            data,
            function (obj) {
                showLog("获取openId",obj.data);
                if(obj.code==-2){//首次登陆
                    //弹出授权框
                    var redirect = encodeURI("http://cyy.tunnel.qydev.com/index-wx.html?type=2");
                    var url1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc93df0671af5918e&redirect_uri=" + redirect + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
                    location.href = url1;
                }
            }
        );
    }

    $scope.share = function () {
        wxShare(false);
    }
    /**
     * 微信分享
     * signatureAgain 是否重新签名
     */
    function wxShare(signatureAgain) {
        alert(window.location.href);/***用于获得当前连接url用**/
        /***用户点击分享到微信圈后加载接口接口*******/
        var data={url:window.location.href,signatureAgain:signatureAgain};
        myHttp(
            api_wx_share,
            data,
            function (obj) {
                var data = obj.data;
                //console.log(data.appid+" "+data.timestamp+" "+data.nonceStr+" "+data.signature);
                wx.config({
                    debug: true,
                    appId: data.appid,
                    timestamp:data.timestamp,
                    nonceStr:data.nonceStr,
                    signature:data.signature,
                    jsApiList: [
                        'checkJsApi',
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage',
                        'hideOptionMenu',
                    ]
                });
                wx.error(function(res){
                    showLog("config信息验证失败",res);
                    wxShare(true);
                });
                var shareTitle = "一起分享吧！";
                wx.ready(function(){
                    alert("准备分享到朋友22222");
                    /*wx.onMenuShareTimeline({
                        title : shareTitle, // 分享标题
                        link : 'http://cyy.tunnel.qydev.com/index-wx.html', // 分享链接
                        imgUrl : shareImg, // 分享图标
                        success : function() {
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
                    });*/
                    wx.onMenuShareAppMessage({
                        title: '来报名吧，聚优惠2', // 分享标题
                        desc: '无缘无故省了200块，什么情况,续集', // 分享描述
                        link: 'http://cyy.tunnel.qydev.com/index-wx.html?id=520', // 分享链接
                        imgUrl: wx_share_img, // 分享图标
                        type: 'link', // 分享类型,music、video或link，不填默认为link
                        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                        success: function () {
                            // 用户确认分享后执行的回调函数
                            alert("分享成功");
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
                            alert("分享取消");
                        }
                    });
                    //wx.hideOptionMenu();/***隐藏分享菜单****/
                });
            }
        );
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