import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ColorfulInventory {

    JFrame frame;
    JTextField idField,nameField,categoryField,quantityField,priceField,supplierField,searchField;
    JTable table;
    DefaultTableModel model;
    JLabel status;

    public ColorfulInventory(){

        frame = new JFrame("Colorful Inventory System");
        frame.setSize(1000,650);
        frame.setLayout(null);

        // Top Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255,182,193)); // Pink
        titlePanel.setBounds(0,0,1000,60);
        titlePanel.setLayout(null);

        JLabel title = new JLabel("Inventory Management System");
        title.setFont(new Font("Arial",Font.BOLD,28));
        title.setForeground(Color.BLUE);
        title.setBounds(280,10,500,40);

        titlePanel.add(title);
        frame.add(titlePanel);

        // Left Panel for form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255,255,153)); // Yellow
        formPanel.setBounds(20,80,350,470);
        formPanel.setLayout(null);

        frame.add(formPanel);

        JLabel id = new JLabel("Item ID");
        JLabel name = new JLabel("Item Name");
        JLabel category = new JLabel("Category");
        JLabel quantity = new JLabel("Quantity");
        JLabel price = new JLabel("Price");
        JLabel supplier = new JLabel("Supplier");

        id.setBounds(20,20,100,25);
        name.setBounds(20,60,100,25);
        category.setBounds(20,100,100,25);
        quantity.setBounds(20,140,100,25);
        price.setBounds(20,180,100,25);
        supplier.setBounds(20,220,100,25);

        formPanel.add(id);
        formPanel.add(name);
        formPanel.add(category);
        formPanel.add(quantity);
        formPanel.add(price);
        formPanel.add(supplier);

        idField = new JTextField();
        nameField = new JTextField();
        categoryField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        supplierField = new JTextField();

        idField.setBounds(120,20,180,25);
        nameField.setBounds(120,60,180,25);
        categoryField.setBounds(120,100,180,25);
        quantityField.setBounds(120,140,180,25);
        priceField.setBounds(120,180,180,25);
        supplierField.setBounds(120,220,180,25);

        formPanel.add(idField);
        formPanel.add(nameField);
        formPanel.add(categoryField);
        formPanel.add(quantityField);
        formPanel.add(priceField);
        formPanel.add(supplierField);

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");

        add.setBounds(30,280,120,35);
        update.setBounds(180,280,120,35);
        delete.setBounds(30,330,120,35);
        clear.setBounds(180,330,120,35);

        add.setBackground(new Color(102,178,255)); // Blue
        update.setBackground(new Color(255,153,204)); // Pink
        delete.setBackground(new Color(255,102,102)); // Red
        clear.setBackground(new Color(255,204,102)); // Yellow

        formPanel.add(add);
        formPanel.add(update);
        formPanel.add(delete);
        formPanel.add(clear);

        status = new JLabel("");
        status.setBounds(30,390,250,30);
        status.setForeground(Color.BLUE);
        formPanel.add(status);

        // Search
        JLabel searchLabel = new JLabel("Search");
        searchLabel.setBounds(400,80,80,25);
        frame.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(460,80,200,25);
        frame.add(searchField);

        JButton search = new JButton("Search");
        search.setBounds(680,80,100,25);
        search.setBackground(new Color(102,178,255));
        frame.add(search);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Quantity");
        model.addColumn("Price");
        model.addColumn("Supplier");

        table = new JTable(model);
        table.setRowHeight(25);
        table.setBackground(new Color(224,255,255));

        table.getTableHeader().setBackground(new Color(255,182,193));
        table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(400,120,550,430);
        frame.add(pane);

        // Add
        add.addActionListener(e->{

            model.addRow(new Object[]{
                    idField.getText(),
                    nameField.getText(),
                    categoryField.getText(),
                    quantityField.getText(),
                    priceField.getText(),
                    supplierField.getText()
            });

            status.setText("Item Added Successfully");
        });

        // Update
        update.addActionListener(e->{

            int i = table.getSelectedRow();

            if(i>=0){

                model.setValueAt(idField.getText(),i,0);
                model.setValueAt(nameField.getText(),i,1);
                model.setValueAt(categoryField.getText(),i,2);
                model.setValueAt(quantityField.getText(),i,3);
                model.setValueAt(priceField.getText(),i,4);
                model.setValueAt(supplierField.getText(),i,5);

                status.setText("Item Updated");
            }
        });

        // Delete
        delete.addActionListener(e->{

            int i = table.getSelectedRow();

            if(i>=0){
                model.removeRow(i);
                status.setText("Item Deleted");
            }
        });

        // Clear
        clear.addActionListener(e->{

            idField.setText("");
            nameField.setText("");
            categoryField.setText("");
            quantityField.setText("");
            priceField.setText("");
            supplierField.setText("");
        });

        // Search
        search.addActionListener(e->{

            String key = searchField.getText();

            for(int i=0;i<table.getRowCount();i++){

                if(model.getValueAt(i,1).toString().equalsIgnoreCase(key)){

                    table.setRowSelectionInterval(i,i);
                    status.setText("Item Found");
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new ColorfulInventory();
    }
}