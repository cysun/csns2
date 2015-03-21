<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("select[name='quarter'] option").each(function(){
       if( $(this).val() == ${quarter.code}) 
           $(this).attr('selected', true);
    });
    $("select[name='quarter']").change(function(){
        var quarter = $("select[name='quarter'] option:selected").val();
        window.location.href = "taught?quarter=" + quarter;
    });
    $("#addSectionForm").hide();
    $("#addSectionLink").click(function(){
       $("#addSectionForm").toggle(); 
    });
    $(".add").each(function(){
        $(this).autocomplete({
            source: "<c:url value='/autocomplete/course' />",
            select: function(event, ui) {
                if( ui.item )
                    $("<input>").attr({
                        type: "hidden",
                        name: "courseId",
                        value: ui.item.id
                    }).appendTo($(this).parent());
            }
        });
    });
    $(".clear").each(function(){
       $(this).click(function(event){
           event.preventDefault();
           $("input[name='courseId']").remove();
           $(".add").each(function(){
              $(this).val("");
           });
       });
    });
    $(".viewtable").tablesorter({
        headers: { 3: {sorter: false} }
    });
});
function publish( id, type )
{
    var msg = "Do you want to publish this assignment now?";
    if( confirm(msg) )
        $("#pdate-"+id).load( "<c:url value='/' />" + type + "/publish?id=" + id );
}
function createJournal( sectionId )
{
    var msg = "Do you want to create a course journal for this section?";
    if( confirm(msg) )
        window.location.href = "journal/create?sectionId=" + sectionId;
}
</script>

<ul id="title">
  <li>Instructor's Home</li>
  <li class="align_right">
    <select class="formselect" name="quarter">
      <c:forEach var="q" items="${quarters}"><option value="${q.code}">${q}</option></c:forEach>
    </select>
  </li>
</ul>

<c:forEach var="section" items="${sections}">
<a id="section-${section.id}"></a>
<table class="outer_viewtable">
  <tr class="rowtypea">
    <td>
      <a href="<c:url value='${section.siteUrl}' />">${section.course.code}
         ${section.course.name} - ${section.number}</a>
    </td>
    <td class="action">
<c:choose>
  <c:when test="${empty section.journal}">
      <a href="javascript:createJournal(${section.id})"> <img alt="[Course Journal]"
         title="Course Journal" src="<c:url value='/img/icons/report.png'/>" /></a>
  </c:when>
  <c:otherwise>
      <a href="journal/view?sectionId=${section.id}"> <img alt="[Course Journal]"
         title="Course Journal" src="<c:url value='/img/icons/report.png'/>" /></a>
  </c:otherwise>
</c:choose>
      <a href="<c:url value='/section/edit?id=${section.id}' />"> <img alt="[Edit]"
         title="Edit" src="<c:url value='/img/icons/table_edit.png'/>" style="margin-left: 10px;"/></a>
    </td>
  </tr>
  <tr> 
    <td colspan="2">
      <table class="viewtable">
        <thead>
        <tr>
          <th>Assignment</th><th class="datetime">Publish Date</th><th class="datetime">Due Date</th><th class="action"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${section.assignments}" var="assignment">
        <tr>
          <td><a href="<c:url value='/submission/list?assignmentId=${assignment.id}' />">${assignment.name}</a></td>
          <td class="datetime"><csns:publishDate date="${assignment.publishDate.time}" datePattern="yyyy-MM-dd hh:mm a"
              datePast="${assignment.published}" itemId="${assignment.id}" itemType="assignment" /></td>
          <td class="datetime"><csns:dueDate date="${assignment.dueDate.time}"
              datePast="${assignment.pastDue}" /></td>
          <td class="action">
            <c:if test="${assignment.online}">
              <a href="<c:url value='/assignment/online/view?id=${assignment.id}' />"><img alt="[View Assignment]" 
                 title="View Assignment" src="<c:url value='/img/icons/script_view.png'/>" /></a>
            </c:if>
            <c:if test="${not assignment.online and assignment.description != null}">
              <a href="<c:url value='/assignment/view?id=${assignment.id}' />"><img alt="[View Assignment]" 
                 title="View Assignment" src="<c:url value='/img/icons/script_view.png'/>" /></a>
            </c:if>
            <a href="<c:url value='/assignment/edit?id=${assignment.id}' />"><img alt="[Edit Assignment]"
               title="Edit Assignment" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
          </td>
        </tr>
        </c:forEach>
        <c:forEach items="${section.rubricAssignments}" var="assignment">
        <tr>
          <td><a href="<c:url value='/rubric/submission/instructor/list?assignmentId=${assignment.id}' />">${assignment.name}</a></td>
          <td class="datetime"><csns:publishDate date="${assignment.publishDate.time}" datePattern="yyyy-MM-dd hh:mm a"
              datePast="${assignment.published}" itemId="${assignment.id}" itemType="rubric/assignment" /></td>
          <td class="datetime"><csns:dueDate date="${assignment.dueDate.time}"
              datePast="${assignment.pastDue}" /></td>
          <td class="action">
              <a href="<c:url value='/department/${dept}/rubric/view?id=${assignment.rubric.id}' />"><img
                 alt="[View Rubric]" title="View Rubric" src="<c:url value='/img/icons/script_view.png'/>" /></a>
            <a href="<c:url value='/rubric/assignment/edit?id=${assignment.id}' />"><img alt="[Edit Assignment]"
               title="Edit Assignment" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
          </td>
        </tr>
        </c:forEach>
        </tbody>
      </table>
    </td>
  </tr>
  <tr class="rowtypeb">
    <td colspan="2">
      <a href="<c:url value='/assignment/create?sectionId=${section.id}' />">Create Assignment</a> |
      <a href="roster?id=${section.id}">View Students</a> |
      <a href="roster/import?sectionId=${section.id}">Enroll Students</a> |
      <a href="attendance?sectionId=${section.id}">Take Attendance</a>
    </td>
  </tr>
</table>
</c:forEach>

<p><a id="addSectionLink">Add Section</a></p>

<form id="addSectionForm" action="add" method="post">
<input type="text" class="forminput add" name="name" size="40"
    placeholder="Search for courses to add" />
<input type="hidden" name="quarterCode" value="${quarter.code}" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</form>
