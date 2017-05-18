/* Controllers */
app.controller('ExamCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    $scope.applyData = {};
    initData();
    function initData() {
        showLog("ExamCtrl init");
    }

    $scope.apply = function(){
        if(!checkMobile($scope.applyData.user_phone)){
            showMsg("请输入正确的手机号");
            return;
        }
        $state.go("access.answer",
            {
                name:$scope.applyData.user_name,
                phone:$scope.applyData.user_phone,
                area:$scope.applyData.user_area
            });
    }
  }])
;