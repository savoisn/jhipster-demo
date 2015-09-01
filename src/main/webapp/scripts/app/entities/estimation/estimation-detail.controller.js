'use strict';

angular.module('startmeupApp')
    .controller('EstimationDetailController', function ($scope, $rootScope, $stateParams, entity, Estimation, Idea, User, Pledge) {
        $scope.estimation = entity;
        $scope.load = function (id) {
            Estimation.get({id: id}, function(result) {
                $scope.estimation = result;
            });
        };
        $rootScope.$on('startmeupApp:estimationUpdate', function(event, result) {
            $scope.estimation = result;
        });
    });
