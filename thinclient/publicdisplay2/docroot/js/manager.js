// Some minimal variables and functions for the manager.
//
// author: Neil Ellis

//The new rotation set.
var rotationSet = null;

// This is used by other frames to check if the manager is present.
var allOkay=true;

function initialize()
{
	log("Manager initializing...");
	initializeRotationSet();
	setTimeout("reloadPage()",managerReload);
	log("Manager initialized.");
	top.lhs.controller.rotator.serverOkay();
}

//This is used to constantly check for new rotation sets.
function reloadPage()
{
	window.location.href="./FileServlet?uri="+getDisplayId();
}

window.onload= initialize;