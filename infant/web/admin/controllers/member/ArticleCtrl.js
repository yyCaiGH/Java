/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('ArticleCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_article_list =  $scope.host+"article/getAll";
    var api_article_update =  $scope.host+"article/updateArticle";
    var pageNo = 1;
    var totalPage = 1;
    $scope.article ={
        startTime : null,
        endTime : null,
        type : 0//0:全部，1：置顶，2：推荐，3：隐藏
    }
    initData();
    function  initData() {
        showLog("朋友圈文章管理")
        initLayui();
        initArticleList(pageNo);
    }

    $scope.getAtricle = function (type) {
        $scope.article.type = type;
        pageNo = 1;
        initArticleList(pageNo);
    }
    $scope.hide = function(id){
        update(id,2);
    }
    $scope.show = function(id){
        update(id,2);
    }
    $scope.recommend = function(id){
        update(id,1);
    }
    $scope.noRecommend = function(id){
        update(id,1);
    }
    $scope.top = function(id){
        update(id,0);
    }
    $scope.noTop = function(id){
        update(id,0);
    }
    function initArticleList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            startTime:$scope.article.startTime,
            endTime:$scope.article.endTime,
            type:$scope.article.type
        };
        myHttp(
            api_get_article_list,
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
    function update(id,type) {
        var data = {
            artId:id,
            type:type,
        };
        myHttp(
            api_article_update,
            data,
            function (obj) {
                initArticleList(pageNo);
            }
        );
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
                        initArticleList(pageNo);
                    }
                }
            });

        });
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
}])
;