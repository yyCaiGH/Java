/* Controllers */
// 登陆 controller
app.controller('SigninCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    $scope.host = ENV.api;
    var api_login = $scope.host + "admin/login";
    $scope.user={};
    initData();
    function initData() {
        showLog("SigninCtrl init");
    }

    $scope.login = function() {
        if($scope.user.account=="admin"){
            if ($scope.user.password=="admin2017"){
                localStorage.setItem("account","admin");
                localStorage.setItem("id",0);
                $scope.app.account = "admin";
                $scope.app.id = 0;
                $state.go("app.puser");
            }
            else{
                showMsg("密码错误")
            }
        }
        else{
            orgLogin();
        }

    }

    function orgLogin() {
        var data = $scope.user;
        myHttp(
            api_login,
            data,
            function (obj) {
                showMsg(obj.msg);
                localStorage.setItem("account",obj.data.account);
                localStorage.setItem("password",obj.data.password);
                localStorage.setItem("id",obj.data.id);
                $scope.app.account = obj.data.account;
                $scope.app.password = obj.data.password;
                $scope.app.id = obj.data.id;
                $state.go("app.user");
            }
        );
    }
}])
;