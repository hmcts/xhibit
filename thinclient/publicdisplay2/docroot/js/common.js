//Config

var managerReload=900000; //15 mins  //5000;  // 1800000 == 30 mins
var retryInitialization=30000;
var reloadEntireFrameDelay= 14400000;   //should be 14400000 in live.
var doTopFrameReload= false;
var maxLogCharacters= 10000;
var frameWidth= 0;
var delayBeforeCheckingRHS= 1500;
var cachingDuration= 300000;
var loaded= true;
var retry_initialization_delay= 300000; // == 5 mins. Used in rhs.html in case initialization stalls.

var BASE_WEB_URL= "/PublicDisplay/FileServlet?uri=";

var logOn= false;
var logOff= false;


// Find out the display ID passed to the top frame, please note this is not robust.
// It just takes the value of the first parameter passed!


function getDisplayId() {
    return getParameter("display");
}

function getParameterForWindow(theWindow, param) {
	urlquery=theWindow.location.href.split("?");
	if(urlquery == null || urlquery.length < 1) {
		return "undefined";
	}
	pairs=urlquery[1].split("&");
	for(var i= 0; i < pairs.length; i++) {
	    namevalue=pairs[i].split("=");
        name=namevalue[0];
	    value=namevalue[1];
	    if(name == param) {
	        return value;
	    }
	}
	return null;
}

 function getParameter(param) {
    return getParameterForWindow(top,param);
 }

// Determine if logging is activated, ie the log window is ready and logging has not been explicitly turned off by the logging parameter
function isLogActive() {
    if(logOff) return false;
    if(logOn) return true;
    if(window.top.lhs && window.top.lhs.log && window.top.lhs.log.document &&  window.top.lhs.log.document.body &&  window.top.lhs.log.document.body.all['log'] && window.top.lhs.log.document.body.all['end'] ) {
        if(getParameter("logging") == "true") {
            logOn= true;
            return true;
        } else {
            logOff= true;
            return false;
        }
    }
    return false;
}

// Logs a message to wherever we're currently logging to.

function log(message)
{
    window.status=message;
    if(isLogActive()) {
		//window.top.lhs.log.document.write(message);
		var logEl= window.top.lhs.log.document.body.all['log'];
		logEl.innerHTML += "<br/><b>" + self.document.title+"</b> : "+(message);
		window.status=message;
		//Opera compatability issue.
		if(!is_opera) {
		    window.top.lhs.log.document.body.all['end'].scrollIntoView();
		}
		//Prune the log if larger than 5000 characters
		if(logEl.innerHTML.length > maxLogCharacters) {
		    //Knock off 1000 characters
		    logEl.innerHTML= logEl.innerHTML.substring(1000);
		}
    }

}

function error(message)
{
    window.status=message;
    log("ERROR: "+message);
}

function fail(message)
{
    top.document.clear();
    top.document.write("<h1 style='color:red'>"+message+"<h1>");
}

function initializeDisplay()
{
    checkBrowser();

    var widthStr= getParameter("frameWidth");
    if(widthStr != null) {
        frameWidth= parseInt(widthStr);
    }
    top.mainFrameset.cols=frameWidth+"%,"+(100-frameWidth)+"%";

    var reload= getParameter("managerReloadMinutes");
    if(reload != null) {
        managerReload= parseInt(reload)*60*1000;
     }
    top.document.title= getDisplayId() + ' - XHIBIT - Public Displays';

    displayCopyright();
    displayConfiguration();
}


function displayConfiguration()
{
    log("-----------------------------------------------------------------------------------");
    log("-----------------------------------------------------------------------------------");
    log("--------------------------- Configuration -----------------------------------------");
    log("-----------------------------------------------------------------------------------");
    log("-- Client side caching of lists is set to a maximum of "+cachingDuration/1000+" seconds.");
    log("-- Manager reload is set at " + managerReload/1000 + " seconds.");
    log("-- Retrying of initialization is set at " + retryInitialization/1000 + " seconds.");
    if(doTopFrameReload)
    {
        log("-- The top frame will be reloaded after " + reloadEntireFrameDelay/1000 + " seconds.");
    }
    log("-- The log will be truncated to " + maxLogCharacters + " characters.");
    log("-- The LHS frame width (frameWidth) is set to "+frameWidth);
    log("-- The RHS will be checked to make sure it loaded okay after "+delayBeforeCheckingRHS/1000+" seconds.");
    log("-- The base web url is '"+BASE_WEB_URL+"'.");
    log("-- display="+ getParameter('display')+" (reqired)");
    log("-- ignoreBrowser="+ getParameter('ignoreBrowser')+" (defaults to false)");
    log("-- ignoreOS="+ getParameter('ignoreOS')+" (defaults to false)");
    log("-- logging="+ getParameter('logging')+" (defaults to false)");
    log("-----------------------------------------------------------------------------------");
    log("-----------------------------------------------------------------------------------");
    log("");
    log("");
}

function displayCopyright() {
    log("-----------------------------------------------------------------------------------");
    log("-----------------------------------------------------------------------------------");
    log("----------------------------- Public Displays  ------------------------------------");
    log("-----------------------------------------------------------------------------------");
    log("--------------------- (c) 2004 Electronic Data Systems ----------------------------");
    log("-----------------------------------------------------------------------------------");
    log("-----------------------------------------------------------------------------------");
}


