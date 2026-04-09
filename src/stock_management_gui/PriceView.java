/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package stock_management_gui;

import model.InvoiceItem;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Frame;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.InvoiceData;
import model.MySql;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author sanja
 */
public class PriceView extends javax.swing.JDialog {

    private String loggedInUsername;
    private String orderId;
    private List<InvoiceItem> itemList;

    /**
     * Creates new form PriceView
     */
    public PriceView(Frame parent, boolean modal, String username, String orderId) {
        super(parent, modal);
        List<InvoiceItem> items;
        this.itemList = itemList;

        initComponents();
    }

    public String generateInvoiceID() {

        String prefix = "INV";
        String query = "SELECT invoice_id FROM invoice ORDER BY invoice_id DESC LIMIT 1";

        try {

            ResultSet rs = MySql.executeSearch(query);

            if (rs.next()) {

                String lastId = rs.getString("invoice_id").replace(prefix, "");
                int id = Integer.parseInt(lastId) + 1;
                return String.format("%s%05d", prefix, id);

            } else {

                return prefix + "00001";

            }

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

    public String getEmployeeIdByUsername(String username) {

        String query = "SELECT employee_employee_id FROM employee_user WHERE user_name = '" + username + "'";

        try {

            ResultSet rs = MySql.executeSearch(query);

            if (rs.next()) {

                return rs.getString("employee_employee_id");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public void saveInvoice(String invoiceId, String orderId, double total, double paid, double balance, String employeeId) {

        try {

            // Check if an invoice already exists for this order
            String checkSQL = "SELECT invoice_id FROM invoice WHERE order_details_order_id = '" + orderId + "'";
            ResultSet existing = MySql.executeSearch(checkSQL);

            if (existing.next()) {

                // Invoice already exists
                String existingInvoiceId = existing.getString("invoice_id");
                JOptionPane.showMessageDialog(this, "Invoice already exists for this order.\nInvoice ID: " + existingInvoiceId, "Warning", JOptionPane.WARNING_MESSAGE);
                return;

            }

            // Insert invoice
            String insertInvoiceSQL = String.format(
                    "INSERT INTO invoice (invoice_id, order_details_order_id, total_amount, paid_price, balance, employee_employee_id) "
                    + "VALUES ('%s', '%s', %.2f, %.2f, %.2f, '%s')",
                    invoiceId, orderId, total, paid, balance, employeeId
            );

            MySql.executeUpdate(insertInvoiceSQL);

            // Insert invoice items
            String selectItemsSQL = "SELECT oid, qty, price FROM order_items WHERE order_details_order_id = '" + orderId + "'";
            ResultSet rs = MySql.executeSearch(selectItemsSQL);

            while (rs.next()) {

                int oid = rs.getInt("oid");
                int qty = rs.getInt("qty");
                double price = rs.getDouble("price");

                String insertItemSQL = String.format(
                        "INSERT INTO invoice_items (invoice_invoice_id, order_details_order_id, order_items_oid, qty, price) "
                        + "VALUES ('%s', '%s', %d, %d, %.2f)",
                        invoiceId, orderId, oid, qty, price
                );

                MySql.executeUpdate(insertItemSQL);
            }

            JOptionPane.showMessageDialog(null, "Invoice Generated Successfully!\nInvoice ID: " + invoiceId);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Method to set the total price
    public void setTotalPrice(String totalPrice) {

        total_price.setText(totalPrice);

    }

    // Method to calculate balance
    private void calculateBalance() {

        try {

            double total = Double.parseDouble(total_price.getText());
            double paid = Double.parseDouble(paidTextField.getText());

            // Prevent negative paid amount
            if (paid < 0) {

                JOptionPane.showMessageDialog(this, "Paid amount cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                paidTextField.setText("0");
                paid = 0;

            }

            double balance = paid - total; // Positive if paid > total

            // Display balance
            balanceValueLabel.setText(String.format("%.2f", balance));

            // Enable or disable Get Invoice button based on balance
            if (balance < 0) {

                getInvoiceButton.setEnabled(false); // Not enough payment

            } else {

                getInvoiceButton.setEnabled(true);  // Full or extra payment

            }

        } catch (NumberFormatException e) {

            balanceValueLabel.setText("0.00");

            getInvoiceButton.setEnabled(false); // Disable if input is invalid
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        total_price = new javax.swing.JLabel();
        balanceValueLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        getInvoiceButton = new com.k33ptoo.components.KButton();
        jLabel5 = new javax.swing.JLabel();
        paidTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel1.setText("Total Amount :");

        total_price.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        total_price.setText("00.00");

        balanceValueLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        balanceValueLabel.setText("00.00");

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Balance :");

        getInvoiceButton.setText("Get Invoice");
        getInvoiceButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getInvoiceButton.setkEndColor(new java.awt.Color(0, 204, 204));
        getInvoiceButton.setkHoverEndColor(new java.awt.Color(0, 102, 153));
        getInvoiceButton.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        getInvoiceButton.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        getInvoiceButton.setkPressedColor(new java.awt.Color(0, 102, 153));
        getInvoiceButton.setkSelectedColor(new java.awt.Color(0, 102, 153));
        getInvoiceButton.setkStartColor(new java.awt.Color(0, 102, 153));
        getInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getInvoiceButtonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel5.setText("Paid Amount :");

        paidTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paidTextFieldActionPerformed(evt);
            }
        });
        paidTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paidTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(getInvoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(balanceValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(2, 2, 2)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(total_price, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(paidTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_price, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paidTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balanceValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(getInvoiceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void paidTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paidTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paidTextFieldActionPerformed

    private void paidTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidTextFieldKeyReleased

        calculateBalance();

    }//GEN-LAST:event_paidTextFieldKeyReleased

    private void getInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getInvoiceButtonActionPerformed
//        try {
//            String invoiceId = generateInvoiceID();
//            String orderId = this.orderId;
//
//            double total = Double.parseDouble(total_price.getText().trim());
//            double paid = Double.parseDouble(paidTextField.getText().trim());
//            double balance = paid - total;
//
//            String username = loggedInUsername;
//            String employeeId = getEmployeeIdByUsername(username);
//
//            if (employeeId == null) {
//                JOptionPane.showMessageDialog(this, "Employee not found for username: " + username, "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // ✅ Save invoice to database
//            saveInvoice(invoiceId, orderId, total, paid, balance, employeeId);
//
//            // ✅ Prepare data for JasperReport
//            List<InvoiceItem> myListOfItems = getInvoiceItems( orderId); 
//            InvoiceData invoice = new InvoiceData();
//            invoice.setInvoice_id(invoiceId);
//            invoice.setOrder_id(orderId);
//            invoice.setTotal(total);
//            invoice.setPaid(paid);
//            invoice.setBalance(balance);
//            invoice.setItems(myListOfItems);
//
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("invoice_id", invoice.getInvoice_id());
//            parameters.put("order_id", invoice.getOrder_id());
//            parameters.put("total", invoice.getTotal());
//            parameters.put("paid", invoice.getPaid());
//            parameters.put("balance", invoice.getBalance());
//
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoice.getItems());
//
//            JasperReport jasperReport = JasperCompileManager.compileReport("invoice_template.jrxml");
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//            // ✅ Show the invoice
//            JasperViewer.viewReport(jasperPrint, false);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


    }//GEN-LAST:event_getInvoiceButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PriceView dialog = new PriceView(new javax.swing.JFrame(), true, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balanceValueLabel;
    private com.k33ptoo.components.KButton getInvoiceButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField paidTextField;
    private javax.swing.JLabel total_price;
    // End of variables declaration//GEN-END:variables
}
