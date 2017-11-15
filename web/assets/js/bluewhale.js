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
var db = firebase.firestore();

//-------------------------------------------------------------
//-------------------------------------------------------------
//-------------------------------------------------------------
//-------------------------------------------------------------
//INITIALISEER STORAGE. ALLEEN DOEN ALS DIT NODIG IS!!!!!!!!!
var storage = firebase.storage(); //---------------------------
//-------------------------------------------------------------
//-------------------------------------------------------------
//-------------------------------------------------------------
//-------------------------------------------------------------


var loggedInUser;
firebase.auth().onAuthStateChanged(function(user) {
if (user) {
  console.log(user);
  loggedInUser = user.uid;
  if (document.getElementById('login-wrap')) {
      goodLogin();
      getClass();
    }

    else {
      //GET LOGGED IN USER
      db.collection("users").doc(loggedInUser.toString()).get().then(function(doc) {
        console.log(doc.data());
        var nameLetterLoginGiven = doc.data().Name.charAt(0);
        var nameLetterLoginSur = doc.data().Surname.split(" ").pop().charAt(0);
        document.getElementById('profile-letters-big').innerHTML = nameLetterLoginGiven + nameLetterLoginSur;
        document.getElementById('profile-info-wrap').innerHTML = '<h2 class="profile-info-name">' + doc.data().Name + ' ' + doc.data().Surname + '</h2><span class="profile-info-function">CEO</span><span class="profile-info-company">Blue Whale Ventures</span>';
      });
    }
  }
else {
  if (document.getElementById('popup-background')) {
    //alert('foute inlog');
    window.location = "index.html";
  }
}
});

var allPeople = [];
var allVentures = [];




//GET ALL STARTUPS
if(document.getElementById("cards-wrap-people")){
  db.collection("Startups").get().then((querySnapshot) => {
    var startupCounter=0;
    querySnapshot.forEach((doc) => {
    var i=0;
    allVentures[startupCounter]= doc.data();
    startupCounter++;

    db.collection("Startups").doc(doc.data().hash).collection('Experiments').get().then((querySnapshot2) => {
      querySnapshot2.forEach((doc) => {
        i++;
        doneLoad();
      });
      var nameLetter = doc.data().Name.charAt(0);
      document.getElementById('cards-wrap-clients').innerHTML ='<div onClick="setExperiments(\'' + doc.data().hash + '\', \'' + doc.data().Name + '\');" class="single-card"><span class="card-circle circle-active">' + nameLetter + '</span><h3 class="card-company-name">' + doc.data().Name + '</h3><div class="projects-amount-wrap"><span class="projects-number">' + i + '</span></div></div>' + document.getElementById('cards-wrap-clients').innerHTML;
      // document.getElementById('cards-wrap-clients').innerHTML+='<div class="single-card card-plus" onClick="startNewCompany();"><i class="fa fa-plus-circle" aria-hidden="true"></i></div>';

    });

  });
  });
}

//GET ALL PEOPLE
if(document.getElementById("cards-wrap-people")){
  db.collection("users").get().then((querySnapshotUsers) => {
    var i=0;
    querySnapshotUsers.forEach((doc) => {
      var nameLetter2given = doc.data().Name.charAt(0);
      var nameLetter2sur = doc.data().Surname.split(" ").pop().charAt(0);

      var nameLetter2 = nameLetter2given + nameLetter2sur;


      // document.getElementById('cards-wrap-people').innerHTML+='<div class="single-person"><span class="card-circle circle-active person-circle">' + nameLetter2 + '</span><div class="single-person-name">' + doc.data().Name + ' ' + doc.data().Surname + '<span class="person-email">' + doc.data().Email + '</span></div><span class="single-person-company">KOMTNOG</span></div>';
      allPeople[i] = doc.data();
      currentVentureId = allPeople[i]['ventureID'];

      for (ventureCounter = 0; ventureCounter < allVentures.length; ventureCounter++) {
        //alert(allVentures[ventureCounter]['hash']);
        if (allVentures[ventureCounter]['hash'] == currentVentureId) {
          document.getElementById('cards-wrap-people').innerHTML+='<div class="single-person"><span class="card-circle circle-active person-circle">' + nameLetter2 + '</span><div class="single-person-name">' + doc.data().Name + ' ' + doc.data().Surname + '<span class="person-email">' + doc.data().Email + '</span></div><span class="single-person-company">' + allVentures[ventureCounter]['Name'] + '</span></div>';
        }
      }

      i++;
    });
  //  console.log(allPeople);
    //console.log(allVentures);
  });
}

