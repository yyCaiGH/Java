/**
 * Created by Administrator on 2016/10/22 0022.
 * 入库明细
 */
app.controller('PurchaseDetailCtrl', ['ENV','$scope','$state',"$stateParams",function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    var api_get_repertory_detail = $scope.host+"repertory/getPurchaseDetail"
    var purchaseId = $stateParams.id;
    $scope.title = $stateParams.time;
    initData();
    function  initData() {
        showLog("入库明细:"+purchaseId);
        initSkuGoodsList();
    }


    /**
     * 初始化化商品属性
     */
    function initSkuGoodsList(){
        var data = {purchaseId:purchaseId};
        myHttp(
            api_get_repertory_detail,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.skuGoodsList = obj.data;
                });
            }
        );
    }
}])
;