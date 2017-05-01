/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('BrandDetailCtrl', ['ENV','$scope','$state',"$stateParams",function(ENV,$scope, $state,$stateParams) {
    $scope.host = ENV.api;
    var api_get_goods_list= $scope.host+"goods/getList";
    var api_get_brand_by_id= $scope.host+"brand/getById";
    var api_get_country_list= $scope.host+"country/getList";
    var api_update_brand= $scope.host+"brand/update";
    var api_brand_sale_list= $scope.host+"yorder/getOrderGoodsList";
    var mTab = 0;
    var pageNo = 1;
    var totalPage = 1;
    var brandId = $stateParams.id;
    $scope.title="品牌";
    $scope.brand={
        id:brandId,
        name_en:"",
        name_zh:"",
        country_id:"",
        logo_big_url:"",
        logo_small_url:"",
        banner_url:"",
        about:"",
        introduce:"",
        status:1,
        recommend:false
    }
    $scope.goodsSearch = {
        putaway:1//是否上架（1、上架，2、下架）
    }
    var editor;
    initData();
    function  initData() {
        showLog("品牌使用明细:"+brandId);
        initLayui();
        initGoodsList();
        getBrandyId();
        editor = createEditor(ENV.wangEditorImgUploadApi);
    }
    $scope.editGoods = function (goodsId) {
        $state.go('app.editGoods',{goodsId:goodsId});
    }
    function getCountryList(){
        $.ajax({
            type:"post",
            url:api_get_country_list,
            data:{},
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    $scope.$apply(function () {
                        $scope.countryList = obj.data;
                    });
                    renderLayuiForm();
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

    $scope.couponTab = function (tab) {
        $("#couponTab"+tab).show();
        $(".showTab").hide();
        $(".showTab").removeClass("showTab");
        $("#couponTab"+tab).addClass("showTab");
        totalPage = 1;
        pageNo = 1;
        mTab = tab;
        initPage(pageNo);
        if(tab==0){//出售中的商品
            $scope.goodsSearch.putaway = 1;
            initGoodsList();
        }
        else if(tab==1){//下架中的商品
            $scope.goodsSearch.putaway = 2;
            initGoodsList();
        }
        else if(tab==2){//销售明细
            getSaleList();
        }
        else if(tab==3){//品牌信息
            initLayui();
        }
    }
    $scope.detail = function (id) {
        $state.go("app.orderDetail",{id:id});
    }
    //销售明细
    function getSaleList(){
        var data = {
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            brandId:brandId,
        };
        myHttp(
            api_brand_sale_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.saleList = obj.data.list;
                    totalPage = obj.data.totalPage;
                    initPage(pageNo);
                });
            }
        );
    }
    /**
     * 初始化化商品列表
     */
    function initGoodsList(){
        var data = {
            pageSize:ENV.pageSize,
            pageNo:pageNo,
            brand_id:brandId,
            putaway:$scope.goodsSearch.putaway
        };
        myHttp(
            api_get_goods_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.goodsList = obj.data.list;
                    totalPage = obj.data.totalPage;
                    initPage(pageNo);
                });
            }
        );
    }

    function getBrandyId() {
        var data = {
            brandId : brandId
        };
        myHttp(
            api_get_brand_by_id,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.brand = obj.data;
                    $scope.title = $scope.brand.name_zh;
                });
                getCountryList();
                initView();
            }
        );
    }

    function initView() {
        if($scope.brand.status==2){
            $("#putaway2").attr("checked","checked");
        }
        editor.$txt.append($scope.brand.introduce);
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
            form.on('checkbox(filter)', function(data){
                console.log(data.elem.checked);
                if (data.elem.checked){
                    $scope.brand.recommend =true;
                }
                else{
                    $scope.brand.recommend =false;
                }
            });
            form.on('radio(filter)', function(data){
                console.log(data.value); //被点击的radio的value值
                $scope.brand.status = data.value;
            });
            form.on('select', function(data){
                console.log(data.value); //得到被选中的值
                $scope.brand.country_id = data.value;
            });
            form.on('submit(go)', function(data){
               /* var content = layedit.getContent(index);
                console.log(content);*/
                var content = editor.$txt.html();
                $scope.brand.introduce = content;
                console.log($scope.brand);
                updateBrand($scope.brand);
            });
        });

        function  updateBrand(brand) {
            $.ajax({
                type:"post",
                url:api_update_brand,
                data:brand,
                dataType:"json",
                success: function (obj) {
                    console.log(obj);
                    if(obj.code==0){
                        showMsg("修改成功");
                        //$state.go("app.brands");
                        getBrandyId();
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
        //图片上传
        layui.use('upload', function(){
            //var  file =  layero.find('input[id="brandBigImg"]');
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#brandBigImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBigImg = $scope.host+obj.data;
                        $scope.brand.logo_big_url =  obj.data;
                        //$("#brandBigImg").val($scope.brand.logo_big_url)
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#brandSmallImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        // $scope.brandSmallImg = $scope.host+obj.data;
                        $scope.brand.logo_small_url =  obj.data;
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#brandBannerImg',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBannerImg = $scope.host+obj.data;
                        $scope.brand.banner_url =  obj.data;
                    });
                }
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
                        if(mTab==0||mTab==1){
                            initGoodsList();
                        }
                        else if(mTab==2){
                            getSaleList();
                        }
                    }
                }
            });

        });
    }
}])
;