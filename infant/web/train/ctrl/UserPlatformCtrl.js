/* Controllers */
// 平台
app.controller('UserPlatformCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    $scope.host = ENV.api;
    var api_get_all_apply = $scope.host + "admin/getAllApply";
    $scope.user = {};
    initData();
    function initData() {
        showLog("UserPlatformCtrl init");
        getOrgApplyUser();
    }

    function getOrgApplyUser() {
        var data = {};
        myHttp(
            api_get_all_apply,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                });
            }
        );
    }
}])
;