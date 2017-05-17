/* Controllers */
app.controller('ExamCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    initData();
    function initData() {
        showLog("ExamCtrl init");
    }

  }])
;