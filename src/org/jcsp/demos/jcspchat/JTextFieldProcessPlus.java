    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2006 Peter Welch and Paul Austin.            //
    //                2001-2004 Quickstone Technologies Limited.        //
    //                                                                  //
    //  This library is free software; you can redistribute it and/or   //
    //  modify it under the terms of the GNU Lesser General Public      //
    //  License as published by the Free Software Foundation; either    //
    //  version 2.1 of the License, or (at your option) any later       //
    //  version.                                                        //
    //                                                                  //
    //  This library is distributed in the hope that it will be         //
    //  useful, but WITHOUT ANY WARRANTY; without even the implied      //
    //  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR         //
    //  PURPOSE. See the GNU Lesser General Public License for more     //
    //  details.                                                        //
    //                                                                  //
    //  You should have received a copy of the GNU Lesser General       //
    //  Public License along with this library; if not, write to the    //
    //  Free Software Foundation, Inc., 59 Temple Place, Suite 330,     //
    //  Boston, MA 02111-1307, USA.                                     //
    //                                                                  //
    //  Author contact: P.H.Welch@ukc.ac.uk                             //
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.demos.jcspchat;

import javax.swing.*;
import javax.swing.event.*;
import org.jcsp.lang.*;
import org.jcsp.net.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */
public class JTextFieldProcessPlus implements CSProcess {
  private ChannelOutput out;
  private JTextField jtf;
  private String user;

  public JTextFieldProcessPlus(JTextField field, ChannelOutput chan, String username) {
    jtf = field;
    out = chan;
    user = username;
    jtf.setText("Type your message here");

    jtf.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()+"\n"));
          jtf.setText("");
        }
      }

    });

    jtf.addFocusListener(new FocusAdapter() {
      boolean initialMessage = true;
      public void focusGained(FocusEvent e) {
        if (initialMessage) {
          jtf.setText("");
          initialMessage=false;
        }
      }
    });

    jtf.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
      }
      public void insertUpdate (DocumentEvent e) {
        JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()));
      }
      public void removeUpdate (DocumentEvent e) {
        JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()));
      }
    });

  }
  public void run() {
  }
}