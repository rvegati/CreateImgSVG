/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 *
 * @author OIM
 */
public class JFCrearSVG extends javax.swing.JFrame {

    private ActionListener btnBuscarRutaActionListener;
    private ActionListener btnGenerarSVGActionListener;
    private FocusListener txtNombreArchivoFocusListener;
    private FocusListener txtaNombreArchivoFocusListener;
    private WindowListener jfCrearSVGWindowListener;
    private FlavorListener clipboardFlavorListener;
    private final Clipboard clipboard;
    private int txtNombreArchivoFocus;
    private int txtaNombreArchivoFocus;
    //StringSelection stringSelection = new StringSelection(myString);
    //Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
    //clpbrd.setContents(stringSelection, null);

    /**
     * Creates new form JFCrearSVG
     */
    public JFCrearSVG() {
        initComponents();
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.txtNombreArchivoFocus = -1;
        this.txtaNombreArchivoFocus = -1;
        this.CrearEventos();
        this.AgregarEventosPrincipal();
    }

    private void CrearEventos() {
        this.btnBuscarRutaActionListener = (e) -> {
            BuscarRuta();
        };
        this.btnGenerarSVGActionListener = (e) -> {
            GenerarSVG();
        };
        this.jfCrearSVGWindowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                RemoverEventos();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                AgregarEventosSecundario();
            }
        };
        this.txtNombreArchivoFocusListener = new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("txtNombreArchivo pierder foco");
//                txtNombreArchivoFocus = 0;
            }

            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("txtNombreArchivo gana foco");
                txtaNombreArchivoFocus = 0;
                txtNombreArchivoFocus = 1;
            }
        };
        this.txtaNombreArchivoFocusListener = new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("txtaNombreArchivo pierde foco");
