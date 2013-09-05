<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li><a class="bc" href="score">Scores</a></li>
<li>Import</li>
</ul>

<table class="general autowidth">
<tr>
  <th>Date</th>
  <td><fmt:formatDate pattern="MM/dd/yyyy" value="${importer.date}" /></td>
</tr>
</table>

<p></p>

<table class="viewtable autowidth">
<tr><th></th><th>Last Name</th><th>First Name</th><th>CIN</th><th>Score</th></tr>
<c:forEach items="${importer.scores}" var="score" varStatus="status">
<tr>
  <td>${status.index+1}</td>
  <td>${score.user.lastName}</td>
  <td>${score.user.firstName}</td>
  <td>${score.user.cin}</td>
  <td>${score.value}</td>
</tr>
</c:forEach>
</table>

<c:if test="${fn:length(importer.failedUsers) > 0}">
<p class="error">The scores of the following students cannot be imported:</p>
<table class="viewtable autowidth">
<tr><th>Last Name</th><th>First Name</th><th>CIN</th></tr>
<c:forEach items="${importer.failedUsers}" var="user">
<tr>
  <td>${user.lastName}</td>
  <td>${user.firstName}</td>
  <td>${user.cin}</td>
</tr>
</c:forEach>
</table>
<p>There are two possible reasons: a) the CIN of the student in the score report is
incorrect, or b) the CIN of the student is missing and the system cannot
find a <em>unique</em> student record that matches the last name and first
name. Please go back to the previous page, insert or correct the CIN for
each of these students, then try again.</p>
</c:if>

<form:form modelAttribute="importer"><p>
  <input type="hidden" name="_page" value="1" />
  <input type="submit" name="_target0" value="Back" class="subbutton" />
  <c:if test="${fn:length(importer.failedUsers) == 0}">
    <input type="submit" name="_target2" value="Import" class="subbutton" />
  </c:if>
</p></form:form>
