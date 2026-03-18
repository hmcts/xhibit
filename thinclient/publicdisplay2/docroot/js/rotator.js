

//Our 'singleton'.

var rotator = null;
var firstTime = false;

//NB: live value should be 14400000 (4 hours) delay between automatic complete refreshes.



/*
 * Rotator class.
 *
 * Responsible for rotating through a rotation set and dealing with incoming
 * updates from the manager.
 *
 * author: Neil Ellis
 */
function Rotator()
{
    //The 'instance' variables
    this.currentRotationSet = null;
    this.newRotationSet = null;
    this.documentList = null;
    this.documentNumber = 0;
    this.displayType= 'unknown';
    this.serverOkayFlag= true;
    this.cachingAllowed= true;
    
    //The Methods
    this.rotate = Rotator_rotate;
    this.changeRotationSet = Rotator_changeRotationSet;
    this.managerFramePresent = Rotator_managerFramePresent;
    this.reloadManagerFrame = Rotator_reloadManagerFrame;
    this.processNewRotationSet = Rotator_processNewRotationSet;
    this.moveToNextDocument = Rotator_moveToNextDocument;
    this.moveToNextPage = Rotator_moveToNextPage;
//    this.isServerOkay= Rotator_isServerOkay;
    this.isPageRotationFinished= Rotator_isPageRotationFinished;
    this.getCurrentDisplayFrame= Rotator_getCurrentDisplayFrame;
    this.checkRHSFrameLoadedOkay= Rotator_checkRHSFrameLoadedOkay;
    this.serverOkay= Rotator_serverOkay;
    this.serverNotResponding= Rotator_serverNotResponding;
    this.isServerOkay= Rotator_isServerOkay;
    this.showCurrentDisplayFrame= Rotator_showCurrentDisplayFrame;
    this.heartbeatFramePresent = Rotator_heartbeatFramePresent
    this.heartbeatServer = Rotator_heartbeatServer;
    
  //The constructor
    this.firstTime = true;
    this.rotate();

/*
    //This schedules a reload of the entire frame (to help get around any potential hangs etc.
    //log("The top rame will be reloaded in "+reloadEntireFrameDelay+" milliseconds, this value is in rotator.js and should be about 14400000 (4 hours). If different please arrange for this to be changed.");
    //setTimeout("log('Triggering reload of top frame.');doTopFrameReload=true;", reloadEntireFrameDelay);

*/
}

/** Tell the rotator that the server is okay. **/
function Rotator_serverOkay() {
    this.serverOkayFlag= true;
}

/** Tell the rotator that the server is not responding. **/
function Rotator_serverNotResponding() {
    log("serverNotResponding:: Server Down");
    this.serverOkayFlag= false;
}

/** Is the server okay?? **/
function Rotator_isServerOkay() {
    return this.serverOkayFlag;
}

// This function is called by the manager frame when it is has a new rotation set
// for the rotator to rotate through.
function Rotator_changeRotationSet(rotationSet)
{
    this.newRotationSet = rotationSet;
}


function Rotator_processNewRotationSet()
{
    log("New RotationSet has arrived.");
    if(this.currentRotationSet != null && this.newRotationSet.equals(this.currentRotationSet)) {
        log("New rotation set same as old, no action taken.");
        this.newRotationSet = null;
        return;
    }
    this.currentRotationSet = this.newRotationSet;
    this.documentNumber = 0;
    log("Obtaining document list from rotation set.");
    this.documentList = this.currentRotationSet.documentList;
    this.displayType= this.currentRotationSet.displayType;
    log("Document list obtained okay.");
    this.newRotationSet = null;
}

