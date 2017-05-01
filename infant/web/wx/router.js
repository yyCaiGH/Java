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
                    templateUrl: 'wx/page/page_home.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['wx/ctrl/HomeCtrl.js'] );
                            }]
                    }
                })
                .state('access.update', {
                    url: '/update?id',
                    templateUrl: 'wx/page/page_update.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['wx/ctrl/UpdateCtrl.js'] );
                            }]
                    }
                })
                .state('access.apply', {
                    url: '/apply?id',
                    templateUrl: 'wx/page/page_apply_result.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['wx/ctrl/ApplyResultCtrl.js'] );
                            }]
                    }
                })
                .state('access.applylist', {
                    url: '/applylist?phone',
                    templateUrl: 'wx/page/page_apply_list.html',
                    resolve: {
                        deps: ['uiLoad',
                            function( uiLoad ){
                                return uiLoad.load( ['wx/ctrl/ApplyListCtrl.js'] );
                            }]
                    }
                })
        }
    );