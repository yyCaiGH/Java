/**
 * Created by Administrator on 2016/10/22 0022.
 * 品牌管理
 */
app.controller('AddBrandCtrl', ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {

    $scope.host = ENV.api;
    var api_add_brand= $scope.host+"brand/add";
    var api_get_country_list= $scope.host+"country/getList";
    $scope.brand={
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
    $scope.brandBigImg="";
    var editor;
    initData();
    function initData(){
        console.log("初始化添加品牌界面！");
        getCountryList();
        initLayui();
        editor = createEditor(ENV.wangEditorImgUploadApi);
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

    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form', 'layedit'], function(){
            var layedit = layui.layedit
                ,$ = layui.jquery;
            //构建一个默认的编辑器
            var index = layedit.build('LAY_demo1');
            showLog("图片上传接口："+ENV.layeditImgUploadApi);
            layedit.set({
                uploadImage: {
                    url: ENV.layeditImgUploadApi //接口url
                }
            })
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
                /*var content = layedit.getContent(index);
                console.log(content);*/
                // 获取编辑器区域完整html代码
                var content = editor.$txt.html();
                $scope.brand.introduce = content;
                console.log($scope.brand);
                addBrand($scope.brand);
            });
        });

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
    function  addBrand(brand) {
        $.ajax({
            type:"post",
            url:api_add_brand,
            data:brand,
            dataType:"json",
            success: function (obj) {
                console.log(obj);
                if(obj.code==0){
                    showMsg("添加成功");
                    $state.go("app.brands");
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

