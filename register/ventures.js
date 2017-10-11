var database = firebase.firestore();

var usersRef = database.collection('users');

var user, email, name, gender, number, age, registering;

function registerUser() {

  $('.register-form').each(function(){

      $(this).validate({
        rules: {
            name: {
                required: true
            },
            password: {
                required: true,
                minlength: 4
            },
            passwordverify: {
                required: true,
                minlength: 4
            },
            number: {
                required: true,
                minlength: 4
            },
            age: {
                required: true,
                minlength: 2
            },
            gender: {
                required: true
            },
            terms: {
                required: true
            }
        },
        errorPlacement: function(error, element) {

        }
      });

  });

  if (!$('#basic-form').valid()){
    $('#form_errors').text("Please fill in all fields correctly.");
  }
  if (!$('#detail-form').valid()){
    $('#form_errors').text("Please fill in all fields correctly.");
  }
  else if (!$('#acceptterms').prop('checked') == true) {
    $('#form_errors').text("Please accept the terms of service.");
  }
  if ((!$('#email').val()) || (!$('#password').val()) || (!$('#password-verify').val())) {
    $('#form_errors').text("Please fill in all fields correctly.");
  }
  else if (($('#password').val()) != ($('#password-verify').val())){
    $('#form_errors').text("The passwords you've entered don't match.");
  }

  else {

  $('#form_errors').text("");
  email = $('#email').val().trim();
  var password = $('#password').val().trim();
  name = $('#name').val().trim();
  gender = $('#gender').val().trim();
  number = $('#phone').val().trim();
  age = $('#age').val().trim();

  firebase.auth().signInWithEmailAndPassword(email, " ").catch(function(error) {
      if(error.code === "auth/wrong-password") {
          $('#form_errors').text("the email adress you've entered already exists.");
      } else if(error.code === "auth/user-not-found"){
        registering = true;

        firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
          // Handle Errors here.
          var errorCode = error.code;
          var errorMessage = error.message;
          // ...
        });
      }
  });
}
}

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    user = user;
    // var displayName = user.displayName;
    // var email = user.email;
    // var emailVerified = user.emailVerified;
    // var photoURL = user.photoURL;
    // var isAnonymous = user.isAnonymous;
    // var uid = user.uid;
    // var providerData = user.providerData;

    if (registering) {
      createUserProfile(user);
    }

  } else {

  }
});

function createUserProfile(user) {
  var venturesRef = database.collection('Startups').doc(hash.toString());
  venturesRef.get().then(function(doc) {
      if (doc.exists) {
        if (doc.data()['hashValid']) {

        }
      } else {

      }
  }).catch(function(error) {

  });
  usersRef.doc(user.uid).set({
    name: name,
    email: email,
    phoneNumber: number,
    age: age,
    gender: gender,
    uid: user.uid
  })
  .then(function() {
      sendVerification(user);
  })
  .catch(function(error) {
  });

  user.updateProfile({
    displayName: name,
    phoneNumber: number
  }).then(function() {
    user = user;
  }).catch(function(error) {
    // An error happened.
  });
}

function sendVerification(user) {
  user.sendEmailVerification().then(function() {
    $('#page-succes').addClass('visible');
    logOut();
  }).catch(function(error) {
    // An error happened.
  });
}

function logOut() {
  firebase.auth().signOut().then(function() {
    //location.reload();
  }, function(error) {
    return(0);
  });
}
