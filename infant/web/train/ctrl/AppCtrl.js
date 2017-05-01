

/**
 * 登陆
 */
app.controller('AppCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    initData();
    function initData() {
        showLog("AppCtrl init============");
        $scope.app.account = localStorage.getItem("account");
        $scope.app.password = localStorage.getItem("password");
        $scope.app.id = localStorage.getItem("id");
    }

}])
;