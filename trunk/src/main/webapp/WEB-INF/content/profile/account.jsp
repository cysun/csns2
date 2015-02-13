<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(function() {
    $("#tabs").tabs({
        beforeLoad: function(event, ui){
            ui.ajaxSettings.cache = false;	
        }
    });
    if( window.location.hash )
        $("#tabs").tabs({
            active : window.location.hash.substring(1)
        });
    $("#birthday").datepicker({
        inline: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-60:+00"
    });
    $("#cellPhone").mask("(999) 999-9999");
    $("#homePhone").mask("(999) 999-9999");
    $("#workPhone").mask("(999) 999-9999");
});
</script>

<ul id="title">
<li>${user.name}'s Profile</li>
<li class="align_right"><a href="<c:url value='/profile/edit' />"><img title="Edit" alt="[Edit]"
    src="<c:url value='/img/icons/user_edit.png' />" /></a></li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#account">Account</a></li>
  <li><a href="<c:url value='/profile/courses' />">Course Work</a></li>
  <li><a href="<c:url value='/profile/program' />">Program</a></li>
  <li><a href="<c:url value='/profile/advisement' />">Advisement</a>
  <li><a href="<c:url value='/profile/forums' />">Forum Subscriptions</a></li>
  <li><a href="<c:url value='/profile/mailinglists' />">Mailing List Subscriptions</a></li>
</ul>
<div id="account">

<div style="float: right; margin-left: 1em;">
<c:choose>
<c:when test="${not empty user.profilePicture}">
  <img src="<c:url value='/download.html?fileId=${user.profilePicture.id}' />"
       alt="[Profile Picture]" title="Profile Picture" />
</c:when>
<c:otherwise>
  <img src="<c:url value='/img/nopp.png' />" alt="[NoPP]" title="No Profile Picture" />
</c:otherwise>
</c:choose>
</div>

<table class="general autowidth">
  <tr>
    <th>Name</th>
    <td>${user.firstName} ${user.middleName} ${user.lastName}</td>
  </tr>
  <tr>
    <th>CIN</th>
    <td>${user.cin}</td>
  </tr>
  <tr>
    <th>Username</th>
    <td>${user.username}</td>
  </tr>
  <tr>
    <th>Primary Email</th>
    <td>${user.primaryEmail}</td>
  </tr>
  <tr>
    <th>Secondary Email</th>
    <td>${user.secondaryEmail}</td>
  </tr>
  <tr>
    <th>Cell Phone</th>
    <td>${user.cellPhone}</td>
  </tr>
  <tr>
    <th>Home Phone</th>
    <td>${user.homePhone}</td>
  </tr>
  <tr>
    <th>Work Phone</th>
    <td>${user.workPhone}</td>
  </tr>
  <tr>
    <th>Address</th>
    <td>${user.address}</td>
  </tr>
  <tr>
    <th>Gender</th>
    <td>
      <c:if test="${user.gender == 'M'}">Male</c:if>
      <c:if test="${user.gender == 'F'}">Female</c:if>
    </td>
  </tr>
  <tr>
    <th>Birthday</th>
    <td><fmt:formatDate pattern="MM/dd/yyyy" value="${user.birthday}" /></td>
  </tr>
  <tr>
    <th>Disk Quota (MB)</th>
    <td>${user.diskQuota}</td>
  </tr>
  <tr>
    <th>Web Service Access Key</th>
    <td>${user.accessKey}</td>
  </tr>
</table>
</div> <!-- account -->
</div> <!-- tabs -->
