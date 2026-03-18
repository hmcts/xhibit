
To test CCN0361 changes run 

@CCN0361_test.sql

and create a CUSMSEC and CUSMCUR disposal.

To restore the DB to the pre CCN0361 level run

@CCN0361_revert.sql

Note that the CCN0361_test.sql script adds values to the DB that would be added 
on the Crest side by the equivalent Crest CCN0361 change.  The values would
normally be brough over from Crest by Mercator.