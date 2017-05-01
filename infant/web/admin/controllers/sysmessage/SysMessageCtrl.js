/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('SysMessageCtrl', ['ENV', '$scope', '$state',"$modal", function (ENV, $scope, $state,$modal) {
    $scope.host = ENV.api;
    var api_get_sys_message_list = $scope.host + "message/getPageListByType";
    var api_sys_message_delete = $scope.host + "message/del";
    var pageNo = 1;
    var totalPage = 1;
    initData();
    function initData() {
        showLog("系统消息列表");
        initSysMessageList();
        initPage(pageNo);
    }

    function initSysMessageList() {
        var data = {pageNo:pageNo,pageSize:ENV.pageSize,type:0};
        myHttp(
            api_get_sys_message_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data.list;
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
                        initSysMessageList(pageNo);
                    }
                }
            });

        });
    }

    $scope.delete = function(id){
        var index = layer.confirm('确定删除吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            deleteMes(id);
            layer.close(index);
        },function(){
        });
    }

    function deleteMes(id) {
        var data = {id:id};
        myHttp(
            api_sys_message_delete,
            data,
            function (obj) {
                showMsg("删除成功!");
                pageNo = 1;
                initSysMessageList(pageNo);
            }
        );
    }
    $scope.open = function (id,size) {
        $scope.items = {
            id : id
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_add_sys_message.html',
            controller: 'ModalAddSysMesCtrl',
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
                showMsg("发布系统消息成功!");
                initSysMessageList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

}])
;

app.controller('ModalAddSysMesCtrl', ['ENV','$scope','$modalInstance','$state','items',function(ENV,$scope,$modalInstance, $state,items) {
    $scope.host = ENV.api;
    var api_sys_message_add = $scope.host + "message/add";
    var api_get_sys_message = $scope.host + "message/info";
    var layedit;
    var index;
    $scope.mes = {
        id:items.id,
        title:null,
        content:null
    }
    initData();
    function  initData() {
        showLog("发布系统消息");
        //initLayui();
        setTimeout(
            function  initLayui() {
                layui.use(['layedit'], function(){
                    layedit = layui.layedit
                        ,$ = layui.jquery;
                    //构建一个默认的编辑器
                    index = createLayerEditor(layedit);
                });
            }
        ,100);
        if (items.id==null){//添加
            $scope.title = "发布系统消息";
        }
        else{
            $scope.title = "查看系统消息";
            initMes();
        }
    }
    function  initLayui() {
        layui.use(['layedit'], function(){
            layedit = layui.layedit
                ,$ = layui.jquery;
            //构建一个默认的编辑器
            index = layedit.build('LAY_demo1');
        });
    }

    $scope.ok = function () {
        if(items.id==null){
            var content =layedit.getContent(index);
            showLog("内容："+content);
            $scope.mes.content = content;
            if(isEmpty($scope.mes.title)){
                showMsg("标题不能为空");
            }
            else if(isEmpty($scope.mes.content)){
                showMsg("内容不能为空")
            }
            else{
                add();
            }
        }
        else{
            $modalInstance.close(1);
        }

    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    function  add() {
        var data = $scope.mes;
        myHttp(
            api_sys_message_add,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }
    function  initMes() {
        var data = {id:items.id};
        myHttp(
            api_get_sys_message,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.mes = obj.data;
                });
                initLayui();
            }
        );
    }
}])
;