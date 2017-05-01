/* Controllers */
// 登陆 controller
app.controller('OrgCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    $scope.host = ENV.api;
    var api_get_org_users = $scope.host + "admin/getAllOrg";
    initData();
    function initData() {
        showLog("OrgCtrl init");
        getOrgApplyUser();
    }

    function getOrgApplyUser() {
        var data = {};
        myHttp(
            api_get_org_users,
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