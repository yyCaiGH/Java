/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('CouponListCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_coupon_list= $scope.host+"coupon/getList";
    var pageNo = 1;
    var totalPage = 1;
    $scope.coupon = {
        get_type : 0,
        cdkey : null
    }
    initData();
    function  initData() {
        showLog("优惠劵列表")
        initLayui();
        changeTab( $scope.coupon.get_type);
        initCouponList(pageNo);
        initPage(pageNo);
    }
    function initCouponList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            get_type:$scope.coupon.get_type
        };
        myHttp(
            api_get_coupon_list,
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

    $scope.detail = function (id) {
        $state.go("app.couponDetail",{id:id});
    }
    $scope.couponTab  = function (getType) {
        $scope.coupon.get_type = getType;
        changeTab(getType);
        pageNo = 1;
        initCouponList(pageNo);
    }
    function changeTab(getType) {
        if (getType==2){//系统自动派送
            $("#normalCouponTable").hide();
            $("#autoCouponTable").show();
            $("#zcautoCouponTable").hide();
        }
        else if (getType==3){
            $("#normalCouponTable").hide();
            $("#autoCouponTable").hide();
            $("#zcautoCouponTable").show();
        }
        else{//先生成后认领的优惠券
            $("#normalCouponTable").show();
            $("#autoCouponTable").hide();
            $("#zcautoCouponTable").hide();
        }
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
                        initCouponList(pageNo);
                    }
                }
            });

        });
    }
}])
;