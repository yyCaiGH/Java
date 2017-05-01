/* Controllers */
// 登陆 controller
app.controller('UserCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    $scope.host = ENV.api;
    var api_get_org_users = $scope.host + "admin/getOrgApplyUser";
    var api_update_status = $scope.host + "admin/updateStatus";
    var api_check_cd_key = $scope.host + "admin/checkCdKey";
    $scope.user = {};
    initData();
    function initData() {
        showLog("UserCtrl init");
        getOrgApplyUser();
    }

    function getOrgApplyUser() {
        var data = {id:$scope.app.id};
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
    function updateStatus(id,status) {
        var data = {id: id, status: status};
        myHttp(
            api_update_status,
            data,
            function (obj) {
                showMsg(obj.msg);
                getOrgApplyUser();
            }
        );
    }
    function checkCdKey(id,cd_key) {
        var data = {id: id, cd_key: cd_key};
        myHttp(
            api_check_cd_key,
            data,
            function (obj) {
                showMsg(obj.msg);
                getOrgApplyUser();
            }
        );
    }
    $scope.ok = function (id,name) {
        layer.prompt({title: "请输入用户'"+name+"'的验证码进行验证", formType: 2}, function(text, index){
            layer.close(index);
            checkCdKey(id,text);
        });
    }
    $scope.cancel = function (id,name) {
        layer.confirm("确认用户'"+name+"'已经放弃培训？", {
            btn: ['确认','取消'] //按钮
        }, function(){
            updateStatus(id,3);
        }, function(){

        });
    }
}])
;