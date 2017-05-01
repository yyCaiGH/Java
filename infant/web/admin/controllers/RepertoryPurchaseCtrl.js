/**
 * Created by Administrator on 2016/10/22 0022.
 * 新增采购入库
 */
app.controller('RepertoryPurchaseCtrl', ['ENV','$scope','$modal','$state',function(ENV,$scope,$modal, $state) {
    $scope.host = ENV.api;
    var api_update_repertory = $scope.host+"repertory/updateRepertory"
    $scope.sukGoodsSelected = [];//处理过的数据（选择的商品）
    $scope.remark=null;
    var original  = [];//原始数据（选择的商品）

    initData();
    function  initData() {
        showLog("新增采购入库");
        initLayui();
    }

    $scope.open = function (size) {
        var modalInstance = $modal.open({
            templateUrl: 'model_repertory_purchase_select.html',
            controller: 'RepertoryPurchaseSelectCtrl',
            size: size,
            resolve: {
                items: function () {
                    return original;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            showLog("已选择：",sucData);
            initSelectedDatas(sucData);
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

    function initSelectedDatas(sucData) {
        original  = sucData;
        $scope.sukGoodsSelected = [];
        for(var i = 0;i<sucData.length;i++){
            var datas = sucData[i].split(",");
            var skuGoods ={
                id:datas[0],
                img1_url : datas[1],
                name_zh : datas[2],
                one_cat_name :datas[3],
                tow_cat_name :datas[4],
                main_number : datas[5],
                goods_number : datas[6],
                sku_info :datas[7],
                repertory : null
            }
            $scope.sukGoodsSelected.push(skuGoods);
        }
        initLayui();
    }
    $scope.delete = function (id) {
        for(var i = 0; i<$scope.sukGoodsSelected.length;i++){
            var datas = original[i].split(",");
            if(datas[0]==id){
                original.splice($.inArray(original[i], original), 1);
            }
            if($scope.sukGoodsSelected[i].id==id){
                $scope.sukGoodsSelected.splice($.inArray($scope.sukGoodsSelected[i], $scope.sukGoodsSelected), 1);
                break;
            }
        }
    }

    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('submit(go)', function(data){
                showLog($scope.sukGoodsSelected);
                if($scope.sukGoodsSelected.length==0){
                    showMsg("请选择商品！");
                    return;
                }
                updateRepertory(JSON.stringify($scope.sukGoodsSelected));
            });
        });
    }

    /**
     * 更新商品的库存
     * @param sukGoodsJson
     */
    function updateRepertory(sukGoodsJson) {
        var data = {
            sukGoodsJson:sukGoodsJson,
            remark:$scope.remark
        };
        myHttp(
            api_update_repertory,
            data,
            function (obj) {
                showMsg("更新库存成功");
                $state.go("app.repertory");
            }
        );
    }
}])
;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 选择入库商品
 */
app.controller('RepertoryPurchaseSelectCtrl', ['ENV','$scope','$modalInstance','$state','items',function(ENV,$scope,$modalInstance, $state,items) {
    $scope.host = ENV.api;
    var api_get_repertory_list = $scope.host+"repertory/getList"
    var api_get_search_info= $scope.host+"brand/getPutawayBrands";
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
    var selectedSkuGoods = [] ;
    initData();
    function  initData() {
        showLog("选择入库商品");
        selectedSkuGoods = items;
        initLayui();
        initSearchInfo();
        initSkuGoodsList();
        //initPage(1);
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
            form.on('checkbox(select)', function(data){
                console.log(data.elem.checked); //是否被选中，true或者false
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                selectSkuGodos(data.elem.checked,data.value);
            });
            form.on('submit(go)', function(data){
                $scope.repertoryParams.pageNo = 1;//页面重置为1
                initSkuGoodsList();
            });
        });
    }

    //选择商品
    function selectSkuGodos(checked,value) {
        if(checked){//选择，存储起来
            if ($.inArray(value, selectedSkuGoods)==-1){
                selectedSkuGoods.push(value);
            }
        }
        else{//不选择，从存储中去除
            selectedSkuGoods.splice($.inArray(value, selectedSkuGoods), 1);
        }
        showLog(selectedSkuGoods);
    }
    //初始化选择数据
    function initSelectDate() {
        $(".myChecked").each(function(){//复选框初始化
            $(this).removeAttr("checked");
        });
        for(var i = 0;i<selectedSkuGoods.length;i++){
            var id = selectedSkuGoods[i].split(",")[0];
            $("#myChecked"+id).prop("checked","true");
        }
        renderLayuiForm();
    }
    function  initPage(currPage) {
        layui.use(['laypage'], function(){
            var laypage = layui.laypage;
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
                renderLayuiForm();
                initSelectDate();
            }
        );
    }

    $scope.ok = function () {
        $modalInstance.close(selectedSkuGoods);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}])
;