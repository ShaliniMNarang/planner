'use strict';

var myApp = angular.module('myApp', [ 'ngRoute' ]);

myApp.config(function($routeProvider, $qProvider) {
	$routeProvider.when("/", {
		templateUrl : "login.html",
		controller : "loginController"
	}).when("/login", {
		templateUrl : "login.html",
		controller : "loginController"	
	}).when("/about", {
		templateUrl : "about.html"	
	})
	.when("/signup", {
		templateUrl : "signup.html",
		controller : "signupController"			
	}).when("/list", {
		templateUrl : "list.html",
		controller : "appController"
	}).when("/eventcreate", {
		templateUrl : "event-create.html",
		controller : "eventCreateController"
	}).when("/invitees", {
		templateUrl : "invitees.html",
		controller : "inviteesController"
	}).when("/uploadPhoto", {
		templateUrl : "upload-photo.html",
		controller : "uploadphotoController"
	});

	$qProvider.errorOnUnhandledRejections(false);
});

myApp.controller('appController', function($scope, $http, $window,
		eventService, $location) {
	console.log('appController starting');
	$http.get("/event-planner/event-planner/getevents").then(
			function(response) {
				console.log(response.data);
				$scope.events = response.data;
			});

	$scope.eventClicked = function(Id) {
		eventService.setEventId(Id);
		$location.path("/eventcreate");
	}
	
	$scope.updateId = function(Id) {
		console.log(Id);
		eventService.setEventId(Id);
	}
});

myApp.controller('eventCreateController', function($scope, httpPost,
		eventService, $http) {
	$scope.eventCreated = false;
	var eventId = eventService.getEventId();
	if (eventId === null) {
		console.log("created event")
		$scope.title = "Create Event";
		$scope.action = "Create Event";
		$scope.createEvent = function() {
			var fd = new FormData();
			fd.append('title', $scope.eventtitle);
			fd.append('location', $scope.location);
			fd.append('eventDate', $scope.eventdate);
			fd.append('eventTime', $scope.eventtime);
			fd.append('description', $scope.description);
			fd.append('userId', $scope.userId);
			var postUrl = "/event-planner/event-planner/createevent";
			httpPost.postCall(fd, postUrl);
			$scope.eventCreated = true;
			eventService.setEventId(null);
			//$location.url("/list");
			window.history.back();
		};
	} else {
		$scope.title = "Update Event";
		$scope.action = "Update Event";
		$http.get("/event-planner/event-planner/getevents/" + eventId).then(
				function(response) {
					console.log("getevents:" + eventId);
					console.log(response.data);
					$scope.eventtitle = response.data.title;
					$scope.location = response.data.location;
					$scope.eventdate = response.data.eventDate;
					$scope.eventtime = response.data.eventTime;
					$scope.description = response.data.description;
					eventService.setEventId(null);
				});
	}

});

myApp.controller('inviteesController', function($scope, httpPost, eventService,
		$http) {
	function Invitee(counter, id,name, email, phone) {
		this.counter = counter;
		this.id=id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}
	$scope.invitationSent=false;
	var counter = 0;
	$scope.invitees = [];
	//$scope.invitees.push(new Invitee(counter++, "", "", ""));
	$scope.addInviteeRow = function() {
		$scope.invitees.push(new Invitee(counter++,"0", "", "", ""));
		console.log($scope.invitees);
	}
	
	function getInvitee(){
		var id=eventService.getEventId(id);
		$http.get("/event-planner/event-planner/getinvitees/"+id).then(
				function(response) {
					console.log(response.data);
					var existingInvitees = response.data;
					var idx;
					for (idx in existingInvitees){
						$scope.invitees.push(new Invitee(counter++, existingInvitees[idx].id,existingInvitees[idx].guestName, existingInvitees[idx].emailId, existingInvitees[idx].phoneNumber));
					}
				});
	}
	getInvitee();
	
	$scope.sendInvite = function(type) {
		var id=eventService.getEventId(id);
		console.log(id);
		console.log($scope.invitees);
		var idx;
		for (idx in $scope.invitees) {
			console.log("sendInvite");
			console.log($scope.invitees[idx].name);
			var fd = new FormData();
			//fd.append('eventId', $scope.eventId);
			fd.append('eventid', id);
			fd.append('id', $scope.invitees[idx].id);
	        fd.append('name', $scope.invitees[idx].name);
	        fd.append('email', $scope.invitees[idx].email);
	        fd.append('phone', $scope.invitees[idx].phone);
	        fd.append('type', type);
	        var postUrl = "/event-planner/event-planner/sendemail";
	        httpPost.postCall(fd, postUrl);
	        $scope.invitationSent=true;
	        window.history.back();
		}
		
	}
	
	$scope.deleteRow = function(invitee) {
		$scope.invitees.splice(invitee, 1);
		console.log($scope.invitees);
	}

});

