/**
 * Created by Administrator on 2016/10/22 0022.
 * 国家管理
 */
app.controller('CountryCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {
    $scope.host = ENV.api;
    var api_get_country_list= $scope.host+"country/getList";
    var api_country_delete= $scope.host+"country/delete";
    initData();
    function initData(){
        console.log("初始化国家管理界面！");
        initCountryList();
    }

    function initCountryList(){
        $.ajax({
            type:"post",
            url:api_get_country_list,
            data:{},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.countryList = obj.data;
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

    //删除国家
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
            url:api_country_delete,
            data:{id:id},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    initCountryList();
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
    $scope.addCountry = function(size){

        var modalInstance = $modal.open({
            templateUrl: 'modal_add_country.html',
            controller: 'ModalAddCountryCtrl',
            size: size,
            resolve: {
                items: function () {
                    return $scope.items;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            if(sucData==0){
                showMsg("添加国家成功!");
                initCountryList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });

    }

    $scope.openEdit = function (id,size) {
        var modalInstance = $modal.open({
            templateUrl: 'modal_edit_country.html',
            controller: 'ModalEditCountryCtrl',
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
                showMsg("更新国家成功!");
                initCountryList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
}])
;

app.controller('ModalAddCountryCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    $scope.host = ENV.api;
    var api_country_add= ENV.api+"country/add";
    $scope.country = {
        name : "",
        img_url : ""
    }
    initData();
    function initData(){
        console.log("初始化新增国家界面！");
        getExpList();
    }
    $scope.addOk = function () {
        if(isEmpty($scope.country.name)){
            showMsg("国家名称不能为空！");
            return false;
        }
        if(isEmpty($scope.country.img_url)){
            showMsg("国旗图片不能为空！");
            return false;
        }
        $.ajax({
            url:api_country_add,
            type:"post",
            data:$scope.country,
            success:function(obj){
                console.log(obj);
                if(obj.code==0){
                    $modalInstance.close(0);
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error:function(obj){
                console.log(obj);
                showMsg("请求失败");
            },
            beforeSend: function () {
                loadIndex = loadingTip();
            },
            complete: function () {
                layer.close(loadIndex);
            }
        });
    };

    function getExpList() {
        var data = {};
        myHttp(
            ENV.api+"config/getExpList",
            data,
            function (obj) {
                initLayui();//只是为了延迟加载图片按钮
            }
        );
    }

    function initGoodsTagList(){
        var data = {};
        myHttp(
            $scope.host+"goodsTag/getList",
            data,
            function (obj) {
                initLayui();//只是为了延迟加载图片按钮
            }
        );
    }

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    function  initLayui() {
        console.log("表单初始化");
        //图片上传
        layui.use('upload', function(){
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsTagImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        $scope.country.img_url =  obj.data;
                    });
                }
            });
        });
    }

}])
;

app.controller('ModalEditCountryCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    $scope.host = ENV.api;
    var api_update_country= $scope.host+"country/update";
    var api_get_country_by_id = $scope.host+"country/getById";
    initData();
    function initData(){
        var id = items;
        getCountryData(id);
    }
    $scope.ok = function () {
        if(isEmpty($scope.country.name)){
            showMsg("国家名称不能为空！");
            return;
        }
        if(isEmpty($scope.country.img_url)){
            showMsg("国旗图片不能为空！");
            return false;
        }
        $.ajax({
            url:api_update_country,
            type:"post",
            data:$scope.country,
            success:function(obj){
                console.log(obj);
                if(obj.code==0){
                    $modalInstance.close(0);
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error:function(obj){
                console.log(obj);
                showMsg("请求失败");
            },
            beforeSend: function () {
                loadIndex = loadingTip();
            },
            complete: function () {
                layer.close(loadIndex);
            }
        });
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    function getCountryData(id){
        $.ajax({
            type:"post",
            url:api_get_country_by_id,
            data:{id:id},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.country = obj.data;
                    });
                    initLayui()
                }
                else{
                    showMsg(obj.msg);
                }
            },
            error: function (obj) {
                console.log(obj);
                showMsg("请求失败！");
            },
            beforeSend: function () {
                loadIndex = loadingTip();
            },
            complete: function () {
                layer.close(loadIndex);
            }
        });
    }
    function  initLayui() {
        console.log("表单初始化");
        //图片上传
        layui.use('upload', function(){
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsTagImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        $scope.country.img_url =  obj.data;
                    });
                }
            });
        });
    }
}])
;