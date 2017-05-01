/**
 * Created by Administrator on 2016/10/22 0022.
 * 品牌管理
 */
app.controller('GoodsTagCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {
    $scope.host = ENV.api;
    var api_get_goods_tag_list= $scope.host+"goodsTag/getList";
    var api_goods_tag_delete= $scope.host+"goodsTag/delete";
    initData();
    function initData(){
        console.log("初始化商品tag界面！");
        initGoodsTagList();
    }

    function initGoodsTagList(){
        var data = {};
        myHttp(
            api_get_goods_tag_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.goodsTagList = obj.data;
                });
            }
        );
    }

    //删除
    $scope.delete = function(id){
        var index = layer.confirm('确定删除吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            deleteFun(id);
            layer.close(index);
        },function(){
        });
    };
    function deleteFun(id){
        var data = {id:id};
        myHttp(
            api_goods_tag_delete,
            data,
            function (obj) {
                initGoodsTagList();
            }
        );
    }
    $scope.open = function(id,name,imgUrl,size){
        var goodsTag = {
            id : id,
            name : name,
            img_url : imgUrl
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_goods_tag.html',
            controller: 'ModalGoodsTagCtrl',
            size: size,
            resolve: {
                items: function () {
                    return goodsTag;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            if(sucData==0){
                if(id == null){
                    showMsg("添加商品标签成功!");
                }
                else{
                    showMsg("修改商品标签成功!");
                }
                initGoodsTagList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });

    }

}])
;

app.controller('ModalGoodsTagCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    $scope.host = ENV.api;
    var api_goods_tag_add= ENV.api+"goodsTag/add";
    var api_goods_tag_update= ENV.api+"goodsTag/update";
    var api_get_goods_tag_list= $scope.host+"goodsTag/getList";
    $scope.goodsTag = items;
    initData();
    function initData(){
        if($scope.goodsTag.id == null){//添加商品tag
            $scope.title = "新增商品标签"
        }
        else{
            $scope.title = "修改商品标签"
        }
        initGoodsTagList();
    }

    function initGoodsTagList(){
        var data = {};
        myHttp(
            api_get_goods_tag_list,
            data,
            function (obj) {
                initLayui();//只是为了延迟加载图片按钮
            }
        );
    }

    function  initLayui() {
        console.log("表单初始化！cyy");
        //图片上传
        layui.use('upload', function(){
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsTagImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        $scope.goodsTag.img_url =  obj.data;
                    });
                }
            });
        });
    }

    $scope.ok = function () {
        if (isEmpty( $scope.goodsTag.name)){
            showMsg("标签名称不能为空！");
            return false;
        }
        if(isEmpty($scope.goodsTag.img_url)){
            showMsg("标签图片不能为空！");
            return false;
        }
        if($scope.goodsTag.id == null) {//添加商品tag
            var data = $scope.goodsTag;
            myHttp(
                api_goods_tag_add,
                data,
                function (obj) {
                    $modalInstance.close(0);
                }
            );
        }
        else{//修改商品tag
            var data = $scope.goodsTag;
            myHttp(
                api_goods_tag_update,
                data,
                function (obj) {
                    $modalInstance.close(0);
                }
            );
        }
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };


}])
;
