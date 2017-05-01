/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('AddCouponCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_add_normal_coupon= $scope.host+"coupon/addNormalCoupon";
    var api_add_auto_coupon= $scope.host+"coupon/addAutoCoupon";
    var type = 0;//0:先生成后认领,1:系统自动派送
    $scope.coupon = {
        title:null,
        issueCount : null,
        startTime :null,
        endTime:null,
        type:0,
        typeInfo:null,//免运费，也需要传递这个字段
        getType:0,
        des:null,

        //系统自动派送字段
        day:1,//过期时间
        expand : 1//每次获取数量
    }
    initData();
    function  initData() {
        showLog("创建优惠劵")
        initLayui();
        initView(0);
        renderLayuiForm();
    }

    $scope.tab = function (sort) {
        initView(sort);
    }
    function  initView(sort) {
        if (sort==0){//tab0，先生成后认领
            type = 0;
            $(".sysType").each(function(){
                $(this).hide();
            });
            $(".oneselfType").each(function(){
                $(this).show();
            });
        }
        else if(sort==1){//tab1,系统自动派送
            type = 1;
            $scope.coupon.getType = 2;
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
        else if(sort==2){//tab2，注册赠送
            type = 2;
            $scope.coupon.getType = 3;
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
    }
    function  addNormal() {
        showLog("==========================",$scope.coupon)
        var data = $scope.coupon;
        myHttp(
            api_add_normal_coupon,
            data,
            function (obj) {
                showMsg("添加认领劵成功！");
                $state.go("app.couponList");
            }
        );
    }
    function  addAuto() {
        var data = $scope.coupon;
        myHttp(
            api_add_auto_coupon,
            data,
            function (obj) {
                showMsg("添加自动派发劵成功！");
                $state.go("app.couponList");
            }
        );
    }
    function initTypeInfo() {
        if($scope.coupon.type==0){
            $scope.coupon.typeInfo = $("#typeInfo0").val();
        }
        else if($scope.coupon.type==1){
            $scope.coupon.typeInfo = $("#typeInfo1").val();
        }
        else if($scope.coupon.type==2){
            $scope.coupon.typeInfo = $("#typeInfo2").val()+"-"+$("#typeInfo3").val();
        }
        else{//免运费
            $scope.coupon.typeInfo = -1;
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
                $scope.coupon.getType= data.value;
            });
            form.on('submit(go)', function(data){
                var content = layedit.getContent(index);
                console.log(content);
                $scope.coupon.des = content;
                initTypeInfo();
                showLog($scope.coupon);
                if (type==0){
                    addNormal();
                }
                else{
                    addAuto();
                }
            });

            var laydate = layui.laydate;
            var start = {
                min: laydate.now()
                ,format: 'YYYY-MM-DD hh'
                ,istime: true
                ,max: '2099-06-16 23:59:59'
                ,istoday: false
                ,choose: function(datas){
                    $scope.coupon.startTime = datas;
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
                    $scope.coupon.endTime = datas;
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
}])
;