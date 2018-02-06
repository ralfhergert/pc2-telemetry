package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.repository.LookupRepository;
import de.ralfhergert.telemetry.repository.Repository;
import de.ralfhergert.telemetry.repository.RepositoryConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

/**
 * This panel groups multiple {@link GraphCanvas}.
 */
public class MultiGraphCanvas extends JPanel {

	private final LookupRepository<String,LineGraph> lookupRepository;

	private final MouseListener popupListener;
	private final Action addLineGraphAction;

	public MultiGraphCanvas(Repository<LineGraph> repository) {
		super(new GridBagLayout());
		setBackground(Color.LIGHT_GRAY);

		lookupRepository = new LookupRepository<>((graph) -> String.valueOf(graph.getProperty("name", "")));
		new RepositoryConnector<>(repository, lookupRepository);

		addLineGraphAction = new AbstractAction(ResourceBundle.getBundle("messages").getString("multiGraphCanvas.action.addFurtherGraphCanvas")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphCanvas graphCanvas = new GraphCanvas();
				graphCanvas.addMouseListener(popupListener);
				add(graphCanvas, createLayoutConstraints());
				revalidate();
			}
		};

		popupListener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					JPopupMenu popup = new JPopupMenu();
					popup.add(new JMenuItem(addLineGraphAction));
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		addMouseListener(popupListener);
	}

	protected GridBagConstraints createLayoutConstraints() {
		return new GridBagConstraints(0,-1,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0);
	}
}
