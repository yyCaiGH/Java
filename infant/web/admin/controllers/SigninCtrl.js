

/* Controllers */
  // 登陆 controller
app.controller('SigninController', ['$scope', '$http', '$state','SigninFactory',function($scope, $http, $state,SigninFactory) {
    $scope.user = {};
    $scope.authError = null;
    $scope.login = function() {
      $scope.authError = null;
      // Try to login
        SigninFactory.adminLogin( $scope.user);
      /*$http.post('http://localhost:8083/admin/login', {userName: $scope.user.email, password: $scope.user.password})
      .then(function(response) {
        if ( !response.data.user ) {
          $scope.authError = 'Email or Password not right';
        }else{
          $state.go('app.dashboard-v1');
        }
      }, function(x) {
        $scope.authError = 'Server Error';
      });*/

    };
  }])
;