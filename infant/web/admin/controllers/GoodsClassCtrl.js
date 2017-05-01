/**
 * Created by Administrator on 2016/10/22 0022.
 * 商品分类管理
 */
app.controller('GoodsClassCtrl', ['ENV','$scope','$state','$modal',function(ENV,$scope, $state,$modal) {
    var host = ENV.api;
    var api_get_class_list_sort= host+"category/getListSort";
    var api_class_delete= host+"category/delete";

    initDate();
    function initDate(){
        console.log("初始化商品分类管理界面！");
        initClassList();
    }

    function initClassList(){
        $.ajax({
            type:"post",
            url:api_get_class_list_sort,
            data:{parent_id:-1},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.classList = obj.data;
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

    /**
     * 添加分类
     * @param size
     * @param parentId 父类id
     */
    $scope.addClass = function (parentId,catName,size) {

        var modalInstance = $modal.open({
            templateUrl: 'modal_add_class.html',
            controller: 'ModalAddClassCtrl',
            size: size,
            resolve: {
                items: function () {
                    var items = {
                        parentId : parentId,
                        catName : catName
                    }
                    return items;
                }
            }
        });
        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if(sucData==0){
                showMsg("添加分类成功!");
                initClassList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    }
    
    $scope.delete = function (id,parentId) {
        var index = layer.confirm('确定删除吗？', {
            title: "提示",
            btn: ['确定', '取消'] //按钮
        }, function () {
            deleteFun(id,parentId);
            layer.close(index);
        }, function () {
        });
    }
    function deleteFun(id,parentId){
        $.ajax({
            type:"post",
            url:api_class_delete,
            data:{id:id,parent_id:parentId},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    showMsg("删除分类成功")
                    initClassList();
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

app.controller('ModalAddClassCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_add_class= host+"category/add";
    initData();
    function  initData() {
        var parentId = items.parentId;
        var catName = items.catName;
        if(parentId == 0){//添加一级分类
            $scope.title = "添加一级分类";
        }
        else{//添加二级分类
            $scope.title = "添加“"+catName+"”的二级分类";
        }
    }
    $scope.ok = function () {
        var className = document.getElementById('className').value;
        if(isEmpty(className)){
            showMsg("分类名称不能为空！");
            return;
        }
        addClass(items.parentId,className);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    //添加SKU
    function addClass(parent_id,cat_name){
        $.ajax({
            type:"post",
            url:api_add_class,
            data:{parent_id:parent_id,cat_name:cat_name},
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