'use strict';

angular.module('startmeupApp')
    .controller('PledgeDetailController', function ($scope, $rootScope, $stateParams, entity, Pledge, User, Estimation) {
        $scope.pledge = entity;
        $scope.load = function (id) {
            Pledge.get({id: id}, function(result) {
                $scope.pledge = result;
            });
        };
        $rootScope.$on('startmeupApp:pledgeUpdate', function(event, result) {
            $scope.pledge = result;
        });
    });
