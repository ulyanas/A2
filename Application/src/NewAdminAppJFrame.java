
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */



public class NewAdminAppJFrame extends javax.swing.JFrame {
    public RemoteInterface remote;
    private String userName;
    public String serverApplicationIDAddress;
    
    /**
     * Creates new form ContactEditor
     */
    public NewAdminAppJFrame(RemoteInterface remote, String serverApplicationIPAddress) {
        initComponents();
        TableColumnModel tcm = jTable1.getTableHeader().getColumnModel();
        Object[] columnNames = new Object[tcm.getColumnCount()];
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            TableColumn tc = tcm.getColumn(i);
            columnNames[i] = tc.getHeaderValue();
        }
        
        DefaultTableModel dtm;
        dtm = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;  
            }
        };

       
        jTable1.setModel(dtm);
        
       
        setLocationRelativeTo( null );
        this.remote = remote;
        this.jTextField3.setText(serverApplicationIPAddress);
        this.serverApplicationIDAddress = serverApplicationIPAddress;
//        try {
//            System.setProperty("java.security.policy", "policy.txt");
//            System.setSecurityManager(new java.rmi.RMISecurityManager());
//            remote = (RemoteInterface) Naming.lookup("//localhost:1234/Remote");
//        } catch (NotBoundException ex) {
//            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (RemoteException ex) {
//            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        updateTable();
    }
    
    
    public void setUserName(String userName){
        this.userName = userName;
        return;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setText("New Admin Application");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel1.setText("jLabel1");

        jTextField2.setText("localhost");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel6.setText("Database Server IP");
        jLabel6.setAlignmentX(0.3F);

        jButton3.setText("Sign In");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("New User");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("New Admin Application");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Username", "Role"
            }
        )
    );
    jScrollPane1.setViewportView(jTable1);

    jButton1.setText("Delete User");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton2.setText("Add User");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    jTextField1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1ActionPerformed(evt);
        }
    });
    jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            jTextField1FocusGained(evt);
        }
    });

    jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jPasswordField1ActionPerformed(evt);
        }
    });
    jPasswordField1.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            jPasswordField1FocusGained(evt);
        }
    });

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Inventory Manager", "Shipping Manager", "Order Manager", "Admin" }));

    jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
    jLabel3.setText("Username");

    jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
    jLabel4.setText("Password");
    jLabel4.setToolTipText("");

    jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
    jLabel2.setText("Role");

    jTextField3.setText("localhost");
    jTextField3.setFocusable(false);
    jTextField3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField3ActionPerformed(evt);
        }
    });
    jLabel7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
    jLabel7.setText("Database Server IP");
    jLabel7.setAlignmentX(0.3F);

    jTextField3.setText("localhost");
    jTextField3.setFocusable(false);

    jTextArea1.setColumns(20);
    jTextArea1.setRows(5);
    jScrollPane2.setViewportView(jTextArea1);

    jLabel5.setText("Messages:");

    jButton5.setText("Log out");
    jButton5.setFocusable(false);
    jButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
        }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 399, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jButton5))
                .add(layout.createSequentialGroup()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 418, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(18, 18, 18)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(14, 14, 14))
                                .add(jLabel5)))
                        .add(layout.createSequentialGroup()
                            .add(116, 116, 116)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jLabel3)
                                .add(jLabel4)
                                .add(jLabel2)
                                .add(jLabel7))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jTextField3)
                                .add(jTextField1)
                                .add(jPasswordField1)
                                .add(layout.createSequentialGroup()
                                    .add(14, 14, 14)
                                    .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
        .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 594, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(12, 12, 12)
                    .add(jLabel9))
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jButton5)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel7)
                .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel3))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel4))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel2))
            .add(10, 10, 10)
            .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(18, 18, 18)
            .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createSequentialGroup()
                    .add(jButton1)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(jLabel5)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(16, 16, 16))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int[] selection = jTable1.getSelectedRows();
        jTextArea1.setText("");
        if(selection.length == 0){
            jTextArea1.setText("Please select/add \n to delete");
        }
        for (int i = 0; i < selection.length; i++) {
            String login = (String) jTable1.getModel().getValueAt(selection[i], 0);
            try {
                remote.deleteUser(login);
                jTextArea1.setText("User " + login + " \n successfully deleted");
            } catch (RemoteException ex) {
                Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        updateTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean checkDuplicateUser(String login){
        LinkedList<UserInfo> listUsers = new LinkedList<UserInfo>();
        try {
            listUsers = remote.getListUsers();
        } catch (RemoteException ex) {
            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(UserInfo item:listUsers){
            if(login != null && login.equals(item.getLogin())){
                jTextArea1.setText("User Name already \n exists , please try \n a new user name \n");
                return false;
            }
        }
        return true;
    } 
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        boolean flag = true;
        jTextArea1.setText("");
        int index = jComboBox1.getSelectedIndex();
        String role = findRoleByIndex(index);
        
        String login = (String) jTextField1.getText();
        String password = new String(jPasswordField1.getPassword());
       try{
            if(login.equals("Username")){
                jTextField1.setText("");
                jPasswordField1.setText("");
                jTextArea1.setText("Please choose a \n valid user name \n");
                flag = false;
            }
            else {
            if(login.equals("") || password.equals("")){
                flag = false;
                jTextArea1.setText("Login/Password cannot \n be empty string");
            }
            else
               flag = checkDuplicateUser(login);
            }    
            if(flag) {
              remote.addUser(login, password, role);
              jTextField1.setText("");
              jPasswordField1.setText("");
              jTextArea1.setText("");
              jTextArea1.setText("User " + login + " \n successfully created.");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updateTable();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private String findRoleByIndex(int index) {
        String role = "";
        switch (index) {
            case 0:
                role = "inventory";
                break;
            case 1:
                role = "shipping";
                break;
            case 2:
                role = "order";
                break;
            case 3:
                role = "admin";
                break;
            default:
                break;
        }
        return role;
    }

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1FocusGained

    private void jPasswordField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1FocusGained
        jPasswordField1.setText("");
    }//GEN-LAST:event_jPasswordField1FocusGained

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String login = jTextField1.getText();
        String password = new String(jPasswordField1.getPassword());
        try {
            String role = remote.login(login, password);
            if (!role.equals("badAuthorization")){
                if (role.equals("inventory")){
                    // open inventory
                    dispose();
                    NewInventoryMainFrame newWindow = new NewInventoryMainFrame(remote, serverApplicationIDAddress);
                    newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    newWindow.setVisible(true);
                }
                else if (role.equals("order")){
                    // open order
                    dispose();
                    NewOrderJFrame newWindow = new NewOrderJFrame(remote, serverApplicationIDAddress);
                    newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    newWindow.setVisible(true);
                }
                else if (role.equals("admin")){
                    // open admin
                    dispose();
                    NewAdminAppJFrame newWindow = new NewAdminAppJFrame(remote, serverApplicationIDAddress);
                    newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    newWindow.setVisible(true);
                }
                else if (role.equals("shipping")){
                    // open shipping
                    dispose();
                    NewShippingJFrame newWindow = new NewShippingJFrame(remote, serverApplicationIDAddress);
                    newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    newWindow.setVisible(true);
                }
            }
            else {
                // notify user about wrong password
                JOptionPane.showMessageDialog(null, "Wrong Username or Password. Please try again.");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(LoginJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // TODO add your handling code here:
            remote.logout(userName);
            System.out.println("logout button");
        } catch (RemoteException ex) {
            Logger.getLogger(NewAdminAppJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        LoginJFrame newWindow = new LoginJFrame();
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            // TODO add your handling code here:
            remote.logout(this.userName);
        } catch (RemoteException ex) {
            Logger.getLogger(NewAdminAppJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    
    private void updateTable() {
        LinkedList<UserInfo> listUsers = new LinkedList<UserInfo>();
        try {
            listUsers = remote.getListUsers();
        } catch (RemoteException ex) {
            Logger.getLogger(NewInventoryMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        TableColumnModel tcm = jTable1.getTableHeader().getColumnModel();
        Object[] columnNames = new Object[tcm.getColumnCount()];
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            TableColumn tc = tcm.getColumn(i);
            columnNames[i] = tc.getHeaderValue();
        }

        DefaultTableModel dtm = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
        };

        for (UserInfo user : listUsers) {
            Object[] data = {user.getLogin(), user.getRole()};
            dtm.addRow(data);
        }
        jTable1.setModel(dtm);
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
//            for (int idx=0; idx<installedLookAndFeels.length; idx++)
//                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
//                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
//                    break;
//                }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(NewAdminAppJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(NewAdminAppJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(NewAdminAppJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(NewAdminAppJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new NewAdminAppJFrame().setVisible(true);
//            }
//        });
//    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
    
}
