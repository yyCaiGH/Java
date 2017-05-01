/**
 * Created by Administrator on 2016/10/22 0022.
 * 实时库存管理
 */
app.controller('RepertoryCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_search_info= $scope.host+"brand/getPutawayBrands";
    var api_get_repertory_list = $scope.host+"repertory/getList"

    $scope.repertoryParams = {
        totalPage:1,
        pageSize : ENV.pageSize,
        pageNo : 1,
        putaway : "",
        brandsId : "",
        minRepertory : "",
        maxRepertory : "",
        goodsName : "",
        goodsNumber : ""
    }
    initData();
    function  initData() {
        showLog("实时库存管理");
        initSearchInfo();
        initLayui();
        initSkuGoodsList();
        initPage(1);
    }
    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
                $scope.repertoryParams.brandsId = data.value;
            });
            form.on('select(putaway)', function(data){
                console.log("putaway:"+data.value);
                $scope.repertoryParams.putaway = data.value;
            });
            form.on('submit(go)', function(data){
                $scope.repertoryParams.pageNo = 1;//页面重置为1
                initSkuGoodsList();
            });
        });
    }

    function  initPage(currPage) {
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage
                ,layer = layui.layer;

            laypage({
                cont: 'layuiPage'
                ,pages: $scope.repertoryParams.totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        $scope.repertoryParams.pageNo = obj.curr;
                        initSkuGoodsList();
                    }
                }
            });

        });
    }

    function  initSearchInfo() {
        var data = {};
        myHttp(
            api_get_search_info,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.brandList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    /**
     * 初始化化商品属性
     */
    function initSkuGoodsList(){
        var data = $scope.repertoryParams;
        myHttp(
            api_get_repertory_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.skuGoodsList = obj.data.list;
                    $scope.repertoryParams.totalPage = obj.data.totalPage;
                    initPage($scope.repertoryParams.pageNo);
                });
            }
        );
    }
}])
;