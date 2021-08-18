import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ProductList {
    private JTextField txtname;
    private JTextField txtmrp;
    private JTextField txtDsprice;
    private JTextField txtid;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton saveButton;
    private JTable table1;
    private JPanel mainPanel;

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/productslist", "root","root");
            System.out.println("Success");
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ProductList() {
        connect();
        table_load();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name, mrp, DisPrice;
                name = txtname.getText();
                mrp = txtmrp.getText();
                DisPrice = txtDsprice.getText();

                try {
                    pst = con.prepareStatement("insert into products(name,mrp,DisPrice)values(?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, mrp);
                    pst.setString(3, DisPrice);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added.");
                    table_load();
                    txtname.setText("");
                    txtmrp.setText("");
                    txtDsprice.setText("");
                    txtname.requestFocus();
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String empid = txtid.getText();

                    pst = con.prepareStatement("select name,mrp,DisPrice from products where pid = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String name = rs.getString(1);
                        String mrp = rs.getString(2);
                        String DisPrice = rs.getString(3);

                        txtname.setText(name);
                        txtmrp.setText(mrp);
                        txtDsprice.setText(DisPrice);
                        JOptionPane.showMessageDialog(null, "Record Found.");

                    }
                    else
                    {
                        txtname.setText("");
                        txtmrp.setText("");
                        txtDsprice.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Id.");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id, name, mrp, DisPrice;

                name = txtname.getText();
                mrp = txtmrp.getText();
                DisPrice = txtDsprice.getText();
                id = txtid.getText();


                try {
                    pst = con.prepareStatement("update products set name = ?,mrp = ?,DisPrice = ? where pid = ?");
                    pst.setString(1, name);
                    pst.setString(2, mrp);
                    pst.setString(3, DisPrice);
                    pst.setString(4, id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated.");
                    table_load();
                    txtname.setText("");
                    txtmrp.setText("");
                    txtDsprice.setText("");
                    txtname.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id;
                id = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from products  where pid = ?");

                    pst.setString(1, id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted.");
                    table_load();
                    txtname.setText("");
                    txtmrp.setText("");
                    txtDsprice.setText("");
                    txtname.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }
        });
    }


    void table_load() {
        try
        {
            pst = con.prepareStatement("select * from products");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }










    public static void main(String[] args) {
        JFrame frame = new JFrame("ProductList");
        frame.setContentPane(new ProductList().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
