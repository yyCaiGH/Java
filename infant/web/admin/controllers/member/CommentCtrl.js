/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('CommentCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_commnet_list= $scope.host+"article/getComments";
    var api_commnet_update= $scope.host+"article/updateCommentStatus";

    var pageNo = 1;
    var totalPage = 1;
    $scope.comment = {
        type:1,
        status:0
    }
    initData();
    function  initData() {
        showLog("评论管理");
        initLayui();
        initCommentList(pageNo);
    }
    $scope.hind = function (id) {
        update(id,1);
    }

    $scope.show = function (id) {
        update(id,0);
    }

    function update(id,status) {
        var data = {
            id : id,
            status:status
        };
        myHttp(
            api_commnet_update,
            data,
            function (obj) {
                initCommentList(pageNo);
            }
        );
    }

    $scope.getComment = function (status) {
        $scope.comment.status = status;
        pageNo = 1;
        initCommentList(pageNo);
    }

    function initCommentList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            type:$scope.comment.type,
            status:$scope.comment.status
        };
        myHttp(
            api_get_commnet_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
            }
        );
    }

    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
            });
            form.on('submit(go)', function(data){

            });
        });
    }
    function  initPage(currPage) {
        layui.use(['laypage'], function(){
            var laypage = layui.laypage;
            laypage({
                cont: 'layuiPage'
                ,pages: totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        pageNo = obj.curr;
                        initCommentList(pageNo);
                    }
                }
            });

        });
    }
}])
;