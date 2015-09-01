 'use strict';

angular.module('startmeupApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-startmeupApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-startmeupApp-params')});
                }
                return response;
            },
        };
    });