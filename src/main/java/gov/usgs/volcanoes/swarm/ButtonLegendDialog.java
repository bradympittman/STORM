package gov.usgs.volcanoes.swarm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ButtonLegendDialog extends JDialog {
  private static final long serialVersionUID = -1;
  private static final JFrame applicationFrame = Swarm.getApplicationFrame();

  private static final int WIDTH = 700;
  private static int height = 800;

  /**
   * Construct an about dialog.
   */
  public ButtonLegendDialog() {
    super(applicationFrame, "Button Legend", true);
    
    this.setLocation();
    this.setResizable(true);
    
    ImageIcon[] icons = {Icons.pin, Icons.settings,Icons.left,
        Icons.right, Icons.xminus, Icons.xplus, Icons.yminus,
        Icons.yplus, Icons.zoomplus, Icons.zoomminus, Icons.gear,
        Icons.wave, Icons.spectra, Icons.spectrogram, 
        Icons.particle_motion, Icons.clipboard, Icons.delete,
        Icons.camera, Icons.tag, Icons.wavezoom, Icons.heli,
        Icons.monitor, Icons.rsam_values, Icons.earth, Icons.wave_folder,
        Icons.save, Icons.saveall, Icons.helilink, Icons.clock,
        Icons.geosort, Icons.resize, Icons.deleteall, Icons.pick,
        Icons.new_server, Icons.refresh};
    String[] description = {"Helicorder always on top",
        "Helicorder view settings", "Scroll back time",
        "Scroll forward time", "Compress X-axis", "Expand X-axis",
        "Compress Y-axis", "Expand Y-axis", "Decrease zoom time window",
        "Increase zoom time window", "Wave view settings",
        "Wave view /\n Opens waves in the\n real-times view window",
        "Spectra view", "Spectogram view", "Particle motion view", 
        "Copy inset to clipboard /\n Puts waves on the clipboard", "Remove inset wave", 
        "Save helicorder image /\n Save clipboard image", "Tag mode", 
        "Toggle between adjusting\nhelicorder scale and clip",
        "Opens helicorder views", "Puts waves on the real-time monitor",
        "Opens RSAM viewr", "Shows channels on a map", "Shows channels on a map",
        "Open a Saved Wave", "Save Selected Wave", "Save all waves",
        "Synchronize times with helicorder wave",
        "Synchronize times with selected wave",
        "Sort waves by nearest to selected wave",
        "Set clipboard wave size", "Remove all waves from clipboard",
        "Pick mode", "New data source", "Refresh data source"};
    
    JPanel panel = new JPanel(new GridLayout(15, 1));
    
    for (int x = 0; x < icons.length && x < description.length; x++) {
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel l = new JLabel(icons[x]);
      l.setBorder(new LineBorder(Color.black));
      buttonPanel.add(l);
      for (String text: description[x].split("\n")) {
        buttonPanel.add(new JLabel(text));
      }
      
      panel.add(buttonPanel);
      //height += 50;

      
    }
    add(panel);
    panel.setVisible(true);
    
    for (Component c : panel.getComponents()) {
      c.setVisible(true);
    }
    this.setSize(WIDTH, height);

  }

  private void setLocation() {
    Dimension parentSize = applicationFrame.getSize();
    Point parentLoc = applicationFrame.getLocation();
    this.setLocation(parentLoc.x + (parentSize.width / 2 - WIDTH / 2),
        parentLoc.y + (parentSize.height / 2 - height / 2));
  }
  


  

}