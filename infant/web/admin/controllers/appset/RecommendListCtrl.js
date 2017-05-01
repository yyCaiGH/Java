/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('RecommendListCtrl', ['ENV','$scope','$state',"$modal",function(ENV,$scope, $state,$modal) {
    $scope.host = ENV.api;
    var api_get_goods_list= $scope.host+"goods/getList";
    var api_goods_no_recommend = $scope.host+"goods/noRecommend"
    var pageNo = 1;
    var totalPage = 1;
    initDate();
    function initDate(){
        console.log("首页每日推荐！");
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
            putaway:1,
            recommend:1
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

    function  initPage(currPage) {
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage;

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

    function cancelRecommend(id) {
        var data = {id:id};
        myHttp(
            api_goods_no_recommend,
            data,
            function (obj) {
                pageNo = 1;
                initGoodsList(pageNo);
            }
        );
    }
    $scope.suerCancel = function(id){
        var index = layer.confirm('确定取消推荐吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            cancelRecommend(id);
            layer.close(index);
        },function(){
        });
    }

    $scope.open = function (size) {
        var modalInstance = $modal.open({
            templateUrl: 'model_goods_select.html',
            controller: 'GoodsSelectCtrl',
            size: size,
            resolve: {
                items: function () {
                    return $scope.items;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if(sucData==0){
                showMsg("推荐商品成功!");
                initGoodsList(pageNo);
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

}])
;


/**
 * Created by Administrator on 2016/10/22 0022.
 * 选择
 */
app.controller('GoodsSelectCtrl', ['ENV','$scope','$modalInstance','$state','items',function(ENV,$scope,$modalInstance, $state,items) {
    $scope.host = ENV.api;
    var api_get_goods_list= $scope.host+"goods/getList";
    var api_goods_recommend = $scope.host+"goods/recommend"
    var pageNo = 1;
    var totalPage = 1;
    var selectedSkuGoods = [] ;
    initData();
    function  initData() {
        showLog("选择入库商品");
        initLayui();
        initGoodsList();
        //initPage(1);
    }
    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
            });
            form.on('checkbox(select)', function(data){
                console.log(data.elem.checked); //是否被选中，true或者false
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                selectSkuGodos(data.elem.checked,data.value);
            });
            form.on('submit(go)', function(data){
                pageNo = 1;//页面重置为1
                initGoodsList();
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
                cont: 'layuiPage1'
                ,pages: totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        pageNo = obj.curr;
                        initGoodsList();

                    }
                }
            });

        });
    }

    /**
     * 初始化化商品属性
     */
    function initGoodsList(){
        var data = {
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            putaway:1,
            recommend:0
        };
        myHttp(
            api_get_goods_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.goodsList = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
                renderLayuiForm();
                initSelectDate();
            }
        );
    }

    /**
     * 推荐商品
     */
    function recommend() {
        var data = {selectGoodsIds:JSON.stringify(selectedSkuGoods)};
        myHttp(
            api_goods_recommend,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }

    $scope.ok = function () {
        recommend();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}])
;