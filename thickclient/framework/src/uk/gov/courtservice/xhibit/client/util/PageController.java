package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;

public class PageController {
	
	protected boolean enabled = true;
	private boolean pageChanged = false;

	public PageController() {
	}
	
	public void reset() {
		enabled = true;
		pageChanged = false;
	}
	
	public boolean isPageChanged() {
		return pageChanged;
	}
	
	protected void setPageChanged() {
		if (enabled) {
			pageChanged = true;
		}
	}
	
	public void addChangeListeners(Component[] components) {
		for (Component component : Arrays.asList(components)) {
			if (component instanceof JScrollPane) {
				Component[] childComponents = ((JScrollPane)component).getViewport().getComponents();
				for (Component childComponent : Arrays.asList(childComponents)) {
					addComponentListener(childComponent);
				}
			} else if (component instanceof JPanel) {
				Component[] childComponents = ((JPanel)component).getComponents();
				for (Component childComponent : Arrays.asList(childComponents)) {
					addComponentListener(childComponent);
				}	
			} else {
				addComponentListener(component);
			}
		}
	}	
			
	private void addComponentListener(Component component) {
		if (component instanceof XTextField) {
			((XTextField) component).getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					setPageChanged();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					setPageChanged();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					setPageChanged();
				}
			});
		} else if (component instanceof XTextArea) {
			((XTextArea) component).getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					setPageChanged();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					setPageChanged();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					setPageChanged();
				}
			});
		} else if (component instanceof JRadioButton) {
			((JRadioButton) component).addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED ||e.getStateChange() == ItemEvent.DESELECTED) {
						setPageChanged();
					}
				}
			});
		} else if (component instanceof XDatePanel) {
			((XDatePanel) component).getDateComponent().addMChangeListener(new MChangeListener() {
				@Override
				public void valueChanged(MChangeEvent event) {
					if (event.getType() == MChangeEvent.CHANGE) { 
						setPageChanged();
					} 
				}
			});

			((XDatePanel) component).getEntryField().getDisplay().addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					setPageChanged();
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}
			});
		} else if (component instanceof JComboBox) {
			((JComboBox) component).addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setPageChanged();
				}
			});
		} else if (component instanceof JCheckBox) {
			((JCheckBox) component).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setPageChanged();
				}
			});
		}
	}
}