/* Controllers */
app.controller('ApplyResultCtrl',  ['ENV','$scope', '$modal','$state','$stateParams','$location',function(ENV,$scope,$modal, $state ,$stateParams,$location) {
    $scope.host = ENV.api;
    var api_apply_data= $scope.host+"wx/getApplyDataById";
    var api_update= $scope.host+"wx/update";
    $scope.applyData = {};
    var id = $stateParams.id;
    initData();
    function initData() {
        showLog("ApplyResultCtrl init:"+id);
        getApplyData();
    }

    function getApplyData() {
        var data={id:id};
        myHttp(
            api_apply_data,
            data,
            function (obj) {
                if(obj.data==null){
                    id = localStorage.getItem("phone");
                    if(!isEmpty2(id)){
                        getApplyData();
                    }
                }
                else{
                    $scope.$apply(function () {
                        $scope.applyData = obj.data;
                    });
                }

            }
        );
    }
    $scope.hasPay = function () {
        var info = "";
        if (isEmpty2($scope.applyData.wx_account)){
            info = "请留下您的微信号，客服会添加您并在确认您已支付培训费用的前提下给您转账50元";
        }
        else{
            info = "请确认您的微信号，客服会添加您并在确认您已支付培训费用的前提下给您转账50元";
        }
        $.prompt({
            text: info,
            input: $scope.applyData.wx_account,
            empty: false, // 是否允许为空
            onOK: function (text) {
                if(isEmpty2(text)){
                    showMsg("请留下您的微信号！");
                    return;
                }
                else{
                    $scope.applyData.wx_account = text;
                    $scope.applyData.user_sure_pay = true;
                    update();
                }
            },
            onCancel: function () {
                //点击取消
            }
        });
    }

    $scope.update = function () {
        $state.go("access.update",{id:$scope.applyData.id});
    }

    function update() {
        var data=$scope.applyData;
        yHttp(
            api_update,
            data,
            function (obj) {
                console.log(obj);
                showMsg(obj.msg);
                location.reload();
            }
        );
    }
    $scope.giveUp = function () {
        $.confirm("确认放弃培训?", function() {
            $scope.applyData.status = 3;
            update();
        }, function() {
            //点击取消后的回调函数
        });
    }
  }])
;