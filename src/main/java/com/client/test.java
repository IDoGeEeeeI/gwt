package com.client;

import com.google.gwt.user.client.ui.*;
import com.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class test implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button deleteButton = new Button("Delete");
    final Button sendButton = new Button("Add");
    final TextBox nameField = new TextBox();
    nameField.setText("User 40");
    final Label errorLabel = new Label();

    // We can add style names to widgets
    sendButton.addStyleName("sendButton");
    deleteButton.addStyleName("sendButton");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("sendButtonContainer").add(deleteButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);

    // Focus the cursor on the name field when the app loads
    nameField.setFocus(true);
    nameField.selectAll();

    // Create the popup dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Table");
    dialogBox.setAnimationEnabled(true);
    final Button closeButton = new Button("Close");

    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");
    final HTML serverResponseLabel = new HTML();
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    final FlexTable flexTable = new FlexTable();
    flexTable.setBorderWidth(1);
    dialogVPanel.add(flexTable);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    // Add a handler to close the DialogBox
    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
        sendButton.setEnabled(true);
        sendButton.setFocus(true);
        deleteButton.setEnabled(true);
        deleteButton.setFocus(true);
      }
    });

    class deleteHandler implements  ClickHandler{
      @Override
      public void onClick(ClickEvent event) {
        sendNameToServer();
      }
      private void sendNameToServer() {
        // First, we validate the input.
        errorLabel.setText("");
        String textToServer = nameField.getText();
        if (!FieldVerifier.isValidName(textToServer)) {
          errorLabel.setText("Please enter at least four characters");
          return;
        }
        // Then, we send the input to the server.
        sendButton.setEnabled(false);
        deleteButton.setEnabled(false);
        serverResponseLabel.setText("");
        greetingService.dellServer(textToServer, new AsyncCallback<String>() {
          public void onFailure(Throwable caught) {
            // Show the RPC error message to the user
            serverResponseLabel.addStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(SERVER_ERROR);
            dialogBox.center();
            closeButton.setFocus(true);
          }

          public void onSuccess(String result) {
            serverResponseLabel.removeStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(result);
            dialogBox.center();
            closeButton.setFocus(true);
            String[] line = result.split(" ");
            flexTable.setText(0,0,line.toString());
            List<String> lines = new ArrayList<>(Arrays.asList(line));
            int row=0;
            int cell=0;
            for (int i=0;i<lines.size();i++){
              if(i%3==0 && i!=0){
                row++;
                cell=0;
              }
              if(i+1!=lines.size()) {
                flexTable.setText(row, cell, lines.get(i));
                cell++;
              }
            }
          }
        });
      }
    }



    // Create a handler for the sendButton and nameField
    class addHandler implements ClickHandler, KeyUpHandler {
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
        sendNameToServer();
      }

      /**
       * Fired when the user types in the nameField.
       */
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendNameToServer();
        }
      }

      /**
       * Send the name from the nameField to the server and wait for a response.
       */
      private void sendNameToServer() {
        // First, we validate the input.
        errorLabel.setText("");
        String textToServer = nameField.getText();
        if (!FieldVerifier.isValidName(textToServer)) {
          errorLabel.setText("Please enter at least four characters");
          return;
        }

        // Then, we send the input to the server.
        sendButton.setEnabled(false);
        deleteButton.setEnabled(false);
        serverResponseLabel.setText("");
        greetingService.greetServer(textToServer, new AsyncCallback<String>() {
          public void onFailure(Throwable caught) {
            // Show the RPC error message to the user
            serverResponseLabel.addStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(SERVER_ERROR);
            dialogBox.center();
            closeButton.setFocus(true);
          }

          public void onSuccess(String result) {
            serverResponseLabel.removeStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(result);
            dialogBox.center();
            closeButton.setFocus(true);
            String[] line = result.split(" ");
            flexTable.setText(0,0,line.toString());
            List<String> lines = new ArrayList<>(Arrays.asList(line));
            int row=0;
            int cell=0;
            for (int i=0;i<lines.size();i++){
              if(i%3==0 && i!=0){
                row++;
                cell=0;
              }
              if(i+1!=lines.size()) {
                flexTable.setText(row, cell, lines.get(i));
                cell++;
              }
            }
          }
        });
      }
    }

    // Add a handler to send the name to the server
    addHandler handler = new addHandler();
    deleteHandler deleteHandler = new deleteHandler();
    deleteButton.addClickHandler(deleteHandler);
    sendButton.addClickHandler(handler);
    nameField.addKeyUpHandler(handler);
  }
}
