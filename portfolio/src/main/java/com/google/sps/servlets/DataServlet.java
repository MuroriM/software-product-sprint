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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/get_comments")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String commentText = (String) entity.getProperty("text");
      String email = (String) entity.getProperty("email");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comm = new Comment(id, commentText, email, timestamp);
      comments.add(comm);
    }

    // Send the JSON as the response
    response.setContentType("application/json;");
    String json = new Gson().toJson(comments);
    response.getWriter().println(json);
  }
  

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/shoutbox");
      return;
    }
    
    // Get the input from the form.
    String text = getParameter(request, "comment-box", "");
    String email = userService.getCurrentUser().getEmail();
    long timestamp = System.currentTimeMillis();

    // Add new comment to comment arraylist
    if (!(text.equals(""))) 
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("email", email);
        commentEntity.setProperty("timestamp", timestamp);
        
        datastore.put(commentEntity);
    }
    
    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}


