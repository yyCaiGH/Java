/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('MemberListCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_member_list= $scope.host+"member/getPageList";
    var api_get_member_count= $scope.host+"member/getMemberCount";
    var api_get_list= $scope.host+"vip/getList";
    var pageNo = 1;
    var totalPage = 1;
    $scope.member = {
        nickname : null,//代表昵称和手机号，后台处理
        status : 0,
        vip_grade_id : null
    }
    initData();
    function  initData() {
        showLog("会员列表")
        initLayui();
        initMemberCount();
        initMemberList(pageNo);
        initGradeList();
    }

    $scope.detail = function (id) {
        $state.go("app.memberDetail",{id:id});
    }
    function initGradeList() {
        var data = {};
        myHttp(
            api_get_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.gradeList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    function initMemberList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            nickname:$scope.member.nickname,
            status:$scope.member.status,
            vip_grade_id:$scope.member.vip_grade_id
        };
        myHttp(
            api_get_member_list,
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

    function initMemberCount() {
        var data = {
        };
        myHttp(
            api_get_member_count,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.totalMember = obj.data.totalMember;
                    $scope.yesterdayMember = obj.data.yesterdayMember;
                    $scope.todayMember = obj.data.todayMember;
                });
            }
        );
    }

    $scope.getMembers = function (status) {
        $scope.member.status = status;
        pageNo = 1;
        initMemberList(pageNo);
    }
    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(grade)', function(data){
                console.log("brand:"+data.value);
                $scope.member.vip_grade_id  = data.value;
            });
            form.on('submit(go)', function(data){
                pageNo = 1;
                initMemberList(pageNo);
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
                        initMemberList(pageNo);
                    }
                }
            });

        });
    }
}])
;