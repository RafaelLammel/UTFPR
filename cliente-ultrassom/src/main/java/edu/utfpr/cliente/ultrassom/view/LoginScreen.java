/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.cliente.ultrassom.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Rafael
 */
public class LoginScreen extends javax.swing.JFrame {

    /**
     * Creates new form LoginScreen
     */
    public LoginScreen() {
        initComponents();
        this.setResizable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameCadastroUsuario = new javax.swing.JFrame();
        panelTitle1 = new javax.swing.JPanel();
        labelTitle1 = new javax.swing.JLabel();
        panelCadastro = new javax.swing.JPanel();
        labelLogin1 = new javax.swing.JLabel();
        labelNome = new javax.swing.JLabel();
        labelSenha = new javax.swing.JLabel();
        labelEmail = new javax.swing.JLabel();
        textFieldLoginCadastro = new javax.swing.JTextField();
        textFieldNome = new javax.swing.JTextField();
        textFieldEmail = new javax.swing.JTextField();
        buttonCadastra = new javax.swing.JButton();
        passwordFieldCadastro = new javax.swing.JPasswordField();
        panelTitle = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        panelLoginForm = new javax.swing.JPanel();
        labelLogin = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        textFieldLogin = new javax.swing.JTextField();
        buttonLogin = new javax.swing.JButton();
        buttonCadastrar = new javax.swing.JButton();
        passwordFieldLogin = new javax.swing.JPasswordField();

        frameCadastroUsuario.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frameCadastroUsuario.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTitle1.setBackground(new java.awt.Color(0, 0, 153));

        labelTitle1.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelTitle1.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle1.setText("Cadastro de Usuário");

        javax.swing.GroupLayout panelTitle1Layout = new javax.swing.GroupLayout(panelTitle1);
        panelTitle1.setLayout(panelTitle1Layout);
        panelTitle1Layout.setHorizontalGroup(
            panelTitle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitle1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle1)
                .addContainerGap(131, Short.MAX_VALUE))
        );
        panelTitle1Layout.setVerticalGroup(
            panelTitle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitle1Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(labelTitle1)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        frameCadastroUsuario.getContentPane().add(panelTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 50));

        panelCadastro.setBackground(new java.awt.Color(153, 153, 153));

        labelLogin1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        labelLogin1.setForeground(new java.awt.Color(255, 255, 255));
        labelLogin1.setText("Login:");

        labelNome.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        labelNome.setForeground(new java.awt.Color(255, 255, 255));
        labelNome.setText("Nome:");

        labelSenha.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        labelSenha.setForeground(new java.awt.Color(255, 255, 255));
        labelSenha.setText("Senha:");

        labelEmail.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        labelEmail.setForeground(new java.awt.Color(255, 255, 255));
        labelEmail.setText("E-mail:");

        textFieldLoginCadastro.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N

        textFieldNome.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N

        textFieldEmail.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N

        buttonCadastra.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        buttonCadastra.setText("CADASTRAR");
        buttonCadastra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCadastraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCadastroLayout = new javax.swing.GroupLayout(panelCadastro);
        panelCadastro.setLayout(panelCadastroLayout);
        panelCadastroLayout.setHorizontalGroup(
            panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCadastroLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonCadastra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelCadastroLayout.createSequentialGroup()
                        .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNome)
                            .addComponent(labelLogin1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldNome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldLoginCadastro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCadastroLayout.createSequentialGroup()
                        .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEmail)
                            .addComponent(labelSenha))
                        .addGap(18, 18, 18)
                        .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(passwordFieldCadastro))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        panelCadastroLayout.setVerticalGroup(
            panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCadastroLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLogin1)
                    .addComponent(textFieldLoginCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNome)
                    .addComponent(textFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEmail)
                    .addComponent(textFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCadastroLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(labelSenha))
                    .addGroup(panelCadastroLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordFieldCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(buttonCadastra, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        frameCadastroUsuario.getContentPane().add(panelCadastro, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 360, 250));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(393, 287));
        setPreferredSize(new java.awt.Dimension(393, 287));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTitle.setBackground(new java.awt.Color(0, 0, 153));
        panelTitle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTitle.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle.setText("Projeto Ultrassom");
        panelTitle.add(labelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        getContentPane().add(panelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 70));

        panelLoginForm.setBackground(new java.awt.Color(153, 153, 153));
        panelLoginForm.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelLogin.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelLogin.setForeground(new java.awt.Color(255, 255, 255));
        labelLogin.setText("Login:");
        panelLoginForm.add(labelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        labelPassword.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelPassword.setForeground(new java.awt.Color(255, 255, 255));
        labelPassword.setText("Senha:");
        panelLoginForm.add(labelPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        textFieldLogin.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        textFieldLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        panelLoginForm.add(textFieldLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 250, 30));

        buttonLogin.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        buttonLogin.setText("LOGIN");
        buttonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoginActionPerformed(evt);
            }
        });
        panelLoginForm.add(buttonLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 140, 40));

        buttonCadastrar.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        buttonCadastrar.setText("CADASTRAR");
        buttonCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCadastrarActionPerformed(evt);
            }
        });
        panelLoginForm.add(buttonCadastrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 140, 40));
        panelLoginForm.add(passwordFieldLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 250, 30));

        getContentPane().add(panelLoginForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 400, 220));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoginActionPerformed
        URL url;
        try {
            url = new URL("http://localhost:8080/login");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            //Declaranco método e tipo do conteúdo
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            //Falando que vamos enviar algo
            con.setDoOutput(true);
            con.setDoInput(true);
            //Montando o Body do JSON
            JSONObject body = new JSONObject();
            body.put("login", textFieldLogin.getText());
            body.put("senha", passwordFieldLogin.getText());
            //Enviando o JSON no Body
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body.toString());
            wr.flush();
            wr.close();
            //Lendo resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String response = "";
            while((inputLine = in.readLine()) != null){
                response += inputLine;
            }
            in.close();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            showMessageDialog(null,"Usuário cadastrado com sucesso!\n"+json.toJSONString());
            frameCadastroUsuario.dispatchEvent(new WindowEvent(frameCadastroUsuario, WindowEvent.WINDOW_CLOSING));
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonLoginActionPerformed

    private void buttonCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCadastrarActionPerformed
        frameCadastroUsuario.setSize(360,330);
        frameCadastroUsuario.setLocationRelativeTo(null);
        frameCadastroUsuario.setResizable(false);
        frameCadastroUsuario.setVisible(true);
        frameCadastroUsuario.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setEnabled(true);
            }
        });
        this.setEnabled(false);
    }//GEN-LAST:event_buttonCadastrarActionPerformed

    private void buttonCadastraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCadastraActionPerformed
        if("".equals(textFieldLoginCadastro.getText()))
            showMessageDialog(null,"Login não pode ser vazio!");
        else if("".equals(textFieldEmail.getText()))
            showMessageDialog(null,"Email não pode ser vazio!");
        else if("".equals(passwordFieldCadastro.getText()))
            showMessageDialog(null,"Senha não pode ser vazio!");
        else{
            try {
                //Abrindo conexão com o servidor
                URL url = new URL("http://localhost:8080/cadastroUsuario");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                //Declaranco método e tipo do conteúdo
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                //Falando que vamos enviar algo
                con.setDoOutput(true);
                //Montando um JSON para o Body
                JSONObject body = new JSONObject();
                body.put("login", textFieldLoginCadastro.getText());
                body.put("nome", textFieldNome.getText());
                body.put("email",textFieldEmail.getText());
                body.put("senha", passwordFieldCadastro.getText());
                //Enviando o JSON no Body
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(body.toString());
                wr.flush();
                wr.close();
                //Lendo resposta
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                String response = "";
                while((inputLine = in.readLine()) != null){
                    response += inputLine;
                }
                in.close();
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(response);
                showMessageDialog(null,"Usuário cadastrado com sucesso!\n"+json.get("id").toString());
                frameCadastroUsuario.dispatchEvent(new WindowEvent(frameCadastroUsuario, WindowEvent.WINDOW_CLOSING));
            } catch (MalformedURLException ex) {
                Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
                showMessageDialog(null,"Erro no Servidor");
            } catch (IOException | ParseException ex) {
                Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
                showMessageDialog(null,"Erro ao enviar ou ler os dados");
            }
        }
    }//GEN-LAST:event_buttonCadastraActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCadastra;
    private javax.swing.JButton buttonCadastrar;
    private javax.swing.JButton buttonLogin;
    private javax.swing.JFrame frameCadastroUsuario;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelLogin;
    private javax.swing.JLabel labelLogin1;
    private javax.swing.JLabel labelNome;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelSenha;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JPanel panelCadastro;
    private javax.swing.JPanel panelLoginForm;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JPanel panelTitle1;
    private javax.swing.JPasswordField passwordFieldCadastro;
    private javax.swing.JPasswordField passwordFieldLogin;
    private javax.swing.JTextField textFieldEmail;
    private javax.swing.JTextField textFieldLogin;
    private javax.swing.JTextField textFieldLoginCadastro;
    private javax.swing.JTextField textFieldNome;
    // End of variables declaration//GEN-END:variables
}
