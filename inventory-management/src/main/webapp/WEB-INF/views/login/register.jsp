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
  </head>

  <body class="login">
    <div>
      <a class="hiddenanchor" id="signup"></a>
      <a class="hiddenanchor" id="signin"></a>

      <div class="login_wrapper">
        <div id="register" class="animate form login_form">
          <section class="login_content">
            <form:form modelAttribute="registerForm" servletRelativeAction="/processRegister" method="POST">
              <h1>Create Account</h1>
              <div>
                <form:input path="userName" class="form-control" placeholder="Username" />
                <div class="has-error">
                	<form:errors path="userName" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <form:password path="password" class="form-control" placeholder="Password" />
                <div class="has-error">
                	<form:errors path="password" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <form:password path="rePassword" class="form-control" placeholder="Reenter Password" />
                <div class="has-error">
                	<form:errors path="rePassword" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <form:input path="email" class="form-control" placeholder="Email" />
                <div class="has-error">
                	<form:errors path="email" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <form:input path="name" class="form-control" placeholder="Full Name" />
                <div class="has-error">
                	<form:errors path="name" cssClass="help-block"></form:errors>
                </div>
              </div>
              <div>
                <button class="btn btn-default submit" type="submit">Register</button>
              </div>

              <div class="clearfix"></div>

              <div class="separator">
                <p class="change_link">Already a member ?
                  <a href="<c:url value="/login"/>" class="to_register"> Log in </a>
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
  </body>
</html>
