'use strict';

angular.module('startmeupApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('estimation', {
                parent: 'entity',
                url: '/estimations',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.estimation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estimation/estimations.html',
                        controller: 'EstimationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estimation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('estimation.detail', {
                parent: 'entity',
                url: '/estimation/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.estimation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estimation/estimation-detail.html',
                        controller: 'EstimationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estimation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Estimation', function($stateParams, Estimation) {
                        return Estimation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('estimation.new', {
                parent: 'estimation',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estimation/estimation-dialog.html',
                        controller: 'EstimationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, cost: null, pledged: null, creation_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('estimation', null, { reload: true });
                    }, function() {
                        $state.go('estimation');
                    })
                }]
            })
            .state('estimation.edit', {
                parent: 'estimation',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estimation/estimation-dialog.html',
                        controller: 'EstimationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Estimation', function(Estimation) {
                                return Estimation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
