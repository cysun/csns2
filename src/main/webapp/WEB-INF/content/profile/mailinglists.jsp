<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
function unsubscribe( id )
{
    var msg = "Do you want to unsubscribe from this mailing list?";
    if( confirm(msg) )
        $.ajax({
            url: "<c:url value='/subscription/mailinglist/unsubscribe' />",
            data: { "id": id, "ajax": true },
            success: function(){
                var current_index = $("#tabs").tabs("option", "active");
                $("#tabs").tabs("load", current_index);
            },
            cache: false
        });
}
</script>

<div> <!-- for tab -->

<c:if test="${fn:length(subscriptions) == 0 }">
<p>You are not subscribed to any mailing list.</p>
</c:if>

<c:if test="${fn:length(subscriptions) > 0}">
<h3>Department Mailing Lists</h3>
<table class="viewtable autowidth">
  <tr>
    <th>Department</th><th>Mailing List</th>
    <th>Auto-Subscribed</th><th>Unsubscribe</th>
  </tr>
  <c:forEach items="${subscriptions}" var="subscription">
  <tr>
    <td>${subscription.subscribable.department.name}</td>
    <td><span class="tt">${subscription.subscribable.name}</span></td>
    <td class="center">${subscription.autoSubscribed}</td>
    <td class="center">
      <a href="javascript:unsubscribe(${subscription.subscribable.id})"><img title="Unsubscribe"
         alt="[Unsubscribe]" src="<c:url value='/img/icons/star_delete.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</c:if>

</div>
