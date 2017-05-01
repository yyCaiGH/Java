/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('MemberDetailCtrl', ['ENV','$scope','$state',"$stateParams",function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    var api_get_articles_by_member= $scope.host+"article/getArticlesByMember";//获取会员文章列表
    var api_get_member_info= $scope.host+"member/memberDetail";//获取用户信息,孩子列表，地址列表
    var api_get_concern_list= $scope.host+"member/concern";//获取关注、被关注列表
    var api_get_collect_list= $scope.host+"member/getCollectList";//收藏列表
    var api_get_order_list= $scope.host+"yorder/getPageList";
    var api_get_cart_list= $scope.host+"order/getCart";
    var api_member_update_status= $scope.host+"member/updateStatus";
    var mTab = 0;
    var pageNo = 1;
    var totalPage = 1;
    var memberId = $stateParams.id;
    $scope.title="title";
    initData();
    function  initData() {
        showLog("会员详情");
        initMemberDetail();
    }
    
    $scope.memberTab = function (tab) {
        $("#memberTab"+tab).show();
        $(".showTab").hide();
        $(".showTab").removeClass("showTab");
        $("#memberTab"+tab).addClass("showTab");
        totalPage = 1;
        pageNo = 1;
        mTab = tab;
        initPage(pageNo);
        if(tab==0){//会员基本信息

        }
        else if(tab==1){//购买记录
            getOrderList(null);
        }
        else if(tab==2){//退货记录
            getOrderList(6);
        }
        else if(tab==3){//会员文章
            initMemberArticleList(pageNo);
        }
        else if(tab==4){//关注的朋友
            initCouponList(pageNo,true);
        }
        else if(tab==5){//粉丝
            initCouponList(pageNo,false);
        }
        else if(tab==6){//收藏的商品
            initCollectList(pageNo);
        }
        else if(tab==7){//购物车
            getCartList();
        }

    }
    $scope.blackMember = function () {
        var index = layer.confirm('确定拉黑该会员吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            updateStatus(1);
            layer.close(index);
        },function(){
        });
    }
    
    $scope.noBlackMember = function () {
        var index = layer.confirm('确定取消拉黑该会员吗？', {
            title:"提示",
            btn: ['确定','取消'] //按钮
        }, function(){
            updateStatus(0);
            layer.close(index);
        },function(){
        });
    }
    $scope.detail = function (id) {
        $state.go("app.orderDetail",{id:id});
    }

    function updateStatus(status) {
        var data={
            status : status,
            id : memberId
        };
        myHttp(
            api_member_update_status,
            data,
            function (obj) {
                initMemberDetail();
            }
        );
    }

    function getOrderList(status) {
        var data={
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            status : status,
            member_id : memberId
        };
        myHttp(
            api_get_order_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.orderList = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
                renderLayuiForm();
            }
        );
    }

    function getCartList() {
        var data={
            memberId : memberId
        };
        myHttp(
            api_get_cart_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.cartList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    //收藏列表
    function initCollectList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            memberId:memberId,
        };
        myHttp(
            api_get_collect_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.collectlist = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
            }
        );
    }

    //关注，被关注
    function initCouponList(pageNo,is) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            memberId:memberId,
            is:is
        };
        myHttp(
            api_get_concern_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.couponlist = obj.data.list;
                    totalPage = obj.data.totalPage;
                });
                initPage(pageNo);
            }
        );
    }

    function initMemberDetail() {
        var data = {
            id:memberId
        };
        myHttp(
            api_get_member_info,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.member = obj.data.member;
                    $scope.childrenList = obj.data.childrenList;
                    $scope.addressList = obj.data.addressList;
                });
            }
        );
    }
    function initMemberArticleList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            memberId:memberId
        };
        myHttp(
            api_get_articles_by_member,
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
                        if(mTab==1){
                            getOrderList(null);
                        }
                        else if(mTab ==2){
                            getOrderList(6);
                        }
                        else if(mTab ==3){
                            initMemberArticleList(pageNo);
                        }
                        else if(mTab ==4){
                            initCouponList(pageNo,true);
                        }
                        else if(mTab ==5){
                            initCouponList(pageNo,false);
                        }
                        else if(mTab ==6){
                            initCollectList(pageNo);
                        }
                        else if(mTab ==7){
                            getCartList();
                        }
                    }
                }
            });

        });
    }
}])
;