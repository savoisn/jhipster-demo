'use strict';

angular.module('startmeupApp').controller('IdeaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Idea', 'User', 'Estimation',
        function($scope, $stateParams, $modalInstance, entity, Idea, User, Estimation) {

        $scope.idea = entity;
        $scope.users = User.query();
        $scope.estimations = Estimation.query();
        $scope.load = function(id) {
            Idea.get({id : id}, function(result) {
                $scope.idea = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('startmeupApp:ideaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.idea.id != null) {
                Idea.update($scope.idea, onSaveFinished);
            } else {
                Idea.save($scope.idea, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
