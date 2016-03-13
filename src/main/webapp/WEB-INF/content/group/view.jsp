<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
	$(".selectAll").click(function(){
	    var checked = $(this).is(":checked");
    	$(this).parents("form").find(":checkbox[name='userId']").prop("checked",checked);
	});
	$("#email").click(function(){
	    if( $("#users-form").find(":checkbox[name='userId']:checked").length  > 0 )
	        $("#users-form").submit();
	    else
	        window.location.href = "<c:url value='/email/compose?backUrl=/department/${dept}/popup/current' />";
	});
});

function email( userId )
{
    var url = "<c:url value='/email/compose?userId=' />" + userId;
    url += "&backUrl=/department/" + "${dept}" + "/people#group";
    window.location.href=url;
}
</script>

<ul id="title">
  <li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">Group</a></li>
  <li>${group.name}</li>
  
  <li class="align_right"><a href="<c:url value='/department/${dept}/group/export?id=${group.id}' />"><img title="Export to Excel"
    alt="[Export to Excel]" src="<c:url value='/img/icons/export_excel.png' />" /></a></li>
  
  <li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Users"
    alt="[Email Users]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<c:choose>
<c:when test="${fn:length(group.users) > 0 }">
<form id="users-form" action="<c:url value='/email/compose' />" method="post">
  <table class="viewtable">
  <tr>
  	<th><input class="selectAll" type="checkbox" /></th>
    <th>CIN</th>
    <th>Name</th>
    <th>Email</th>
  </tr>
  
  <c:forEach items="${group.users}" var="user">
  	<tr>
  	<td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  	<td>${user.cin}</td>
  	<td>${user.name}</td>
  	<td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  	</tr>
  </c:forEach>
  </table>
  <input type="hidden" name="backUrl" value="/department/${dept}/people#group" />
</form>
</c:when>
<c:otherwise>
	<p><span>There are no users in this group.</span></p>
</c:otherwise>
</c:choose>

