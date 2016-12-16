<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">${group.department.name}</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/group/view?id=${group.id}' />">${group.name}</a></li>
<li>Import Users</li>
</ul>

<p>Please copy and paste the list of users to the text area below in the following format:</p>
<p style="margin-left: 2em; font-family: monospace;">cin first_name last_name ...</p>
<p>Note that fields must be separated by <em>tabs</em>, and there could be additional
fields after last name, though currently they are not processed.</p>

<form:form modelAttribute="importer">
<p><form:textarea path="text" rows="20" cols="80" cssStyle="width: 100%; border: 1px solid;" /></p>
<p><input type="hidden" name="_page" value="0" />
<button class="subbutton" name="_target" value="1">Next</button></p>
</form:form>
