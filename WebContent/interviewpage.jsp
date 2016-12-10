<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>Interview</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <!-- Optional theme -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <!-- Latest compiled and minified JavaScript -->
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.2/angular.min.js"></script>
        <script>
	         var mainApp = angular.module("mainApp", []);	         
	         mainApp.controller('ViewCandidateDetailsController', function($scope, $http) {
	        	var url = "getCandidateDetails/"+<%=request.getParameter("analysticskey") %>+"";
        		$http.get(url).success( function(response) {
        			$scope.candidate = response; 
        		});        		
	         });
			
      </script>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h1>Interview</h1>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Candiant details</h3>
                </div>
                	<div class="panel-body" ng-app="mainApp" ng-controller="ViewCandidateDetailsController">
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
								<td><a href="downloadFile/{{candidate.filename}}">click here to download the file.</a></td>
							</tr>	
							<form action="subitFeedback" method="post">
							<tr>
								<td><label> OverAll Score </label></td>
								<td><input type="text" class="form-control" id="score" placeholder="score" name="score"></td>
							</tr>
							<tr>
								<td><label> FeedBack </label></td>
								<td><textarea class="form-control" id="feedback" placeholder="feedback" name="feedback"></textarea></td>
							</tr>
							<tr>							
								<td></td>
								<td><button type="submit" class="btn btn-success">Submit</button><button type="submit" class="btn btn-danger">Clear</button></td>
							
							</tr>
							</form>								
						</tbody>
					</table>
                 </div>
                </div>
            </div>
        </div>       
</body>
</html>
