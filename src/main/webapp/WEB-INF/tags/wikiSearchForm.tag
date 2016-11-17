<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="text" required="false" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<li class="align_right">
  <form action="<c:url value='/wiki/search' />">
    <input class="formselect" type="text" name="text" size="25" required value="${text}" />
    <input class="subbutton" type="submit" name="search" value="Search" />
  </form>
</li>
