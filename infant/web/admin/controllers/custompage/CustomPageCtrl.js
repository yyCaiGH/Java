/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('CustomPageCtrl', ['ENV','$scope','$state',"$modal",function(ENV,$scope, $state,$modal) {
    $scope.host = ENV.api;
    var api_get_custom_page_list= $scope.host+"page/getList";
    var api_get_custom_page_del= $scope.host+"page/del";

    initData();

    function  initData() {
        showLog("自定义单页列表")
        initCustomPageList();
    }

    function initCustomPageList() {
        var data = {};
        myHttp(
            api_get_custom_page_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                });
            }
        );
    }

    $scope.edit = function (id) {
        $state.go("app.addcustomPage",{id:id});
    }

    $scope.delete = function (id) {
        var index = layer.confirm('确定删除吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            doDelete(id);
            layer.close(index);
        },function(){
        });
    }
    function doDelete(id) {
        var data = {id:id};
        myHttp(
            api_get_custom_page_del,
            data,
            function (obj) {
                showMsg("删除成功");
                initCustomPageList();
            }
        );
    }
    $scope.open = function (id,size) {
        $scope.item = {
            id:id
        }
        var modalInstance = $modal.open({
            templateUrl: 'model_edit_custom_page.html',
            controller: 'EditCustomPageCtrl',
            size: size,
            resolve: {
                items: function () {
                    return $scope.item;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            $scope.selected = sucData;
            if(sucData==0){
                showMsg("修改单页完成!");
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
}])
;

app.controller('EditCustomPageCtrl', ['ENV','$scope','$modalInstance','$state','items',function(ENV,$scope,$modalInstance, $state,items) {
    $scope.host = ENV.api;
    var api_custom_page_add= $scope.host+"page/add";
    $scope.customPage = {
        title : null,
        note : null,
        content : null,
        is_show : 0
    }
    initData();
    function  initData() {
        showLog("发布单页")
        initLayui();
    }

    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form', 'layedit'], function(){
            var layedit = layui.layedit
                ,$ = layui.jquery;
            //构建一个默认的编辑器
            var index = layedit.build('LAY_demo1');
            var form = layui.form();
            form.on('radio(filter)', function(data){
                console.log(data.value); //被点击的radio的value值
                $scope.customPage.is_show = data.value;
            });
            form.on('submit(go)', function(data){
                var content = layedit.getContent(index);
                console.log(content);
                if(isEmpty(content)){
                    showMsg("内容不能为空!");
                    return;
                }
                $scope.customPage.content = content;
                addCustomPage();
            });
        });
    }

    function  addCustomPage() {
        var data = $scope.customPage;
        myHttp(
            api_custom_page_add,
            data,
            function (obj) {
                showMsg("添加成功");
            }
        );
    }
}])
;