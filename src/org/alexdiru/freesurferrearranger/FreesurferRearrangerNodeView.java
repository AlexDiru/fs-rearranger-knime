package org.alexdiru.freesurferrearranger;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "FreesurferRearranger" Node.
 * 
 *
 * @author Alex Spedding
 */
public class FreesurferRearrangerNodeView extends NodeView<FreesurferRearrangerNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link FreesurferRearrangerNodeModel})
     */
    protected FreesurferRearrangerNodeView(final FreesurferRearrangerNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        FreesurferRearrangerNodeModel nodeModel = 
            (FreesurferRearrangerNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

