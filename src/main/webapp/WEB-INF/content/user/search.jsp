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
            $("#usersForm").attr("action", "<c:url value='/email.html' />").submit();
    });
});
</script>

<ul id="title">
<li>Users</li>
<li class="align_right"><a href="add"><img title="Add" alt="[Add]"
    src="<c:url value='/img/icons/user_add.png' />" /></a></li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty users}">
<table class="viewtable">
<tr><th>CIN</th><th>Name</th><th>Primary Email</th><th></th></tr>
<c:forEach items="${users}" var="user">
<tr>
  <td>${user.cin}</td>
  <td><a href="view?id=${user.id}">${user.name}</a></td>
  <td>${user.primaryEmail}</td>
  <td class="center">
    <a href="edit?id=${user.id}"><img title="Edit" alt="[Edit]"
       src="<c:url value='/img/icons/user_edit.png' />" /></a>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
