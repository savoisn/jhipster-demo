'use strict';

angular.module('startmeupApp').controller('EstimationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Estimation', 'Idea', 'User', 'Pledge',
        function($scope, $stateParams, $modalInstance, entity, Estimation, Idea, User, Pledge) {

        $scope.estimation = entity;
        $scope.ideas = Idea.query();
        $scope.users = User.query();
        $scope.pledges = Pledge.query();
        $scope.load = function(id) {
            Estimation.get({id : id}, function(result) {
                $scope.estimation = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('startmeupApp:estimationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.estimation.id != null) {
                Estimation.update($scope.estimation, onSaveFinished);
            } else {
                Estimation.save($scope.estimation, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
