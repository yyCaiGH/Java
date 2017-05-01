/* Controllers */
app.controller('UpdateCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    var api_update= $scope.host+"wx/update";
    var api_apply_data= $scope.host+"wx/getApplyDataById";
    $scope.applyData = {};
    var id = $stateParams.id;
    initData();
    function initData() {
        showLog("UpdateCtrl init");
        //getHomeData();
        getApplyData();
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
                getApplyData();
            }
        );
    }
    function getApplyData() {
        var data={id:id};
        myHttp(
            api_apply_data,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.applyData = obj.data;
                });
            }
        );
    }

    $scope.change = function(x){
        showLog("value="+x);
    }
    $scope.update = function(){
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
            api_update,
            data,
            function (obj) {
                console.log(obj);
                showMsg(obj.msg);
                localStorage.setItem("phone",$scope.applyData.user_phone);//返回的时候，上个页面的phone还是旧的
            }
        );
    }

  }])
;