<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="img" value="closed_book.png" />
<c:if test="${rubric.isPublic()}">
  <c:set var="img" value="open_book.png" />
</c:if>
<img border="0" alt="[*]" title="Toggle Public/Private" src="<c:url value='/img/icons/${img}' />" />
