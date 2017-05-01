/**
 * Created by Administrator on 2016/12/4 0004.
 */
app.controller('MemberGradeCtrl', ['ENV','$scope','$state',function(ENV,$scope, $state) {
    $scope.host = ENV.api;
    var api_get_list= $scope.host+"vip/getList";
    var api_grade_add= $scope.host+"vip/add";
    initData();
    function  initData() {
        showLog("会员等级设置");
        initLayui();
        initGradeList();
    }

    function initGradeList() {
        var data = {};
        myHttp(
            api_get_list,
            data,
            function (obj) {
                $scope.$apply(function () {
                    $scope.list = obj.data;
                });
            }
        );
    }
    function submitVip(){
        var list = [];//商品sku属性
        var trs = document.getElementsByClassName('myRow');//tr
        showLog("打印table的值:"+trs.length);
        for (var j = 0; j<trs.length;j++){
            var trInputs = trs[j].getElementsByTagName('input');
            showLog("input.length:"+trInputs.length);
            var vipGrade={
                id:"",
                name:"",//商品id,到后台等商品添加后，再加上
                amount:"",
                discount:"",
                rebate:"",
            }
            vipGrade.id = trInputs[0].value;
            vipGrade.name = trInputs[1].value;
            vipGrade.amount = trInputs[2].value;
            vipGrade.discount = trInputs[3].value;
            vipGrade.rebate = trInputs[4].value;
            list.push(vipGrade);
        }
        showLog("list：",list);
        addVipList(JSON.stringify(list))
    }
    function  addVipList(list) {
        var data = {list:list};
        myHttp(
            api_grade_add,
            data,
            function (obj) {
                showMsg("保存成功");
            }
        );
    }

    $scope.addTr = function () {
        //创建table表的头部
        var table = document.getElementById("myTable");
        var newRow = table.insertRow(); //创建新行
        newRow.setAttribute("class","bg-white myRow"); //tr风格
        createBodyTr(newRow);
    }
    /**
     * 创建table身体的固定部分
     * @param newRow
     */
    function createBodyTr(newRow) {
        var newCell2 = newRow.insertCell(); //创建新单元格
        newCell2.innerHTML = '<input type="hidden" class="form-control table-input-50"><td><input type="text" class="form-control table-input-50"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell3 = newRow.insertCell(); //创建新单元格
        newCell3.innerHTML = '<td><input type="number" class="form-control table-input-50"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell4 = newRow.insertCell(); //创建新单元格
        newCell4.innerHTML = '<td><input type="number" class="form-control table-input-50"  lay-verify="required"></td>' ; //单元格内的内容

        var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td><input type="number" class="form-control table-input-50"  lay-verify="required"></td>' ; //单元格内的内容

      /*  var newCell5 = newRow.insertCell(); //创建新单元格
        newCell5.innerHTML = '<td><button type="button" class="btn btn-info " ng-click="delete()">删除</button></td>' ; //单元格内的内容*/
    }
    //删除行
    $scope.deleteTr= function(){
        //得到table对象
        var mytable = document.getElementById("myTable");
        //得到table中的行对象数组
        var arr=mytable.rows;
        if(arr.length==1){

        }else{
            //删除最后一行
            arr[arr.length-1].remove();
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
                submitVip();
            });
        });
    }
}])
;