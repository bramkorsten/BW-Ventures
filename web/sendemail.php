<?php
  //var_dump($_POST);
  $hash = $_POST['hash'];
  $startupName = $_POST['startupname'];
  $msg = "Dear " . $startupName . ",

Please setup your venture.
You can create an account here: https://bramkorsten.nl/bluewhale/register/?h=" . $hash . "\nYou can start with your experiments as soon as you've setup your account.

Kind regards,
The Restart team at Blue Whale Ventures";
$headers = 'From: "Maarten van Kroonenburg" <maarten@bwventures.nl>';
  $i=0;
  while ($i<5) {
    if (isset($_POST[$i])) {
      var_dump($_POST[$i]);
      mail($_POST[$i],"Setup your venture at Blue Whale Ventures",$msg, $headers);
    }
    $i++;
  }

  header('Location: dash.html');die;

  //var_dump($_POST['hash']);

?>
