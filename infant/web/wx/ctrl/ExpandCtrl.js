/* Controllers */
app.controller('ExpandCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_home_data= $scope.host+"wx/home";
    $scope.applyData = {};
    initData();
    function initData() {
        showLog("ExpandCtrl init");
    }

  }])
;