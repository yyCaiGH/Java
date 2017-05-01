/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('AddOrderCtrl', ['ENV','$scope','$state','$modal',function(ENV,$scope, $state,$modal) {
    $scope.host = ENV.api;
    var api_get_addrs= $scope.host+"member/getAddrs";
    var api_get_area= $scope.host+"config/getArea";
    var api_get_postage_group= $scope.host+"postage/getList";
    var api_get_postage_tpl= $scope.host+"postageTpl/getList";
    var api_get_postage_privince= $scope.host+"postageTpl/getPostageProvinceList";
    var api_get_order_origin= $scope.host+"config/getTypeList";
    var api_get_member_list= $scope.host+"member/getMemberList";
    var api_get_member_by_id = $scope.host+"member/getMemberDesById";
    var api_submit_order = $scope.host+"yorder/submitOrder";
    $scope.sukGoodsSelected = [];//处理过的数据（选择的商品）
    var original  = [];//原始数据（选择的商品）

    var pg_id = null;
    var postagePrivinceList = null;
    $scope.member = {
        id : null,
        nickname: null,//包括会员编号，手机号码，昵称
        discount:1,//会员折扣
    }

    $scope.order = {
        member_id : $scope.member.id,
        member_addr_id : "",//选择预留地址
        count:0,//购买商品总数
        price:0,//订单总价(各个商品的折扣价*数量总和)（无优惠）（折扣价即出售价）
        real_price:0,//实收总价（每个商品的实收价之和加上邮费）(总计)
        vip_discounts:1,//会员折扣
        delivery_type:1,//配送方式（0：快递，1：自取）
        coupon_amount:"",//优惠总金额
        postage_price:0,//邮费
        pay_type:"",//付款方式，1支付宝，2微信
        type:1,//订单类型（1、手动建单，2、自主下单(app才有)）
        from:null,//订单来源（1、ios订单，2、安卓订单，3、微信，4微博）
        expressage:"",//选择快递,暂时存运费模板名称
        contact:"",//收货人
        phone:"",//收货人电话号码
        province:"",//自己选的省
        city:"",//自己选的市
        area:"",//自己选的区
        address:"",//自己填写的详细地址
        leave_word:"",//订单备注，买家留言
    }
    $scope.orderExtra = {
        real_price : 0,//商品总实收（每个商品的实收价之和）
        freightType:0//0:包邮，1:选择运费模板
    }
    $scope.skuGoods ={
        name:null,//商品名称
        name_zh:null,//品牌名称
        sku_info:null,//sku信息
        count:0,//购买数量
        promote_price:null,//单价统一用折扣价,即出售价
        vip_discounts:$scope.order.vip_discounts,//会员折扣
        coupon_amount:null,//单个商品的优惠金额
        real_price:0,//单个商品的实收价（单件商品经由会员打折和优惠金额减掉后的实际付款总额）
    }
    initData();
    $scope.addressDes={
        province : null,
        city : null,
        area : null,
        address : null,
    }
    function  initData() {
        showLog("手动新增订单")
        initLayui();
        getPostageGroup();
        initOrderOrigin();
    }

    $scope.inputValueChange = function () {
        showLog("change======"+$scope.member.nickname);
        var searchStr = $scope.member.nickname;
        var index1 = searchStr.indexOf("编号：");
        var index2 = searchStr.indexOf("，");
        showLog("index1="+index1+",index2="+index2);
        if(index1!=-1&&index2!=-1){
            $scope.member.id = searchStr.substring(index1+3,index2);
            getMemberbyId();
            showLog("memberId="+$scope.member.id);
            getAddrs();
            getArea("100000",1);
        }
        initMemberList();
    }
    $scope.inputValueChange1=function () {
        computePrice();
    }
    //计算总实收
    function computePrice() {
        $scope.orderExtra.real_price = 0;
        $scope.order.count = 0;
        $scope.order.price = 0;
        $scope.order.coupon_amount = 0;
        for(var i = 0;i<$scope.sukGoodsSelected.length;i++){
            $scope.order.coupon_amount +=$scope.sukGoodsSelected[i].coupon_amount;
            $scope.sukGoodsSelected[i].real_price =  $scope.sukGoodsSelected[i].count*$scope.sukGoodsSelected[i].promote_price*$scope.member.discount-$scope.sukGoodsSelected[i].coupon_amount;
            $scope.orderExtra.real_price = $scope.orderExtra.real_price+$scope.sukGoodsSelected[i].real_price;
            showLog("商品库存："+$scope.sukGoodsSelected[i].repertory);
            if($scope.sukGoodsSelected[i].count<1){
                showMsg("商品数量不能低于1");
                $scope.sukGoodsSelected[i].count = 1;
            }
            else if ($scope.sukGoodsSelected[i].count>$scope.sukGoodsSelected[i].repertory){
                showMsg("该商品库存仅剩"+$scope.sukGoodsSelected[i].repertory+",请重新输入");
                $scope.sukGoodsSelected[i].count = 1;
            }
            else{
                $scope.order.count += $scope.sukGoodsSelected[i].count;
            }
            $scope.order.price += $scope.sukGoodsSelected[i].count*$scope.sukGoodsSelected[i].promote_price;
        }
        $scope.order.real_price = $scope.orderExtra.real_price + $scope.order.postage_price;
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
    function getMemberbyId() {
        var data = {
            id:$scope.member.id,
        };
        myHttp(
            api_get_member_by_id,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.member = obj.data;
                });
            }
        );
    }
    function initOrderOrigin() {
        var data={id:2};
        myHttp(
            api_get_order_origin,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.orderOriginList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    function getAddrs() {
        var data = {
            id:$scope.member.id
        };
        myHttp(
            api_get_addrs,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.addressList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    function getPostageGroup() {
        var data = {};
        myHttp(
            api_get_postage_group,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.postageGroupList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    function getPostageTpl() {
        var data = {pg_id:pg_id};
        myHttp(
            api_get_postage_tpl,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.postageTplList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }
    function getPostagePrivince(tplId) {
        var data = {id:tplId};
        myHttp(
            api_get_postage_privince,
            data,
            function (obj) {
                postagePrivinceList = obj.data;
                getOrderPostage(true);
            }
        );
    }
    //得到订单邮费
    function getOrderPostage(check) {
        if (check&&$scope.order.province==null){
            showMsg("请先选择省市区地址");
            $scope.$apply(function () {
                $scope.postageTplList = null;
                $scope.order.expressage = null;
            });
            getPostageTpl();
            return;
        }
        if(postagePrivinceList==null){
            return;
        }
        var is = true;
        for(var i = 0;i<postagePrivinceList.length;i++){
            //showLog("运费："+postagePrivinceList[i].postage );
            //showLog(postagePrivinceList[i].province_name+"=="+$scope.order.province)
            if($scope.order.province.indexOf(postagePrivinceList[i].province_name)!=-1){
                $scope.$apply(function () {
                    $scope.order.postage_price = postagePrivinceList[i].postage;
                });
                is = false;
                showLog("运费："+$scope.order.postage_price );
                break;
            }
        }
        if (is){
            $scope.order.postage_price = 0;
        }
        renderLayuiForm();
    }
    /**
     * 获取省市区
     * @param parentCode
     * @param type 1:省，2：市，3：区
     */
    function getArea(parentCode,type) {
        var data = {
            parentCode:parentCode
        };
        myHttp(
            api_get_area,
            data,
            function (obj) {
                $scope.$apply(function () {
                    if(type==1){
                        $scope.List1 = obj.data;
                    }
                    else if(type==2){
                        $scope.List2 = obj.data;
                    }
                    else if(type==3){
                        $scope.List3 = obj.data;
                    }

                });
                renderLayuiForm();
            }
        );
    }

    function  initLayui() {
        showLog("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('radio(filter0)', function(data){
                console.log("radio:"+data.value);
                $scope.order.delivery_type = data.value;
                if(data.value==0){
                    if($scope.member.id==null){
                        showMsg("请选择会员")
                        return;
                    }
                    getAddrs();
                    getArea("100000",1);
                }
            });
            form.on('radio(filter1)', function(data){
                console.log("radio:"+data.value);
                //选择运费模板
                $scope.orderExtra.freightType = data.value;
            });
            form.on('select(address)', function(data){
                showLog(data.value);
                var addr = data.value.split(",");
                $scope.$apply(function () {

                    $scope.List1 = null;
                    $scope.List2 = null;
                    $scope.List3 = null;

                    $scope.order.province = addr[0];
                    $scope.order.city = addr[1];
                    $scope.order.area = addr[2];
                    $scope.order.address = addr[3];
                });
                $scope.order.member_addr_id = addr[4];
                getArea("100000",1);
                getOrderPostage(false);
            });
            form.on('select(area1)', function(data){
                $scope.$apply(function () {
                    $scope.order.province = null;
                    $scope.order.city = null;
                    $scope.order.area = null;
                    $scope.order.address = null;
                    $scope.List2 = null;
                    $scope.List3 = null;
                    $scope.addressList = null;
                });
                var values = data.value.split(",");
                $scope.order.province = values[1];
                getAddrs();
                getArea(values[0],2);
                getOrderPostage(false);
            });
            form.on('select(area2)', function(data){
                var values = data.value.split(",");
                $scope.order.city = values[1];
                $scope.List3 = null;
                getArea(values[0],3);
            });
            form.on('select(area3)', function(data){
                var values = data.value.split(",");
                $scope.order.area = values[1];
            });
            form.on('select(orderOrigin)', function(data){
                $scope.order.from = data.value;
            });
            form.on('select(payType)', function(data){
                $scope.order.pay_type = data.value;
            });
            form.on('select(postageGroupId)', function(data){
                $scope.$apply(function () {
                    $scope.postageTplList = null;
                });
                pg_id = data.value;
                $scope.order.expressage = null;
                getPostageTpl();
            });
            form.on('select(postageTplId)', function(data){
                var tpl = data.value.split(",")
                getPostagePrivince(tpl[0]);
                $scope.order.expressage = tpl[1];
                computePrice();
            });
            form.on('submit(go1)', function(data){
                if($scope.order.delivery_type==0){//快递
                    if(isEmpty($scope.order.province)||isEmpty($scope.order.city)||isEmpty($scope.order.area)||isEmpty($scope.order.address)){
                        showMsg("地址信息不完整");
                        return;
                    }
                }
                if($scope.orderExtra.freightType==1){//选择运费模板
                    if(pg_id==null||$scope.order.expressage==null){
                        showMsg("请选择运费模板");
                        return;
                    }
                }
                if($scope.orderExtra.real_price<=0||$scope.order.real_price<=0){
                    showMsg("请选择正确的商品和数量");
                    return;
                }
                $scope.order.member_id = $scope.member.id;
                $scope.order.vip_discounts = $scope.member.discount;
                showLog($scope.order,$scope.sukGoodsSelected);
                submitOrder(JSON.stringify($scope.order),JSON.stringify($scope.sukGoodsSelected));
            });
        });
    }
    function submitOrder(order,skuGoodsList) {
        var data = {order:order,skuGoodsList:skuGoodsList};
        myHttp(
            api_submit_order,
            data,
            function (obj) {
                showMsg("添加成功");
                $state.go("app.order");
            }
        );
    }
    $scope.open = function (size) {
        var modalInstance = $modal.open({
            templateUrl: 'model_repertory_purchase_select.html',
            controller: 'RepertoryPurchaseSelectCtrl',
            size: size,
            resolve: {
                items: function () {
                    return original;
                }
            }
        });

        modalInstance.result.then(function (sucData) {//确认，对应$modalInstance.close
            showLog("已选择：",sucData);
            initSelectedDatas(sucData);
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };

    function initSelectedDatas(sucData) {
        original  = sucData;
        $scope.sukGoodsSelected = [];
        for(var i = 0;i<sucData.length;i++){
            var datas = sucData[i].split("|*|");
            var skuGoods ={
                id:datas[0],
                img1_url : datas[1],
                name_zh : datas[2],
                name :datas[3],
                promote_price :datas[4],
                market_price : datas[5],
                goods_number : datas[6],
                sku_info :datas[7],
                repertory : datas[8],
                count:1,
                coupon_amount:0,
                real_price:0
            }
            $scope.sukGoodsSelected.push(skuGoods);
        }
        initLayui();
        computePrice();
    }

    $scope.delete = function (id) {
        for(var i = 0; i<$scope.sukGoodsSelected.length;i++){
            var datas = original[i].split("|*|");
            if(datas[0]==id){
                original.splice($.inArray(original[i], original), 1);
            }
            if($scope.sukGoodsSelected[i].id==id){
                $scope.sukGoodsSelected.splice($.inArray($scope.sukGoodsSelected[i], $scope.sukGoodsSelected), 1);
                break;
            }
        }
        computePrice();
    }

}])
;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 选择入库商品
 */
app.controller('RepertoryPurchaseSelectCtrl', ['ENV','$scope','$modalInstance','$state','items',function(ENV,$scope,$modalInstance, $state,items) {
    $scope.host = ENV.api;
    var api_get_repertory_list = $scope.host+"repertory/getList"
    var api_get_search_info= $scope.host+"brand/getPutawayBrands";
    $scope.repertoryParams = {
        totalPage:1,
        pageSize : ENV.pageSize,
        pageNo : 1,
        putaway : 1,
        brandsId : "",
        minRepertory : "",
        maxRepertory : "",
        goodsName : "",
        goodsNumber : ""
    }
    var selectedSkuGoods = [] ;
    initData();
    function  initData() {
        showLog("选择入库商品");
        selectedSkuGoods = items;
        initLayui();
        initSearchInfo();
        initSkuGoodsList();
        //initPage(1);
    }
    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
                $scope.repertoryParams.brandsId = data.value;
            });
            form.on('checkbox(select)', function(data){
                console.log(data.elem.checked); //是否被选中，true或者false
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                selectSkuGodos(data.elem.checked,data.value);
            });
            form.on('submit(go)', function(data){
                 $scope.repertoryParams.pageNo = 1;//页面重置为1
                 initSkuGoodsList();
            });
        });
    }

    //选择商品
    function selectSkuGodos(checked,value) {
        if(checked){//选择，存储起来
            if ($.inArray(value, selectedSkuGoods)==-1){
                selectedSkuGoods.push(value);
            }
        }
        else{//不选择，从存储中去除
            selectedSkuGoods.splice($.inArray(value, selectedSkuGoods), 1);
        }
        showLog(selectedSkuGoods);
    }
    //初始化选择数据
    function initSelectDate() {
        $(".myChecked").each(function(){//复选框初始化
            $(this).removeAttr("checked");
        });
        for(var i = 0;i<selectedSkuGoods.length;i++){
            var id = selectedSkuGoods[i].split("|*|")[0];
            $("#myChecked"+id).prop("checked","true");
        }
        renderLayuiForm();
    }
    function  initPage(currPage) {
        layui.use(['laypage'], function(){
            var laypage = layui.laypage;
            laypage({
                cont: 'layuiPage'
                ,pages: $scope.repertoryParams.totalPage //总页数
                ,curr:currPage
                ,jump: function(obj, first){
                    console.log(obj.curr);
                    if(!first){
                        $scope.repertoryParams.pageNo = obj.curr;
                        initSkuGoodsList();
                    }
                }
            });

        });
    }

    function  initSearchInfo() {
        var data = {};
        myHttp(
            api_get_search_info,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.brandList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    /**
     * 初始化化商品属性
     */
    function initSkuGoodsList(){
        var data = $scope.repertoryParams;
        myHttp(
            api_get_repertory_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.skuGoodsList = obj.data.list;
                    $scope.repertoryParams.totalPage = obj.data.totalPage;
                    initPage($scope.repertoryParams.pageNo);
                });
                renderLayuiForm();
                initSelectDate();
            }
        );
    }

    $scope.ok = function () {
        $modalInstance.close(selectedSkuGoods);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}])
;