<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Login Form</title>

    <!-- Bootstrap -->
    <link href="<c:url value="/resources/vendors/bootstrap/dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="<c:url value="/resources/vendors/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet">
    <!-- NProgress -->
    <link href="<c:url value="/resources/vendors/nprogress/nprogress.css"/>" rel="stylesheet">
    <!-- Animate.css -->
    <link href="<c:url value="/resources/vendors/animate.css/animate.min.css"/>" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="<c:url value="/resources/build/css/custom.min.css"/>" rel="stylesheet">
    <!-- PNotify -->
	<link href="<c:url value="/resources/vendors/pnotify/dist/pnotify.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/vendors/pnotify/dist/pnotify.buttons.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/vendors/pnotify/dist/pnotify.nonblock.css"/>" rel="stylesheet">
	<!-- jQuery -->
	<script src="<c:url value="/resources/vendors/jquery/dist/jquery.min.js"/>"></script>
  </head>

  <body class="login">
    <div>
      <a class="hiddenanchor" id="signup"></a>
      <a class="hiddenanchor" id="signin"></a>

      <div class="login_wrapper">
        <div class="animate form login_form">
          <section class="login_content">
            <form:form modelAttribute="loginForm" servletRelativeAction="/processLogin" method="POST">
              <h1>Login Form</h1>
              <div>
                <form:input path="userName" cssClass="form-control" placeholder="Username" />
                <div class="has-error">
                	<form:errors path="userName" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <form:password path="password" cssClass="form-control" placeholder="Password" />
                <div class="has-error">
                	<form:errors path="password" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <button class="btn btn-default submit" type="submit">Log in</button>
                <!-- <a class="reset_pass" href="#">Lost your password?</a> -->
              </div>
              <div>
              	<br>
              	<p>For testing: id=admin, password=12345</p>
              </div>

              <div class="clearfix"></div>

              <div class="separator">
                <p class="change_link">New to site?
                  <a href="<c:url value="/register"/>" class="to_register"> Create Account </a>
                </p>

                <div class="clearfix"></div>
                <br />

                <div>
                  <h1><i class="fa fa-paw"></i> Inventory Management System</h1>
                </div>
              </div>
            </form:form>
          </section>
        </div>
      </div>
    </div>
    
    <!-- PNotify -->
	<script src="<c:url value="/resources/vendors/pnotify/dist/pnotify.js"/>"></script>
	<script src="<c:url value="/resources/vendors/pnotify/dist/pnotify.buttons.js"/>"></script>
	<script src="<c:url value="/resources/vendors/pnotify/dist/pnotify.nonblock.js"/>"></script>
    <script type="text/javascript">
		$(document).ready(function() {
			processMessage();
		});
		function processMessage() {
			var successMsg = '${successMsg}';
			var errorMsg = '${errorMsg}';
			if(successMsg) {
				new PNotify({
			        title: 'Success',
			        text: successMsg,
			        type: 'success',
			        styling: 'bootstrap3'
			    });
			}
			if(errorMsg) {
				new PNotify({
			        title: 'Error',
			        text: errorMsg,
			        type: 'error',
			        styling: 'bootstrap3'
			    });
			}
		}
    </script>
  </body>
</html>
