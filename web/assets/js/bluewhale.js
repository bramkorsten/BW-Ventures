function expandForm() {
  document.getElementById("login-title").classList.toggle('class-hidden');
  document.getElementById("login-form").classList.toggle('form-expand');
  document.getElementById("login-background").classList.toggle('form-filter');
  document.getElementById("true-form").classList.toggle('class-gone');
  document.getElementById("background-img").classList.toggle('add-filter');

  setTimeout(function(){ document.getElementById("login-title").classList.toggle('class-gone'); }, 300);
}
