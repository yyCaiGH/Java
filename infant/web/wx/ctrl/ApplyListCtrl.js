/* Controllers */
app.controller('ApplyListCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_apply_data= $scope.host+"wx/getApplyDataByPhone";
    $scope.applyData = {};
    var phone = $stateParams.phone;
    initData();
    function initData() {
        showLog("ApplyListCtrl init");
        getApplyData();
    }
    function getApplyData() {
        var data={phone:phone};
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

    $scope.gotoLook = function(id) {
        $state.go("access.apply",{id:id});
    }
  }])
;