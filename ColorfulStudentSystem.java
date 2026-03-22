import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class ColorfulStudentSystem extends JFrame {

    JTextField idField, nameField, courseField, emailField, phoneField, searchField;
    JTable table;
    DefaultTableModel model;

    public ColorfulStudentSystem() {

        setTitle("🌸 Student Information System");
        setSize(950,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255,230,250));
        add(mainPanel);

        // Title
        JLabel title = new JLabel("Student Information System", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setForeground(new Color(255,20,147));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mainPanel.add(title, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6,2,15,15));
        inputPanel.setBackground(new Color(255,255,180));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        Dimension bigField = new Dimension(220,40);

        idField = new JTextField();
        nameField = new JTextField();
        courseField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        searchField = new JTextField();

        JTextField[] fields = {idField,nameField,courseField,emailField,phoneField,searchField};

        for(JTextField f : fields){
            f.setPreferredSize(bigField);
            f.setFont(fieldFont);
        }

        JLabel idLabel = new JLabel("Student ID"); idLabel.setFont(labelFont);
        JLabel nameLabel = new JLabel("Name"); nameLabel.setFont(labelFont);
        JLabel courseLabel = new JLabel("Course"); courseLabel.setFont(labelFont);
        JLabel emailLabel = new JLabel("Email"); emailLabel.setFont(labelFont);
        JLabel phoneLabel = new JLabel("Phone"); phoneLabel.setFont(labelFont);
        JLabel searchLabel = new JLabel("Search"); searchLabel.setFont(labelFont);

        inputPanel.add(idLabel); inputPanel.add(idField);
        inputPanel.add(nameLabel); inputPanel.add(nameField);
        inputPanel.add(courseLabel); inputPanel.add(courseField);
        inputPanel.add(emailLabel); inputPanel.add(emailField);
        inputPanel.add(phoneLabel); inputPanel.add(phoneField);
        inputPanel.add(searchLabel); inputPanel.add(searchField);

        mainPanel.add(inputPanel, BorderLayout.WEST);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Course");
        model.addColumn("Email");
        model.addColumn("Phone");

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Arial",Font.PLAIN,16));
        table.setBackground(new Color(220,240,255));
        table.setSelectionBackground(new Color(255,182,193));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(100,149,237));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial",Font.BOLD,18));

        JScrollPane scroll = new JScrollPane(table);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255,230,250));

        JButton addBtn = createButton("Add", new Color(255,105,180));
        JButton updateBtn = createButton("Update", new Color(30,144,255));
        JButton deleteBtn = createButton("Delete", new Color(255,140,0));
        JButton clearBtn = createButton("Clear", new Color(138,43,226));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Events
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                idField.setText(model.getValueAt(row,0).toString());
                nameField.setText(model.getValueAt(row,1).toString());
                courseField.setText(model.getValueAt(row,2).toString());
                emailField.setText(model.getValueAt(row,3).toString());
                phoneField.setText(model.getValueAt(row,4).toString());
            }
        });

        // Search
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

                String text = searchField.getText().toLowerCase();

                for(int i=0;i<table.getRowCount();i++){

                    String name = model.getValueAt(i,1).toString().toLowerCase();

                    if(name.contains(text)){
                        table.setRowSelectionInterval(i,i);
                        break;
                    }
                }
            }
        });
    }

    JButton createButton(String text, Color color){

        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial",Font.BOLD,16));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120,40));

        return btn;
    }

    void addStudent(){

        if(idField.getText().isEmpty() || nameField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Fill required fields");
            return;
        }

        model.addRow(new Object[]{
                idField.getText(),
                nameField.getText(),
                courseField.getText(),
                emailField.getText(),
                phoneField.getText()
        });

        clearFields();
    }

    void updateStudent(){

        int row = table.getSelectedRow();

        if(row>=0){

            model.setValueAt(idField.getText(),row,0);
            model.setValueAt(nameField.getText(),row,1);
            model.setValueAt(courseField.getText(),row,2);
            model.setValueAt(emailField.getText(),row,3);
            model.setValueAt(phoneField.getText(),row,4);

        }else{
            JOptionPane.showMessageDialog(this,"Select a row first");
        }
    }

    void deleteStudent(){

        int row = table.getSelectedRow();

        if(row>=0){

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this student?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm == JOptionPane.YES_OPTION)
                model.removeRow(row);

        }else{
            JOptionPane.showMessageDialog(this,"Select a row first");
        }
    }

    void clearFields(){

        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    public static void main(String[] args){

        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch(Exception e){}

        new ColorfulStudentSystem().setVisible(true);
    }
}