var allQuestions;

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

    setTimeout(function() {
      document.getElementById("white-overlay").classList.toggle('overlay-gone');
    }, 300);

    setTimeout(function() {
      window.location = "dash.html";
    }, 500);
}


function getTab(x) {
  if (x == 'clients') {
    // document.getElementById('profile-title-active').className='title-border-left';
    // document.getElementById('menu-item-clients').className='menu-item menu-item-top menu-item-active';
    // document.getElementById('menu-item-experiments').className='menu-item';
    // document.getElementById('menu-item-people').className='menu-item';

    document.getElementById('title-clients').className='title-mid-active';
    document.getElementById('title-experiments').className='experiments-hidden';
    document.getElementById('title-people').className='';
    document.getElementById('profile-title-active').className='title-border-left';


    goClients();
    backToClients();

  }
  if (x == 'experiments') {
    // document.getElementById('profile-title-active').className='';
    // document.getElementById('menu-item-clients').className='menu-item menu-item-top';
    // document.getElementById('menu-item-experiments').className='menu-item menu-item-active';
    // document.getElementById('menu-item-people').className='menu-item';

    document.getElementById('title-clients').className='';
    document.getElementById('title-experiments').className='title-mid-active';
    document.getElementById('title-people').className='';
    document.getElementById('profile-title-active').className='title-border-center';

    goClients();
  }
  if (x == 'people') {
    // document.getElementById('profile-title-active').className='title-border-right';
    // document.getElementById('menu-item-clients').className='menu-item menu-item-top';
    // document.getElementById('menu-item-people').className='menu-item menu-item-active';
    // document.getElementById('menu-item-experiments').className='menu-item';

    document.getElementById('title-clients').className='';
    document.getElementById('title-people').className='title-mid-active';
    if (document.getElementById('title-experiments').className=='experiments-hidden') {
      document.getElementById('profile-title-active').className='title-border-semi-right';
      document.getElementById('title-experiments').className='experiments-hidden';
    }
    else {
      document.getElementById('profile-title-active').className='title-border-right';
      document.getElementById('title-experiments').className='';
    }


    goPeople();
  }
}
function startNewCompany() {
  document.getElementById('popup-background').classList.toggle('popup-background-vis');
}

var currentHash;

