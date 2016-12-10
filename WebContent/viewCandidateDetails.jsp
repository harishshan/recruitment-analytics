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
	        	var url = "getCandidateDetails/"+<%=request.getParameter("analysticskey") %>+"";
        		$http.get(url).success( function(response) {
        			$scope.candidate = response; 
        		});        		
	         });
			
      </script>
    </head>
    <body ng-app="mainApp" ng-controller="viewCandidateDetailsController">
        <div class="container">
            <div class="page-header">
                <h1>View Candidate Details</h1>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">View Candidate Details</h3>
                </div>
                <div class="panel-body" >
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
								<td>{{candidate.phonenumber}}</td>
							</tr>
							<tr>
								<td>Status</td>
								<td>{{candidate.status}}</td>
							</tr>
							<tr>
								<td>File</td>
								<td><a href="downloadFile/{{candidate.filename}}">click here to download the file.</a></td>
							</tr>								
						<tbody>
					</table>					
					<div ng-if="0 != candidate.rountcount">
						<form action="scheduleInterview" method="get">
							<div class="form-group">
								<label> Employee EmailID </label> <input type="text" class="form-control"
									id="emailid" placeholder="emailid" name="emailid">
							</div>
							<input type="hidden" name="analysticskey" value="{{candidate.analysticskey}}"> 
							<div class="form-group">
								<button type="submit" class="btn btn-success">Schedule First Round</button>
								<button type="submit" class="btn btn-danger">Clear</button>
							</div>
						</form>
					</div>
					<div ng-if="0 == candidate.rountcount">
						<table class="table table-hover table-bordered">
                            			<thead>
                                		<tr>	
                                    		<th>#</th>
                                    		<th>Analytics</th>                                    		
                                    		<th>Attitude</th>
                                    		<th>Coding</th>
                                    		<th>Problem Solving</th>
                                    		<th>Status</th>
                                    		<th>Interviewer Comment</th> 
                                    		<th>Recruiter Comment</th>                                    
                                		</tr>
                            			</thead>
										<tbody>
											<tr ng-repeat = "round in candidate.rounds">
                                   	 			<th scope="row">{{round.index}}</th>
                                    			<td>{{round.analytics}}</td>
												<td>{{round.attitue}}</td>
												<td>{{round.coding}}</td>
												<td>{{round.problem}}</td>
												<td>{{round.status}}</td>
												<td>{{round.icomment}}</td>
												<td>{{round.rcomment}}</td>
                                			</tr>
										<tbody>
									</table>
    				</div> 
                </div>
            </div>
        </div>
    </body>
</html>
