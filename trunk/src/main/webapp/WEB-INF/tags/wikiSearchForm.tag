<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<li class="align_right">
  <form action="<c:url value='/wiki/search' />" method="post">
    <input class="formselect" type="text" name="term" size="25" />
    <input class="subbutton" type="submit" name="search" value="Search" />
  </form>
</li>
