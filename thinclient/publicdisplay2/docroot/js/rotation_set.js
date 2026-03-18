/*
 * class RotationSet
 *
 * This class represents a collection of DocumentReferences which will be scrolled through
 * by a Rotator.
 *
 * author: Neil Ellis
 *
 */
function RotationSet()
{
	this.documentList = new Array(0);
	//I'm afraid we couldn't find a better place for displayType, maybe some refactoring?
	this.displayType= 'unknown';
	this.add = RotationSet_add;
	this.equals= RotationSet_equals;
}

/*
 * Add a document reference to the rotation set
 */
function RotationSet_add(documentReference)
{
	this.documentList.push(documentReference);
}

/*
 * Comparison function so we can see if a rotation set has been changed.
 */
function RotationSet_equals(rotationSet)
{
    if(this.documentList.length != rotationSet.documentList.length) {
        return false;
    }
    for(var i= 0; i < this.documentList.length; i++)
    {
        if(this.documentList[i].uri != rotationSet.documentList[i].uri ||
           this.documentList[i].delay != rotationSet.documentList[i].delay )
        {
                return false;
        }
     }
     return true;


}
/* end of RotationSet */


/*
 * class DocumentReference
 *
 * This class represents a single DisplayDocument via it's URI and an associated display
 * period (technically a delay).
 *
 * author: Neil Ellis
 *
 */

function DocumentReference(uri, delay) {
	this.uri= uri;
	this.delay= delay;
}

/* end of DocumentReference */

