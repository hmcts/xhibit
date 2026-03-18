/*
 * Title:       Java Script Utils
 *
 * Description: All Java Script Utilities used by the application should be placed in here.
 *              To maximise cross platform portablility the amount of java script used on the
 *              client should be kept to a minimum, unfortunatly to write a fully functional 
 *              application some scripting is required. There should be NO javascript code blocks 
 *              in the application, this stops business logic being encoded in the jsps. This rule
 *              obviously does not apply to event handlers.
 *
 * Copyright:   Copyright (c) 2003
 * Company:     EDS
 *
 * Author:      William Fardell, Xdevelopment LLP (2003)
 * Version:     V1.0.0
 * $Log: util.js,v $
 * Revision 1.1  2006/05/04 10:18:42  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
 *
 * Revision 1.4  2006/02/15 13:32:03  xztnfq
 * Change: PR58533
 * Comment: The message checker tries to connect to /Messaging, which then redirects to /Messaging/home This causes unnecessary network traffic as the request is, effectively, doubled.Ensure that the Message checker is directed straight to Messaging/home in the javascript. Update javascript to correct location
 *
 * Revision 1.3  2005/04/27 08:26:57  bzjrnl
 * Manual Merge From BRANCH_7_X
 *
 * Revision 1.1.2.1  2005/04/25 13:37:34  bzjrnl
 * Changes to move jspc into the framework.
 *
 * Revision 1.7  2003/05/01 20:18:16  fz0n8j
 * *** empty log message ***
 *
 * Revision 1.6  2003/04/30 10:03:02  fz0n8j
 * Added messaging to probation service. (EFC)
 *
 * Revision 1.5  2003/04/02 13:39:55  rz3jq5
 * Added an open window with focus function.
 *
 * Revision 1.4  2003/04/01 11:04:42  hzf3bb
 * added function to populate a drop down list.
 *
 * david.duncan-eds@eds.com
 *
 * Revision 1.3  2003/03/28 12:23:07  hzf3bb
 * *** empty log message ***
 *
 * Revision 1.2  2003/03/28 12:06:24  hzf3bb
 * added function to focus first field of a form if present
 *
 * Revision 1.1  2003/03/26 17:49:03  fz0n8j
 * Moved files to shared html dir
 *
 * Revision 1.7  2003/03/26 16:54:48  fz0n8j
 * Bug fixes.
 *
 * Revision 1.6  2003/03/24 08:23:25  fz0n8j
 * Added view daily list, and public display
 *
 * Revision 1.5  2003/03/19 19:34:39  fz0n8j
 * added function to force action on form
 *
 * Revision 1.4  2003/03/11 16:31:44  fz0n8j
 * Added CVS log comments - ecawley
 *
 */

function psSubmitForm(formName) {
    eval('document.' + formName + '.submit()');
}

function psSubmitFormAction(formName, action) {
    eval('document.' + formName + '.action = action');
    eval('document.' + formName + '.submit()');
}

function psSetStatus(newStatus) {
    window.status = newStatus;
    return true;
}

function submitOnEnter(formName, event)
{   
    if (event.keyCode==13)
        eval('document.' + formName + '.submit()');
}

function focusFirstFieldAndCheckMessages() {
    focusFirstField();
    checkMessages();
}

function focusFirstField() {

    // check if there is a form present
    if (document.forms.length > 0) {

        allFields = document.forms[0].elements

        // find only non-hidden fields 
        for (i=0; i<allFields.length; i++) {
            field = allFields[i];

            if (field.type && field.type == "hidden" ) {
                continue;
            }

            // focus on the first non-hidden element
            field.focus();
            break;
        }
    }
}

function populateComboBox(commaDelimString, formName, comboName)
{
    //parse comma delimited string and populate an array...
    var listArray = commaDelimString.split(",");
    
    //get reference to combobox...	
    
    var comboBox = eval("document."+formName+"."+comboName);
    
    //delete all options already present...
    var length = comboBox.options.length;

    for (var i=0; i<length ; i++) {
        comboBox.options[0] = null;
        
    }

    for (var i=0; i<listArray.length; i++) {
        newOption = new Option(listArray[i]);
        comboBox.options[i] = newOption;            
    }   
}

function openWindowWithFocus(windowUrl, windowName, parameters)
{
    var w;
    w=window.open(
                 windowUrl,
                 windowName,
                 parameters
                 );
    w.focus();
}


function checkMessages()
{

    var messageChecker;
    // open up a new window which will check the messages . . .

    messageChecker = window.open("/Messaging/home","Messaging","width=300,height=50");

    the_timeout = setTimeout("checkMessages();", 10000);
}

function checkMessageWindow()
{

if(window.opener.closed)
{
   window.close();
}

    the_timeout = setTimeout("checkMessageWindow();", 10000);
}


