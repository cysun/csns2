function gotoUrl( url )
{
    window.location.href = url;
}

function confirmGoto( message, url )
{
    if( confirm(message) ) window.location.href = url;
}
