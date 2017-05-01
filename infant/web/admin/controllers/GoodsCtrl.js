/**
 * Created by Administrator on 2016/10/22 0022.
 * 商品管理
 */
app.controller('GoodsCtrl', ['ENV','$scope','$state','$stateParams',function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    var putaway = $stateParams.putaway;
    var api_get_goods_list= $scope.host+"goods/getList";
    var api_get_goods_attr= $scope.host+"goods/getGoodsSearchInfo";
    var api_get_class_list= $scope.host+"category/getList";
    $scope.title = "在售商品";
    var pageNo = 1;
    var totalPage = 1;
    $scope.goods={
        id:'',
        one_cat_id:"",//一级分类id
        tow_cat_id:"",//二级分类id
        brand_id:"",//品牌id
        main_number:"",//商品主编号
        name:"",//商品名称
        market_price:"",//零售价
        promote_price:"",//折扣价
        apply_sex:"",//适用性别
        apply_age:"",//适用年龄段
        apply_season:"",//适用季节
        texture:"",//材质
        img1_url:"",//
        img2_url:"",//
        img3_url:"",//
        img4_url:"",//
        img5_url:"",//
        tag_id:"",//
        production_area:"",//产地
        describe:"",//商品描述
        postage:0,//邮费（0、包邮，1...运费组id）
        packing_list:"",//包装清单
        after_sales:"",//售后服务
        putaway:putaway//是否上架（1、上架，2、下架）
    }
    initDate();
    function initDate(){
        console.log("初始化商品管理界面！");
        if(putaway==1){
            $scope.title = "在售商品";
        }
        else{
            $scope.title = "下架商品";
        }
        initLayui();
        initSearchInfo();
        initGoodsList(pageNo);
        initPage(pageNo);
    }

    /**
     * 初始化化商品属性
     */
    function initGoodsList(pageNo){
        var data = {
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            name:$scope.goods.name,
            one_cat_id:$scope.goods.one_cat_id,
            tow_cat_id:$scope.goods.tow_cat_id,
            brand_id:$scope.goods.brand_id,
            main_number:$scope.goods.main_number,
            name:$scope.goods.name,
            putaway:$scope.goods.putaway
        };
        myHttp(
            api_get_goods_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.goodsList = obj.data.list;
                    totalPage = obj.data.totalPage;
                    initPage(pageNo);
                });
            }
        );
    }
    function  initSearchInfo() {
        var data = {};
        myHttp(
            api_get_goods_attr,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.brandList = obj.data.brandList;
                    $scope.oneGoodsClassList = obj.data.oneGoodsClassList;
                });
                renderLayuiForm();
            }
        );
    }
    function  initTowClassList(oneClassId) {
        var data = {parent_id : oneClassId};
        myHttp(
            api_get_class_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.towGoodsClassList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
                $scope.goods.brand_id = data.value;
            });
            form.on('select(oneClass)', function(data){
                console.log("oneClass:"+data.value);
                $scope.goods.one_cat_id = data.value;
                $scope.$apply(function () {
                    $scope.towGoodsClassList = null;
                });
                initTowClassList($scope.goods.one_cat_id);
            });
            form.on('select(towClass)', function(data){
                console.log("towClass:"+data.value);
                $scope.goods.tow_cat_id = data.value;
            });
            form.on('submit(go)', function(data){
                pageNo = 1;
                initGoodsList(pageNo);
            });
        });
    }
    function  initPage(currPage) {
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage
                ,layer = layui.layer;

            laypage({
                cont: 'layuiPage'
                ,pages: totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        pageNo = obj.curr;
                        initGoodsList(pageNo);
                    }
                }
            });

        });
    }
    $scope.editGoods = function(id) {
        $state.go('app.editGoods',{goodsId:id});
    }
}])
;