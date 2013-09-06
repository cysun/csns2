<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
blockquote {
    font-family: Georgia, serif;
    font-size: 18px;
    font-style: italic;
    margin: 0.25em 50px;
    padding: 0.25em 40px;
    line-height: 1.45;
    position: relative;
    color: #383838;
}
blockquote:before {
    display: block;
    content: "\201C";
    font-size: 80px;
    position: absolute;
    left: -5px;
    top: -20px;
    color: #7a7a7a;
}
blockquote cite {
    color: #999999;
    font-size: 14px;
    display: block;
    margin-top: 5px;
}
blockquote cite:before {
    content: "\2014 \2009";
}
</style>

<ul id="title">
<li>MFT</li>
<li class="align_right"><a href="distribution"><img alt="[National Distributions]"
  title="National Distributions" src="<c:url value='/img/icons/table_heatmap.png' />" /></a></li>
<li class="align_right"><a href="ai"><img alt="[Assessment Indicators]"
  title="Assessment Indicators" src="<c:url value='/img/icons/table_select.png' />" /></a></li>
<li class="align_right"><a href="score"><img alt="[Scores]"
  title="Scores" src="<c:url value='/img/icons/table.png' />" /></a></li>
</ul>

<blockquote>
<p>The ETS&reg; Major Field Tests are comprehensive undergraduate and MBA
outcomes assessments designed to measure the critical knowledge and understanding
obtained by students in a major field of study. The Major Field Tests go beyond
the measurement of factual knowledge by helping you evaluate students' ability
to analyze and solve problems, understand relationships and interpret material
from their major field of study.</p>

<p>ETS offers comprehensive national comparative data for the Major Field Tests,
enabling you to evaluate your students' performance and compare your program's
effectiveness to programs at similar institutions nationwide.</p>

<cite><a href="http://www.ets.org/mft/about">ETS</a></cite>
</blockquote>

<p style="margin: 1em 50px;">MFT reports are available at
<a href="http://admin.mft-ets.org/">http://admin.mft-ets.org/</a>, and the
annual comparative data guides can be downloaded from
<a href="<c:url value='/file/view?id=4509691' />">here</a>.</p>
