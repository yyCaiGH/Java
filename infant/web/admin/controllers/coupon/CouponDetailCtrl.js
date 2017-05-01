/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('CouponDetailCtrl', ['ENV','$scope','$state',"$stateParams","$modal",function(ENV,$scope, $state,$stateParams,$modal) {
    $scope.host = ENV.api;
    var api_get_coupon_list= $scope.host+"coupon/getCouponList";
    var api_get_coupon_by_id= $scope.host+"coupon/getCouponById";
    var api_update_normal_coupon= $scope.host+"coupon/updateNormalCoupon";
    var mTab = 0;
    var pageNo = 1;
    var totalPage = 1;
    var oldIssueCount;//原来的发行量
    var couponId = $stateParams.id;
    $scope.title="优惠券";
    $scope.couponSearch = {
        coupon_id : couponId,
        status : 1,
        allot : true
    }
    initData();
    function  initData() {
        showLog("优惠劵使用明细");
        initLayui();
        initCouponList(pageNo);
        getCouponById();
    }

    $scope.send = function (id,cdKey,title,size) {
        if($scope.coupon.overdue==1){//优惠券已过期，不能发配
            showMsg("优惠券已过期!");
            return;
        }
        $scope.items = {
            id:id,
            cdKey:cdKey,
            title:title
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_send_coupon.html',
            controller: 'ModalSendCouponCtrl',
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
                showMsg("发配成功!");
                initCouponList(pageNo);
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
    $scope.couponTab = function (tab) {
        $("#couponTab"+tab).show();
        $(".showTab").hide();
        $(".showTab").removeClass("showTab");
        $("#couponTab"+tab).addClass("showTab");
        totalPage = 1;
        pageNo = 1;
        mTab = tab;
        initPage(pageNo);
        if(tab==0){//已使用
            $scope.couponSearch.status = 1;
            $scope.couponSearch.allot = true;
            initCouponList(pageNo);
        }
        else if(tab==1){//未使用
            $scope.couponSearch.status = 0;
            $scope.couponSearch.allot = true;
            initCouponList(pageNo);
        }
        else if(tab==2){//未发配
            $scope.couponSearch.status = null;
            $scope.couponSearch.allot = false;
            initCouponList(pageNo);
        }
        else if(tab==3){//优惠券基础信息
            renderLayuiForm();
        }
    }

    function initCouponList(pageNo) {
        var data = {
            pageNo:pageNo,
            pageSize:ENV.pageSize,
            status:$scope.couponSearch.status,
            allot:$scope.couponSearch.allot,
            coupon_id : $scope.couponSearch.coupon_id
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

    function getCouponById() {
        var data = {
            id : $scope.couponSearch.coupon_id
        };
        myHttp(
            api_get_coupon_by_id,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.coupon = obj.data;
                    $scope.title = $scope.coupon.title;
                });
                oldIssueCount = $scope.coupon.issue_count;
                initView($scope.coupon.get_type)
            }
        );
    }

    function  initView(getType) {
        if (getType==0||getType==1){//先生成后认领
            $(".sysType").each(function(){
                $(this).hide();
            });
            $(".oneselfType").each(function(){
                $(this).show();
            });
        }
        else if(getType==2){//系统自动派送
            $(".sysType").each(function(){
                $(this).show();
            });
            $(".oneselfType").each(function(){
                $(this).hide();
            });
            $(".zcPs").each(function(){
                $(this).hide();
            });
        }
        else if(getType==3){//注册赠送
            $(".sysType").each(function(){
                $(this).show();
            });
            $(".oneselfType").each(function(){
                $(this).hide();
            });
            $(".autoPs").each(function(){
                $(this).hide();
            });
        }

        if($scope.coupon.type==0){
            $("#type0").attr("checked","checked");
            $("#typeInfo0").val($scope.coupon.type_info);
        }
        if($scope.coupon.type==1){
            $("#type1").attr("checked","checked");
            $("#typeInfo1").val($scope.coupon.type_info);
        }
        else if($scope.coupon.type==2){
            $("#type2").attr("checked","checked");
            var strs = $scope.coupon.type_info.split("-");
            $("#typeInfo2").val(strs[0]);
            $("#typeInfo3").val(strs[1]);
        }
        else if($scope.coupon.type==3){
            $("#type3").attr("checked","checked");
        }

        if($scope.coupon.get_type==1){
            $("#statusHide").attr("checked","checked");
        }
        initLayui();
    }


    function  updateNormal() {
        var data = {
            id:$scope.coupon.id,
            title:$scope.coupon.title,
            issueCount : $scope.coupon.issue_count,
            startTime :$scope.coupon.start_time,
            endTime:$scope.coupon.end_time,
            type:$scope.coupon.type,
            typeInfo:$scope.coupon.type_info,
            getType:$scope.coupon.get_type,
            des:$scope.coupon.des,
            oldIssueCount : oldIssueCount
        };
        myHttp(
            api_update_normal_coupon,
            data,
            function (obj) {
                showMsg("修改认领劵成功！");
                $state.go("app.couponList");
            }
        );
    }

    function initTypeInfo() {
        if($scope.coupon.type==0){
            $scope.coupon.type_info = $("#typeInfo0").val();
        }
        else if($scope.coupon.type==1){
            $scope.coupon.type_info = $("#typeInfo1").val();
        }
        else if($scope.coupon.type==2){
            $scope.coupon.type_info = $("#typeInfo2").val()+"-"+$("#typeInfo3").val();
        }
        else{
            $scope.coupon.type_info = null;
        }
    }

    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form','laydate', 'layedit'], function(){
            var layedit = layui.layedit
                ,$ = layui.jquery;
            //构建一个默认的编辑器
            var index = createLayerEditor(layedit);

            var form = layui.form();
            form.on('radio(filter1)', function(data){
                console.log(data.value); //被点击的radio的value值
                var type = data.value;
                $scope.coupon.type = data.value;
            });
            form.on('radio(filter2)', function(data){
                console.log(data.value); //被点击的radio的value值
                $scope.coupon.get_type= data.value;
            });
            form.on('submit(go)', function(data){
                var content = layedit.getContent(index);
                console.log(content);
                $scope.coupon.des = content;
                initTypeInfo();
                showLog($scope.coupon);
                if($scope.coupon.issue_count<oldIssueCount){
                    showMsg("优惠券发行数量不能低于之前的设置")
                    return;
                }
                updateNormal();
            });

            var laydate = layui.laydate;
            var start = {
                min: laydate.now()
                ,format: 'YYYY-MM-DD hh'
                ,istime: true
                ,max: '2099-06-16 23:59:59'
                ,istoday: false
                ,choose: function(datas){
                    $scope.coupon.start_time = datas;
                    end.min = datas; //开始日选好后，重置结束日的最小日期
                    end.start = datas //将结束日的初始值设定为开始日
                }
            };

            var end = {
                format: 'YYYY-MM-DD hh'
                ,istime: true
                ,max: '2099-06-16 23:59:59'
                ,istoday: false
                ,choose: function(datas){
                    $scope.coupon.end_time = datas;
                    start.max = datas; //结束日选好后，重置开始日的最大日期
                }
            };

            document.getElementById('LAY_demorange_s').onclick = function(){
                start.elem = this;
                laydate(start);
            }
            document.getElementById('LAY_demorange_e').onclick = function(){
                end.elem = this
                laydate(end);
            }
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

app.controller('ModalSendCouponCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    var host = ENV.api;
    var api_send_coupon_to_member= host+"coupon/sendCouponToMember";
    var api_get_member_list= host+"member/getMemberList";
    $scope.coupon = {
        id:items.id,
        cdKey:items.cdKey,
        title:items.title
    }
    $scope.member = {
        id:null,
        phone:null,
        nickname:null,
    }
    $scope.ok = function () {
        if(!isEmpty($scope.member.id)&&!isEmpty($scope.member.phone)&&!isEmpty($scope.member.nickname)){
            sendCoupon();
        }
        else{
            showMsg("信息填写完整")
        }
    };

    $scope.inputValueChange = function () {
        showLog("change======"+$scope.member.nickname);
        var searchStr = $scope.member.nickname;
        var memberInfo = searchStr.split("，");
        var index1 = searchStr.indexOf("编号：");
        var index2 = searchStr.indexOf("昵称：");
        var index3 = searchStr.indexOf("电话：");
        if(index1!=-1&&index2!=-1&&index3!=-1){
            $scope.member.id = memberInfo[0].substring(3,memberInfo[0].length);
            $scope.member.nickname = memberInfo[1].substring(3,memberInfo[1].length);
            $scope.member.phone = memberInfo[2].substring(3,memberInfo[2].length);
        }
        showLog($scope.member);
        initMemberList();
    }
    //联想搜索会员，每次只展示10条
    function initMemberList() {
        var data = {
            pageNo:1,
            pageSize:10,
            nickname:$scope.member.nickname,
            status:0,
        };
        myHttp(
            api_get_member_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.memberList = obj.data.list;
                });
            }
        );
    }
    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

    //发配优惠券
    function sendCoupon(skuName,skus){
        $.ajax({
            type:"post",
            url:api_send_coupon_to_member,
            data:{
                memberCouponId:$scope.coupon.id,
                memberId:$scope.member.id
            },
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $modalInstance.close(0);
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
}])
;