//                txtaNombreArchivoFocus = 0;
            }

            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("txtaNombreArchivo gana foco");
                txtNombreArchivoFocus = 0;
                txtaNombreArchivoFocus = 1;
            }
        };
        this.clipboardFlavorListener = (FlavorEvent e) -> {
            try {
                Thread.sleep(200);
                if (clipboard.getContents(null) != null && clipboard.getContents(null).isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String texto = (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
                    if (txtNombreArchivoFocus == 1) {
                        txtNombreArchivo.setText(texto);
                        txtaNombreArchivo.requestFocus();
                    }
                    if (txtaNombreArchivoFocus == 1) {
                        txtaNombreArchivo.setText(texto);
                        btnGenerarSVG.doClick();
                    }
                }
            } catch (UnsupportedFlavorException | IOException | InterruptedException ex) {
                JOptionPane.showMessageDialog(null, (ex.getMessage() != null) ? ex.getMessage() : ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private void AgregarEventosPrincipal() {
        this.addWindowListener(this.jfCrearSVGWindowListener);
    }

    private void AgregarEventosSecundario() {
        this.btnBuscarRuta.addActionListener(this.btnBuscarRutaActionListener);
        this.btnGenerarSVG.addActionListener(this.btnGenerarSVGActionListener);
        this.txtNombreArchivo.addFocusListener(this.txtNombreArchivoFocusListener);
        this.txtaNombreArchivo.addFocusListener(this.txtaNombreArchivoFocusListener);
        this.clipboard.addFlavorListener(this.clipboardFlavorListener);
    }

    private void RemoverEventos() {
        this.btnBuscarRuta.removeActionListener(this.btnBuscarRutaActionListener);
        this.btnGenerarSVG.removeActionListener(this.btnGenerarSVGActionListener);
        this.txtNombreArchivo.removeFocusListener(this.txtNombreArchivoFocusListener);
        this.txtaNombreArchivo.removeFocusListener(this.txtaNombreArchivoFocusListener);
        this.clipboard.removeFlavorListener(this.clipboardFlavorListener);
        this.removeWindowListener(this.jfCrearSVGWindowListener);
    }

    private void BuscarRuta() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(fileChooser);
        this.txtDireccion.setText((fileChooser.getSelectedFile() != null) ? fileChooser.getSelectedFile().getAbsolutePath() : "");
    }

    private void GenerarSVG() {
        String msj = this.ValidarGenerarSVG();
        if (!msj.isEmpty()) {
            JOptionPane.showMessageDialog(null, msj.trim(), "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String svg = this.txtaNombreArchivo.getText().trim();
            int tamanio = 17;
            int recalculo;
            while (tamanio > 16) {
//                if (tamanio == 17) {
//                    List<Object[]> lineas = new ArrayList<>();
//                    String link = "https://www.flaticon.es/icono-gratis/campo_4614470?k=1658689267753&log-in=google#";
//                    URL url = new URL(link);
//                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//                    String inputLine = "", inputText = "";
//                    int linea = 0;
//                    while ((inputLine = in.readLine()) != null) {
//                        inputText = inputText + inputLine;
//                        lineas.add(new Object[]{linea, inputLine});
//                        linea++;
//                    }
//                    System.out.println(inputText);
//                    lineas.stream().forEach(p -> {
//                        System.out.println("POS: " + p[0] + " | LINEA" + p[1]);
//                    });
//                    in.close();
//                }
                tamanio = this.RetornarTamanio(svg);
//                System.out.println("SIZE: " + tamanio);
                File file = new File(this.txtDireccion.getText() + "\\svg");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String ruta = this.txtDireccion.getText() + "\\svg" + "\\" + this.txtNombreArchivo.getText().trim() + "_" + tamanio + "x" + tamanio + ".svg";
                PrintWriter writer = new PrintWriter(ruta, "UTF-8");
                writer.println(svg);
                writer.close();
                this.convertSVGtoPNG(ruta);
                recalculo = this.RecalcularTamanio(tamanio);
//                System.out.println("RECALCULO: " + recalculo);
                svg = svg.trim()
                        .replaceAll("width=\"" + tamanio + "\"", "width=\"" + recalculo + "\"")
                        .replaceAll("height=\"" + tamanio + "\"", "height=\"" + recalculo + "\"");
//                this.MostrarImagenSVGLabel(ruta);
            }
            this.txtaNombreArchivo.setText("");
            this.txtNombreArchivo.setText("");
            this.txtNombreArchivo.requestFocus();
            JOptionPane.showMessageDialog(null, "Se generaron el SVG y el PNG exitosamente.", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException | UnsupportedEncodingException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String ValidarGenerarSVG() {
        String msj = "";
        if (this.txtDireccion.getText().trim().isEmpty()) {
            msj = msj.trim() + "\n" + "Debe seleccionar Dirección de destino.";
        }
        if (this.txtNombreArchivo.getText().trim().isEmpty()) {
            msj = msj.trim() + "\n" + "Debe Ingresar un nombre al archivo SVG a crear.";
        }
        if (this.txtaNombreArchivo.getText().trim().isEmpty()) {
            msj = msj.trim() + "\n" + "Ingrese Data SVG";
        }
        return msj;
    }

//    private void MostrarImagenSVGLabel(String ruta) {
//        try {
//            File file = new File(ruta);
//            System.out.println(ruta);
//            System.out.println(ruta.replaceAll("\\\\", "/"));
//            if (file.exists()) {
////                this.lblMuestraImagen.setIcon(new ImageIcon(file.toURL()));
//                this.lblMuestraImagen.setIcon(new ImageIcon(getClass().getResource(ruta.replaceAll("\\\\", "/"))));
//            } else {
//                JOptionPane.showMessageDialog(null, "No se logro crear el archivo.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (HeadlessException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//        } catch (Exception e) {
//            System.out.println(e);
//            System.out.println(e.getLocalizedMessage());
//            System.out.println(e.getMessage());
//            System.out.println(e.toString());
//            JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
//        /*catch (MalformedURLException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR);
//        }*/
//    }
    private int RecalcularTamanio(int tamanio) {
        int nuevoTamanio;
        if (tamanio <= 64) {
            nuevoTamanio = tamanio - 8;
        } else {
            nuevoTamanio = tamanio / 2;
        }
        return nuevoTamanio;
    }

    private int RetornarTamanio(String svg) {
        int tamanio = 0;
        if (svg.trim().contains("width=\"512\"") || svg.trim().contains("height=\"512\"")) {
            tamanio = 512;
        }
        if (svg.trim().contains("width=\"256\"") || svg.trim().contains("height=\"256\"")) {
            tamanio = 256;
        }
        if (svg.trim().contains("width=\"128\"") || svg.trim().contains("height=\"128\"")) {
            tamanio = 128;
        }
        if (svg.trim().contains("width=\"64\"") || svg.trim().contains("height=\"64\"")) {
            tamanio = 64;
        }
        if (svg.trim().contains("width=\"56\"") || svg.trim().contains("height=\"56\"")) {
            tamanio = 56;
        }
        if (svg.trim().contains("width=\"48\"") || svg.trim().contains("height=\"48\"")) {
            tamanio = 48;
        }
        if (svg.trim().contains("width=\"40\"") || svg.trim().contains("height=\"40\"")) {
            tamanio = 40;
        }
        if (svg.trim().contains("width=\"32\"") || svg.trim().contains("height=\"32\"")) {
            tamanio = 32;
        }
        if (svg.trim().contains("width=\"24\"") || svg.trim().contains("height=\"24\"")) {
            tamanio = 24;
        }
        if (svg.trim().contains("width=\"16\"") || svg.trim().contains("height=\"16\"")) {
            tamanio = 16;
        }
        return tamanio;
    }

    private void convertSVGtoPNG(String ruta) {
        try {
            String cpng = ruta.split("\\\\svg")[0] + "\\png";
            File file = new File(cpng);
            if (!file.exists()) {
                file.mkdirs();
            }
            //Step -1: We read the input SVG document into Transcoder Input
            //We use Java NIO for this purpose
            String svg_URI_input = Paths.get(ruta).toUri().toURL().toString();
            TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);
            //Step-2: Define OutputStream to PNG Image and attach to TranscoderOutput
            OutputStream png_ostream = new FileOutputStream(ruta.replaceAll("\\.svg", "\\.png").replaceAll("svg", "png"));
            TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
            // Step-3: Create PNGTranscoder and define hints if required
            PNGTranscoder my_converter = new PNGTranscoder();
            // Step-4: Convert and Write output
            my_converter.transcode(input_svg_image, output_png_image);
            // Step 5- close / flush Output Stream
            png_ostream.flush();
            png_ostream.close();
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, ((e.getMessage() != null) ? e.getMessage() : e.toString()), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException | TranscoderException e) {
            JOptionPane.showMessageDialog(null, ((e.getMessage() != null) ? e.getMessage() : e.toString()), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, ((e.getMessage() != null) ? e.getMessage() : e.toString()), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, ((e.getMessage() != null) ? e.getMessage() : e.toString()), "ERROR", JOptionPane.ERROR_MESSAGE);
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

        paGeneral = new javax.swing.JPanel();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        btnBuscarRuta = new javax.swing.JButton();
        lblNombreArichivo = new javax.swing.JLabel();
        txtNombreArchivo = new javax.swing.JTextField();
        spSvg = new javax.swing.JScrollPane();
        txtaNombreArchivo = new javax.swing.JTextArea();
        btnGenerarSVG = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CREATE SVG");
        setResizable(false);

        lblDireccion.setText("Dirección:");

        txtDireccion.setEditable(false);

        btnBuscarRuta.setText("...");

        lblNombreArichivo.setText("Name:");

        txtaNombreArchivo.setColumns(20);
        txtaNombreArchivo.setLineWrap(true);
        txtaNombreArchivo.setRows(5);
        txtaNombreArchivo.setWrapStyleWord(true);
        spSvg.setViewportView(txtaNombreArchivo);

        btnGenerarSVG.setText("GENERAR SVG");

        javax.swing.GroupLayout paGeneralLayout = new javax.swing.GroupLayout(paGeneral);
        paGeneral.setLayout(paGeneralLayout);
        paGeneralLayout.setHorizontalGroup(
            paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paGeneralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerarSVG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paGeneralLayout.createSequentialGroup()
                        .addGroup(paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNombreArichivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paGeneralLayout.createSequentialGroup()
                                .addComponent(txtDireccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarRuta))
                            .addComponent(txtNombreArchivo)))
                    .addComponent(spSvg, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        paGeneralLayout.setVerticalGroup(
            paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paGeneralLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarRuta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreArichivo)
                    .addComponent(txtNombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spSvg, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerarSVG)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarRuta;
    private javax.swing.JButton btnGenerarSVG;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblNombreArichivo;
    private javax.swing.JPanel paGeneral;
    private javax.swing.JScrollPane spSvg;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombreArchivo;
    private javax.swing.JTextArea txtaNombreArchivo;
    // End of variables declaration//GEN-END:variables
}