function verifyCompany() {
  newStartupName=document.getElementById('input-company-name').value;
  newStartupLocation=document.getElementById('input-company-location').value;
  newStartupStreet=document.getElementById('input-company-street').value;
  newStartupZip=document.getElementById('input-company-zip').value;

  db.collection("Startups").add({
    Name: newStartupName,
    hash: "emptyhash",
    hashValid: true
  })
  .then(function(docRef) {
      console.log("Document written with ID: ", docRef.id);
      document.getElementById('success-code').innerHTML = docRef.id;
      currentHash = docRef.id;
      db.collection("Startups").doc(docRef.id).update({
        Name: newStartupName,
        hash: docRef.id,
        hashValid: true,
        startupLocation: newStartupLocation,
        street: newStartupStreet,
        ZIPcode: newStartupZip
      });
  })
  .catch(function(error) {
      console.error("Error adding document: ", error);
  });

  document.getElementById('success-title').innerHTML = newStartupName + ' succesfully created!';

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

var emails = [];

function setEmail() {
  if (emails.length < 5) {
    document.getElementById('emails-wrap').innerHTML='';
    enteredEmail = document.getElementById('input-emails').value;
    curEmails = emails.length;
    emails[curEmails] = enteredEmail;
    for (emailCounter = 0; emailCounter < emails.length; emailCounter++) {
      document.getElementById('emails-wrap').innerHTML+='<span onClick="removeEmail(' + emailCounter + ')">' + emails[emailCounter] + '</span>';
    }
  }
  if (emails.length > 0) {
    document.getElementById('send-email-div').classList.remove('create-company-down');
  }
}

function removeEmail(x) {
  emails.splice(x, 1);
  if (emails.length < 1) {
    document.getElementById('send-email-div').classList.add('create-company-down');
  }
  document.getElementById('emails-wrap').innerHTML='';
  for (emailCounter = 0; emailCounter < emails.length; emailCounter++) {
    document.getElementById('emails-wrap').innerHTML+='<span onClick="removeEmail(' + emailCounter + ')">' + emails[emailCounter] + '</span>';
  }
}

function sendEmail() {
    method = "post";

    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", "sendemail.php");

    for(var key in emails) {
        if(emails.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", emails[key]);

            form.appendChild(hiddenField);
        }
    }
    var inputField = document.createElement("input");
    inputField.setAttribute("type", "hidden");
    inputField.setAttribute("name", "hash");
    inputField.setAttribute("value", currentHash);
    form.appendChild(inputField);

    var inputField2 = document.createElement("input");
    inputField2.setAttribute("type", "hidden");
    inputField2.setAttribute("name", "startupname");
    inputField2.setAttribute("value", newStartupName);
    form.appendChild(inputField2);

    document.body.appendChild(form);
    form.submit();
}

function goPeople() {
  // document.getElementById('cards-wrap-clients').className='cards-wrap clients-left';
  document.getElementById('left-side-wrap').className='left-side-wrap clients-left';
  document.getElementById('cards-wrap-people').className='cards-contain';
}

function goClients() {
  //document.getElementById('cards-wrap-clients').className='cards-wrap';
  //document.getElementById('cards-wrap-people').className='cards-contain projects-right';
  document.getElementById('left-side-wrap').className='left-side-wrap';
  document.getElementById('cards-wrap-people').className='cards-contain projects-right';
  doneLoad();
}

function setBubble() {
  document.getElementById('dropdown-bubble').classList.toggle('bubble-gone');
}


function setExperiments(startupHash, startupName) {
  startLoad();
  document.getElementById('cards-clients-contain').classList.toggle('experiments-cards-gone');
  document.getElementById('cards-experiments-contain').classList.toggle('experiments-cards-gone');
  document.getElementById('title-experiments').className='title-mid-active';
  document.getElementById('title-clients').className='';
  document.getElementById('profile-title-active').className='title-border-center';
  document.getElementById('cards-wrap-experiments').className='cards-wrap';
  document.getElementById('experiment-questions-wrap').className='questions-wrap questions-gone';
  //checkAudioPlayer();

  document.getElementById('cards-wrap-experiments').innerHTML='';
  document.getElementById('experiments-banner-name').innerHTML = startupName + ' - Experiments';

  var allExperiments = [];
  db.collection("Startups").doc(startupHash).collection('Experiments').get().then((querySnapshot3) => {
    experimentCounter=0;
    querySnapshot3.forEach((doc) => {
      //console.log(doc.data().ExperimentNumber);
      allExperiments[experimentCounter] = doc.data();
      allExperiments[experimentCounter]['id'] = doc.id;
        //document.getElementById('cards-wrap-experiments').innerHTML+= '<div class="single-card" onClick="getQuestions(\'' + startupHash + '\', \'' + doc.id + '\');"><span class="card-circle card-circle-experiments">' + doc.data().ExperimentNumber + '</span><div class="experiments-info"><span class="experiments-info-name">' + doc.data().ExperimentName + '</span><span class="experiments-info-sub">' + doc.data().ExperimentSubtitle + '</span><span class="experiments-info-results">No results yet</span></div><div class="projects-amount-wrap experiments-ellipsis-wrap"><i class="fa fa-ellipsis-v" aria-hidden="true" onClick= "getExperimentSettings();"></i></div></div>';
        experimentCounter++;
    });

    //console.log(allExperiments);
        var byNumber = allExperiments.slice(0);
    byNumber.sort(function(a,b) {
        return a.ExperimentNumber - b.ExperimentNumber;
    });
    startDate = new Date(byNumber[0]['DateCreated']);
    curDate = new Date();
    console.log(startDate);
    console.log(curDate);
    var diff = Math.abs(startDate-curDate);
    diff = Math.round(diff / (1000*3600*24));
    console.log(diff);
    document.getElementById('single-day').innerHTML=diff;

    for (experimentIndex = 0; experimentIndex < byNumber.length; experimentIndex++) {
      document.getElementById('cards-wrap-experiments').innerHTML+= '<div class="single-card" onClick="getQuestions(\'' + startupHash + '\', \'' + byNumber[experimentIndex]['id'] + '\');"><span class="card-circle card-circle-experiments">' + byNumber[experimentIndex]['ExperimentNumber'] + '</span><div class="experiments-info"><span class="experiments-info-name">' + byNumber[experimentIndex]['ExperimentName'] + '</span><span class="experiments-info-sub">' + byNumber[experimentIndex]['ExperimentSubtitle'] + '</span><span class="experiments-info-results">No results yet</span></div><div class="projects-amount-wrap experiments-ellipsis-wrap"><i class="fa fa-ellipsis-v" aria-hidden="true" onClick= "getExperimentSettings(\'' + startupHash + '\', \'' + byNumber[experimentIndex]['id'] + '\');"></i></div></div>';
      doneLoad();
    }
  });
}

function backToClients() {
  // document.getElementById('cards-clients-contain').classList.toggle('experiments-cards-gone');
  // document.getElementById('cards-experiments-contain').classList.toggle('experiments-cards-gone');
  document.getElementById('cards-clients-contain').className='cards-contain';
  document.getElementById('cards-experiments-contain').className='cards-contain experiments-cards-gone';
}

function getExperimentSettings(startupHash, projectId) {
  document.getElementById('popup-background2').classList.toggle('popup-background-vis');
  document.getElementById('experiment-data-title').innerHTML='';
  document.getElementById('experiment-data-data').innerHTML='';

  db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).get().then(function(doc) {
    console.log(doc.data());
    document.getElementById('experiment-data-title').innerHTML='Experiment ' + doc.data().ExperimentNumber;
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Experiment Name</div><div class="experiment-data data-value">' + doc.data().ExperimentName + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Experiment Subtitle</div><div class="experiment-data data-value">' + doc.data().ExperimentSubtitle + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Date Created</div><div class="experiment-data data-value">' + doc.data().DateCreated + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Problem Hypothesis</div><div class="experiment-data data-value">' + doc.data().ProblemHypothesis + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Number of Interviews</div><div class="experiment-data data-value">' + doc.data().NumberInterviews + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Customer Location</div><div class="experiment-data data-value">' + doc.data().CustomerLocation + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Customer Segment</div><div class="experiment-data data-value">' + doc.data().CustomerSegment + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Learning Goal</div><div class="experiment-data data-value">' + doc.data().LearningGoal + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Fail Condition</div><div class="experiment-data data-value">' + doc.data().FailCondition + '</div>';
    document.getElementById('experiment-data-data').innerHTML+='<div class="experiment-data data-string">Stop Condition</div><div class="experiment-data data-value">' + doc.data().StopCondition + '</div>';
  });
}

