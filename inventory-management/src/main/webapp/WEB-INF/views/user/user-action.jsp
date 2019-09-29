<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h2>${pageTitle}</h2>
			</div>
		</div>

		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					
					<div class="x_content">
						<br />
						<form:form modelAttribute="modelForm" 
							cssClass="form-horizontal form-label-left" servletRelativeAction="/user/save" method="POST">
							
							<form:hidden path="id" />
							<form:hidden path="activeFlag" />
							<form:hidden path="createDate" />
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="userName">User Name <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="userName" cssClass="form-control col-md-7 col-xs-12" disabled="${viewOnly}" />
									<div class="has-error">
                						<form:errors path="userName" cssClass="help-block"></form:errors>
                					</div>
								</div>
							</div>
							<c:if test="${!editMode}">
								<div class="form-group">
									<label class="control-label col-md-3 col-sm-3 col-xs-12"
										for="password">Password <span class="required">*</span>
									</label>
									<div class="col-md-6 col-sm-6 col-xs-12">
										<form:password path="password" cssClass="form-control col-md-7 col-xs-12" />
										<div class="has-error">
	                						<form:errors path="password" cssClass="help-block"></form:errors>
	                					</div>
									</div>
								</div>
							</c:if>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="roleId">Role <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<c:choose>
										<c:when test="${!viewOnly}">
											<form:select path="roleId" cssClass="form-control">
												<form:options items="${roleMap}" />
											</form:select>
											<div class="has-error">
		                						<form:errors path="roleId" cssClass="help-block"></form:errors>
		                					</div>
										</c:when>
										<c:otherwise>
											<input type="text" value="${userRoleName}" class="form-control col-md-7 col-xs-12" disabled />
										</c:otherwise>
									</c:choose>
									
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" 
									for="name">Full Name <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="name" cssClass="form-control col-md-7 col-xs-12" disabled="${viewOnly}" />
									<div class="has-error">
                						<form:errors path="name" cssClass="help-block"></form:errors>
                					</div>
								</div>
							</div>
							<div class="form-group">
								<label for="email" class="control-label col-md-3 col-sm-3 col-xs-12">Email</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="email" cssClass="form-control col-md-7 col-xs-12" disabled="${viewOnly}" />
									<div class="has-error">
                						<form:errors path="email" cssClass="help-block"></form:errors>
                					</div>
								</div>
							</div>
							
							<div class="ln_solid"></div>
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button class="btn btn-primary" type="button" onclick="cancel();">Cancel</button>
									<c:if test="${!viewOnly}">
										<button class="btn btn-primary" type="reset">Reset</button>
										<button type="submit" class="btn btn-success">Submit</button>
									</c:if>
								</div>
							</div>

						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('#userlistId').addClass('current-page').siblings().removeClass('current-page');
		var parent = $('#userlistId').parents('li');
		parent.addClass('active').siblings().removeClass('active');
		$('#userlistId').parents().show();
	});
	function cancel() {
		window.location.href='<c:url value="/user/list" />'
	}
</script>