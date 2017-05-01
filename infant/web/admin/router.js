/**
 * Created by Administrator on 2016/10/21 0021.
 */
'use strict';
    app.run(
        function ($rootScope,   $state,   $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    )
    .config(
        function ($stateProvider,   $urlRouterProvider) {
            $urlRouterProvider
                .otherwise('/access/signin');
            $stateProvider
                .state('access', {
                    url: '/access',
                    template: '<div ui-view class="fade-in-right-big smooth"></div>'
                })
                .state('access.signin', {
                    url: '/signin',
                    templateUrl: 'admin/page/page_signin.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/SigninCtrl.js','admin/services/SigninSer.js'] );
                            }]
                    }
                })
                .state('app', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'admin/app.html',
                })
                .state('app.goods', {
                    url: '/goods?putaway',
                    templateUrl: 'admin/page/page_goods_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/GoodsCtrl.js'] );
                            }]
                    }
                })
                .state('app.repertory', {
                    url: '/repertory',
                    templateUrl: 'admin/page/page_repertory_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/RepertoryCtrl.js'] );
                            }]
                    }
                })
                .state('app.repertoryPurchase', {
                    url: '/repertoryPurchase',
                    templateUrl: 'admin/page/page_repertory_purchase_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/RepertoryPurchaseCtrl.js'] );
                            }]
                    }
                })
                .state('app.purchaseLog', {
                    url: '/purchaseLog',
                    templateUrl: 'admin/page/page_purchase_log.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/PurchaseLogCtrl.js'] );
                            }]
                    }
                })
                .state('app.purchaseDetail', {
                    url: '/purchaseDetail?id&time',
                    templateUrl: 'admin/page/page_purchase_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/PurchaseDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.returnsLog', {
                    url: '/returnsLog',
                    templateUrl: 'admin/page/page_returns_log.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/ReturnsLogCtrl.js'] );
                            }]
                    }
                })
                .state('app.addGoods', {
                    url: '/addGoods',
                    templateUrl: 'admin/page/page_add_goods.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/AddGoodsCtrl.js'] );
                            }]
                    }
                })
                .state('app.editGoods', {
                    url: '/editGoods?goodsId',
                    templateUrl: 'admin/page/page_edit_goods.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/EditGoodsCtrl.js'] );
                            }]
                    }
                })
                .state('app.goodsClass', {
                    url: '/goodsClass',
                    templateUrl: 'admin/page/page_goods_class.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/GoodsClassCtrl.js'] );
                            }]
                    }
                })
                .state('app.goodsTag', {
                    url: '/goodsTag',
                    templateUrl: 'admin/page/page_goods_tag.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/GoodsTagCtrl.js'] );
                            }]
                    }
                })
                .state('app.sku', {
                    url: '/sku',
                    templateUrl: 'admin/page/page_sku_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/SkuCtrl.js'] );
                            }]
                    }
                })
                .state('app.postageGroup', {
                    url: '/postageGroup',
                    templateUrl: 'admin/page/page_postage_group.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/PostageGroupCtrl.js'] );
                            }]
                    }
                })
                .state('app.postageTpl', {
                    url: '/postageTpl?id&name',
                    templateUrl: 'admin/page/page_postage_tpl.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/PostageTplCtrl.js'] );
                            }]
                    }
                })
                .state('app.sysConfig', {
                    url: '/sysConfig',
                    templateUrl: 'admin/page/page_sys_config_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/SysConfigCtrl.js'] );
                            }]
                    }
                })
                .state('app.country', {
                    url: '/country',
                    templateUrl: 'admin/page/page_country_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/CountryCtrl.js'] );
                            }]
                    }
                })
                .state('app.brands', {
                    url: '/brands',
                    templateUrl: 'admin/page/page_brands_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/BrandsCtrl.js'] );
                            }]
                    }
                })
                .state('app.brandDetail', {
                    url: '/brandDetail?id',
                    templateUrl: 'admin/page/page_brand_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/BrandDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.addBrands', {
                    url: '/addBrands?type',
                    templateUrl: 'admin/page/page_add_brand.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/AddBrandsCtrl.js'] );
                            }]
                    }
                })
                .state('app.order', {
                    url: '/order',
                    templateUrl: 'admin/page/order/page_order_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/order/OrderCtrl.js'] );
                            }]
                    }
                })
                .state('app.orderDetail', {
                    url: '/orderDetail?id',
                    templateUrl: 'admin/page/order/page_order_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/order/OrderDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.returnsDetail', {
                    url: '/returnsDetail?id',
                    templateUrl: 'admin/page/order/page_returns_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/order/ReturnsDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.addOrder', {
                    url: '/addOrder',
                    templateUrl: 'admin/page/order/page_add_order.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/order/AddOrderCtrl.js'] );
                            }]
                    }
                })
                .state('app.memberList', {
                    url: '/memberList',
                    templateUrl: 'admin/page/member/page_member_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/member/MemberListCtrl.js'] );
                            }]
                    }
                })
                .state('app.article', {
                    url: '/article',
                    templateUrl: 'admin/page/member/page_article_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/member/ArticleCtrl.js'] );
                            }]
                    }
                })
                .state('app.comment', {
                    url: '/comment',
                    templateUrl: 'admin/page/member/page_comment_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/member/CommentCtrl.js'] );
                            }]
                    }
                })
                .state('app.memberGrade', {
                    url: '/memberGrade',
                    templateUrl: 'admin/page/member/page_member_grade_manage.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/member/MemberGradeCtrl.js'] );
                            }]
                    }
                })
                .state('app.couponList', {
                    url: '/couponList',
                    templateUrl: 'admin/page/coupon/page_coupon_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/coupon/CouponListCtrl.js'] );
                            }]
                    }
                })
                .state('app.couponDetail', {
                    url: '/couponDetail?id',
                    templateUrl: 'admin/page/coupon/page_coupon_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/coupon/CouponDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.memberDetail', {
                    url: '/memberDetail?id',
                    templateUrl: 'admin/page/member/page_member_detail.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/member/MemberDetailCtrl.js'] );
                            }]
                    }
                })
                .state('app.addCoupon', {
                    url: '/addCoupon',
                    templateUrl: 'admin/page/coupon/page_add_coupon.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/coupon/AddCouponCtrl.js'] );
                            }]
                    }
                })
                .state('app.sysMessageList', {
                    url: '/sysMessageList',
                    templateUrl: 'admin/page/sysmessage/page_sys_message_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/sysmessage/SysMessageCtrl.js'] );
                            }]
                    }
                })
                .state('app.pushMessageList', {
                    url: '/pushMessageList',
                    templateUrl: 'admin/page/sysmessage/page_push_message_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/sysmessage/PushMessageCtrl.js'] );
                            }]
                    }
                })
                .state('app.customPageList', {
                    url: '/customPageList',
                    templateUrl: 'admin/page/custompage/page_custom_page_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/custompage/CustomPageCtrl.js'] );
                            }]
                    }
                })
                .state('app.addcustomPage', {
                    url: '/addcustomPage?id',
                    templateUrl: 'admin/page/custompage/page_add_custom_page.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/custompage/AddCustomPageCtrl.js'] );
                            }]
                    }
                })
                .state('app.carouselList', {
                    url: '/carouselList',
                    templateUrl: 'admin/page/appset/page_carousel_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/appset/CarouselListCtrl.js'] );
                            }]
                    }
                })
                .state('app.recommend', {
                    url: '/recommend',
                    templateUrl: 'admin/page/appset/page_recommend_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['admin/controllers/appset/RecommendListCtrl.js'] );
                            }]
                    }
                })

        }
    );