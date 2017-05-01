/**
 * Created by Administrator on 2016/10/22 0022.
 * 配置管理
 */
app.controller('SysConfigCtrl', ['ENV','$scope', '$modal','$state',function(ENV,$scope,$modal, $state) {
    var host = ENV.api;
    var api_get_config_list= host+"config/getAll";
    var api_sku_delete= host+"sku/delete";
    initData();
    function initData(){
        console.log("初始化配置管理界面！");
        initConfigList();
    }

    function initConfigList(){
        $.ajax({
            type:"post",
            url:api_get_config_list,
            data:{},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                       $scope.configList = obj.data;
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

    $scope.openEdit = function (id,title,identifying,size) {
        var tempUrl;
        var ctrl;
        if (id==7){//退货地址
            tempUrl = "modal_add_returns_addr_config.html";
            ctrl ="ModalAddReturnsAddrConfigCtrl";
        }
        else{
            tempUrl = "modal_add_config.html";
            ctrl ="ModalAddConfigCtrl";
        }
        $scope.items={
            id:id,
            title:title,
            identifying :identifying
        }
        var modalInstance = $modal.open({
            templateUrl: tempUrl,
            controller: ctrl,
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
                showMsg("修改配置成功!");
                initConfigList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

}])
;

app.controller('ModalAddConfigCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_get_type_list= host+"config/getTypeList";
    var api_config_update= host+"config/update";
    var api_get_exp_list= host+"config/getExpList";
    initData();
    function initData() {
        $scope.configInfo = items;
        initLayui();
        initConfig();
        if($scope.configInfo.id==1){//物流设置
            getExpList();
        }
    }
    function submit(){
        var keyList = [];
        var valueList = [];
        var list = [];
        var trs = document.getElementsByClassName('myRow');//tr
        showLog("打印table的值:"+trs.length);
        for (var j = 0; j<trs.length;j++){
            var trInputs = trs[j].getElementsByTagName('input');
            showLog("input.length:"+trInputs.length);
            var config={
                id:"",
                key:"",
                value:"",
                simple_name:""
            }
            config.id = trInputs[0].value;
            config.key = trInputs[1].value;
            config.value = trInputs[2].value;

            if($.inArray(trInputs[1].value, keyList)!=-1){
                showMsg("配置值不能重复")
                return;
            }
            if($.inArray(trInputs[2].value, valueList)!=-1){
                showMsg("配置名称不能重复")
                return;
            }
            keyList.push(trInputs[1].value);
            valueList.push(trInputs[2].value);
            if($scope.configInfo.id==1){//物流设置
               for(var i = 0;i<$scope.expList.length;i++){
                   if($scope.expList[i].expName.indexOf(trInputs[2].value) >= 0){//包含该快递
                        config.simple_name = $scope.expList[i].simpleName;
                   }
               }
               if(isEmpty(config.simple_name)){
                   showMsg(trInputs[2].value+",找不到对应的快递公司");
                   return;
               }
            }
            list.push(config);
        }
        showLog("list：",list);
        updateConfig(JSON.stringify($scope.configInfo),JSON.stringify(list))
    }

    function  updateConfig(configInfo,list) {
        var data = {
            configInfo:configInfo,
            list:list
        };
        myHttp(
            api_config_update,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }

    $scope.addTr = function () {
        var table = document.getElementById("myTable");
        var newRow = table.insertRow(); //创建新行
        newRow.setAttribute("class","bg-white myRow"); //tr风格
        createBodyTr(newRow);
    }
    /**
     * 创建table身体的固定部分
     * @param newRow
     */
    function createBodyTr(newRow) {
        var newCell2 = newRow.insertCell(); //创建新单元格
        newCell2.innerHTML = '<input type="hidden" class="form-control table-input-50"><td><input type="number" class="form-control"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell3 = newRow.insertCell(); //创建新单元格
        newCell3.innerHTML = '<td><input type="text" class="form-control"  lay-verify="required"></td>' ; //单元格内的内容
    }
    //删除行
    $scope.deleteTr= function(){
        //得到table对象
        var mytable = document.getElementById("myTable");
        //得到table中的行对象数组
        var arr=mytable.rows;
        if(arr.length==1){

        }else{
            //删除最后一行
            arr[arr.length-1].remove();
        }
    }
    function initConfig() {
        var data = {id: $scope.configInfo.id};
        myHttp(
            api_get_type_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                });
            }
        );
    }

    /**
     * 全部物流公司列表
     */
    function getExpList() {
        var data = {};
        myHttp(
            api_get_exp_list,
            data,
            function (obj) {
                $scope.expList = obj.data;
            }
        );
    }
    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('submit(go)', function(data){
                submit();
            });
        });
    }
    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}])
;

app.controller('ModalAddReturnsAddrConfigCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_get_type_list= host+"config/getTypeList";
    var api_config_update= host+"config/update";

    initData();
    function initData() {
        $scope.configInfo = items;
        initLayui();
        initConfig()
    }
    function submit(){
        var list = [];
        var trs = document.getElementsByClassName('myRow');//tr
        showLog("打印table的值:"+trs.length);
        for (var j = 0; j<trs.length;j++){
            var trInputs = trs[j].getElementsByTagName('input');
            showLog("input.length:"+trInputs.length);
            var config={
                id:"",
                key:"",
                value:"",
            }
            config.id = trInputs[0].value;
            config.key = trInputs[1].value;
            config.value = trInputs[2].value+"；"+trInputs[3].value+"；"+trInputs[4].value;
            list.push(config);
        }
        showLog("list：",list);
        updateConfig(JSON.stringify($scope.configInfo),JSON.stringify(list))
    }

    function  updateConfig(configInfo,list) {
        var data = {
            configInfo:configInfo,
            list:list
        };
        myHttp(
            api_config_update,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }

    $scope.addTr = function () {
        var table = document.getElementById("myTable");
        var newRow = table.insertRow(); //创建新行
        newRow.setAttribute("class","bg-white myRow"); //tr风格
        createBodyTr(newRow);
    }
    /**
     * 创建table身体的固定部分
     * @param newRow
     */
    function createBodyTr(newRow) {
        var newCell2 = newRow.insertCell(); //创建新单元格
        newCell2.innerHTML = '<input type="hidden" class="form-control table-input-50"><td><input type="number" class="form-control"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell3 = newRow.insertCell(); //创建新单元格
        newCell3.innerHTML = '<td><input type="text" class="form-control"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell4 = newRow.insertCell(); //创建新单元格
        newCell4.innerHTML = '<td><input type="text" class="form-control"  lay-verify="phone"></td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td ><input type="text" class="form-control"  lay-verify="required"></td>' ; //单元格内的内容
    }
    //删除行
    $scope.deleteTr= function(){
        //得到table对象
        var mytable = document.getElementById("myTable");
        //得到table中的行对象数组
        var arr=mytable.rows;
        if(arr.length==1){

        }else{
            //删除最后一行
            arr[arr.length-1].remove();
        }
    }
    function initConfig() {
        var data = {id: $scope.configInfo.id};
        myHttp(
            api_get_type_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                    initAddrList();
                });
            }
        );
    }
    $scope.configList = [];
    function initAddrList() {
        showLog($scope.list);
        for(var i = 0 ; i<$scope.list.length;i++){
            showLog($scope.list[i]);
            var values = $scope.list[i].value.split("；");
            showLog(values);
            $scope.configList.push({
                id:$scope.list[i].id,
                key:$scope.list[i].key,
                name:values[0],
                phone:values[1],
                address:values[2]
            })
        }
    }
    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('submit(go)', function(data){
                submit();
            });
        });
    }
    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}])
;
