// @flow

import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
require('bootstrap')
<form id = "userForm" action = "http://ceclnx01.cec.miamioh.edu/~kauchaaj/cse383/cse-383-final-group-project/server/src/index.php">
Enter username:
	<input form = "text">
 
  Enter password:
  <input form = "text">
  <input type="submit">
</form>

xmlhttp.open("POST", "http://ceclnx01.cec.miamioh.edu/~kauchaaj/cse383/cse-383-final-group-project/server/src/index.php", true);

$(document).ready(() => {
  // TODO: Add event handlers
  $('userForm').submit(() => {
    // TODO: Add code here
  })
})
