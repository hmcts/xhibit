<%--
This JSP should be included as part of a valid javascript block to start up the
MessageCheck JSP.
--%>
doLoad()
{
window.open(
    "MessageCheck.jsp",
        "Message Check",
        "fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no,directories=no,location=no,width=100,height=100,left=0,top=0"
        );
}
