'use strict';

angular.module('startmeupApp')
    .factory('Idea', function ($resource, DateUtils) {
        return $resource('api/ideas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertLocaleDateFromServer(data.creation_date);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creation_date = DateUtils.convertLocaleDateToServer(data.creation_date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creation_date = DateUtils.convertLocaleDateToServer(data.creation_date);
                    return angular.toJson(data);
                }
            }
        });
    });
