/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('AddCustomPageCtrl', ['ENV','$scope','$state','$stateParams',function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    $scope.title = null;
    var api_custom_page_add= $scope.host+"page/add";
    var api_custom_page_update= $scope.host+"page/updatePage";
    var api_get_custom_page_by_id = $scope.host+"page/info";
    var id = $stateParams.id;//0：发布，>0：编辑
    $scope.customPage = {
        id:id,
        title : null,
        note : null,
        content : null,
        is_show : 0
    }
    var editor;
    initData();
    function  initData() {
        showLog("发布单页")
        if(id==0){
            showLog("发布");
            $scope.title = "发布单页";
        }
        else{
            showLog("编辑,id="+id);
            $scope.title = "编辑单页"
            initCustomPage(id);
        }
        initLayui();
        renderLayuiForm();
        editor = createEditor(ENV.wangEditorImgUploadApi);

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
                /*var content = layedit.getContent(index);
                console.log(content);*/
                var content = editor.$txt.html();
                if(isEmpty(content)){
                    showMsg("内容不能为空!");
                    return;
                }
                $scope.customPage.content = content;
                if(id==0) {
                    addCustomPage();
                }
                else{
                    updateCustomPage();
                }
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
                $state.go("app.customPageList");
            }
        );
    }
    function  updateCustomPage() {
        var data = $scope.customPage;
        myHttp(
            api_custom_page_update,
            data,
            function (obj) {
                showMsg("更新成功");
                $state.go("app.customPageList");
            }
        );
    }
    function  initCustomPage() {
        var data = {id:id};
        myHttp(
            api_get_custom_page_by_id,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.customPage = obj.data;
                });
                initLayui();//富文本编辑器需要再次加载
                initView();
                renderLayuiForm();
            }
        );
    }
    function  initView() {
        if($scope.customPage.is_show==1){
            $("#type2").attr("checked","checked");
        }
        editor.$txt.append($scope.customPage.content);
    }
}])
;