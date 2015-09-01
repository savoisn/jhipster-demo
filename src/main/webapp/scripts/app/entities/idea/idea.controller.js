'use strict';

angular.module('startmeupApp')
    .controller('IdeaController', function ($scope, Idea) {
        $scope.ideas = [];
        $scope.loadAll = function() {
            Idea.query(function(result) {
               $scope.ideas = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Idea.get({id: id}, function(result) {
                $scope.idea = result;
                $('#deleteIdeaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Idea.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteIdeaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.idea = {name: null, description: null, creation_date: null, id: null};
        };
    });
