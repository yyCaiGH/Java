/**
 * Created by Administrator on 2016/10/22 0022.
 * Sku管理
 */
app.controller('PostageGroupCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {
    var host = ENV.api;
    var api_get_postage_list= host+"postage/getList";
    var api_postage_delete= host+"postage/delete";
    initData();
    function initData(){
        console.log("初始化运费组管理界面！");
        initPostageList();
    }

    function initPostageList(){
        $.ajax({
            type:"post",
            url:api_get_postage_list,
            data:{},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.postageGroupList = obj.data;
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

    $scope.openTpl = function(id,name){
        $state.go("app.postageTpl",{id:id,name:name});
    }
    function deleteFun(id){
        $.ajax({
            type:"post",
            url:api_postage_delete,
            data:{id:id},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    initPostageList();
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
    $scope.open = function (id,name,size) {
        $scope.postageGroup={
            id : id,
            name : name
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_add_postage_group.html',
            controller: 'ModalAddPostageGroupCtrl',
            size: size,
            resolve: {
                items: function () {
                    return $scope.postageGroup;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if(sucData==0){
                if(id==null){
                    showMsg("添加运费组成功!");
                }
                else{
                    showMsg("更新运费组成功!");
                }
                initPostageList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
}])
;

app.controller('ModalAddPostageGroupCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_add_postage= host+"postage/add";
    var api_update_postage= host+"postage/update";
    initData();
    function  initData() {
        if(items.id!=null){//更新操作
            $scope.name = items.name;
            $scope.title = "更新运费组";
        }
        else{
            $scope.title = "新增运费组";
        }
    }
    $scope.ok = function () {
        var name = document.getElementById('name').value;
        if(isEmpty(name)){
            showMsg("属性名不能为空！");
            return;
        }
        if(items.id==null){
            addPostage(name);
        }
        else{
            updatePostage(items.id,name);
        }

    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    //添加运费组
    function addPostage(name){
        $.ajax({
            type:"post",
            url:api_add_postage,
            data:{name:name},
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

    //更新运费组
    function updatePostage(id,name){
        $.ajax({
            type:"post",
            url:api_update_postage,
            data:{id:id,name:name},
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