function closeExperimentSettings() {
  document.getElementById('popup-background2').classList.toggle('popup-background-vis');
}

function getQuestions(startupHash, projectId) {
  if (document.getElementById('popup-background2').classList.contains('popup-background-vis')) {
    return;
  }
  startLoad();
  document.getElementById('cards-wrap-experiments').classList.toggle('experiments-cards-gone');
  document.getElementById('experiment-questions-wrap').classList.toggle('questions-gone');
  document.getElementById('full-audio-wrap').innerHTML='';

  // document.getElementById('experiment-questions-wrap').innerHTML='';
  // document.getElementById('experiment-questions-wrap').innerHTML+='<div id="dropdown-names" class="dropdown-names bubble-gone"></div>';
  // document.getElementById('experiment-questions-wrap').innerHTML+='<h3 id="questions-wrap-experiment-title"></h3><h4 id="questions-wrap-experiment-name"></h4><h5>Person interviewed:</h5>';
  // document.getElementById('experiment-questions-wrap').innerHTML+='<div class="person-name-wrap" onClick="dropdownNames();"><i class="fa fa-chevron-left" aria-hidden="true"></i> <span id="person-name">No person selected</span><i class="fa fa-chevron-right" aria-hidden="true"></i></div>';
  // document.getElementById('experiment-questions-wrap').innerHTML+='<div id="stepper-wrap" class="stepper-wrap"></div>';

  document.getElementById('dropdown-names').innerHTML='';
  document.getElementById('questions-wrap-experiment-title').innerHTML='';
  document.getElementById('questions-wrap-experiment-name').innerHTML='';
  document.getElementById('stepper-wrap').innerHTML='';
  document.getElementById('person-name').innerHTML='No person selected';

  //checkAudioPlayer();

  db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).get().then(function(doc) {
    //console.log(doc.data());
      document.getElementById('questions-wrap-experiment-title').innerHTML='Experiment ' + doc.data().ExperimentNumber;
      document.getElementById('questions-wrap-experiment-name').innerHTML= doc.data().ExperimentName;
      allQuestions = doc.data().questions;
      //console.log(allQuestions);
      doneLoad();

  });

  db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).collection('people').get().then((querySnapshot4) => {
    querySnapshot4.forEach((doc) => {
      var ref = doc.data().person;
      ref.get().then(function(doc) {
        //console.log(doc.data());
        //console.log(doc.id);
        document.getElementById('dropdown-names').innerHTML+='<div class="dropdown-name" onClick="getQuestionsFromName(\'' + startupHash + '\', \'' + projectId + '\', \'' + doc.id + '\');">' + doc.data().Name + ' ' + doc.data().Surname + '</div>';
      });
    });
  });


}
var numberOfQuestions;
var personAnswers = [];
var questionData = {};

