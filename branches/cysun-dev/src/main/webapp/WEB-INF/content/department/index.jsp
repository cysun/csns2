<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<img id="welcome" src="<c:url value='/img/style/welcome_banner.jpg' />" alt="welcome" />

<c:if test="${fn:length(newses) == 0}">
<ul id="title" style="margin:0;">
<li>Welcome to the ${department.fullName}</li>
</ul>
</c:if>

<c:if test="${fn:length(newses) > 0}">
<ul id="title" style="margin: 0px;">
<li>News and Announcements</li>
<security:authorize access="authenticated and principal.isFaculty('${dept}')">
<li class="align_right"><a href="news/post"><img alt="[Post News]"
  title="Post News" src="<c:url value='/img/icons/newspaper_add.png' />" /></a></li>
</security:authorize>
</ul>
<div id="blk">
<c:forEach items="${newses}" var="news">
<div class="blk_wrap">
  <div class="post_subject">
    <h3>${news.topic.firstPost.subject}</h3>
    <h4>Posted by ${news.topic.firstPost.author.name}
    <fmt:formatDate value="${news.topic.firstPost.date}" pattern="MM/dd/yyyy" />
    
    [<a href="forum/topic/view?id=${news.topic.id}">Discuss</a><security:authorize
      access="authenticated and principal.isFaculty('${dept}')"> |
    <a href="news/edit?id=${news.id}">Edit</a></security:authorize>]
    </h4>
  </div>
  
  <div class="post_content">
    ${news.topic.firstPost.content}
    <c:if test="${fn:length(news.topic.firstPost.attachments) > 0}">
    <div class="news-attachments"> <b>Attachments</b>:
      <ul>
        <c:forEach items="${news.topic.firstPost.attachments}" var="attachment">
          <li><a href="<c:url value='/download.html?fileId=${attachment.id}' />">${attachment.name}</a></li>
        </c:forEach>
      </ul>
    </div>
    </c:if>
  </div>
</div>
</c:forEach>
</div>
</c:if>
