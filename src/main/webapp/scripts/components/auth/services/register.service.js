'use strict';

angular.module('startmeupApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


