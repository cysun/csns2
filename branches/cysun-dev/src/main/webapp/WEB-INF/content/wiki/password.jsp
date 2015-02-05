<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs path="${path}" />
</ul>

<div id="wiki_content">

<p>The page <span class="tt">${path}</span> you are trying to access requires a password:</p>
<form action="<c:url value='/wiki/password' />" method="post">
  <input class="leftinput" type="password" name="password" />
  <input type="hidden" name="path" value="${path}" />
  <input class="subbutton" type="submit" name="ok" value="OK" />
</form>
</div>
