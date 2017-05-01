/**
 * Created by Administrator on 2016/10/22 0022.
 * 运费管理
 */
app.controller('PostageTplCtrl', ['ENV', '$scope', '$modal', '$state', '$stateParams', function (ENV, $scope, $modal, $state, $stateParams) {
    var host = ENV.api;
    var api_get_postage_list = host + "postageTpl/getList";
    var api_postage_delete = host + "postageTpl/delete";
    $scope.postageGroup = {
        id: $stateParams.id,
        name: $stateParams.name
    }
    initData();
    function initData() {
        console.log("初始化运费模板管理界面！");
        //showMsg($stateParams.id+","+$stateParams.name);
        initPostageList($stateParams.id);
    }

    function initPostageList(pg_id) {
        $.ajax({
            type: "post",
            url: api_get_postage_list,
            data: {pg_id : pg_id},
            dataType: "json",
            success: function (obj) {
                console.log(obj);
                if (obj.code == 0) {
                    $scope.$apply(function () {
                        $scope.postageTplList = obj.data;
                    });
                }
                else {
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
    $scope.delete = function (id) {
        var index = layer.confirm('确定删除吗？', {
            title: "提示",
            btn: ['确定', '取消'] //按钮
        }, function () {
            deleteFun(id);
            layer.close(index);
        }, function () {
        });
    };

    function deleteFun(id) {
        var data = {id: id};
        myHttp(
            api_postage_delete,
            data,
            function (obj) {
                initPostageList($stateParams.id);
            }
        );
    }

    $scope.open = function (id,size) {
        $scope.postageTpl = {
            pg_id:$scope.postageGroup.id,//运费组id
            id: id,//运费模板id
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_postage_tpl.html',
            controller: 'ModalPostageTplCtrl',
            size: size,
            resolve: {
                items: function () {
                    return $scope.postageTpl;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if (sucData == 0) {
                if (id == null) {
                    showMsg("添加运费模板成功!");
                }
                else {
                    showMsg("更新运费模板成功!");
                }
                initPostageList($stateParams.id);
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
}])
;

app.controller('ModalPostageTplCtrl', ['ENV', '$scope', '$modalInstance', 'items', '$http', function (ENV, $scope, $modalInstance, items, $http) {
    var host = ENV.api;
    var api_add_postage = host + "postageTpl/add";
    var api_update_postage = host + "postageTpl/update";
    var api_get_kd_config = host + "config/getTypeList";
    var api_get_province_list = host + "postageTpl/getProvinces";
    var api_get_postage_tpl = host +"postageTpl/getById";
    $scope.postageTpl = {
        id:items.id,
        pg_id : items.pg_id,
        name: "",
        expressage: ""
    }
    initData();
    function initData() {
        if (items.id != null) {//更新操作
            $scope.title = "更新运费模板";
            initPostage($scope.postageTpl.id);//初始化省份和运费值
        }
        else {
            $scope.title = "新增运费模板";
            initProvince();//初始化省
        }
        initLayui();
        initExpressage();//初始化快递的值
    }

    function initPostage(id) {
        var data = {id: id};
        myHttp(
            api_get_postage_tpl,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.provinceList = obj.data.postageProvinceList;
                    $scope.postageTpl = obj.data.postageTpl;
                    setExpressageValue($scope.postageTpl.expressage);
                });
            }
        );
    }
    function initExpressage() {
        var data = {id: 1};
        myHttp(
            api_get_kd_config,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.kdList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    /**
     * 给快递默认值
     * @param pValue
     */
    function setExpressageValue(pValue){
        var mySelect = document.getElementById("mySelect");
        var options = mySelect.getElementsByTagName('option');
        for(var i=0; i<options.length; i++){
            var value = options[i].value;
           // console.log("option:"+value);
            if(value == pValue) {
                options[i].selected = true;
            }
        }
        renderLayuiForm();
    }

    function initProvince(){
        var data = {};
        myHttp(
            api_get_province_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.provinceList = obj.data;
                });
            }
        );
    }
    $scope.ok = function () {
        var provinces = [];
        var allProvince = document.getElementById("allProvince");
        var lables = allProvince.getElementsByTagName('label');
        var inputs = allProvince.getElementsByTagName('input');
        var emptyInput = true;//inputs至少有一个不为空
        for (var i = 0; i < lables.length; i++) {
            var lable = lables[i].innerHTML;
           // console.log("lable:" + lable);
            var input = inputs[i].value;
            if (isEmpty2(input)){
                input = 0;
            }
            if(emptyInput){
                if (!isEmpty2(input)){
                    emptyInput = false;
                }
            }
            //console.log("input:" + input);
            if (items.id != null) {//更新操作
                provinces.push({id:$scope.provinceList[i].id,province: lable, postage: input});
            }
            else{
                provinces.push({province: lable, postage: input});
            }

        }
        if(isEmpty2($scope.postageTpl.pg_id)){
            console.log("运费组id为空");
            return;
        }
        if (isEmpty($scope.postageTpl.name)){
            showMsg("模板名称不能为空！")
            return;
        }
        if (isEmpty($scope.postageTpl.expressage)){
            showMsg("请选择快递！")
            return;
        }
        if (emptyInput){
            showMsg("配置值不能为空！");
            return;
        }
        if (items.id == null) {
            addPostage($scope.postageTpl,JSON.stringify(provinces));
        }
        else {
            updatePostage($scope.postageTpl,JSON.stringify(provinces));
        }

    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    //添加运费模板
    function addPostage(postageTpl,provinces) {
        var data = {
            pg_id :postageTpl.pg_id,
            name: postageTpl.name,
            expressage : postageTpl.expressage,
            provinces : provinces
        }

        myHttp(
            api_add_postage,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }

    //更新运费模板
    function updatePostage(postageTpl,provinces) {
        var data = {
            id:postageTpl.id,
            pg_id :postageTpl.pg_id,
            name: postageTpl.name,
            expressage : postageTpl.expressage,
            provinces : provinces
        }

        myHttp(
            api_update_postage,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }

    function initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use('form', function () {
            var form = layui.form();
            form.on('select', function (data) {
                console.log(data.value); //得到被选中的值
                $scope.postageTpl.expressage = data.value;
            });
        });

    }

}])
;
