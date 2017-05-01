/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('OrderDetailCtrl', ['ENV','$scope','$state',"$stateParams",function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    var api_get_order_info= $scope.host+"yorder/getOrderInfo";
    var api_get_returns_addr= $scope.host+"config/getTypeList";
    var api_order_mjfh = $scope.host+"yorder/mjfh";
    var api_order_mjtyth = $scope.host+"yorder/mjtyth";
    var api_order_mjqrsh = $scope.host+"yorder/mjqrsh";
    var api_order_mjsqth = $scope.host+"yorder/mjsqth";//买家申请退货
    var api_order_pay_return = $scope.host+"yorder/payReturn";
    var api_order_close = $scope.host+"yorder/closeOrder";

    var orderId = $stateParams.id;
    $scope.title="title";
    initData();
    function  initData() {
        showLog("会员详情");
        initLayui();
        initOrderInfo();
        getReturnAddr();
        getExpressage();
    }
    

    $scope.closeOrder = function () {
        var index = layer.confirm('确定关闭该订单吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            sureClose();
            layer.close(index);
        },function(){
        });
    }

    function sureClose() {
        var data = {
            id : $scope.order.id,
        }
        myHttp(
            api_order_close,
            data,
            function (obj) {
                showMsg("交易关闭！");
                initOrderInfo();
            }
        );
    }
    function initOrderInfo() {
        var data = {
            id:orderId
        };
        myHttp(
            api_get_order_info,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.order = obj.data.order;
                    $scope.orderGoodsList = obj.data.orderGoodsList;
                });
                renderLayuiForm();
            }
        );
    }
    function getReturnAddr() {
        var data = {
            id:7
        };
        myHttp(
            api_get_returns_addr,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.addrList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    //获取快递
    function getExpressage() {
        var data = {
            id:1
        };
        myHttp(
            api_get_returns_addr,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.expressageList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    //卖家发货
    function mjfh() {
        var data = {
            id : $scope.order.id,
            logistics_number : $scope.order.logistics_number,
            deliver_reamrk :  $scope.order.deliver_reamrk,
            expressage : $scope.order.expressage
        }
        myHttp(
            api_order_mjfh,
            data,
            function (obj) {
               showMsg("发货成功！");
                initOrderInfo();
            }
        );
    }
    //卖家同意退货
    function mjtyth() {
        var data = {
            id : $scope.order.id,
            returns_address : $scope.order.returns_address,
            returns_remark :  $scope.order.returns_remark,
        }
        myHttp(
            api_order_mjtyth,
            data,
            function (obj) {
                showMsg("已同意退货！");
                initOrderInfo();
            }
        );
    }
    //卖家确认收货
    /*function mjqrsh() {
        var data = {
            id : $scope.order.id,
            returns_receiving_ramark : $scope.order.returns_receiving_ramark,
        }
        myHttp(
            api_order_mjqrsh,
            data,
            function (obj) {
                showMsg("退货完成！");
                showLog(obj.data);
                $scope.$apply(function () {
                    $("#returnInfo").html(obj.data);
                });
                initOrderInfo();
            }
        );
    }*/
    //卖家确认收货直接调取退款接口，同意退款才能确认收货
    function mjqrsh() {
        var data = {
            orderId : $scope.order.id,
            returns_receiving_ramark : $scope.order.returns_receiving_ramark,
        }
        myHttp(
            api_order_pay_return,
            data,
            function (obj) {
                showMsg("退款");
                showLog(obj.data);
                $scope.$apply(function () {
                    $("#returnInfo").html(obj.data);
                });
            }
        );
    }
    function mjsqth() {
        var data = {
            id : $scope.order.id,
        }
        myHttp(
            api_order_mjsqth,
            data,
            function (obj) {
                showMsg("买家退货");
                initOrderInfo();
            }
        );
    }
    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(address)', function(data){
                console.log("address:"+data.value);
                $scope.order.returns_address = data.value;
            });
            form.on('select(expressage)', function(data){
                console.log("expressage:"+data.value);
                $scope.order.expressage = data.value;
            });
            form.on('submit(go)', function(data){//卖家确认发货
                showMsg("go");
                mjfh();
            });
            form.on('submit(go1)', function(data){//卖家同意退货
                showMsg("go1");
                mjtyth();
            });
            form.on('submit(go2)', function(data){//卖家确认收到买家退货
                showMsg("go2");
                mjqrsh();
            });
            form.on('submit(go11)', function(data){//买家申请退货
                showMsg("go11");
                mjsqth();
            });
        });
    }
}])
;