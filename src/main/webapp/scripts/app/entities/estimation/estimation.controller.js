'use strict';

angular.module('startmeupApp')
    .controller('EstimationController', function ($scope, Estimation) {
        $scope.estimations = [];
        $scope.loadAll = function() {
            Estimation.query(function(result) {
               $scope.estimations = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Estimation.get({id: id}, function(result) {
                $scope.estimation = result;
                $('#deleteEstimationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Estimation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEstimationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.estimation = {name: null, description: null, cost: null, pledged: null, creation_date: null, id: null};
        };
    });