/** The following function makes use of the buffering/caching aspects of Public Display.
    The RHS contains a set of frames which are sequentially loaded with the current document.
    As we pass onto the next document we load up the next frame and then make that frame
    100% in size.

    When we detect the server is not present we keep cycling through the frames but stop
    writing to them; this means the user just sees previously cached documents.

**/
function Rotator_moveToNextDocument()
{
     if(doTopFrameReload) {
         log("Reloading top frame as requested.");
         top.location.reload();
         return;
     }
     if(this.newRotationSet != null)
     {
         log("New rotation set has arrived.");
         this.processNewRotationSet();
     }

    log("Moving to the next document.");
    try {
        this.getCurrentDisplayFrame().frameVisible= false;
    } catch(e) {
        log("Could not tell the frame it was not visible.");
        this.checkRHSFrameLoadedOkay()
    }

    if(this.documentNumber < this.documentList.length-1)
    {
        if(this.firstTime == true)
        {
            this.firstTime = false;
        }
        else
        {
            this.documentNumber++;
        }
    }
    else
    {
        this.documentNumber = 0;
    }

    var newUri= this.documentList[this.documentNumber].uri;
    log("Displaying: "+ newUri+" for "+this.documentList[this.documentNumber].delay);

    var newURL= BASE_WEB_URL+newUri+'&displayType='+this.displayType;
    var bufferFrame= this.getCurrentDisplayFrame();


    /*
        This logic reloves around the complexities of caching documents.
        We basically have three modes:

            1) The server is present
            2) The server is not present and we are not caching.
            3) The server is not present and we are caching.

        Caching can only go on for a limited period of time after which we
        switch it back off again.

     */
    if(this.isServerOkay()) {
        log("Using live page as server is available and placing in a buffer.");
        bufferFrame.location.href= newURL;

        //Double check the frame loaded okay.
        //If it is then it will show the current display frame.
        setTimeout("rotator.checkRHSFrameLoadedOkay()", delayBeforeCheckingRHS);

        //We now reset the caching mode to true in case we lose the server again.
        this.cachingAllowed= true;
    }
        else if(!this.cachingAllowed)
    {
       log("Server not okay but caching switched off so showing live page immediately.");
       this.reloadManagerFrame();
       bufferFrame.location.href= newURL;
       try {
        bufferFrame.frameVisible= true;
       } catch(e) {
        log("Could not set buffer frame to be visible.");
        this.checkRHSFrameLoadedOkay()
       }

           setTimeout("rotator.showCurrentDisplayFrame()", delayBeforeCheckingRHS);

    }
        else
    {
        log("Using cached page as server is unavailable.");
        this.reloadManagerFrame();
        try {
            log("Resetting the buffer frame so that we can rotate the pages.");
            bufferFrame.thisDisplayDocument.reset();
        bufferFrame.frameVisible= true;
        } catch(e) {
            log("Couldn't reset buffer frame, probably wasn't loaded.");
        this.checkRHSFrameLoadedOkay()
        }
            this.showCurrentDisplayFrame();
        setTimeout("rotator.cachingAllowed= false", cachingDuration);
    }

}

/** Okay so we requested that RHS Frame change, but did it?
    This function checks to see if it has, if not try going
    back to the previous page.
 **/
function Rotator_checkRHSFrameLoadedOkay()
{
    var loaded= false;
    try {
        loaded= this.getCurrentDisplayFrame().loaded;
    } catch (e) {
        loaded= false;
    }
    if(loaded) {
        log("RHS loaded okay");
     } else {
        log("RHS not loaded using whatever is in the history if any.");
    try {
            this.getCurrentDisplayFrame().history.back();
            this.getCurrentDisplayFrame().history.forward();
    } catch(e) {
        log("Could not go back then forwards to show cached copy.");
    }
        this.serverNotResponding();
     }
     this.showCurrentDisplayFrame();
}

function Rotator_moveToNextPage()
{
    log("Moving to the next page.");
    var frame= this.getCurrentDisplayFrame();
    frame.thisDisplayDocument.move();
}

function Rotator_getCurrentDisplayFrame()
{
    var frameNumber= this.documentNumber % window.top.rhs.frames.length;
    var bufferFrame= window.top.rhs.frames[frameNumber];
    return bufferFrame;
}

