'use strict';

angular.module('startmeupApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('idea', {
                parent: 'entity',
                url: '/ideas',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.idea.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/idea/ideas.html',
                        controller: 'IdeaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('idea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('idea.detail', {
                parent: 'entity',
                url: '/idea/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'startmeupApp.idea.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/idea/idea-detail.html',
                        controller: 'IdeaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('idea');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Idea', function($stateParams, Idea) {
                        return Idea.get({id : $stateParams.id});
                    }]
                }
            })
            .state('idea.new', {
                parent: 'idea',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/idea/idea-dialog.html',
                        controller: 'IdeaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, creation_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('idea', null, { reload: true });
                    }, function() {
                        $state.go('idea');
                    })
                }]
            })
            .state('idea.edit', {
                parent: 'idea',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/idea/idea-dialog.html',
                        controller: 'IdeaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Idea', function(Idea) {
                                return Idea.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('idea', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
