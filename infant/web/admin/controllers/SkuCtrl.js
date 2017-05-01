/**
 * Created by Administrator on 2016/10/22 0022.
 * Sku管理
 */
app.controller('SkuCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {
    var host = ENV.api;
    var api_get_sku_list= host+"sku/getList";
    var api_sku_delete= host+"sku/delete";
    initData();
    function initData(){
        console.log("初始化SKU管理界面！");
        initSkuList();
    }

    function initSkuList(){
        $.ajax({
            type:"post",
            url:api_get_sku_list,
            data:{parent_id:0},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.skuList = obj.data;
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

    //删除Sku
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
        $.ajax({
            type:"post",
            url:api_sku_delete,
            data:{id:id},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    initSkuList();
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error: function (obj) {
                console.log(obj);
                showMsg("请求失败！")
            }
        });
    }
    $scope.items = ['item1', 'item2', 'item3'];

    $scope.open = function (size) {
        var modalInstance = $modal.open({
            templateUrl: 'modal_add_sku.html',
            controller: 'ModalAddSkuCtrl',
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
                showMsg("添加SKU成功!");
                initSkuList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

    $scope.openEdit = function (id,size) {
        var modalInstance = $modal.open({
            templateUrl: 'modal_edit_sku.html',
            controller: 'ModalEditSkuCtrl',
            size: size,
            resolve: {
                items: function () {
                    return id;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if(sucData==0){
                showMsg("更新SKU成功!");
                initSkuList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

}])
;

app.controller('ModalAddSkuCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_add_sku= host+"sku/addBatch";
    $scope.ok = function () {
        var inputList = document.getElementById('inputList');

        var inputs = inputList.getElementsByTagName('input');

        var skuName = document.getElementById('skuName').value;

        var skus = [];

        for(var i = 0; i < inputs.length; i++) {
            var str = inputs[i].value;
            if(!isEmpty(str)){
                skus.push({name:str});
            }
        }
        if(isEmpty(skuName)){
            showMsg("属性名不能为空！");
            return;
        }
        if(skus.length==0){
            showMsg("属性值不能为空！");
            return;
        }
        addSku(skuName,JSON.stringify(skus));
        //$modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    $scope.addInput = function(){
        $('#inputList').append("<input type='text' class='form-control' style='width: 17%;margin: 5px 10px'>");
    }

    //添加SKU
    function addSku(skuName,skus){
        $.ajax({
            type:"post",
            url:api_add_sku,
            data:{skuName:skuName,skus:skus},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $modalInstance.close(0);
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
}])
;

app.controller('ModalEditSkuCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_update_sku= host+"sku/updateBatch";
    var api_get_sku_by_id = host+"sku/getById";
    var oldSkuName = "";
    initData();
    function initData(){
        var id = items;
        initLayui()
        getSkuData(id);
    }
    function ok() {
        var inputList = document.getElementById('inputList');

        var inputs = inputList.getElementsByTagName('input');

        var skuName = document.getElementById('skuName').value;

        var skus = [];

        for(var i = 0; i < inputs.length; i++) {
            var str = inputs[i].value;
            if(!isEmpty(str)){
                skus.push({name:str});
            }
            if(i>=$scope.skus.length){
                $scope.skus.push({name:str})
            }
            else{
                $scope.skus[i].name = str;
            }
        }
        if(isEmpty(skuName)){
            showMsg("属性名不能为空！");
            return;
        }
        if(skus.length==0){
            showMsg("属性值不能为空！");
            return;
        }
        updateSku(skuName,JSON.stringify($scope.skus));
        //$modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    $scope.addInput = function(){
        $('#inputList').append("<input type='text' class='form-control'  lay-verify='required' style='width: 17%;margin: 5px 10px'>");
    }

    /**
     *
     * @param skuName
     * @param skus
     */
    function updateSku(skuName,skus){
        $.ajax({
            type:"post",
            url:api_update_sku,
            data:{
                oldSkuName:oldSkuName,
                skuId:$scope.skuName.id,
                skuName:skuName,
                skus:skus
            },
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $modalInstance.close(0);
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error: function (obj) {
                console.log(obj);
                showMsg("请求失败！");
            }
        });
    }

    function getSkuData(id){
        $.ajax({
            type:"post",
            url:api_get_sku_by_id,
            data:{id:id},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.skuName = obj.data.skuName;
                        $scope.skus = obj.data.skus;
                        oldSkuName = $scope.skuName.name;
                    });
                    renderLayuiForm();
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error: function (obj) {
                console.log(obj);
                showMsg("请求失败！");
            }
        });
    }
    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('submit(go)', function(data){
                ok();
            });
        });
    }
}])
;