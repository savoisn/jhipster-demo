'use strict';

angular.module('startmeupApp').controller('PledgeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pledge', 'User', 'Estimation',
        function($scope, $stateParams, $modalInstance, entity, Pledge, User, Estimation) {

        $scope.pledge = entity;
        $scope.users = User.query();
        $scope.estimations = Estimation.query();
        $scope.load = function(id) {
            Pledge.get({id : id}, function(result) {
                $scope.pledge = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('startmeupApp:pledgeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.pledge.id != null) {
                Pledge.update($scope.pledge, onSaveFinished);
            } else {
                Pledge.save($scope.pledge, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
