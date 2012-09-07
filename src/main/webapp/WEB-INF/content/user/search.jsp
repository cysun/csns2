<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#add").click(function(){
        window.location.href = "add";
    });
    $("#search").autocomplete({
        source: "<c:url value='/user/autocomplete' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    });
    $("#selectAll").toggle(
        function(){ $(":checkbox[name='userId']").attr("checked",true); },
        function(){ $(":checkbox[name='userId']").attr("checked",false); }
    );
    $("#email").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the user(s) to contact." );
        else
            $("#usersForm").attr("action", "<c:url value='/email/compose' />").submit();
    });
});
</script>

<ul id="title">
<li>Users</li>
<li class="align_right"><a href="add"><img title="Add User" alt="[Add User]"
    src="<c:url value='/img/icons/user_add.png' />" /></a></li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email User(s)"
    alt="[Email Users]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty users}">
<form id="usersForm" method="post">
<table class="viewtable">
<tr>
  <th><input id="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Email</th><th></th>
</tr>
<c:forEach items="${users}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="view?id=${user.id}">${user.name}</a></td>
  <td>
    ${user.primaryEmail}<c:if test="${not empty user.secondaryEmail}">,
    ${user.secondaryEmail}</c:if>
  </td>
  <td class="center">
    <a href="edit?id=${user.id}"><img title="Edit User" alt="[Edit User]"
       src="<c:url value='/img/icons/user_edit.png' />" /></a>
  </td>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/user/search" />
</form>
</c:if>