myApp.controller('uploadphotoController', function($scope, httpPost,
		eventService, $http,$location,$window) {
	console.log("uploadphotoController");
	function Photo(counter, file) {
		this.counter = counter;
		this.file = file;
	}
	$scope.photoUploaded=false;
	var counter = 0;
	$scope.photos = [];
	$scope.photos.push(new Photo(counter++, ""));

	$scope.addPhotoRow = function() {
		$scope.photos.push(new Photo(counter++, ""));
		console.log($scope.photos);
	}

	$scope.uploadPhoto = function(id) {
		console.log(id);
		var id=eventService.getEventId(id);
		console.log($scope.photos);
		var idx;
		for (idx in $scope.photos) {
			console.log("uploadPhoto");
			console.log($scope.photos[idx].file);
			var fd = new FormData();
			//fd.append('eventId', $scope.eventId);
			fd.append('eventid', id);
	        fd.append('photo', $scope.photos[idx].file);
	        var postUrl = "/event-planner/event-planner/uploadphoto";
	        httpPost.postCall(fd, postUrl);
	        $scope.photoUploaded=true;
		}
		
	}
	
	$scope.uploadedPhotos = function() {
		var id=eventService.getEventId(id);
		console.log($location.$$host+':'+$location.$$port);
		//window.location.href="http://"+$location.$$host+':'+$location.$$port+"/event-planner/event-planner/photos/"+id;
		var href="http://"+$location.$$host+':'+$location.$$port+"/event-planner/event-planner/photos/"+id;
		$window.open(href, '_blank');
	}
	
	$scope.deleteRow = function(photos) {
		$scope.photos.splice(photos, 1);
		console.log($scope.photos);
	}
});

myApp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

myApp.service('httpPost', [ '$http', function($http) {
	this.postCall = function(fd, postUrl) {
		console.log("postCall");
		$http.post(postUrl, fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}).then(function(response) {
			console.log("response")
		})
	}
} ]);

myApp.service('eventService', function() {
	var Id = null;

	return {
		getEventId : function() {
			return Id;
		},
		setEventId : function(value) {
			Id = value;
		}
	}
});

myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);


myApp.controller('loginController', ['$scope', '$rootScope', '$http', '$location', function($scope, $rootScope, $http, $location){
			$scope.doSignup = function() {
				$location.url("/signup");	
			}

			$scope.doLogin = function() {
				var userId = $scope.userId;
				var password = $scope.password;
				var url = "/event-planner/event-planner/login";

				var data = new FormData();
				data.append('userId', userId);
				data.append('password', password);

				console.log(url + ": " + data.userId + ":" + data.password);
				var config = {
					transformRequest : angular.identity,
					transformResponse : angular.identity,
					headers : {
						'Content-Type' : undefined
					}
				}

				$http.post(url, data, config).then(function(response) {
					if (response.data != "FAIL") {
						console.log("logged in ");
						$rootScope.isUserLoggedIn = true;
						$rootScope.userId = userId;
						$location.url("/list");
					} else {
						$rootScope.isUserLoggedIn = false;
						alert("Invalid credentails!!! Pl. try again.")
					}
				}, function(response) {
				});
			};
}]);

myApp.controller('signupController', ['$scope', '$rootScope', '$http', '$location', function($scope, $rootScope, $http, $location){
	
	   $scope.doSignUp = function(){
	       var userId = $scope.userId;
	       var firstName = $scope.firstName;
	       var lastName = $scope.lastName;
	       var password = $scope.password;
	       var mailId = $scope.mailId;
	       var url = "/event-planner/event-planner/signUp";
	       
	       var data = new FormData();
	       data.append('userId', userId);
	       data.append('firstName',firstName);
	       data.append('lastName',lastName);
	       data.append('password', password);
	       data.append('mailId',mailId);
	       
	       console.log(url + ": " + data.userId + ":"+data.password);
	       var config = {
	    	   	transformRequest: angular.identity,
	    	   	transformResponse: angular.identity,
		   		headers : {
		   			'Content-Type': undefined
		   	    }
	       }
	       
	       $http.post(url, data, config).then(function (response) {
	    	   if(response.data != "FAIL")  {
	    		   console.log("userCreated ");
	    		   //alert("User created !!! Pl. sign In")
	    		   //$location.url("/login");
	    		   window.history.back();
	    	   }
			});
	    };
	    
	    $scope.doCancel = function(){
//	    	alert("User creation aborted!!!");
//	    	$location.url("/login");
	    	window.history.back();
	    }
}]);


myApp.controller('logoutController', ['$scope','$rootScope','$location', function($scope, $rootScope,$location){
    $scope.logout = function(){
    	   $rootScope.isUserLoggedIn = false;
		   $rootScope.userId = "";
		   $location.url("/login");
    };
}]);
