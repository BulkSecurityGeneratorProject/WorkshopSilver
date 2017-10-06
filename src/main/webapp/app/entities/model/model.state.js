(function() {
    'use strict';

    angular
        .module('workshopSilverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model', {
            parent: 'entity',
            url: '/model?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'workshopSilverApp.model.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model/models.html',
                    controller: 'ModelController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('model');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-detail', {
            parent: 'model',
            url: '/model/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'workshopSilverApp.model.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model/model-detail.html',
                    controller: 'ModelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('model');
                    $translatePartialLoader.addPart('photo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Model', 'Photo', function($stateParams, Model, Photo) {
                    return {
                        photos: Photo.getByModel({ id: $stateParams.id }),
                        id: $stateParams.id
                    }
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-photos', {
            parent: 'model',
            url: '/model/{id}/photos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'workshopSilverApp.model.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model/model-detail.html',
                    controller: 'ModelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('model');
                    $translatePartialLoader.addPart('photo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Model', 'Photo', function($stateParams, Model, Photo) {
                    return {
                        photos: Photo.getByModel({ id: $stateParams.id }),
                        id: $stateParams.id
                    }
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-photos.new', {
            parent: 'model-photos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-dialog.html',
                    controller: 'PhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null,
                                model: {
                                    id: $stateParams.id
                                }
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-photos', null, { reload: 'model-photos' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-photos.delete', {
            parent: 'model-photos',
            url: '/{photoId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-delete-dialog.html',
                    controller: 'PhotoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Photo', function(Photo) {
                            return Photo.get({id : $stateParams.photoId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-photos', null, { reload: 'model-photos' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-detail.edit', {
            parent: 'model-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model.new', {
            parent: 'model',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('model');
                });
            }]
        })
        .state('model.edit', {
            parent: 'model',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model.delete', {
            parent: 'model',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model/model-delete-dialog.html',
                    controller: 'ModelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
