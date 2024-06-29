package view;

import controller.Controller;
import model.Photos;
import service.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Window extends JFrame {
    private static final String[] ROVERS = {"Curiosity", "Opportunity", "Spirit"};
    private static final String[] CAMERAS = {"CHEMCAM", "FHAZ", "MAST",  "MAHLI", "MARDI", "MINITES", "NAVCAM", "PANCAM", "RHAZ"};

    private JComboBox<String> roverBox;
    private JComboBox<String> cameraBox;
    private JTextField solText;
    private JTextField startDate;
    private JTextField endDate;
    private JButton btn1;
    private JPanel resultPanel;
    private JLabel status;

    private final Controller viewerController;

    public Window() {
        setTitle("Buscador de API Mars");
        setSize(1300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        startDatePanel.add(new JLabel("Fecha desde donde iniciar busqueda : \n aaaa-mm-dd"));
        startDate = new JTextField(10);
        startDate.setText("aaaa-mm-dd");
        startDate.setToolTipText("Ingrese la fecha valida.");
        startDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (startDate.getText().equals("aaaa-mm-dd")) {
                    startDate.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (startDate.getText().isEmpty()) {
                    startDate.setText("aaaa-mm-dd");
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel cameraPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        cameraPanel.add(new JLabel("Camera:"));
        cameraBox = new JComboBox<>(CAMERAS);
        cameraPanel.add(cameraBox);


        JPanel solPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        solPanel.add(new JLabel("Sol:"));
        solText = new JTextField(5);
        solText.setToolTipText("Ingrese el número de sol (día marciano).");
        solPanel.add(solText);

        JPanel roverPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        roverPanel.add(new JLabel("Rover:"));
        roverBox = new JComboBox<>(ROVERS);
        roverPanel.add(roverBox);

        startDatePanel.add(startDate);

        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        endDatePanel.add(new JLabel("Fecha desde donde finaliza la busqueda : \n aaaa-mm-dd"));
        endDate = new JTextField(10);
        endDate.setText("aaaa-mm-dd");
        endDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (endDate.getText().equals("aaaa-mm-dd")) {
                    endDate.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (endDate.getText().isEmpty()) {
                    endDate.setText("aaaa-mm-dd");
                }
            }
        });
        endDatePanel.add(endDate);


        JPanel fetchButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        btn1 = new JButton("Buscar");
        fetchButtonPanel.add(btn1);

        controlPanel.add(roverPanel);
        controlPanel.add(cameraPanel);
        controlPanel.add(solPanel);
        controlPanel.add(startDatePanel);
        controlPanel.add(endDatePanel);
        controlPanel.add(fetchButtonPanel);

        add(controlPanel, BorderLayout.EAST);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment
        add(new JScrollPane(resultPanel), BorderLayout.CENTER);

        status = new JLabel();
        add(status, BorderLayout.SOUTH);

        btn1.addActionListener(e -> loads_photos());

        this.viewerController = new Controller(new Service());
    }

    private void loads_photos() {
        try {
            String rover = (String) roverBox.getSelectedItem();
            String camera = (String) cameraBox.getSelectedItem();
            String solText = this.solText.getText();
            String startDateText = startDate.getText();
            String endDateText = endDate.getText();

            if (solText.isEmpty() || startDateText.isEmpty() || endDateText.isEmpty()) {
                showErrorMessage("Por favor ingrese los datos requeridos correctamente.");
                return;
            }

            int sol = Integer.parseInt(solText);
            LocalDate startDate = invalid_date(startDateText);
            LocalDate endDate = invalid_date(endDateText);

            List<Photos> photos = viewerController.fetchPhotos(rover, sol, camera, startDate, endDate);
            displayPhotos(photos);
            status.setText("Busqueda finalizada.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMessage("Error al en la busqueda: " + ex.getMessage());
        }
    }

    private LocalDate invalid_date(String dateText) {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            showErrorMessage("Formato de fecha inválido. Utilice el formato YYYY-MM-DD.");
            throw e;
        }
    }

    private void displayPhotos(List<Photos> photos) {
        resultPanel.removeAll(); // Limpiar el panel antes de agregar nuevas fotos

        for (Photos photo : photos) {
            JPanel singlePhotoPanel = new JPanel();
            singlePhotoPanel.setLayout(new BoxLayout(singlePhotoPanel, BoxLayout.Y_AXIS));
            singlePhotoPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear componentes a la izquierda

            JLabel dateLabel = new JLabel("Date: " + photo.getEarth_date().toString());
            JLabel cameraLabel = new JLabel("Camera: " + photo.getCm_name() + " (" + photo.getCm_full_name() + ")");
            JLabel roverLabel = new JLabel("Rover: " + photo.getRover_name() + " - Sol " + photo.getMax_sol());

            dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            cameraLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            roverLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Obtener la URL de la imagen
            String imageUrlStr = photo.getImg_src();

            // Crear un JLabel como enlace para abrir la URL en el navegador
            JLabel urlLabel = new JLabel("<html><a href=\"" + imageUrlStr + "\">" + imageUrlStr + "</a></html>");
            urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambiar el cursor al estilo de mano
            urlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            urlLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(imageUrlStr));
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                            showErrorMessage("Error al abrir la URL: " + ex.getMessage());
                        }
                    } else {
                        showErrorMessage("Error al abrir navegador.");
                    }
                }
            });

            // Agregar los detalles de la foto y el enlace al panel
            singlePhotoPanel.add(urlLabel);
            singlePhotoPanel.add(dateLabel);
            singlePhotoPanel.add(cameraLabel);
            singlePhotoPanel.add(roverLabel);

            resultPanel.add(singlePhotoPanel); // Agregar el panel de la foto al panel principal
        }

        resultPanel.revalidate(); // Revalidar el panel para actualizar la interfaz gráfica
        resultPanel.repaint(); // Repintar el panel para mostrar los cambios
    }


    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        status.setText(message);
    }



}
