package topiarytool;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;


public class TreeToolbar extends JToolBar {

    JButton zoomOutButton  = new JButton("-");
    JButton zoomInButton = new JButton("+");
    JSlider zoomSlider = new JSlider(0, 8, 0);
    JTextField search = new JTextField();
    JPanel spacer1 = new JPanel();

    MainFrame frame = null;

    public TreeToolbar(MainFrame _frame) {

        frame = _frame;

        zoomOutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                zoomSlider.setValue(zoomSlider.getValue() - 1);
                syncTreeWithZoomSlider();
            }

        });
        zoomInButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                zoomSlider.setValue(zoomSlider.getValue() + 1);
                syncTreeWithZoomSlider();
            }

        });
        add(spacer1);
        add(zoomOutButton);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setPreferredSize(new Dimension(200,28));
        zoomSlider.setMaximumSize(new Dimension(200,28));
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (zoomSlider.getValueIsAdjusting()){
                    syncTreeWithZoomSlider();
                }
            }
        });

        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void changedUpdate(javax.swing.event.DocumentEvent evt) {
				doSearch();
			}

			public void insertUpdate(javax.swing.event.DocumentEvent evt) {
				doSearch();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent evt) {
				doSearch();
			}

			private void doSearch() {
				String str = search.getText().trim().toUpperCase();
				ArrayList<Node> nodes = frame.tree.getTree().getNodes();
				boolean first = true;
				for (Node n : nodes) {
					if (str.length() > 0 && n.getLabel().toUpperCase().indexOf(str) != -1) {
						if (first) {
							frame.tree.setSelectedNode(n);
							first = false;
						}
						frame.tree.getHilightedNodes().add(n);
					} else {
						frame.tree.getHilightedNodes().remove(n);
					}
				}
				frame.tree.redraw();
			}
		});


        add(zoomSlider);
        add(zoomInButton);
        setFloatable(false);
        spacer1.setPreferredSize(new Dimension(100,10));
        add(spacer1);
        add(new JLabel("Search:"));
        search.setPreferredSize(new Dimension(100,28));
        search.setMaximumSize(new Dimension(100,28));
        add(search);
    }

    public void syncTreeWithZoomSlider() {
        if (frame.tree.getTree() == null) return;
		double minXScale = (frame.tree.getWidth()-frame.tree.getMargin()-frame.tree.getTreeMargin())/frame.tree.getTree().depth();
        double newScale = minXScale * Math.pow(Math.pow(2.0, 0.5), zoomSlider.getValue());
        frame.tree.setScaleFactor(newScale, frame.tree.getYScale(), frame.tree.getXStart(), frame.tree.height/2.0);
    }

    public void syncZoomSliderWithTree() {
        if (frame.tree.getTree() == null) return;
		double minXScale = (frame.tree.getWidth()-frame.tree.getMargin()-frame.tree.getTreeMargin())/frame.tree.getTree().depth();
        int currStep = (int) Math.floor(Math.log(frame.tree.getXScale()/minXScale) / Math.log(Math.pow(2.0,0.5))+0.00001);
        zoomSlider.setValue(currStep);
    }

}
