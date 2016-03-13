<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="department" value="${importer.department}" />

<ul id="title">
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">Group</a></li>
<li>Import Students in ${group.name}</li>
</ul>

<p>Please copy and paste the list of students to the text area below, or enter the information of each
student manually, one on each line, in the following format:</p>
<p style="text-align: center; font-family: monospace;">
term cin first_name last_name</p>
<p>Note that</p>
<ul>
  <li>Fields must be separated by tabs.</li>
  <li>Term must be a 4-digit code, e.g. 2111.</li>
  <li>There could be additional fields after last name, though currently they
      are not processed.</li>
</ul>

<form:form modelAttribute="importer">
<p><form:textarea path="text" rows="20" cols="80" cssStyle="width: 100%; border: 1px solid;" /></p>
<p><input type="hidden" name="_page" value="0" />
<input type="submit" name="_target1" value="Next" class="subbutton" /></p>
</form:form>
