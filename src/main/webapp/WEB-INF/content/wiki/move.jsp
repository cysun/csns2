<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs dept="${dept}" path="${param.from}" />
<li>Move</li>
</ul>

<div id="wiki_content">
<p>Move page <span class="tt">${param.from}</span> to: </p>
<form action="<c:url value='/department/${dept}/wiki/move' />" method="post">
<input class="leftinput" type="text" name="to" size="${fn:length(param.from)+5}" />
<input type="hidden" name="from" value="${param.from}" />
<input class="subbutton" type="submit" name="ok" value="OK" />
</form>
</div>