function Rotator_showCurrentDisplayFrame()
{
    log("Showing current display frame");
    var frameNumber= this.documentNumber % window.top.rhs.frames.length;
    var rowStr="";
    for(var i=0; i <  window.top.rhs.frames.length; i++ ) {
        if(i != 0) {
            rowStr+=",";
        }
        if(i == frameNumber) {
            rowStr+="100%";
         } else {
            rowStr+="0%";
         }
    }
    log(rowStr);
    window.top.rhs.document.all.bufferframes.rows= rowStr;
    log("Frame displayed.");
}

// Check to see if the current displayDocument has a 'thisDisplayDocument' static variable
// which indicates that it potentially has multiple pages.
// If it has and it's state is not 'finished' then return true
function Rotator_isPageRotationFinished()
{
    var frame= this.getCurrentDisplayFrame();
    return frame &&
            frame.thisDisplayDocument != null &&
            !frame.thisDisplayDocument.finished;
}

// This forms the main loop of the rotator. First it prods the manage frame if it
// doesn't exist it then looks for new rotation sets checks that there is one to start
// with and if there is it starts rotating. If it is applicable to rotate through pages
// it will otherwise it will go straight to the next document.
function Rotator_rotate()
{
    try {
        log("Starting rotate.");
        if(!this.managerFramePresent())
        {
            log("No manager frame so attempting a reload of the manager frame.");
            this.newRotationSet= null;
            this.serverNotResponding();
            this.reloadManagerFrame();
        }
        if(!this.heartbeatFramePresent())
        {
            log("No heartbeat frame so attempting a reload of the heartbeat frame.");
            this.serverNotResponding();
            this.heartbeatServer();
        }
        log("Scheduled rotate.");


        if(this.isPageRotationFinished())
        {
            log("Moving to next page.");
            this.moveToNextPage();
        }
        else
        {
            log("Moving to next document.");
            if(this.newRotationSet != null)
            {
                     log("New rotation set has arrived.");
                     this.processNewRotationSet();
             }

            if(this.currentRotationSet == null || this.documentList == null)
            {
                log("Rotator not initialized yet.");
                this.reloadManagerFrame();
                setTimeout("rotator.rotate()", retryInitialization);
                log("Awaiting update from manager frame.");
                return;
            }
            this.moveToNextDocument();
        }

        pageDelay = this.documentList[this.documentNumber].delay * 1000;
        
        // Send a heart beat request 5 seconds before the page is required
        // to rotate.
        if (pageDelay < 10000) heartbeatDelay = 5000;
        else heartbeatDelay = pageDelay - 5000;

        setTimeout("rotator.heartbeatServer()", heartbeatDelay);
    
        setTimeout("rotator.rotate()", pageDelay);

        log("Finished rotate.");
    } catch(e) {
        error("Error in Rotator.rotate() : "+e.message);
        this.reloadManagerFrame();
        setTimeout("rotator.rotate()",1000);
        // Do not do this in production.
//        throw e;
    }
}

// If the manager frame is not ready we attempt to reload it.
function Rotator_reloadManagerFrame()
{
    log("Trying to load the manager frame .");
    try {
        //We set init=true to request the jsp to provide our initial rotation set.
        top.lhs.manager.location.href = 'FileServlet?init=true&uri=' + getDisplayId()+'';
    } catch(e) {
        error(e);
        this.serverNotResponding();
    }
}

// Reload the heartbeat page to check server is alive
function Rotator_heartbeatServer()
{
    log("Trying to load the heart beat frame .");
    try {
        //We set init=true to request the jsp to provide our initial rotation set.
        top.lhs.heartbeat.location.href = 'heartbeat.html?uri=' + getDisplayId()+'';
    } catch(e) {
        error(e);
        this.serverNotResponding();
    }

}

//Have we got a manager frame yet?
function Rotator_managerFramePresent()
{
    try {
      return window.top.lhs.manager.allOkay;
    } catch (e) {
         this.serverNotResponding();
         return false;
    }
}

//Have we got a manager frame yet?
function Rotator_heartbeatFramePresent()
{
    try {
      return window.top.lhs.heartbeat.allOkay;
    } catch (e) {
         this.serverNotResponding();
         return false;
    }
}

