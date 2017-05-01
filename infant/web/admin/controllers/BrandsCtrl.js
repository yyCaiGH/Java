/**
 * Created by Administrator on 2016/10/22 0022.
 * 品牌管理
 */
app.controller('BrandCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {

    $scope.host = ENV.api;
    var pageSize = ENV.pageSize;
    var api_get_brand_list= $scope.host+"brand/getList";
    initData();
    $scope.totalPage = 1;
    function initData(){
        console.log("初始化品牌管理界面！");
        initPage(1);
        initBrandList(true,1);
    }

    $scope.detail = function (id) {
        $state.go("app.brandDetail",{id:id});
    }

    function  initPage(currPage) {
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage
                ,layer = layui.layer;

            laypage({
                cont: 'demo1'
                ,pages: $scope.totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        initBrandList(true,obj.curr);
                    }
                }
            });

        });
    }
    function  initBrandList(isSale,pageNo) {
        $.ajax({
            type:"post",
            url:api_get_brand_list,
            data:{
                pageSize:pageSize,
                pageNo:pageNo,
                is:isSale
            },
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.brandList = obj.data.list;
                        $scope.totalPage = obj.data.totalPage;
                        initPage(pageNo);
                    });
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error: function (obj) {
                console.log(obj);
                showMsg("请求失败！")
            },
            beforeSend: function () {
                loadIndex = loadingTip();
            },
            complete: function () {
                layer.close(loadIndex);
            }
        });
    }
    $scope.onSellBrands= function(){
        initBrandList(true,1);
    }

    $scope.haltSalesBrands = function(){
        initBrandList(false,1);
    }

}])
;