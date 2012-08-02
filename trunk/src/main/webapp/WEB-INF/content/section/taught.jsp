<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
            source: "<c:url value='/course/autocomplete' />",
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
});
</script>

<ul id="title">
  <li>Sections</li>
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
    <td>${section.course.code} ${section.course.name} - ${section.number}</td>
    <td class="action">
      <a href="<c:url value='/section/edit?id=${section.id}' />"> <img alt="[Edit]"
         title="Edit" src="<c:url value='/img/icons/table_edit.png'/>" /></a>
    </td>
  </tr>
  <tr> 
    <td colspan="2">
      <table class="viewtable">
        <tr><th>Assignment</th><th>Due Date</th><th></th></tr>
        <c:forEach items="${section.assignments}" var="assignment">
        <tr>
          <td><a href="<c:url value='/submission/list?assignmentId=${assignment.id}' />">${assignment.name}</a></td>
          <td class="duedate"><fmt:formatDate value="${assignment.dueDate.time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
          <td class="action">
            <c:if test="${assignment.online}">
              <a href="<c:url value='/assignment/view?id=${assignment.id}' />"><img alt="[View Assignment]" 
                 title="View Assignment" src="<c:url value='/img/icons/script.png'/>" />View</a>
            </c:if>
            <a href="<c:url value='/assignment/edit?id=${assignment.id}' />"><img alt="[Edit Assignment]"
               title="Edit Assignment" src="<c:url value='/img/icons/script_edit.png'/>" />Edit</a>
          </td>
        </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
  <tr class="rowtypeb">
    <td colspan="2">
      <a href="<c:url value='/assignment/add?sectionId=${section.id}' />">Add Assignment</a> |
      <a href="roster?id=${section.id}">View Students</a> |
      <a href="roster/import?id=${section.id}">Enroll Students</a> |
      <a href="viewEnrollments.html?sectionId=${section.id}">Enter Grades</a> |
      <a href="<c:url value='/assessment/editCourseJournal.html?sectionId=${section.id}'/>">Assessment</a>
    </td>
  </tr>
</table>
</c:forEach>

<p><a id="addSectionLink">Add Section</a></p>

<form id="addSectionForm" action="add" method="post">
<input type="text" class="forminput add" name="name" size="40" />
<input type="hidden" name="quarterCode" value="${quarter.code}" />
<input type="submit" class="subbutton" name="add" value="Add" />
<button class="subbutton clear">Clear</button>
</form>
