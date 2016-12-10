<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>View Candidate Details</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <!-- Optional theme -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
        <!-- Latest compiled and minified JavaScript -->
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.2/angular.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js">
        </script>
        <script>
	         var mainApp = angular.module("mainApp", []);
	         
	         mainApp.controller('viewCandidateDetailsController', function($scope, $http) {
	        	var url = "getCandidateDetails/"+<%=request.getParameter("analysticsKey") %>+"";
        		$http.get(url).success( function(response) {
        			$scope.candidate = response; 
        		});        		
	         });
			
      </script>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h1>View Candidate Details</h1>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">View Candidate Details</h3>
                </div>
                <div class="panel-body" ng-app="mainApp" ng-controller="viewCandidateDetailsController">
                    <table class="table table-hover table-bordered" >
                      	<thead>
                      	<tr>	
                       		<th>Details</th>
                       		<th>Value</th>                         
                      	</tr>
                      	</thead>
						<tbody>
							<tr>
								<td>Name</td>
								<td>{{candidate.name}}</td>
							</tr>
							<tr>
								<td>Email ID</td>
								<td>{{candidate.emailid}}</td>
							</tr>
							<tr>
								<td>Phone</td>
								<td>{{candidate.phone}}</td>
							</tr>
							<tr>
								<td>Status</td>
								<td>{{candidate.status}}</td>
							</tr>
							<tr>
								<td>File</td>
								<td><object data="downloadFile/{{candidate.filename}}" type="application/pdf"><a href="filename.pdf">click here to download the file.</a></object>
</td>
							</tr>								
						<tbody>
					</table>					
					   
                </div>
            </div>
        </div>
    </body>
</html>
