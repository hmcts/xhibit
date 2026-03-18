/*
 * Title:       Key Script Utils
 *
 * Description: Used to trap key sequences on the thinClient and disable
 *
 * Copyright:   Copyright (c) 2004
 * Company:     EDS
 *
 * Author:      Surtar Bachra
 */
document.attachEvent("onkeydown", my_onkeydown_handler);

// disable ctrl-H
function my_onkeydown_handler()
{
     switch (event.keyCode)
     {
       case 72 : // 'ctrl-H'
       if (event.ctrlKey)
       {
         // trap and disable the ctrl-H key
         event.returnValue = false;
       }
       break;
       case 67 : // 'ctrl-C'
       if (event.ctrlKey)
       {
         event.returnValue = false;
       }
       break;
       case 86 : // 'ctrl-V'
       if (event.ctrlKey)
       {
         event.returnValue = false;
       }
       break;
       case 70 : // 'ctrl-F'
       if (event.ctrlKey)
       {
	  event.keyCode=0;
          return false;
       }
       break;
       case 80 : // 'ctrl-P'
       if (event.ctrlKey)
       {
	  event.keyCode=0;
          return false;
       }
       break;
     }
}

// disable F1
function Help()
    {
      return false;
    }
    window.onhelp = Help;
