<?php
  if(isset($_GET['h'])) {
    $hash = $_GET['h'];
  }
?>
<script src="https://www.gstatic.com/firebasejs/4.5.0/firebase.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.5.0/firebase-firestore.js"></script>
<script>
  // Initialize Firebase
  var config = {
    apiKey: "AIzaSyAcs8FGJQ64vgvhvwf2wOgj_fwSVhD_2SU",
    authDomain: "blue-whale-ventures.firebaseapp.com",
    databaseURL: "https://blue-whale-ventures.firebaseio.com",
    projectId: "blue-whale-ventures",
    storageBucket: "blue-whale-ventures.appspot.com",
    messagingSenderId: "573936868876"
  };
  firebase.initializeApp(config);
</script>
<script lang="javascript">
  firebase.auth().signInWithEmailAndPassword("bot", "123456").catch(function(error) {
  // Handle Errors here.
  var errorCode = error.code;
  var errorMessage = error.message;
  // ...
  });


  var jsonResponse = new Object();
  var hash = '<?php echo $hash ?>';
  console.log(hash);
  var database = firebase.firestore();
  var venturesRef = database.collection('Startups').doc(hash.toString());
  venturesRef.get().then(function(doc) {
      if (doc.exists) {
        if (doc.data()['hashValid']) {
           jsonResponse.valid = true;
           jsonResponse.name = doc.data()['Name'];
           var jsonString = JSON.stringify(jsonResponse);
           document.write(jsonString);
        }
      } else {
        jsonResponse.valid = false;
        jsonResponse.name = doc.data()['NULL'];
        var jsonString = JSON.stringify(jsonResponse);
        document.write(jsonString);
      }
  }).catch(function(error) {
      console.log("Error getting document:", error);
      jsonResponse.valid = false;
      jsonResponse.error = error;
      jsonResponse.name = doc.data()['NULL'];
      var jsonString = JSON.stringify(jsonResponse);
      document.write(jsonString);
  });
</script>
