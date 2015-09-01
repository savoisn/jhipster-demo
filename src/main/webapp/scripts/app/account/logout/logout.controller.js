'use strict';

angular.module('startmeupApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
