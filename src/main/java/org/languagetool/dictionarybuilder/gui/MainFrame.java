package org.languagetool.dictionarybuilder.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.languagetool.dictionarybuilder.SpellDictionaryBuilder;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5839119841233482791L;
	private static final String OUTPUT_FILE_SUFFIX = ".dict";
	private static final Dimension FRAME_SIZE = new Dimension(550, 250);
	private JLabel infoPathLabel;
	private JLabel dictionaryPathLabel;
	private JLabel outputPathLabel;
	private JButton generateButton;

	public MainFrame() {
		super("SpellDictionaryBuilder");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		JPanel boxPanel = new JPanel();
		boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

		boxPanel.add(createInfoFilePanel());
		boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		boxPanel.add(createDictionaryFilePanel());
		boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		boxPanel.add(createOutputFilePanel());
		boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		boxPanel.add(createGeneratePanel());

		getContentPane().add(boxPanel, BorderLayout.PAGE_START);

		setSize(FRAME_SIZE);
		setLocationByPlatform(true);

		checkGenerateButtonAvailability();
	}

	private JPanel createGeneratePanel() {
		JPanel panel = new JPanel(new FlowLayout());

		generateButton = new JButton("Generate");
		generateButton.setHorizontalAlignment(JButton.CENTER);
		generateButton.addActionListener(this);
		panel.add(generateButton);
		return panel;
	}

	private void checkGenerateButtonAvailability() {
		boolean emptyPaths = infoPathLabel.getText().isEmpty();
		emptyPaths &= dictionaryPathLabel.getText().isEmpty();
		emptyPaths &= outputPathLabel.getText().isEmpty();
		generateButton.setEnabled(!emptyPaths);
	}

	private Component createOutputFilePanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(createDescriptionLabel("Output Dictionary file: "), BorderLayout.LINE_START);

		outputPathLabel = new JLabel();
		outputPathLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.add(outputPathLabel, BorderLayout.CENTER);

		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(desktop());
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setDialogTitle("Output dictionary file");
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return ".dict Dictionary files";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().toLowerCase().endsWith(OUTPUT_FILE_SUFFIX);
				}
			});
			if (chooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath();
				if (!path.toLowerCase().endsWith(OUTPUT_FILE_SUFFIX))
					path += OUTPUT_FILE_SUFFIX;
				outputPathLabel.setText(path);
				checkGenerateButtonAvailability();
			}
		});

		panel.add(selectButton, BorderLayout.LINE_END);
		return panel;
	}

	private JPanel createDictionaryFilePanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(createDescriptionLabel("Input Dictionary file: "), BorderLayout.LINE_START);

		dictionaryPathLabel = new JLabel();
		dictionaryPathLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.add(dictionaryPathLabel, BorderLayout.CENTER);

		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(e -> {
			chooseLoadFileAndSetPathToLabel(dictionaryPathLabel);
		});

		panel.add(selectButton, BorderLayout.LINE_END);
		return panel;
	}

	private JPanel createInfoFilePanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(createDescriptionLabel("Info file path: "), BorderLayout.LINE_START);

		infoPathLabel = new JLabel();
		infoPathLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.add(infoPathLabel, BorderLayout.CENTER);

		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(e -> {
			chooseLoadFileAndSetPathToLabel(infoPathLabel);
		});

		panel.add(selectButton, BorderLayout.LINE_END);
		return panel;
	}

	private void chooseLoadFileAndSetPathToLabel(JLabel label) {
		JFileChooser chooser = new JFileChooser(desktop());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
			label.setText(chooser.getSelectedFile().getAbsolutePath());
			checkGenerateButtonAvailability();
		}
	}

	private JLabel createDescriptionLabel(String description) {
		JLabel label = new JLabel("<html><p style= 'width:100px';>" + description + "</p></html>");
		return label;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			SpellDictionaryBuilder builder = new SpellDictionaryBuilder(new File(infoPathLabel.getText()));
			builder.setOutputFilename(outputPathLabel.getText());
			builder.build(new File(dictionaryPathLabel.getText()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, stackTraceToString(e1), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}

	private static File desktop() {
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		return desktop;
	}

	private static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
		});
	}
}
