'use strict';

angular.module('startmeupApp')
    .controller('PledgeController', function ($scope, Pledge) {
        $scope.pledges = [];
        $scope.loadAll = function() {
            Pledge.query(function(result) {
               $scope.pledges = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Pledge.get({id: id}, function(result) {
                $scope.pledge = result;
                $('#deletePledgeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Pledge.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePledgeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pledge = {amount: null, comment: null, creation_date: null, id: null};
        };
    });
