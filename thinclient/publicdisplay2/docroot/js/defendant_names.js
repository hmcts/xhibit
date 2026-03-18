var defendantNamesCount= 0;
var defendantNames= new Array();

function switchDefendantNames()
{
    log("switchDefendantNames()");
    if(defendantNamesCount < defendantNames.length-1) {
        defendantNamesCount++;
    } else {
        defendantNamesCount=0;
    }
    document.all.defendant.innerHTML= defendantNames[defendantNamesCount];
    setTimeout("switchDefendantNames()",1500);
}
