'use strict';

angular.module('startmeupApp')
    .controller('IdeaDetailController', function ($scope, $rootScope, $stateParams, entity, Idea, User, Estimation) {
        $scope.idea = entity;
        $scope.load = function (id) {
            Idea.get({id: id}, function(result) {
                $scope.idea = result;
            });
        };
        $rootScope.$on('startmeupApp:ideaUpdate', function(event, result) {
            $scope.idea = result;
        });
    });
