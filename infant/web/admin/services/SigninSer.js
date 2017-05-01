/**
 * Created by Administrator on 2016/10/22 0022.
 */
'use strict';

app.factory('SigninFactory',['ENV','$state','$q','$http', function (ENV,$state,$q,$http) {
    var host = ENV.api;
    var api_admin_login = host+"admin/login";
        return {
            //管理员登陆
            adminLogin: function (model) {
                $.ajax({
                    type:"post",
                    url:api_admin_login,
                    data:{userName: model.userName, password: model.password},
                    dataType:"json",
                    success: function (obj) {
                        console.log(obj);
                        if(obj.code==0){
                            showMsg("登陆成功！");
                            $state.go('app.goods',{putaway:1});
                        }
                        else{
                            showMsg("账号或者密码错误！");
                        }
                    },
                    error: function (obj) {
                        console.log(obj);
                        showMsg("请求失败")
                    }
                });
            }
        }
    }])
