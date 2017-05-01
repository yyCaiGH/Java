/* Controllers */
// 登陆 controller
app.controller('UpdatePswCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    $scope.host = ENV.api;
    var api_update_psw = $scope.host + "admin/updatePsw";
    $scope.psw = {};
    initData();
    function initData() {
        showLog("UpdatePswCtrl init");
        initLayui();
    }

    function updatePsw() {
        var data = {id: $scope.app.id, password: $scope.psw.new_password};
        myHttp(
            api_update_psw,
            data,
            function (obj) {
                showMsg(obj.msg);
            }
        );
    }

    function initLayui() {
        //表单提交
        layui.use(['form'], function () {
            var form = layui.form();
            form.on('submit(go)', function (data) {
                if ($scope.app.password != $scope.psw.old_password) {
                    showMsg("原密码不对");
                }
                else if ($scope.psw.new_password != $scope.psw.new_password_again) {
                    showMsg("两次输入的新密码不一致");
                }
                else {
                    updatePsw();
                }
            });
        });

    }
}])
;