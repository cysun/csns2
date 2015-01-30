<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#add").click(function(){
        window.location.href = "add";
    });
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    })
    .autocomplete( "instance" )._renderItem = function(ul, item){
    	var li = $("<li>");
    	var downloadUrl = "<c:url value='/download.html?fileId=' />";
    	if( item.thumbnail )
    		li.append("<img src='" + downloadUrl + item.thumbnail + "' alt='' style='vertical-align: middle; margin-right: 0.5em;' />");
        return li.append(item.label).appendTo(ul);
    };
    $("#selectAll").click(function(){
        var checked = $("#selectAll").is(":checked");
        $(":checkbox[name='userId']").prop("checked",checked);
    });
    $("#email").click(function(){
        if( $(":checkbox[name='userId']:checked").length == 0 )
            alert( "Please select the user(s) to contact." );
        else
            $("#usersForm").attr("action", "<c:url value='/email/compose' />").submit();
    });
    $(".thumbnails").click(function(){
    	$(".ui-dialog-content").dialog("close");
        var downloadUrl = "<c:url value='/download.html?fileId=' />" + $(this).attr("name");
    	$("<div>").append("<img src='" + downloadUrl + "' alt='' />").dialog({
            autoOpen:       true,
            height:         400,
            width:          350
    	});
    });
});
</script>

<ul id="title">
<li>Users</li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Users"
    alt="[Email Users]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
<li class="align_right"><a href="add"><img title="Add User" alt="[Add User]"
    src="<c:url value='/img/icons/user_add.png' />" /></a></li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" value="${param.term}" />
<input name="dept" type="hidden" value="${dept}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty users}">
<form id="usersForm" method="post">
<table class="viewtable">
<tr>
  <th><input id="selectAll" type="checkbox" /></th><th></th>
  <th>CIN</th><th>Name</th><th>Email</th><th></th>
</tr>
<c:forEach items="${users}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td class="shrink">
    <c:if test="${not empty user.profileThumbnail}">
    <img src="<c:url value='/download.html?fileId=${user.profileThumbnail.id}' />"
      alt="[Profile Thumbnail]" class="thumbnails" name="${user.profilePicture.id}"
      width="24" height="24" />
    </c:if>
  </td>
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
