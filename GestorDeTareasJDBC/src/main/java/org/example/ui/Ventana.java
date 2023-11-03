package org.example.ui;

import org.example.domain.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Ventana extends JFrame {
    public static final TareaDAOImp TAREA_DAO = new TareaDAOImp(DBConnection.getConnection());
    public static final UserDAOImp DAO_USUARIOS = new UserDAOImp(DBConnection.getConnection());

    private JPanel panel1;
    private JLabel info;
    private JTable table1;
    private JTextField txtTarea;
    private JTextField txtDescripcion;
    private JComboBox comboCategoria;
    private JComboBox comboPrioridad;
    private JComboBox comboUsuario;
    private JLabel txtId;
    private JButton actualizarButton;
    private JButton crearNuevoButton;

    private DefaultTableModel data;

    private ListSelectionModel selectionModel;

    private ArrayList<Tarea> tareas = new ArrayList<>(0);
    private ArrayList<User> usuarios = new ArrayList<>(0);
    private Tarea tareaActual = null;

    public Ventana(){
        this.setContentPane(panel1);
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Acceso con JDBC");


        // usuario 0 del combo es el 0 de la lista ...

        comboPrioridad.addItem("Alta");
        comboPrioridad.addItem("Media");
        comboPrioridad.addItem("Baja");

        comboCategoria.addItem("Trabajo");
        comboCategoria.addItem("Personal");
        comboCategoria.addItem("Estudios");

        table1.setRowHeight(40);
        data = (DefaultTableModel) table1.getModel();
        data.addColumn("id");
        data.addColumn("tarea");
        data.addColumn("prioridad");
        data.addColumn("usuario");
        data.addColumn("categoria");
        data.addColumn("descripción");

        //var datos = Database.getAll();

        //datos.forEach( (e)-> data.addRow(e) );
        //table1.doLayout();
        //

        usuarios = DAO_USUARIOS.loadAll();
        comboUsuario.removeAllItems();
        usuarios.forEach( user -> {
            comboUsuario.addItem(user.getName());
        });

        tareas = TAREA_DAO.loadAll();

        fillTable(tareas);

        table1.getSelectionModel().addListSelectionListener(evt -> showTareaDetails(evt) );
        actualizarButton.addActionListener(e -> updateTarea() );

        crearNuevoButton.addActionListener(e -> saveTarea() );
    }

    private void saveTarea() {
        tareaActual = new Tarea();
        updateTareaActualFromPanel();

        TAREA_DAO.save(tareaActual);

        showTareaActualInPanel();

        tareas = TAREA_DAO.loadAll();
        fillTable(tareas);
    }

    private void updateTareaActualFromPanel() {
        tareaActual.setTitulo( txtTarea.getText());
        tareaActual.setUsuario_id( usuarios.get(comboUsuario.getSelectedIndex()).getId());
        tareaActual.setUsuario( usuarios.get(comboUsuario.getSelectedIndex()) );
        tareaActual.setPrioridad((String) comboPrioridad.getSelectedItem()) ;
        tareaActual.setCategoria((String) comboCategoria.getSelectedItem());
        tareaActual.setDescripcion( txtDescripcion.getText());
        System.out.println(tareaActual);
        info.setText(tareaActual.toString());
    }

    private void updateTarea() {
        updateTareaActualFromPanel();
    }

    private void showTareaDetails(ListSelectionEvent evt) {
        if(!evt.getValueIsAdjusting()) {
            Integer selectedRow = table1.getSelectedRow();
            Long id = Long.valueOf( (String) table1.getValueAt(selectedRow, 0));
            //Tarea t = dao.load(id);
            Tarea t = tareas.get(selectedRow);
            t = TAREA_DAO.load(t.getId());
            //User u = new UserDAOImp(DBConnection.getConnection()).load(t.getUsuario_id());

            if (t != null) {
                //JOptionPane.showMessageDialog(null, t);
                info.setText(t.toString());
            }
            tareaActual = t;

            showTareaActualInPanel();
        }
    }

    private void showTareaActualInPanel() {
        txtId.setText(""+ tareaActual.getId() );
        txtDescripcion.setText( tareaActual.getDescripcion());
        txtTarea.setText(tareaActual.getTitulo());
        comboCategoria.setSelectedItem( tareaActual.getCategoria() );
        comboPrioridad.setSelectedItem( tareaActual.getPrioridad() );
        comboUsuario.setSelectedItem( tareaActual.getUsuario().getName() );
    }

    private void fillTable(ArrayList<Tarea> tareas) {
        data.setRowCount(0);
        tareas.forEach( (t)->{
            //data.addRow( new TareaAdapter(t).toArrayString() ) ;
            data.addRow( new TareaAdapter(t).toArrayStringWithUser());
        });
        info.setText("Datos cargados correctamente");
    }

    public void load(){
        setVisible(true);
    }
}
