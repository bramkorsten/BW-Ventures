setTimeout(function(){
  document.getElementById("loadscreen-wrap").classList.toggle('remove-splash');
}, 3500);
// }, 10);

setTimeout(function() {
  document.getElementById("loadscreen-wrap").classList.toggle('gone-form');
}, 3800);
// }, 10);


function expandForm() {
  document.getElementById("login-title").classList.toggle('class-hidden');
  document.getElementById("login-form").classList.toggle('form-expand');
  document.getElementById("login-background").classList.toggle('form-filter');
  document.getElementById("true-form").classList.toggle('class-gone');
  document.getElementById("background-img").classList.toggle('add-filter');

  setTimeout(function(){ document.getElementById("login-title").classList.toggle('class-gone'); }, 300);
}

function tryLogin() {
  var inputUsername = document.getElementById('user-input').value;
  var inputPassword = document.getElementById('user-password').value;

    firebase.auth().signInWithEmailAndPassword(inputUsername, inputPassword).catch(function(error) {
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;

    if (errorCode) {
      //alert(errorCode);
      document.getElementById('login-wrap').classList.toggle('wrong-login');

        setTimeout(function(){
          document.getElementById('login-wrap').classList.toggle('wrong-login');
          //document.getElementById('user-input').value="";
          //document.getElementById('user-password').value="";
        }, 500);

    }


    //console.log(errorMessage);
    // ...
  });


}


      firebase.auth().onAuthStateChanged(function(user) {
      if (user) {
        goodLogin();
        getClass();
        console.log(user);
      }
      });

function logout() {
  firebase.auth().signOut().then(function() {
    location.reload();
  }, function(error) {
    return(0);
  });
}

function getClass() {
  var el = document.getElementById('login-background');
  if (hasClass(el, 'form-filter') == false) {
    document.getElementById("login-background").classList.toggle('form-filter');
    document.getElementById("background-img").classList.toggle('add-filter');
  }
}

function hasClass(element, cls) {
    return (' ' + element.className + ' ').indexOf(' ' + cls + ' ') > -1;
}




function goodLogin() {
    document.getElementById("login-form").classList.toggle('form-login-succes');
    document.getElementById("form-logo").classList.toggle('form-logo-center');
}


function getTab(x) {
  if (x == 'clients') {
    document.getElementById('profile-title-active').className='title-border-left';
    document.getElementById('menu-item-clients').className='menu-item menu-item-top menu-item-active';
    document.getElementById('menu-item-experiments').className='menu-item';
    document.getElementById('menu-item-people').className='menu-item';

    document.getElementById('title-clients').className='title-mid-active';
    document.getElementById('title-people').className='';
  }
  if (x == 'experiments') {
    document.getElementById('profile-title-active').className='';
    document.getElementById('menu-item-clients').className='menu-item menu-item-top';
    document.getElementById('menu-item-experiments').className='menu-item menu-item-active';
    document.getElementById('menu-item-people').className='menu-item';

    document.getElementById('title-clients').className='';
    document.getElementById('title-experiments').className='title-mid-active';
    document.getElementById('title-people').className='';
  }
  if (x == 'people') {
    document.getElementById('profile-title-active').className='title-border-right';
    document.getElementById('menu-item-clients').className='menu-item menu-item-top';
    document.getElementById('menu-item-people').className='menu-item menu-item-active';
    document.getElementById('menu-item-experiments').className='menu-item';

    document.getElementById('title-clients').className='';
    document.getElementById('title-people').className='title-mid-active';
  }
}

function startNewCompany() {
  document.getElementById('popup-background').classList.toggle('popup-background-vis');
}

function verifyCompany() {
  document.getElementById('new-company-form').classList.toggle('hide-form');
  document.getElementById('company-success').classList.toggle('gone-form');
  document.getElementById('create-company-div').classList.toggle('create-company-down');
  setTimeout(function(){
    document.getElementById('new-company-form').classList.toggle('gone-form');
    document.getElementById('create-company-div').classList.toggle('gone-form');
  }, 500);
  setTimeout(function(){
    document.getElementById('company-success').classList.toggle('form-not-yet-here');
  }, 50);
}

function getExperiments() {

}
