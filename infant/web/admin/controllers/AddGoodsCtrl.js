/**
 * Created by Administrator on 2016/10/22 0022.
 * 添加商品
 */
app.controller('AddGoodsCtrl', ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {

    $scope.host = ENV.api;
    var api_get_goods_attr= $scope.host+"goods/getGoodsAttr";
    var api_get_class_list= $scope.host+"category/getList";
    var api_add_goods = $scope.host+"goods/add";

    $scope.goods={
        id:'',
        one_cat_id:"",//一级分类id
        tow_cat_id:"",//二级分类id
        brand_id:"",//品牌id
        main_number:"",//商品主编号
        name:"",//商品名称
        market_price:"",//零售价
        promote_price:"",//折扣价
        apply_sex:"",//适用性别
        apply_age:"",//适用年龄段
        apply_season:"",//适用季节
        texture:"",//材质
        img1_url:"",//
        img2_url:"",//
        img3_url:"",//
        img4_url:"",//
        img5_url:"",//
        tag_id:"",//
        production_area:"",//产地
        describe:"",//商品描述
        postage:0,//邮费（0、包邮，1...运费组id）
        packing_list:"",//包装清单
        after_sales:"",//售后服务
        putaway:1//是否上架（1、上架，2、下架）
    }
    $scope.selectAges = [];//选择年龄段
    $scope.selectSeasons = [];//选择适用季节

    $scope.skuSelectsObject = {};//被选中的sku集合对象
    //$scope.skuSelects = [];//被选中的sku集合数组（对skuSelectsObject的整理）
    $scope.skuParents = {};//sku父类集合
    var editor;
    initData();
    function initData(){
        console.log("初始化添加商品界面11！");
        initGoodsAttr();
        initLayui();
        editor = createEditor(ENV.wangEditorImgUploadApi);
    }
    function initGoodsAttr(){
        var data = {};
        myHttp(
            api_get_goods_attr,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.ageList = obj.data.ageList;
                    $scope.brandList = obj.data.brandList;
                    $scope.countryList = obj.data.countryList;
                    $scope.goodsTagList = obj.data.goodsTagList;
                    $scope.oneGoodsClassList = obj.data.oneGoodsClassList;
                    $scope.postageGroupList = obj.data.postageGroupList;
                    $scope.seasonList = obj.data.seasonList;
                    $scope.sexList = obj.data.sexList;
                    $scope.skuList = obj.data.skuList;
                    $scope.parentSkuList = obj.data.parentSkuList;

                });
                for(var i = 0 ;i<$scope.parentSkuList.length;i++){
                    $scope.skuParents[$scope.parentSkuList[i].id] = $scope.parentSkuList[i].name;
                }
                showLog("sku父级：",$scope.skuParents);
                renderLayuiForm();
            }
        );
    }
    
    function  initTowClassList(oneClassId) {
        var data = {parent_id : oneClassId};
        myHttp(
            api_get_class_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.towGoodsClassList = obj.data;
                });
                renderLayuiForm();
            }
        );
    }

    /**
     * 创建table头部的固定部分
     * @param newRow
     */
    function  createTitleTr(newRow) {
        var newCell2 = newRow.insertCell(); //创建新单元格
        newCell2.innerHTML = '<td class="col-lg-1">零售价</td>' ; //单元格内的内容

        var newCell3 = newRow.insertCell(); //创建新单元格
        newCell3.innerHTML = '<td class="col-lg-1">折扣价</td>' ; //单元格内的内容

        var newCell4 = newRow.insertCell(); //创建新单元格
        newCell4.innerHTML = '<td class="col-lg-1">库存</td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td class="col-lg-1">商家编号</td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td class="col-lg-1">是否上架</td>' ; //单元格内的内容
    }

    /**
     * 创建table身体的固定部分
     * @param newRow
     */
    function createBodyTr(newRow) {
        var newCell2 = newRow.insertCell(); //创建新单元格
        newCell2.innerHTML = '<td><input type="number" class="form-control table-input"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell3 = newRow.insertCell(); //创建新单元格
        newCell3.innerHTML = '<td><input type="number" class="form-control table-input"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell4 = newRow.insertCell(); //创建新单元格
        newCell4.innerHTML = '<td><input type="number" class="form-control table-input"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td><input type="text" class="form-control table-input"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td><input lay-filter="putaway" type="checkbox" name="r" title="上架" value="2"></td>' ; //单元格内的内容
    }
    function  initLayui() {
        console.log("表单初始化！");
        //表单提交
        layui.use(['form', 'layedit'], function(){
            var layedit = layui.layedit
                ,$ = layui.jquery;
            //构建一个默认的编辑器
            var index = layedit.build('LAY_demo1');
            var form = layui.form();
            form.on('checkbox(filter)', function(data){
                console.log(data.elem.checked); //是否被选中，true或者false
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                console.log(data.elem.title);
                console.log(data.elem.name);
                var value = data.value.split(":");
                var parent_id = value[0];
                var id = value[1];
                var saveValue = id+":"+data.elem.title;//将sku的id和名称放在一起存储，到时候再解析出来即可使用
                if (data.elem.checked){//选中
                    //data.elem.value = 0;//设置value的值
                   // $scope.skuSelects.push(data.elem.title);
                    if ($scope.skuSelectsObject[parent_id]==undefined){
                        if(Object.getOwnPropertyNames($scope.skuSelectsObject).length==3){
                            data.elem.checked = false;
                            //showMsg("sku受限三个");
                            renderLayuiForm();
                            return;
                        }
                        $scope.skuSelectsObject[parent_id] = [saveValue];
                    }
                    else{
                        $scope.skuSelectsObject[parent_id].push(saveValue);
                    }
                }
                else{
                   // $scope.skuSelects.splice($.inArray(data.elem.title, $scope.skuSelects), 1);
                    $scope.skuSelectsObject[parent_id].splice($.inArray(saveValue, $scope.skuSelectsObject[parent_id]), 1);
                    if($scope.skuSelectsObject[parent_id].length==0){//没有值就将这个属性删除
                        delete $scope.skuSelectsObject[parent_id];
                    }
                }
                var skuSelectsParent = [];//被选择的sku父级，与skuSelects一一对应
                var skuSelects=[];//被选中的sku集合数组（对$scope.skuSelectsObject的整理）
                //如果遍历map
                for(var prop in $scope.skuSelectsObject){
                    if($scope.skuSelectsObject.hasOwnProperty(prop)){
                        console.log('key is ' + prop +' and value is' + $scope.skuSelectsObject[prop]);
                        if ($scope.skuSelectsObject[prop].length>0){
                            skuSelectsParent.push($scope.skuParents[prop]);
                            skuSelects.push($scope.skuSelectsObject[prop]);
                        }
                    }
                }
                showLog("选择================",$scope.skuSelectsObject);
                showLog(skuSelectsParent);
                showLog(skuSelects);


                if(skuSelects.length==0){

                }
                else if(skuSelects.length==1){//一层sku
                    console.log("一种sku");
                    //创建table表的头部
                    var table = document.getElementById("myTable");
                    table.innerHTML="";//清空table
                    var newRow = table.insertRow(); //创建新行
                    newRow.setAttribute("class","success font-bold"); //tr风格

                    var newCell1 = newRow.insertCell(); //创建新单元格
                    newCell1.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[0]+'</td>' ; //单元格内的内容
                    createTitleTr(newRow);

                    //创建table表的内容
                    for(var i = 0 ;i<skuSelects[0].length ;i++){
                        var newRow1 = table.insertRow(); //创建新行
                        newRow1.setAttribute("class","bg-white myRow"); //tr风格

                        var newCell11 = newRow1.insertCell(); //创建新单元格
                        var sku = skuSelects[0][i].split(":");//数据格式：id：name，即sku的id和名称
                        newCell11.innerHTML = '<td>'+sku[1]+'<input type="hidden" value="'+sku[0]+'"><input type="hidden"><input type="hidden"></td>' ; //单元格内的内容
                        createBodyTr(newRow1);
                    }
                    renderLayuiForm();
                }
                else if(skuSelects.length==2){//俩层sku
                    console.log("两种sku");
                    //创建table表的头部
                    var table = document.getElementById("myTable");
                    table.innerHTML="";//清空table
                    var newRow = table.insertRow(); //创建新行
                    newRow.setAttribute("class","success font-bold"); //tr风格

                    var newCell1 = newRow.insertCell(); //创建新单元格
                    newCell1.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[0]+'</td>' ; //单元格内的内容
                    var newCell2 = newRow.insertCell(); //创建新单元格
                    newCell2.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[1]+'</td>' ; //单元格内的内容
                    createTitleTr(newRow);
                    //创建table表的内容
                    for(var i = 0 ;i<skuSelects[0].length ;i++){
                        for(var j = 0 ;j<skuSelects[1].length ;j++){
                            var newRow1 = table.insertRow(); //创建新行
                            newRow1.setAttribute("class","bg-white myRow"); //tr风格

                            var newCell11 = newRow1.insertCell(); //创建新单元格
                            var sku1 = skuSelects[0][i].split(":");//数据格式：id：name，即sku的id和名称
                            newCell11.innerHTML = '<td>'+sku1[1]+'<input type="hidden" value="'+sku1[0]+'"></td>' ; //单元格内的内容

                            var newCell12 = newRow1.insertCell(); //创建新单元格
                            var sku2 = skuSelects[1][j].split(":");//数据格式：id：name，即sku的id和名称
                            newCell12.innerHTML = '<td>'+sku2[1]+'<input type="hidden" value="'+sku2[0]+'"><input type="hidden"></td>' ; //单元格内的内容

                            createBodyTr(newRow1);
                        }
                    }
                    renderLayuiForm();
                }
                else if(skuSelects.length==3){//三层sku
                    //创建table表的头部
                    var table = document.getElementById("myTable");
                    table.innerHTML="";//清空table
                    var newRow = table.insertRow(); //创建新行
                    newRow.setAttribute("class","success font-bold"); //tr风格

                    var newCell1 = newRow.insertCell(); //创建新单元格
                    newCell1.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[0]+'</td>' ; //单元格内的内容
                    var newCell2 = newRow.insertCell(); //创建新单元格
                    newCell2.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[1]+'</td>' ; //单元格内的内容
                    var newCell3 = newRow.insertCell(); //创建新单元格
                    newCell3.innerHTML = '<td class="col-lg-1">'+skuSelectsParent[2]+'</td>' ; //单元格内的内容
                    createTitleTr(newRow);
                    //创建table表的内容
                    for(var i = 0 ;i<skuSelects[0].length ;i++){
                        for(var j = 0 ;j<skuSelects[1].length ;j++){
                            for(var k = 0 ;k<skuSelects[2].length ;k++){

                                var newRow1 = table.insertRow(); //创建新行
                                newRow1.setAttribute("class","bg-white myRow"); //tr风格

                                var newCell11 = newRow1.insertCell(); //创建新单元格
                                var sku1 = skuSelects[0][i].split(":");//数据格式：id：name，即sku的id和名称
                                newCell11.innerHTML = '<td>'+sku1[1]+'<input type="hidden" value="'+sku1[0]+'"></td>' ; //单元格内的内容

                                var newCell12 = newRow1.insertCell(); //创建新单元格
                                var sku2 = skuSelects[1][j].split(":");//数据格式：id：name，即sku的id和名称
                                newCell12.innerHTML = '<td>'+sku2[1]+'<input type="hidden" value="'+sku2[0]+'"></td>' ; //单元格内的内容

                                var newCell13= newRow1.insertCell(); //创建新单元格
                                var sku3 = skuSelects[2][k].split(":");//数据格式：id：name，即sku的id和名称
                                newCell13.innerHTML = '<td>'+sku3[1]+'<input type="hidden" value="'+sku3[0]+'"></td>' ; //单元格内的内容

                                createBodyTr(newRow1);

                            }
                        }
                    }
                    renderLayuiForm();
                }
            });
            form.on('checkbox(filter_age)', function(data){
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                console.log(data.elem.title);
                if (data.elem.checked) {//选中
                    $scope.selectAges.push(data.value);
                }
                else{
                    $scope.selectAges.splice($.inArray(data.value, $scope.selectAges), 1);
                }
                showLog($scope.selectAges);
            });
            form.on('checkbox(filter_season)', function(data){
                console.log(data.value); //复选框value值，也可以通过data.elem.value得到
                console.log(data.elem.title);
                if (data.elem.checked) {//选中
                    $scope.selectSeasons.push(data.value);
                }
                else{
                    $scope.selectSeasons.splice($.inArray(data.value, $scope.selectSeasons), 1);
                }
                showLog($scope.selectSeasons);
            });
            form.on('checkbox(putaway)', function(data){
                if (data.elem.checked){//选中
                    data.elem.value = 1;//设置为上架
                }
                else{
                    data.elem.value = 2;//设置为下架
                }
            });
            form.on('radio(filter)', function(data){
                console.log(data.value); //被点击的radio的value值
                if (data.value==0){//包邮
                    $scope.goods.postage = 0;
                }
                else{
                    $scope.goods.postage = -1;//可选择运费组
                }
            });
            form.on('radio(filter1)', function(data){//是否上架
                console.log(data.value);
                $scope.goods.putaway = data.value;
            });

            form.on('select(oneClass)', function(data){
                console.log("oneClass:"+data.value);
                $scope.goods.one_cat_id = data.value;
                $scope.$apply(function () {
                    $scope.towGoodsClassList = null;
                });
                initTowClassList($scope.goods.one_cat_id);
            });
            form.on('select(towClass)', function(data){
                console.log("towClass:"+data.value);
                $scope.goods.tow_cat_id = data.value;
            });
            form.on('select(brand)', function(data){
                console.log("brand:"+data.value);
                $scope.goods.brand_id = data.value;
            });
            form.on('select(sex)', function(data){
                console.log("sex:"+data.value);
                $scope.goods.apply_sex = data.value;
            });
            form.on('select(age)', function(data){
                console.log("age:"+data.value);
                $scope.goods.apply_age = data.value;
            });
            form.on('select(season)', function(data){
                console.log("season:"+data.value);
                $scope.goods.apply_season = data.value;
            });
            form.on('select(country)', function(data){
                console.log("country:"+data.value);
                $scope.goods.production_area = data.value;
            });
            form.on('select(goodsTag)', function(data){
                console.log("goodsTag:"+data.value);
                $scope.goods.tag_id = data.value;
            });
            form.on('select(postageGroup)', function(data){
                console.log("postageGroup:"+data.value);
                if($scope.goods.postage==-1){//非包邮
                    $scope.goods.postage = data.value;
                }
            });
            form.on('submit(go)', function(data){
                /*var content = layedit.getContent(index);
                console.log(content);*/
                var content = editor.$txt.html();
                $scope.goods.describe = content;
                submitGoods();
                //addBrand($scope.brand);
            });
            //form.render(); //更新全部
        });

        //图片上传
        layui.use('upload', function(){
            //var  file =  layero.find('input[id="brandBigImg"]');
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsImg1',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBigImg = $scope.host+obj.data;
                        $scope.goods.img1_url =  obj.data;
                        //$("#brandBigImg").val($scope.brand.logo_big_url)
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsImg2',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        $scope.goods.img2_url =  obj.data;
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsImg3',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBannerImg = $scope.host+obj.data;
                        $scope.goods.img3_url =  obj.data;
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsImg4',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBannerImg = $scope.host+obj.data;
                        $scope.goods.img4_url =  obj.data;
                    });
                }
            });
            layui.upload({
                url: ENV.imgUploadApi, //上传接口
                elem: '#goodsImg5',
                success: function(obj){ //上传成功后的回调
                    console.log(obj)
                    $scope.$apply(function () {
                        //$scope.brandBannerImg = $scope.host+obj.data;
                        $scope.goods.img5_url =  obj.data;
                    });
                }
            });
        });
    }

    $scope.addInput = function(){
        $('#inputList').append('<input type="text" class="form-control add-body-input" style="margin-top: 5px">');
    }


    function submitGoods(){
        var goodsSkuList = [];//商品sku属性
        var trs = document.getElementsByClassName('myRow');//tr
        showLog("打印table的值:"+trs.length);
        for (var j = 0; j<trs.length;j++){
            var trInputs = trs[j].getElementsByTagName('input');
            showLog("input.length:"+trInputs.length);
            var goodsSku={
                goods_id:"",//商品id,到后台等商品添加后，再加上
                sku1_id:"",
                sku2_id:"",
                sku3_id:"",
                market_price:"",//零售价
                promote_price:"",//折扣价
                repertory:"",//库存
                goods_number:"",//商品编号
                putaway:""//是否上架（1、上架，2、下架）
            }
            goodsSku.sku1_id = trInputs[0].value;
            goodsSku.sku2_id = trInputs[1].value;
            goodsSku.sku3_id = trInputs[2].value;
            goodsSku.market_price = trInputs[3].value;
            goodsSku.promote_price = trInputs[4].value;
            goodsSku.repertory = trInputs[5].value;
            goodsSku.goods_number = trInputs[6].value;
            goodsSku.putaway = trInputs[7].value;
            goodsSkuList.push(goodsSku);
        }
        showLog("goodsSku：",goodsSkuList);

        var goodsSellPoint = [];//商品卖点
        var inputList = document.getElementById('inputList');
        var inputs = inputList.getElementsByTagName('input');

        for(var i = 0; i < inputs.length; i++) {
            var str = inputs[i].value;
            if(!isEmpty(str)){
                goodsSellPoint.push({sell_point:str});
            }
        }
        console.log(goodsSellPoint);
        if(goodsSkuList.length==0){
            showMsg("请设置至少一个sku商品")
            return;
        }

        var age = "";
        for(var i = 0;i<$scope.selectAges.length;i++){
            age = age+$scope.selectAges[i]+";";
        }
        var seasons = "";
        for(var i = 0;i<$scope.selectSeasons.length;i++){
            seasons = seasons+$scope.selectSeasons[i]+";";
        }
        $scope.goods.apply_age = age;
        $scope.goods.apply_season = seasons;
        if(isEmpty($scope.goods.promote_price)){
            $scope.goods.promote_price = $scope.goods.market_price;
        }
        console.log($scope.goods);
        addGoods(JSON.stringify($scope.goods),JSON.stringify(goodsSellPoint),JSON.stringify(goodsSkuList))
    }
    function  addGoods(goods,goodsSellPoint,goodsSkuList) {
        var data = {goods:goods,goodsSellPoint:goodsSellPoint,goodsSkuList:goodsSkuList};
        myHttp(
            api_add_goods,
            data,
            function (obj) {
                showMsg("添加成功");
                $state.go("app.goods",{putaway:$scope.goods.putaway});
            }
        );
    }

}])
;