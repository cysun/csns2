<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:if test="${not survey.published}">
<script>
$(function(){
    $("input[name='numOfChoices']").change(function(){
        var newNumOfChoices = $(this).val();
        var oldNumOfChoices = $("#choicesInput").children("input[type='text']").length;
        if( newNumOfChoices > oldNumOfChoices )
        {
            for( var i = oldNumOfChoices ; i < newNumOfChoices ; ++i )
                $("#choicesInput").append( "<span>Choice #" + (i+1) + ":</span> " +
                    "<input id='choices" + i + "' name='choices[" + i + "]' type='text' class='leftinput' style='width: 70%;' /> " + 
                    "<br /><br />" );
            if( $("input[name='maxSelections']").val() == oldNumOfChoices )
                $("input[name='maxSelections']").val( newNumOfChoices );
        }
        if( newNumOfChoices < oldNumOfChoices )
        {
            $("#choicesInput span:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput input[type='text']:gt(" + (newNumOfChoices-1) +")").remove();
            $("#choicesInput br:gt(" + (newNumOfChoices*2-1) +")").remove();
            if( $("input[name='maxSelections']").val() > newNumOfChoices )
                $("input[name='maxSelections']").val( newNumOfChoices );
        }
    });
});
function deleteQuestion()
{
    var msg = "Are you sure you want to delete this question?";
    if( confirm(msg) )
        window.location.href = "deleteQuestion?surveyId=${survey.id}&sectionIndex=${param.sectionIndex}&questionId=${question.id}";
}
</script>
</c:if>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="list">Surveys</a></li>
<li><a class="bc" href="editQuestionSheet?surveyId=${survey.id}&amp;sectionIndex=${param.sectionIndex}"><csns:truncate
  value="${survey.name}" length="75" /></a></li>
<li>Edit Question</li>
<c:if test="${not survey.published}">
<li class="align_right"><a href="javascript:deleteQuestion()"><img title="Delete Question"
  alt="[Delete Question]" src="<c:url value='/img/icons/page_delete.png' />" /></a></li>
</c:if>
</ul>

<form:form modelAttribute="question">
<h4>Question description:</h4>
<form:textarea path="description" cssStyle="width: 80%; height: 8em;" />
<div class="error"><form:errors path="description" /></div>

<c:choose>

<c:when test="${question.type == 'CHOICE'}">
<h4>
<c:if test="${not survey.published}">
Number of choices: <form:input path="numOfChoices" cssStyle="width: 2em;" />
</c:if>
</h4>
<div id="choicesInput">
<c:forEach items="${question.choices}" var="choice" varStatus="choiceStatus">
<span>Choice #${choiceStatus.index+1}:</span>
  <form:input path="choices[${choiceStatus.index}]" cssClass="leftinput" cssStyle="width: 70%;" />
  <br /><br />
</c:forEach>
</div>
<h4>Number of selections:
<form:input path="minSelections" cssClass="forminput" cssStyle="width: 1em;" /> -&gt;
<form:input path="maxSelections" cssClass="forminput" cssStyle="width: 1em;" /></h4>
</c:when>

<c:when test="${question.type == 'RATING'}">
<h4>Rating:
<form:input path="minRating" cssClass="forminput" cssStyle="width: 2em;" /> -&gt;
<form:input path="maxRating" cssClass="forminput" cssStyle="width: 2em;" /></h4>
</c:when>

<c:when test="${question.type == 'TEXT'}">
<h4><csns:help name="txtlength" img="false">Text Length</csns:help>:
<form:input path="textLength" cssClass="forminput" cssStyle="width: 4em;" />
</h4>
</c:when>

</c:choose>

<input type="hidden" name="surveyId" value="${survey.id}" />
<input type="hidden" name="sectionIndex" value="${param.sectionIndex}" />
<input class="subbutton" type="submit" value="Save" />
</form:form>

<div id="help-txtlength" class="help">
<em>Text length</em> determines whether a text field or a text area will be
used for the answer of this question. A text field will be used if the text
length is 60 or less; otherwise a text area will be used.
</div>
