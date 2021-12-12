package com.server;

import com.client.GreetingService;
import com.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tz.SessionFactoryUtils;
import com.tz.User;
import com.tz.UserDao;
import com.tz.UserDaoImpl;

import java.util.List;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid.
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
              "Name must be at least 4 characters long");
    }
    SessionFactoryUtils sessionFactoryUtils = new SessionFactoryUtils();
    sessionFactoryUtils.init();
    try {
      UserDao userDao = new UserDaoImpl(sessionFactoryUtils);
      List<User> all = userDao.findAll();
      StringBuilder table = new StringBuilder("id name score " );
      for(int i=0;i<all.size();i++){
        String a = all.get(i).toString() + " ";
        table.append(a);
      }
//      StringBuilder table = new StringBuilder("id name score " + "<br>");
//      for(int i=0;i<all.size();i++){
//        String a = all.get(i).toString() + " <br>";
//        table.append(a);
//      }
      return table.toString();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sessionFactoryUtils.shutdown();
    }

    return "";
  }

  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
        ">", "&gt;");
  }
}
