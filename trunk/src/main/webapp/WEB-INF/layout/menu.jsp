<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
	$("#mft-menu-item").hide();
    $("#assessment-menu").hover(function(){
    	if( $("#mft-menu-item").is(":hidden") )
    	{
            $.ajax({
                url: "<c:url value='/department/${dept}/option/MFT' />",
                dataType: "json",
                success: function(data){
                    if( data.result ) $("#mft-menu-item").show();
                }
            });
    	}
    });
});
</script>

<div id="csns_menu">
<div id="menu">
<ul class="menu">

<security:authorize access="hasRole('ROLE_ADMIN')">
  <li><a href="<c:url value='/admin/department/list' />">Departments</a></li>
  <li><a href="<c:url value='/user/search' />">Users</a></li>
</security:authorize>

<security:authorize access="not hasRole('ROLE_ADMIN')">
<security:authorize access="authenticated">
<li><a href="#">Home</a>
  <div><ul>
    <security:authorize access="principal.instructor">
    <li><a href="<c:url value='/section/taught' />"><img alt=""
         src="<c:url value='/img/icons/user_suit.png' />" />Instructor</a></li>
    </security:authorize>
    <security:authorize access="principal.evaluator">
    <li><a href="<c:url value='/section/evaluated' />"><img alt=""
         src="<c:url value='/img/icons/user_gray.png' />" />Evaluator</a></li>
    </security:authorize>
    <li><a href="<c:url value='/section/taken' />"><img alt=""
           src="<c:url value='/img/icons/user_green.png' />" />Student</a></li>
  </ul></div>
</li>
</security:authorize>

<c:if test="${not empty dept}">
<li><a href="<c:url value='/department/${dept}/' />">Department</a>
  <div><ul>
<security:authorize access="authenticated and principal.isFaculty('${dept}')">
    <li><a href="<c:url value='/department/${dept}/people'/>"><img alt=""
           src="<c:url value='/img/icons/group.png' />" />People</a></li>
</security:authorize>
    <li><a href="<c:url value='/department/${dept}/programs' />"><img alt=""
           src="<c:url value='/img/icons/reports.png' />" />Programs</a></li>
    <li><a href="<c:url value='/department/${dept}/courses' />"><img alt=""
           src="<c:url value='/img/icons/books.png' />" />Courses</a></li>
    <li><a href="<c:url value='/department/${dept}/sections' />"><img alt=""
           src="<c:url value='/img/icons/blackboard_sum.png' />" />Sections</a></li>
    <li><a href="<c:url value='/department/${dept}/projects' />"><img alt=""
           src="<c:url value='/img/icons/bricks.png' />" />Projects</a></li>
  </ul></div>
</li>

<security:authorize access="authenticated and principal.isInstructor('${dept}')">
<li><a id="assessment-menu" href="#">Assessment</a>
  <div><ul>
    <li id="rubrics-menu-item"><a href="<c:url value='/department/${dept}/rubric/list' />"><img
        alt="" src="<c:url value='/img/icons/table_heatmap2.png' />" />Rubrics</a></li>
    <li id="rubrics-menu-item"><a href="<c:url value='/department/${dept}/journal/list' />"><img
        alt="" src="<c:url value='/img/icons/reports.png' />" />Course Journals</a></li>
<security:authorize access="principal.isFaculty('${dept}')">
    <li id="mft-menu-item"><a href="<c:url value='/department/${dept}/mft/overview' />"><img
        alt="" src="<c:url value='/img/icons/mft.png' />" />Major Field Test</a></li>
</security:authorize>
  </ul></div>
</li>
</security:authorize>

<li><a href="#">Resources</a>
  <div><ul>
    <li><a href="<c:url value='/wiki/content/department/${dept}/' />"><img alt=""
           src="<c:url value='/img/icons/wiki.png' />" />Wiki</a></li>
    <li><a href="<c:url value='/department/${dept}/news/current' />"><img alt=""
           src="<c:url value='/img/icons/newspaper.png' />" />News</a></li>
    <li><a href="<c:url value='/department/${dept}/forum/list' />"><img alt=""
           src="<c:url value='/img/icons/forums.png' />" />Forums</a></li>
    <li><a href="<c:url value='/department/${dept}/survey/current' />"><img alt=""
           src="<c:url value='/img/icons/surveys.png' />" />Surveys</a></li>
    <li><a href="<c:url value='/department/${dept}/mailinglist/list' />"><img alt=""
           src="<c:url value='/img/icons/mailinglists.png' />" />Mailing Lists</a></li>
<security:authorize access="authenticated">
    <li><a href="<c:url value='/file/' />"><img alt=""
           src="<c:url value='/img/icons/file_manager.png' />" />File Manager</a></li>
</security:authorize>
  </ul></div>
</li>
</c:if> <%-- end of <c:if test="${not empty dept}"> --%>

<li><a href="<c:url value='/wiki/content/csns/help' />">Help</a></li>
</security:authorize>

</ul>
</div> <!-- end of menu -->
</div> <!-- end of csns-menu -->
