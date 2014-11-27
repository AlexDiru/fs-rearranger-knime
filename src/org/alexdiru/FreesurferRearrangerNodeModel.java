package org.alexdiru;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;


/**
 * This is the model implementation of FreesurferRearranger.
 * 
 *
 * @author Alex Spedding
 */
public class FreesurferRearrangerNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(FreesurferRearrangerNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT = "Count";

    /** initial default count value. */
    static final int DEFAULT_COUNT = 100;

    // example value: the models count variable filled from the dialog 
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelIntegerBounded m_count =
        new SettingsModelIntegerBounded(FreesurferRearrangerNodeModel.CFGKEY_COUNT,
                    FreesurferRearrangerNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
    

    /**
     * Constructor for the node model.
     */
    protected FreesurferRearrangerNodeModel() {
    
        //2 tables as input, 2 as output
        super(2, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	//Input 0 is CAD Data
    	//Input 1 is ADNI Data
    	//Input 2 is Dictionary
    	//We want CAD's columns to be the same as ADNI's

    	//Check if column names are the same
    	String[] namesCAD = inData[0].getSpec().getColumnNames();
    	String[] namesADNI = inData[0].getSpec().getColumnNames();
    	
    	Arrays.sort(namesCAD);
    	Arrays.sort(namesADNI);
    	
    	for (int i = 0; i < namesCAD.length; i++) 
    		if (!namesCAD[i].equals(namesADNI[i]))
    			System.out.println(i + ": " + namesCAD[i] + " <-> " + namesADNI[i]);
    	
    	//Testing shows the column names are the same
    	//So they just need to be rearranged
        
    	
    	//Just output the two input tables
    	DataColumnSpec[] columnSpecCAD = new DataColumnSpec[inData[1].getSpec().getColumnNames().length];
    	DataColumnSpec[] columnSpecADNI = new DataColumnSpec[inData[1].getSpec().getColumnNames().length];
    	
    	DataTableSpec outputCAD, outputADNI;
    	
    	//Column formats
    	for (int i = 0; i < columnSpecCAD.length; i++) {
    		String name = inData[1].getSpec().getColumnNames()[i];
    		DataType type = inData[1].getSpec().getColumnSpec(i).getType();
    		columnSpecCAD[i] = new DataColumnSpecCreator(name, type).createSpec();
    	}
    	
    	outputCAD = new DataTableSpec(columnSpecCAD);
    	
    	BufferedDataContainer containerCAD = exec.createDataContainer(outputCAD);
    	
    	//Generate a column map
    	//Hash<CADColumnIndex> -> ADNIColumnIndex
    	Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
    	
    	for (int i = 0; i < columnSpecCAD.length; i++) {
    		String cadColumnName = inData[0].getSpec().getColumnNames()[i];
    		for (int j = 0; j < columnSpecCAD.length; j++)
    			if (inData[1].getSpec().getColumnNames()[j].equals(cadColumnName)) {
    				columnMap.put(i, j);
    				break;
    			}
    	}
    	
    	//Rearrange columns
    	//CAD needs to be in ADNI format
    	//for (int r = 0; r < inData[0].getRowCount(); r++) {
    	CloseableRowIterator iter = inData[0].iterator();
    		while (iter.hasNext()) {
    			DataRow rowCAD = iter.next();
    			DataCell[] cells = new DataCell[rowCAD.getNumCells()];
    			
    			for (int c = 0; c < rowCAD.getNumCells(); c++) {
    				cells[columnMap.get(c)] = rowCAD.getCell(c);
    			}
    			
    			System.out.println("Key: " + rowCAD.getKey());
    			DataRow newRow = new DefaultRow(rowCAD.getKey(), cells);
    			containerCAD.addRowToTable(newRow);
    		}
    	//}
    	
    	containerCAD.close();
        
        return new BufferedDataTable[] { 
        	containerCAD.getTable()
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

    		
    	DataTableSpec[] arr = new DataTableSpec[getNrOutPorts()];
    	Arrays.fill(arr,null);
        return arr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        // TODO save user settings to the config object.
        
        m_count.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.
        
        m_count.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.

        m_count.validateSettings(settings);

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

}