function dropdownNames() {
  document.getElementById('dropdown-names').classList.toggle('bubble-gone');
}

var audioPlayer;
var durationAudio;

function getQuestionsFromName(startupHash, projectId, personId) {
  startLoad();
  document.getElementById('stepper-wrap').innerHTML='';
  document.getElementById('dropdown-names').classList.add('bubble-gone');
  document.getElementById('full-audio-wrap').innerHTML='';

  //checkAudioPlayer();

  db.collection("Startups").doc(startupHash).collection('Testers').doc(personId).get().then(function(doc) {
    //console.log(doc.data());
    document.getElementById('person-name').innerHTML=doc.data().Name + ' ' + doc.data().Surname;
  });

  // db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).collection('people').doc(personId).get().then(function(doc) {
  //   personAnswers = doc.data().Answers;
  //
  //   document.getElementById('stepper-wrap').innerHTML='';
  //   for (i = 0; i < allQuestions.length; i++) {
  //     var qNumber = i+1;
  //     document.getElementById('stepper-wrap').innerHTML+= '<div id="single-stepper-wrap' + qNumber + '" class="single-stepper-wrap"><div class="single-stepper"><div id="stepper-number' + qNumber + '" class="stepper-number">' + qNumber + '</div><div class="stepper-question">' + allQuestions[i] + '</div><div id="stepper-comment' + qNumber + '" class="stepper-comment stepper-comment-inactive" onClick="setComment(' + qNumber + ');"><i id="question-pencil' + qNumber + '" class="fa fa-pencil" aria-hidden="true"></i></div></div><div id="stepper-answer' + qNumber + '" class="stepper-answer"><textarea id="textarea-question' + qNumber + '" class="stepper-textarea"></textarea></div></div>';
  //     //doneLoad();
  //   }
  //
  //   for (answerCounter = 0; answerCounter < personAnswers.length; answerCounter++) {
  //     currentAnswer = answerCounter+1;
  //     document.getElementById('stepper-answer' + currentAnswer).innerHTML = personAnswers[answerCounter]['Answer'] + '<textarea id="textarea-question' + currentAnswer + '" class="stepper-textarea">' + personAnswers[answerCounter]['Notes'] + '</textarea>';
  //     document.getElementById('stepper-comment' + currentAnswer).classList.remove('stepper-comment-inactive');
  //     doneLoad();
  //   }
  // });

  db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).collection('people').doc(personId).get().then(function(doc) {
      questionData = doc.data().questionData;
      //console.log(questionData);
      console.log(doc.data());



    for (var key in questionData) {

        var obj = questionData[key];
        for (var prop in obj) {
            //console.log(key + ' ' + prop + " = " + obj[prop]);
            if (prop == 'answer') {
              //alert(obj[prop]);
              document.getElementById('stepper-wrap').innerHTML+= '<div id="single-stepper-wrap' + key + '" class="single-stepper-wrap"><div class="single-stepper"><div id="stepper-number' + key + '" class="stepper-number">' + key + '</div><div id="stepper-question' + key + '" class="stepper-question"></div><div id="stepper-comment' + key + '" class="stepper-comment" onClick="setComment(\'' + startupHash + '\', \'' + projectId + '\', \'' + personId + '\', ' + key + ');"><i id="question-pencil' + key + '" class="fa fa-pencil" aria-hidden="true"></i></div></div><div id="stepper-answer' + key + '" class="stepper-answer">' + obj[prop] + '<textarea id="textarea-question' + key + '" class="stepper-textarea"></textarea></div></div>';
            }
            if (prop == 'notes') {
              document.getElementById('textarea-question' + key).innerHTML=obj[prop];
            }
            if (prop == 'question') {
              document.getElementById('stepper-question' + key).innerHTML=obj[prop];
            }
            numberOfQuestions = key;
            doneLoad();
          }
    }

//   "questionData": {
//     1: {
//       "answer": "Antwoord1",
//       "notes": "test1",
//       "question": "question1?",
//       "questionNumber": 1
//     },
//     2: {
//       "answer": "Antwoord2",
//       "notes": "test2",
//       "question": "question2?",
//       "questionNumber": 2
//     },
//     3: {
//       "answer": "Antwoord3",
//       "notes": "test3",
//       "question": "question3?",
//       "questionNumber": 3
//     },
//     4: {
//       "answer": "Antwoord4",
//       "notes": "test4",
//       "question": "question4?",
//       "questionNumber": 4
//     },
//     5: {
//       "answer": "Antwoord5",
//       "notes": "test5",
//       "question": "question5?",
//       "questionNumber": 5
//     },
//     6: {
//       "answer": "Antwoord6",
//       "notes": "test6",
//       "question": "question6?",
//       "questionNumber": 6
//     }
//   }

  });

  //AUDIO RECORDING HIER
  var pathReference = storage.ref('Recordings/' + personId);
  pathReference.child(projectId + '-' + personId + '.aac').getDownloadURL().then(function(url) {
    console.log(url);
    document.getElementById('full-audio-wrap').innerHTML+='<audio id="audioplayer" class="audioplayer"><source src="' + url + '" type="audio/aac"></audio>';
    document.getElementById('full-audio-wrap').innerHTML+='<div id="audio-controls" class="audio-controls"><i id="pButton" class="fa fa-play" aria-hidden="true"></i><div class="audio-timeline"><div id="audio-timeline-subwrap" class="audio-timeline-subwrap"><div class="playhead"></div></div></div></div>';
    // document.getElementById('audioplayer').addEventListener('ended', function() {
    //   document.getElementById('audioplayer').currentTime=0;
    //   document.getElementById('play-btn').className='fa fa-play';
    // });
    audioPlayer = document.getElementById('audioplayer');
    durationAudio = document.getElementById('audioplayer').duration;
    //console.log(durationAudio);
      var controlInterval = setInterval(function(){ checkDuration() }, 100);

      function checkDuration() {
        durationAudio = document.getElementById('audioplayer').duration;
        if (durationAudio > 0) {
          clearInterval(controlInterval);
          setAudioPlayer();
        }
        else {
          console.log(durationAudio);
        }
      }


  }).catch(function(error) {
    // Handle any errors
  });
}

