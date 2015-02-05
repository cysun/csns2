<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test="${file.folder and file.isPublic()}">
    <c:set var="img" value="open_folder.png" />
  </c:when>
  <c:when test="${file.folder and not file.isPublic()}">
    <c:set var="img" value="closed_folder.png" />
  </c:when>
  <c:when test="${not file.folder and file.isPublic()}">
    <c:set var="img" value="open_book.png" />
  </c:when>
  <c:otherwise>
    <c:set var="img" value="closed_book.png" />
  </c:otherwise>
</c:choose>
<img border="0" alt="[*]" title="Toggle Public/Private" src="<c:url value='/img/icons/${img}' />" />
