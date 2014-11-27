package org.alexdiru;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FreesurferRearranger" Node.
 * 
 *
 * @author Alex Spedding
 */
public class FreesurferRearrangerNodeFactory 
        extends NodeFactory<FreesurferRearrangerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FreesurferRearrangerNodeModel createNodeModel() {
        return new FreesurferRearrangerNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<FreesurferRearrangerNodeModel> createNodeView(final int viewIndex,
            final FreesurferRearrangerNodeModel nodeModel) {
        return new FreesurferRearrangerNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new FreesurferRearrangerNodeDialog();
    }

}

