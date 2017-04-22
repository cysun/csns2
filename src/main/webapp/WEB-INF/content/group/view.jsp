<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "addUser?userId=" + ui.item.id + "&groupId=${group.id}";
        }
    })
    $("table").tablesorter({
        headers: { 0: {sorter: false} },
        sortList: [[4,1],[2,0]]
    });
    $(".selectAll").click(function(){
        var checked = $(this).is(":checked");
        $(this).parents("form").find(":checkbox[name='userId']").prop("checked",checked);
    });
    $("#email").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
        	alert( "Please select the members to contact." );
        else
            $("#users-form").attr("action", "<c:url value='/email/compose' />").submit();
    });
    $("#remove").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the members you want to remove." );
        else
            if( confirm("Are you sure you want to remove these users from the group?") )
            {
            	var memberIds = [{name:"groupId",value:"${group.id}"}];
            	$(":checkbox[name='userId']:checked").each(function(){
            		memberIds.push({
            			name: "memberId",
            			value: $(this).attr("data-member-id")
            	    });
            	});
            	window.location.href="removeMembers?" + $.param(memberIds);
            }
    });
});
function email( userId )
{
    var url = "<c:url value='/email/compose?userId=' />" + userId;
    url += "&backUrl=/department/" + "${dept}" + "/group/view?id=${group.id}";
    window.location.href=url;
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">${group.department.name}</a></li>
<li>User Group</li>
<li class="align_right"><a href="export?id=${group.id}"><img title="Export to Excel"
    alt="[Export to Excel]" src="<c:url value='/img/icons/export_excel.png' />" /></a></li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="edit?id=${group.id}"><img title="Edit Group"
    alt="[Edit Group]" src="<c:url value='/img/icons/group_edit.png' />" /></a></li>
<li class="align_right"><a href="import?id=${group.id}"><img title="Import Users"
    alt="[Import Users]" src="<c:url value='/img/icons/group_import.png' />" /></a></li>
<li class="align_right"><a id="remove" href="javascript:void(0)"><img title="Remove Users"
    alt="[Remove Users]" src="<c:url value='/img/icons/user_delete.png' />" /></a></li>
</security:authorize>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Users"
    alt="[Email Users]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<h3>${group.name}</h3>
<p>${group.description}</p>

<p style="margin-top: 1em;">
<span style="margin-right: 1em;">Members: ${fn:length(group.members)}</span>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<button class="subbutton">Add</button>
<input id="search" name="text" type="text" class="forminput" size="40" />
</security:authorize>
</p>

<c:if test="${fn:length(group.members) > 0 }">
<form id="users-form" method="post">
<table class="general2 autowidth">
<thead>
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th>
  <th>Name</th>
  <th>Email</th>
  <th>Date Added</th>
</tr>
</thead>
<tbody>
<c:forEach items="${group.members}" var="member">
  <tr>
  	<td class="center">
      <input type="checkbox" name="userId" data-member-id="${member.id}" value="${member.user.id}" />
    </td>
  	<td>${member.user.cin}</td>
  	<td><a href="<c:url value='/user/view?id=${member.user.id}' />">${member.user.name}</a></td>
  	<td><a href="javascript:email(${member.user.id})">${member.user.primaryEmail}</a></td>
    <td><fmt:formatDate value="${member.date}" pattern="yyyy-MM-dd" /></td>
  </tr>
</c:forEach>
</tbody>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/group/view?id=${group.id}" />
</form>
</c:if>
