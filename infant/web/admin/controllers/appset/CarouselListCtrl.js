/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('CarouselListCtrl', ['ENV', '$scope', '$modal', '$state', function (ENV, $scope,$modal,$state) {
    $scope.host = ENV.api;
    var api_get_banner_list = $scope.host + "banner/getList";
    initData();
    function initData() {
        showLog("轮播管理")
        initBannerList();
    }

    function initBannerList() {
        var data = {pageNo:1,pageSize:100};
        myHttp(
            api_get_banner_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data.list;
                });
            }
        );
    }
    $scope.open = function (id,size) {
        $scope.item={
            id : id,
        }
        var modalInstance = $modal.open({
            templateUrl: 'modal_carousel.html',
            controller: 'ModalCarouselCtrl',
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
                if(id==null){
                    showMsg("新增轮播成功!");
                }
                else{
                    showMsg("更新轮播成功!");
                }
                initBannerList();
            }
        }, function (errorInfo) {//取消，对应$modalInstance.dismiss
        });
    };
}])
;

app.controller('ModalCarouselCtrl', ['ENV','$scope', '$modalInstance', 'items', function(ENV,$scope, $modalInstance, items) {
    $scope.host = ENV.api;
    var api_banner_add= ENV.api+"banner/add";
    var api_banner_update= ENV.api+"banner/update";
    var api_get_brands= $scope.host+"brand/getPutawayBrands";
    var api_get_banner_by_id = $scope.host+"banner/getById";
    var api_get_user_click_couponlist = $scope.host+"coupon/getUserClickCouponList";

    $scope.banner={
        id : items.id,
        name : null,
        img : null,
        sort : null,
        is_show : 1,
        type : 0,//跳转类型
        type_expand : null,//跳转指向
    }
    $scope.goodsTag = items;
    initData();
    function initData(){
        if($scope.banner.id == null){//添加
            $scope.title = "新增轮播"
        }
        else{
            $scope.title = "修改轮播"
            initBanner();
        }
        //initLayui();
        initBrands();
        initCouponList();
        //setTimeout("renderLayuiForm()",200);
    }
    //轮播
    function initBanner() {
        var data = {id:$scope.banner.id};
        myHttp(
            api_get_banner_by_id,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.banner = obj.data;
                });
                initView();
                renderLayuiForm();
            }
        );
    }
    function initView() {
        if($scope.banner.is_show==0){
            $("#statusHide").attr('checked','checked');//添加属性
        }
        if($scope.banner.type == 1){//优惠劵
            $("#type_1").attr('checked','checked');//添加属性
            $("#type1").addClass("hide");
            $("#type2").removeClass("hide");
            $("#type3").addClass("hide");
        }
        else if($scope.banner.type == 2){
            $("#type_2").attr('checked','checked');//添加属性
            $("#type1").removeClass("hide");
            $("#type2").addClass("hide");
            $("#type3").addClass("hide");
        }
        else if($scope.banner.type == 3){//品牌
            $("#type_3").attr('checked','checked');//添加属性
            $("#type1").addClass("hide");
            $("#type2").addClass("hide");
            $("#type3").removeClass("hide");
        }
    }
    /**
     * 给品牌默认值
     * @param pValue
     */
    function setBrandValue(pValue){
        var mySelect = document.getElementById("brand");
        var options = mySelect.getElementsByTagName('option');
        for(var i=0; i<options.length; i++){
            var value = options[i].value;
            // console.log("option:"+value);
            if(value == pValue) {
                options[i].selected = true;
            }
        }
        renderLayuiForm();
    }

    //品牌
    function  initBrands() {
        var data = {};
        myHttp(
            api_get_brands,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.brandList = obj.data;
                });
                //setBrandValue($scope.banner.type_expand);
                initLayui();
                renderLayuiForm();
            }
        );
    }
    //用户点击领取的优惠劵
    function  initCouponList() {
        var data = {};
        myHttp(
            api_get_user_click_couponlist,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.couponList = obj.data;
                });
                //setBrandValue($scope.banner.type_expand);
                initLayui();
                renderLayuiForm();
            }
        );
    }
    function  initLayui() {
        console.log("表单初始化！");
        //图片上传
        layui.use(['form','upload'], function(){
            var form = layui.form();
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
                $scope.banner.type_expand = data.value;
            });
            form.on('select(coupon)', function(data){
                console.log("coupon:"+data.value);
                $scope.banner.type_expand = data.value;
            });
            form.on('radio(filter1)', function(data){
                console.log(data.value); //被点击的radio的value值
                var type = data.value;
                $scope.banner.type = data.value;
                if(type==0||type==2){//单页或者文章
                    $("#type1").removeClass("hide");
                    $("#type2").addClass("hide");
                    $("#type3").addClass("hide");
                }
                else if(type == 1){//优惠劵
                    $("#type1").addClass("hide");
                    $("#type2").removeClass("hide");
                    $("#type3").addClass("hide");
                }else{//品牌
                    $("#type1").addClass("hide");
                    $("#type2").addClass("hide");
                    $("#type3").removeClass("hide");
                }

            });
            form.on('radio(filter2)', function(data){
                console.log(data.value); //被点击的radio的value值
                $scope.banner.is_show= data.value;
            });
            form.on('submit(go)', function(data){
                if($scope.banner.id == null){
                    add();
                }
                else{
                    update();
                }
            });

            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsTagImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        $scope.banner.img =  obj.data;
                    });
                }
            });
        });
    }

    function  add() {
        var data = $scope.banner;
        myHttp(
            api_banner_add,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }
    
    function  update() {
        var data = $scope.banner;
        myHttp(
            api_banner_update,
            data,
            function (obj) {
                $modalInstance.close(0);
            }
        );
    }
    $scope.ok = function () {
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };


}])
;