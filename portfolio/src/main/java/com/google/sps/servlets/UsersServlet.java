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

package com.google.sps.servlets;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class UsersServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

    //   response.getWriter().println("<p>Hello " + userEmail + "!</p>");
    //   response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
      
      ArrayList<String> logged = new ArrayList<>();

      String str1 = "<p>Hello " + userEmail + "!</p>";
      String str2 = "<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>";

      logged.add("true");
      logged.add(str1);
      logged.add(str2);

      // Send the JSON as the response
      response.setContentType("application/json;");
      String json = new Gson().toJson(logged);
      response.getWriter().println(json);
    } else {
      String urlToRedirectToAfterUserLogsIn = "/";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      //   response.getWriter().println("<p>Hello stranger.</p>");
      //   response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");

      ArrayList<String> logged = new ArrayList<>();

      logged.add("false");
      logged.add("<p>Hello stranger.</p>");
      logged.add("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");

      // Send the JSON as the response
      response.setContentType("application/json;");
      String json = new Gson().toJson(logged);
      response.getWriter().println(json);
    }
  }
}
