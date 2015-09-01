'use strict';

angular.module('startmeupApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pledge', {
                parent: 'entity',
                url: '/pledges',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.pledge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pledge/pledges.html',
                        controller: 'PledgeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pledge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pledge.detail', {
                parent: 'entity',
                url: '/pledge/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.pledge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pledge/pledge-detail.html',
                        controller: 'PledgeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pledge');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pledge', function($stateParams, Pledge) {
                        return Pledge.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pledge.new', {
                parent: 'pledge',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pledge/pledge-dialog.html',
                        controller: 'PledgeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {amount: null, comment: null, creation_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pledge', null, { reload: true });
                    }, function() {
                        $state.go('pledge');
                    })
                }]
            })
            .state('pledge.edit', {
                parent: 'pledge',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pledge/pledge-dialog.html',
                        controller: 'PledgeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pledge', function(Pledge) {
                                return Pledge.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pledge', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
