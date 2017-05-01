/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('PurchaseLogCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_purchase_log= $scope.host+"repertory/getPurchaseLog";
    var pageNo = 1;
    var totalPage = 1;
    initData();
    function  initData() {
        showLog("采购入库记录");
        getPurchaseLog()
    }

    $scope.openEdit = function (id,time) {
        $state.go("app.purchaseDetail",{id:id,time:time})
    }
    function getPurchaseLog() {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize
        };
        myHttp(
            api_get_purchase_log,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
            }
        );
    }
    function  initPage(currPage) {
        layui.use(['laypage'], function(){
            var laypage = layui.laypage;
            laypage({
                cont: 'layuiPage'
                ,pages: totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        pageNo = obj.curr;
                        getPurchaseLog();
                    }
                }
            });

        });
    }

}])
;