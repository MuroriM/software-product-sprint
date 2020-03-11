// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Waaazaaa', 'Wait for it', 'What is you doing', 'Why are you running?'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

async function getGreetingAsyncAwait() {
  const response = await fetch('/data');
  const quote = await response.text();
  document.getElementById('greeting-container').innerText = quote;
}


/*
 * Fetches stats from the servers and adds them to the DOM.
 */
function checkLogin() {
  fetch('/login').then(response => response.json()).then((logged) => {

      console.log(logged);

      if (logged.log == true) {
          document.getElementById("comment_form").style.display = "block";
          var str2 = "<p>Logout <a href=\"" + logged.logLink + "\">here</a>.</p>";
      } else {          
          document.getElementById("comment_form").style.display = "none";
          var str2 = "<p>Login <a href=\"" + logged.logLink + "\">here</a>.</p>";
      }
      getComments();

      var str1 = "<p>Hello " + logged.userEmail + "!</p>";

      var greeting = makeElement(str1);
      var loglink = makeElement(str2);

      const logElement = document.getElementById('login');
      logElement.append(greeting);
      logElement.append(loglink);

  
  });
}

function makeElement(text) {
    var z = document.createElement('p'); // is a node
    z.innerHTML = text;

    return z;
}

/*
 * Fetches stats from the servers and adds them to the DOM.
 */
function getComments() {
  fetch('/get_comments').then(response => response.json()).then((comments) => {

    const commentListElement = document.getElementById('comment-container');

    for (const idx in comments) {
        commentListElement.appendChild(
        createListElement(comments[idx].commentText, comments[idx].email));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(comment, user) {
  const liElement = document.createElement('li');
  liElement.innerText = comment + " by " + user;
  return liElement;
}
