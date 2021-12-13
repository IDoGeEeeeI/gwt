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
//@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String input) throws IllegalArgumentException {
    String  out = null;
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
      UserDao userDaoAdd = new UserDaoImpl(sessionFactoryUtils);
      userDaoAdd.save(new User(input));
      UserDao userDao = new UserDaoImpl(sessionFactoryUtils);
      List<User> all = userDao.findAll();

      StringBuilder table = new StringBuilder("id name score" );
      for(int i=0;i<all.size();i++){
        String a = " " + all.get(i).toString();
        table.append(a);
      }
      out=table.toString();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sessionFactoryUtils.shutdown();
    }

    return out;
  }

  @Override
  public String dellServer(String input) throws IllegalArgumentException {
    String  out = null;
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
              "Name must be at least 4 characters long");
    }
    SessionFactoryUtils sessionFactoryUtils = new SessionFactoryUtils();
    sessionFactoryUtils.init();
    try {
      UserDao userDaoAdd = new UserDaoImpl(sessionFactoryUtils);
      userDaoAdd.deleteUser(new User(input));
      UserDao userDao = new UserDaoImpl(sessionFactoryUtils);
      List<User> all = userDao.findAll();

      StringBuilder table = new StringBuilder("id name score" );
      for(int i=0;i<all.size();i++){
        String a = " " + all.get(i).toString();
        table.append(a);
      }
      out=table.toString();
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      sessionFactoryUtils.shutdown();
    }
    return out;
  }

}
