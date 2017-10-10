<?php
  if(isset($_GET['h'])) {
    $hash = $_GET['h'];
  }
?>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#0099ff">
    <title>Register to access your venture | Blue Whale Ventures</title>
    <link rel="stylesheet" href="styles.css">
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
      var hash = '<?php echo $hash ?>';
    </script>
    <script src="jquery.min.js" charset="utf-8"></script>
    <script src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
    <script src="ventures.js" charset="utf-8"></script>
  </head>
  <body>
    <div id="page-succes" class="page-succes">
      <h2>All done!</h2>
      <h3 class="subtitle"><span class="colorized">Thanks for signing up!</span></h3>
      <p>
        We've sent you an email to <span class="colorized">verify your email adress</span>. You can also download the app for both Android and IOS, to help you with your (ad)venture.
        <br>
        <br>
        The best of luck!
        <br>
        <span class="colorized">The Ventures team</span>
      </p>
      <div class="app-wrap">
        <div class="android"></div>
        <div class="ios"></div>
      </div>
    </div>
    <div class="page_wrap">
      <div class="header">
        <h2>Time to setup your new venture</h2>
        <p>
          Welcome to <span class="colorized">Blue Whale Ventures</span>, we have set up a new venture for you, and we need you to create your personal account with it!
        </p>
      </div>
      <div class="personal">
        <h3 class="subtitle">Lets start with the basics!</h3>
        <form id="basic-form" class="register-form">
          <p class="label">What is your <span class="colorized">name</span>?</p>
          <input id="name" class="text-input" type="text" name="name" required placeholder="Morty Smith" value="">
          <p class="label">And what is your <span class="colorized">email adress</span>?</p>
          <input id="email" class="text-input" type="email" name="email" required placeholder="you@example.com" value="">
          <p class="label">Lets create your new <span class="colorized">password</span>! (Please type it twice)</p>
          <input id="password" class="text-input half" type="password" name="password" required placeholder="xxxxxxxx" value="">
          <input id="password-verify" class="text-input half" type="password" name="passwordverify" required placeholder="xxxxxxxx" value="">
        </form>
      </div>
      <div class="personal">
        <h3 class="subtitle">We also need some personal details.</h3>
        <form id="detail-form" class="register-form">
          <p class="label">Please enter your <span class="colorized">phone number</span>?</p>
          <input id="phone" class="text-input" type="text" name="number" required placeholder="+31612345678" value="">
          <p class="label">And your <span class="colorized">age</span>?</p>
          <input id="age" class="text-input" type="number" name="age" required placeholder="25" value="">
          <p class="label">Last we need to know your <span class="colorized">gender</span>.</p>
          <select id="gender" class="select-input" name="gender">
            <option selected value="male">Male</option>
            <option value="female">Female</option>
            <option value="none">I'd rather not tell</option>
          </select>
        </form>
        <div class="terms">
          <div class="switch-wrap">
            <input type="checkbox" id="acceptterms" required name="terms" class="switch-input checkbox">
            <label for="acceptterms" class="switch-label"></label>
          </div>
          <p>
            I agree with the <span class="colorized">terms of service</span>
          </p>
        </div>
        <p class="submit-register">
          <a onclick="registerUser()">SIGN UP</a>
        </p>
        <p id="form_errors" class="error_message">

        </p>
      </div>
    </div>
  </body>
</html>
