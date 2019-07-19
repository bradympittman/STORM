package gov.usgs.volcanoes.swarm.heli;

import gov.usgs.volcanoes.swarm.SwarmFrame;
import gov.usgs.volcanoes.swarm.SwarmModalDialog;
import gov.usgs.volcanoes.swarm.internalFrame.SwarmInternalFrames;
import gov.usgs.volcanoes.swarm.wave.WaveViewSettings;
import gov.usgs.volcanoes.swarm.wave.WaveViewSettingsDialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.math3.util.Pair;

public class HelicorderGroundTruthDialog extends SwarmFrame implements ListSelectionListener{
  
  private static final long serialVersionUID = 1L;
  private static HelicorderGroundTruthDialog dialog;
  private JList<Object> list;
  private ArrayList<Pair<Date, Date>> array;
  
  private HelicorderViewerFrame hvf;
  
  private HelicorderGroundTruthDialog(HelicorderViewerFrame hvf, ArrayList<Pair<Date, Date>> array) {
    
    super("Ground Truth",true,true,true,true);
    System.out.println("Ahhhhhh");
    this.hvf = hvf;
    this.array = array;
    add(array);
    setSize(500,500);
    setOpaque(true);
    setVisible(true);
    SwarmInternalFrames.add(this);
  }
  
  /**
   * Get instance of wave view settings dialog.
   * @param s wave view settings
   * @param count settings count
   * @return wave view settings dialog
   */
  public static HelicorderGroundTruthDialog getInstance(HelicorderViewerFrame hvf, ArrayList<Pair<Date, Date>> array) {
    if (dialog == null) {
      
      dialog = new HelicorderGroundTruthDialog(hvf, array);
    }
    
    return dialog;
  }
  
  public void add(ArrayList<Pair<Date, Date>> array) {
    JPanel axisPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    Object[] arrayList = array.toArray();
    list = new JList<Object>(arrayList);
    list.setVisible(true);
    
    
    axisPanel.setBorder(new TitledBorder(new EtchedBorder(), "Ground Truth"));
    axisPanel.add(list);
    add(axisPanel);
    list.addListSelectionListener(this);
    
  }

  public void valueChanged(ListSelectionEvent e) {
    if (list.getSelectedIndex() > -1 && list.getSelectedIndex() < array.size()) {
      hvf.createCustomWaveInset(array.get(list.getSelectedIndex()).getFirst(), 
          array.get(list.getSelectedIndex()).getSecond());
    }
    
  }
  
}