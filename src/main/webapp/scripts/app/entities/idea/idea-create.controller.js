'use strict';

angular.module('startmeupApp')
    .controller('IdeaCreateController', function ($scope, $rootScope, $stateParams, Idea, User, Estimation) {
        $scope.content2=""
        $scope.static_content='hey'
        $scope.idea = null;
        $scope.load = function (id) {
            Idea.get({id: id}, function(result) {
                $scope.idea = result;
            });
        };
        $rootScope.$on('startmeupApp:ideaUpdate', function(event, result) {
            $scope.idea = result;
        });
    });
