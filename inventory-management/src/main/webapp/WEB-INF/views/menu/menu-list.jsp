<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="right_col" role="main">
	<div class="">
		
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Role List</h2>
                    <div class="clearfix"></div>
                  </div>
				
                  <div class="x_content">

					<a href="<c:url value="/menu/permission" />" class="btn btn-app"><i class="fa fa-plus"></i>Permission</a>                  	
					
					<div class="container" style="padding: 50px;">
                  		<form:form modelAttribute="searchForm" 
							cssClass="form-horizontal form-label-left" servletRelativeAction="/menu/list/1" method="POST">
							
						</form:form>
                  	</div>


                    <div class="table-responsive">
                      <table class="table table-striped jambo_table bulk_action">
                        <thead>
                          <tr class="headings">
                            <th rowspan="2" class="column-title" style="border-left: 2px solid">#</th>
                            <th rowspan="2" class="column-title" style="border-left: 2px solid">Url</th>
                            <th rowspan="2" class="column-title" style="border-left: 2px solid">Status</th>
                            <th colspan="${roles.size()}" class="column-title text-center"  style="border-left: 2px solid">Role</th>
                          </tr>
                          <tr class="headings">
                          	<c:forEach items="${roles}" var="role">
                          		<th class="column-title"  style="border-left: 2px solid">${role.roleName}</th>
                          	</c:forEach>
                          </tr>
                        </thead>

                        <tbody>
	                        <c:forEach items="${menus}" var="menu" varStatus="loop">
	                        	<c:choose>
	                        		<c:when test="${loop.index % 2 == 0}">
	                        			<tr class="even pointer">
	                        		</c:when>
	                        		<c:otherwise>
	                        			<tr class="odd pointer">
	                        		</c:otherwise>
	                        	</c:choose>
	                        	
	                        		<td class=" ">${pageInfo.offset + loop.index + 1}</td>
		                            <td class=" ">${menu.url}</td>
		                            <c:choose>
		                            	<c:when test="${menu.activeFlag==1}">
		                            		<td><a href="javascript:void(0);" onclick="confirmChange(${menu.id}, ${menu.activeFlag});" 
		                            			class="btn btn-round btn-danger">Disable</a></td>
		                            	</c:when>
		                            	<c:otherwise>
		                            		<td><a href="javascript:void(0);" onclick="confirmChange(${menu.id}, ${menu.activeFlag});" 
		                            			class="btn btn-round btn-primary">Enable</a></td>
		                            	</c:otherwise>
		                            </c:choose>
		                            <c:forEach items="${menu.authMap}" var="auth">
		                            	<c:choose>
		                            		<c:when test="${auth.value==1}">
		                            			<td><i class="fa fa-check" style="color: green"></i></td>
		                            		</c:when>
		                            		<c:otherwise>
		                            			<td><i class="fa fa-times" style="color: red"></i></td>
		                            		</c:otherwise>
		                            	</c:choose>
		                            </c:forEach>
		                            
	                        	</tr>
	                        </c:forEach>
                        </tbody>
                      </table>
                      
                      <jsp:include page="../layout/paging.jsp"></jsp:include>
                      
                    </div>	
                  </div>
                </div>
              </div>
		</div>
	</div>
</div>

<script type="text/javascript">
	
	$(document).ready(function() {
		processMessage();
	});
	
	function goToPage(pageIndex) {
		$('#searchForm').attr('action', '<c:url value="/menu/list/"/>' + pageIndex);
		$('#searchForm').submit();
	}
	
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
	
	function confirmChange(id, flag) {
		var msg = flag == 1 ? "Do you want to disable this menu?" : "Do you want to enable this menu?";
		if (confirm(msg)) {
			window.location.href = '<c:url value="/menu/change-status/"/>' + id;
		}
	}
</script>