function setAudioPlayer() {
  pButton = document.getElementById('pButton');
  playHead = document.getElementById('audio-timeline-subwrap');
  timeline = document.getElementById('audio-timeline');

  pButton.addEventListener("click", playAudio);
  audioPlayer.addEventListener("timeupdate", timeUpdate, false);

  function timeUpdate() {
    var playPercent = 100 * (audioPlayer.currentTime / durationAudio);
    console.log(playPercent);
    playHead.style.width = playPercent + "%";
    if (playPercent == 100) {
      pButton.className = "fa fa-play";
    }
  }

  audioPlayer.addEventListener("canplaythrough", function () {
     duration = audioPlayer.duration;
   }, false);


  function playAudio() {
    if (audioPlayer.paused) {
      audioPlayer.play();
      pButton.className = "fa fa-pause";
    } else {
      audioPlayer.pause();
      pButton.className = "fa fa-play";
    }
}
}

// function playAudio() {
//   if (document.getElementById('play-btn').className=='fa fa-play') {
//     document.getElementById('audioplayer').play();
//     document.getElementById('play-btn').className='fa fa-stop';
//   }
//   else if (document.getElementById('play-btn').className=='fa fa-stop') {
//     document.getElementById('audioplayer').pause();
//     document.getElementById('audioplayer').currentTime=0;
//     document.getElementById('play-btn').className='fa fa-play';
//   }
// }

