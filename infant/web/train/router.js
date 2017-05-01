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
                    templateUrl: 'train/page/page_signin.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/SigninCtrl.js'] );
                            }]
                    }
                })
                .state('app', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'train/app.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/AppCtrl.js'] );
                            }]
                    }
                })
                .state('app.user', {
                    url: '/user',
                    templateUrl: 'train/page/page_user.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/UserCtrl.js'] );
                            }]
                    }
                })
                .state('app.puser', {
                    url: '/puser',
                    templateUrl: 'train/page/page_user_platform.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/UserPlatformCtrl.js'] );
                            }]
                    }
                })
                .state('app.org', {
                    url: '/org',
                    templateUrl: 'train/page/page_org.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/OrgCtrl.js'] );
                            }]
                    }
                })
                .state('app.psw', {
                    url: '/psw',
                    templateUrl: 'train/page/page_update_psw.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['train/ctrl/UpdatePswCtrl.js'] );
                            }]
                    }
                })
        }
    );