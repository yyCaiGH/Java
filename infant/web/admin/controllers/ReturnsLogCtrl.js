/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('ReturnsLogCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_order_origin= $scope.host+"config/getTypeList";
    var api_get_order_list= $scope.host+"yorder/getPageList";
    var pageNo = 1;
    var totalPage = 1;
    $scope.orderSearch = {
        searchValue : "",
        minTime : "",
        maxTime : "",
        status : "",
        status_returns : "",
        from : "",
    }
    initData();
    function  initData() {
        initLayui();
        initOrderOrigin();
        $scope.orderSearch.status_returns = 0;//代表包括1,2,3
        getOrderList();
    }

    //退货中
    $scope.onReturn = function () {
        $scope.orderSearch.status_returns = 0;//代表包括1,2,3
        pageNo = 1;
        getOrderList();
    }
    //已入库
    $scope.inStorage = function () {
        $scope.orderSearch.status_returns = 4;
        pageNo = 1;
        getOrderList();
    }
    function initOrderOrigin() {
        var data={id:2};
        myHttp(
            api_get_order_origin,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.orderOriginList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    $scope.detail = function (id) {
        $state.go("app.returnsDetail",{id:id});
    }
    function getOrderList() {
        var data={
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            searchValue : $scope.orderSearch.searchValue,
            minTime : $scope.orderSearch.minTime,
            maxTime : $scope.orderSearch.maxTime,
            status : 6,
            status_returns : $scope.orderSearch.status_returns,
            from : $scope.orderSearch.from,
            isReturns:true//退货入库的标记
        };
        myHttp(
            api_get_order_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.orderList = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
                renderLayuiForm();
            }
        );
    }
    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form','laydate'], function(){
            var form = layui.form();
            form.on('select(orderOrigin)', function(data){
                $scope.orderSearch.from = data.value;
            });
            form.on('submit(go)', function(data){
                var start_time = $("#LAY_demorange_s").val();
                var end_time = $("#LAY_demorange_e").val();
                $scope.orderSearch.minTime = start_time;
                $scope.orderSearch.maxTime = end_time;
                pageNo = 1;
                getOrderList();
            });

            var laydate = layui.laydate;
            var start = {
                format: 'YYYY-MM-DD hh:mm'
                ,istime: true
                ,max: '2099-06-16 23:59:59'
                ,istoday: false
                ,choose: function(datas){
                    $scope.orderSearch.minTime = datas;
                    end.min = datas; //开始日选好后，重置结束日的最小日期
                    end.start = datas //将结束日的初始值设定为开始日
                }
            };

            var end = {
                format: 'YYYY-MM-DD hh:mm'
                ,istime: true
                ,max: '2099-06-16 23:59:59'
                ,istoday: false
                ,choose: function(datas){
                    $scope.orderSearch.maxTime = datas;
                    start.max = datas; //结束日选好后，重置开始日的最大日期
                }
            };

            document.getElementById('LAY_demorange_s').onclick = function(){
                start.elem = this;
                laydate(start);
            }
            document.getElementById('LAY_demorange_e').onclick = function(){
                end.elem = this
                laydate(end);
            }

        });
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
                        getOrderList();
                    }
                }
            });

        });
    }
}])
;