function setComment(startupHash, projectId, personId, x) {
  if (document.getElementById('question-pencil' + x).className=='fa fa-pencil') {
    document.getElementById('textarea-question' + x).className='stepper-textarea stepper-textarea-open';
    document.getElementById('question-pencil' + x).className='fa fa-floppy-o';

    for (i = 1; i <= numberOfQuestions; i++) {
      if (i != x) {
        document.getElementById('stepper-number' + i).className='stepper-number stepper-number-inactive';
        document.getElementById('stepper-comment' + i).className='stepper-comment stepper-comment-inactive';
        document.getElementById('single-stepper-wrap' + i).className='single-stepper-wrap single-stepper-wrap-inactive';
      }
    }
  }
  else if (document.getElementById('question-pencil' + x).className=='fa fa-floppy-o') {

    setQuestion = {
      "questionData" : questionData
    };

    setNote = document.getElementById('textarea-question' + x).value;
    console.log(setNote);

    enne = setQuestion;
    enne['questionData'][x]['notes']=setNote;
    console.log(enne);

    db.collection("Startups").doc(startupHash).collection('Experiments').doc(projectId).collection('people').doc(personId).update(
      enne
    );

    document.getElementById('textarea-question' + x).className='stepper-textarea';
    document.getElementById('question-pencil' + x).className='fa fa-pencil';

    for (i = 1; i <= numberOfQuestions; i++) {
        document.getElementById('stepper-number' + i).className='stepper-number';
        document.getElementById('stepper-comment' + i).className='stepper-comment';
        document.getElementById('single-stepper-wrap' + i).className='single-stepper-wrap';
    }
  }

}

function startLoad() {
  document.getElementById('load-spinner-wrap').classList.remove('spinner-gone');
}

function doneLoad() {
  document.getElementById('load-spinner-wrap').classList.add('spinner-gone');
}
