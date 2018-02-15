package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.action.AddLineGraphToGraphCanvasAction;
import de.ralfhergert.telemetry.action.ChangeLineGraphColorAction;
import de.ralfhergert.telemetry.action.RemoveLineGraphFromGraphCanvasAction;
import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.repository.LookupRepository;
import de.ralfhergert.telemetry.repository.Repository;
import de.ralfhergert.telemetry.repository.RepositoryConnector;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This panel groups multiple {@link GraphCanvas}.
 */
public class MultiGraphCanvas extends JPanel implements Scrollable {

	private final LookupRepository<String,LineGraph> lookupRepository;

	private final MouseListener popupListener;
	private final Action addLineGraphAction;

	public MultiGraphCanvas(Repository<LineGraph> repository) {
		this(repository, null);
	}

	/**
	 * Creates a new {@link MultiGraphCanvas}.
	 * @param repository of {@link LineGraph} the user can choose from
	 * @param initialConfig the initial configuration used directly after creation. It is supposed
	 *        to be a list of lineGraph-names.
	 */
	public MultiGraphCanvas(Repository<LineGraph> repository, List<List<String>> initialConfig) {
		super(new GridBagLayout());
		setBackground(new Color(100, 100, 100));

		lookupRepository = new LookupRepository<>((graph) -> String.valueOf(graph.getProperty("name", "")));
		new RepositoryConnector<>(repository, lookupRepository);

		addLineGraphAction = new AbstractAction(ResourceBundle.getBundle("messages").getString("multiGraphCanvas.action.addGraphCanvas")) {
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
					createPopupFor(e.getComponent()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		addMouseListener(popupListener);

		if (initialConfig != null) {
			for (List<String> graphNames : initialConfig) {
				final GraphCanvas graphCanvas = new GraphCanvas();
				graphCanvas.addMouseListener(popupListener);
				add(graphCanvas, createLayoutConstraints());
				for (String graphName : graphNames) {
					LineGraph lineGraph = lookupRepository.getItem(graphName);
					if (lineGraph != null) {
						graphCanvas.addGraph(lineGraph);
					}
				}
			}
			revalidate();
		}
	}

	protected GridBagConstraints createLayoutConstraints() {
		return new GridBagConstraints(0,-1,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0);
	}

	public JPopupMenu createPopupFor(Component component) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new JMenuItem(addLineGraphAction));
		if (component instanceof GraphCanvas) {
			popupMenu.add(new JMenuItem(new RemoveGraphCanvasAction(this, (GraphCanvas)component)));
		}
		popupMenu.add(new JPopupMenu.Separator());
		if (component instanceof GraphCanvas) {
			GraphCanvas<?,?,?> graphCanvas = (GraphCanvas<?,?,?>)component;
			List<LineGraph> lineGraphsNotShown = lookupRepository.getItemStream().collect(Collectors.toList());
			graphCanvas.getGraphs().forEach((lineGraph) -> {
				lineGraphsNotShown.remove(lineGraph);
				JMenu graphMenu = new JMenu(String.valueOf(lineGraph.getProperty("name", "no name")));
				graphMenu.add(new JMenuItem(new RemoveLineGraphFromGraphCanvasAction(lineGraph, graphCanvas).withCaption(ResourceBundle.getBundle("messages").getString("generic.remove"))));
				graphMenu.add(new JMenuItem(new ChangeLineGraphColorAction(this, lineGraph)));
				popupMenu.add(graphMenu);
			});
			if (!lineGraphsNotShown.isEmpty()) {
				popupMenu.add(new JPopupMenu.Separator());
				lineGraphsNotShown.sort((g1, g2) -> ((String) g2.getProperty("name", "no name")).compareTo((String) g1.getProperty("name", "no name")));
				JMenu graphMenu = new JMenu(ResourceBundle.getBundle("messages").getString("multiGraphCanvas.action.addLineGraphToCanvas"));
				lineGraphsNotShown.forEach((lineGraph) -> {
					graphMenu.add(new JMenuItem(new AddLineGraphToGraphCanvasAction(lineGraph, graphCanvas).withCaption(String.valueOf(lineGraph.getProperty("name", "no name")))));
				});
				popupMenu.add(graphMenu);
			}
		}
		return popupMenu;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return true;
	}

	public static class RemoveGraphCanvasAction extends AbstractAction {

		private final MultiGraphCanvas multiGraphCanvas;
		private final GraphCanvas graphCanvas;

		public RemoveGraphCanvasAction(MultiGraphCanvas multiGraphCanvas, GraphCanvas graphCanvas) {
			super(ResourceBundle.getBundle("messages").getString("multiGraphCanvas.action.removeGraphCanvas"));
			this.multiGraphCanvas = multiGraphCanvas;
			this.graphCanvas = graphCanvas;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			multiGraphCanvas.remove(graphCanvas);
			multiGraphCanvas.revalidate();
		}
	}
}