//
// JavaScript Browser Sniffer
// This has copied over only the required functions from browser_sniffer.js
//
// convert all characters to lowercase to simplify testing
var agt=navigator.userAgent.toLowerCase();
var appVer = navigator.appVersion.toLowerCase();

// *** BROWSER VERSION ***

var is_minor = parseFloat(appVer);
var is_major = parseInt(is_minor);

var is_opera = (agt.indexOf("opera") != -1);
var is_opera2 = (agt.indexOf("opera 2") != -1 || agt.indexOf("opera/2") != -1);
var is_opera3 = (agt.indexOf("opera 3") != -1 || agt.indexOf("opera/3") != -1);
var is_opera4 = (agt.indexOf("opera 4") != -1 || agt.indexOf("opera/4") != -1);
var is_opera5 = (agt.indexOf("opera 5") != -1 || agt.indexOf("opera/5") != -1);
var is_opera6 = (agt.indexOf("opera 6") != -1 || agt.indexOf("opera/6") != -1); // new 020128- abk
var is_opera7 = (agt.indexOf("opera 7") != -1 || agt.indexOf("opera/7") != -1); // new 021205- dmr
var is_opera5up = (is_opera && !is_opera2 && !is_opera3 && !is_opera4);
var is_opera6up = (is_opera && !is_opera2 && !is_opera3 && !is_opera4 && !is_opera5); // new020128
var is_opera7up = (is_opera && !is_opera2 && !is_opera3 && !is_opera4 && !is_opera5 && !is_opera6); // new021205 -- dmr

// Note: On IE, start of appVersion return 3 or 4
// which supposedly is the version of Netscape it is compatible with.
// So we look for the real version further on in the string

var iePos  = appVer.indexOf('msie');
if (iePos !=-1) {
   is_minor = parseFloat(appVer.substring(iePos+5,appVer.indexOf(';',iePos)))
   is_major = parseInt(is_minor);
}

var is_ie   = ((iePos!=-1) && (!is_opera) /*&& (!is_khtml)*/);
var is_ie6   = (is_ie && is_major == 6);
var is_ie6up = (is_ie && is_minor >= 6);

// *** PLATFORM ***
var is_win   = ( (agt.indexOf("win")!=-1) || (agt.indexOf("16bit")!=-1) );
// NOTE: On Opera 3.0, the userAgent string includes "Windows 95/NT4" on all
//        Win32, so you can't distinguish between Win95 and WinNT.
var is_win95 = ((agt.indexOf("win95")!=-1) || (agt.indexOf("windows 95")!=-1));

// is this a 16 bit compiled version?
var is_win16 = ((agt.indexOf("win16")!=-1) ||
           (agt.indexOf("16bit")!=-1) || (agt.indexOf("windows 3.1")!=-1) ||
           (agt.indexOf("windows 16-bit")!=-1) );

var is_win31 = ((agt.indexOf("windows 3.1")!=-1) || (agt.indexOf("win16")!=-1) ||
                (agt.indexOf("windows 16-bit")!=-1));

var is_winme = ((agt.indexOf("win 9x 4.90")!=-1));    // new 020128 - abk
var is_win2k = ((agt.indexOf("windows nt 5.0")!=-1) || (agt.indexOf("windows 2000")!=-1)); // 020214 - dmr
var is_winxp = ((agt.indexOf("windows nt 5.1")!=-1) || (agt.indexOf("windows xp")!=-1)); // 020214 - dmr

// NOTE: Reliable detection of Win98 may not be possible. It appears that:
//       - On Nav 4.x and before you'll get plain "Windows" in userAgent.
//       - On Mercury client, the 32-bit version will return "Win98", but
//         the 16-bit version running on Win98 will still return "Win95".
var is_win98 = ((agt.indexOf("win98")!=-1) || (agt.indexOf("windows 98")!=-1));
var is_winnt = ((agt.indexOf("winnt")!=-1) || (agt.indexOf("windows nt")!=-1));
var is_win32 = (is_win95 || is_winnt || is_win98 ||
                ((is_major >= 4) && (navigator.platform == "Win32")) ||
                (agt.indexOf("win32")!=-1) || (agt.indexOf("32bit")!=-1));


//
// End of browser_sniffer.js
//

function checkBrowser() {

   if(!is_ie6up && !is_opera7up && getParameter('ignoreBrowser') != "true") {
        fail("Only IE 6.0+ and Opera 7.0+ browsers are supported by this application; your browser was detected as '"+agt+"'. Set the parameter ignoreBrowser=true to overide.");
   }

/*   if(!is_frames) {
        fail("Frames required.");
   }*/

   if(!is_winxp && !is_win2k && getParameter('ignoreOS') != "true") {
        fail("Application only tested and guaranteed on Windows XP / Windows 2000. Set the parameter ignoreOS=true to overide.");
   }

}

//
// String utility, trims text to null
//

function trimToNull(text) {
    if(text != null) {
        trimed = text.replace( /^\s+/g, "" );  // strip leading
        trimed = trimed.replace( /\s+$/g, "" );// strip trailing
        if(trimed != "") {
            return trimed;
        }
    }
    return null;
}
