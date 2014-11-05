<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    })
    .autocomplete( "instance" )._renderItem = function(ul, item){
        var li = $("<li>");
        var downloadUrl = "<c:url value='/download.html?fileId=' />";
        if( item.thumbnail )
            li.append("<img src='" + downloadUrl + item.thumbnail + "' alt='' style='vertical-align: middle; margin-right: 0.5em;' />");
        return li.append(item.label).appendTo(ul);
    };
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
        $(this).autocomplete( "instance" )._renderItem = function(ul, item){
            var li = $("<li>");
            var downloadUrl = "<c:url value='/download.html?fileId=' />";
            if( item.thumbnail )
                li.append("<img src='" + downloadUrl + item.thumbnail + "' alt='' style='vertical-align: middle; margin-right: 0.5em;' />");
            return li.append(item.label).appendTo(ul);
        };
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
    $(".selectAll").click(function(){
        var checked = $(this).is(":checked");
        $(this).parents("form").find(":checkbox[name='userId']").prop("checked",checked);
    });
    $("#email").click(function(){
        var form = "#" + $(".ui-tabs-panel:not(.ui-tabs-hide)").attr("id") + "Form";
        if( $(form).find(":checkbox[name='userId']:checked").length  > 0 )
            $(form).attr("action", "<c:url value='/email/compose' />").submit();
        else
            window.location.href = "<c:url value='/email/compose?backUrl=/department/${dept}/people' />";
    });
});
function email( userId )
{
    var url = "<c:url value='/email/compose?userId=' />" + userId;
    url += "&backUrl=/department/" + "${dept}" + "/people%23" + $(".ui-tabs-panel:not(.ui-tabs-hide)").attr("id");
    window.location.href=url;
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li>${department.name}</li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Users"
    alt="[Email Users]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/user/add' />"><img title="Add"
    alt="[Add]" src="<c:url value='/img/icons/user_add.png' />" /></a></li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="user/import"><img alt="[Import Students]"
  title="Import Students" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
</security:authorize>
</ul>

<div id="tabs">
<ul>
  <li><a href="#all">All</a></li>
  <li><a href="#admin">Administrators</a></li>
  <li><a href="#faculty">Faculty</a></li>
  <li><a href="#instructor">Instructors</a></li>
  <li><a href="#evaluator">Rubric Evaluators</a></li>
  <li><a href="#reviewer">Program Reviewers</a></li>
</ul>

<div id="all">
<form action="<c:url value='/user/search' />" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="dept" type="hidden" value="${dept}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>
</div> <!-- end of all -->

<div id="admin">
<c:if test="${fn:length(department.administrators) > 0}">
<form id="adminForm" method="post">
<table class="viewtable">
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.administrators}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/admin/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/people#admin" />
</form>
</c:if>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/admin/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40" 
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div> <!-- end of admin -->

<div id="faculty">
<c:if test="${fn:length(department.faculty) > 0}">
<form id="facultyForm" method="post">
<table class="viewtable">
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.faculty}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/faculty/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/people#faculty" />
</form>
</c:if>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/faculty/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div> <!-- end of faculty -->

<div id="instructor">
<c:if test="${fn:length(department.instructors) > 0}">
<form id="instructorForm" method="post">
<table class="viewtable">
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.instructors}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/instructor/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/people#instructor" />
</form>
</c:if>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/instructor/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div> <!-- end of instructors -->

<div id="evaluator">
<c:if test="${fn:length(department.evaluators) > 0}">
<form id="evaluatorForm" method="post">
<table class="viewtable">
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.evaluators}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/evaluator/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/people#evaluator" />
</form>
</c:if>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/evaluator/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div> <!-- end of evaluators -->

<div id="reviewer">
<c:if test="${fn:length(department.reviewers) > 0}">
<form id="reviewerForm" method="post">
<table class="viewtable">
<tr>
  <th><input class="selectAll" type="checkbox" /></th>
  <th>CIN</th><th>Name</th><th>Primary Email</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th class="center"></th>
  </security:authorize>
</tr>
<c:forEach items="${department.reviewers}" var="user">
<tr>
  <td class="center"><input type="checkbox" name="userId" value="${user.id}" /></td>
  <td>${user.cin}</td>
  <td><a href="<c:url value='/user/view?id=${user.id}' />">${user.name}</a></td>
  <td><a href="javascript:email(${user.id})">${user.primaryEmail}</a></td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="personnel/reviewer/remove?userId=${user.id}"><img
    title="Remove" alt="[Remove]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/people#reviewer" />
</form>
</c:if>

<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<form action="personnel/reviewer/add" method="post"><p>
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for users to add" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</p></form>
</security:authorize>
</div> <!-- end of reviewers -->

</div> <!-- tabs -->
