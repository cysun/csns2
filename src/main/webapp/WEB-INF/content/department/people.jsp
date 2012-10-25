<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    });
    $("#add").click(function(){
        window.location.href = "<c:url value='/user/add' />";
    });
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "<c:url value='/user/view' />?id=" + ui.item.id;
        }
    });
    $(".add").each(function(){
        $(this).autocomplete({
            source: "<c:url value='/autocomplete/user' />",
            select: function(event, ui) {
                if( ui.item )
                    $("<input>").attr({
                        type: "hidden",
                        name: "userId",
                        value: ui.item.id
                    }).appendTo($(this).parent());
            }
        });
    });
    $(".clear").each(function(){
       $(this).click(function(event){
           event.preventDefault();
           $("input[name='userId']").remove();
           $(".add").each(function(){
              $(this).val("");
           });
       });
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li>${department.name}</li>
<li class="align_right"><a href="<c:url value='/user/add' />"><img title="Add"
    alt="[Add]" src="<c:url value='/img/icons/user_add.png' />" /></a></li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#all">All</a></li>
  <li><a href="#admin">Administrators</a></li>
  <li><a href="#faculty">Faculty</a></li>
  <li><a href="#instructor">Instructors</a></li>
  <li><a href="#reviewer">Program Reviewers</a></li>
</ul>

<div id="all">
<form action="<c:url value='/user/search' />" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>
</div>

<div id="admin">
<table class="viewtable">
<tr>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.administrators}" var="user">
<tr>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td>${user.primaryEmail}</td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/admin/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/admin/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40" 
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div>

<div id="faculty">
<table class="viewtable">
<tr>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.faculty}" var="user">
<tr>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td>${user.primaryEmail}</td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/faculty/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/faculty/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div>

<div id="instructor">
<table class="viewtable">
<tr>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.instructors}" var="user">
<tr>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td>${user.primaryEmail}</td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/instructor/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/instructor/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div>

<div id="reviewer">
<table class="viewtable">
<tr>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.reviewers}" var="user">
<tr>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td>${user.primaryEmail}</td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/reviewer/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/reviewer/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div>

</div> <!-- tabs -->
