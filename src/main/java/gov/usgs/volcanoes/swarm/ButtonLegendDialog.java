package gov.usgs.volcanoes.swarm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

  private static final int WIDTH = 300;
  private static int height = 0;

  /**
   * Construct an about dialog.
   */
  public ButtonLegendDialog() {
    super(applicationFrame, "Button Legend", true);
    
    this.setLocation();
    this.setResizable(true);
    
    ImageIcon[] icons = {Icons.pin, Icons.settings,Icons.left,
        Icons.right, Icons.xminus, Icons.xplus, Icons.yminus,
        Icons.yplus};
    String[] description = {"Helicorder always on top",
        "Helicorder view settings", "Scroll back time",
        "Scroll forward time", "Compress X-axis", "Expand X-axis",
        "Compress Y-axis", "Expand Y-axis"};
    
    JPanel panel = new JPanel(new GridLayout(icons.length/2, 4));
    
    for (int x = 0; x < icons.length && x < description.length; x++) {
      JLabel l = new JLabel(icons[x]);
      l.setBorder(new LineBorder(Color.black));
      panel.add(l);
      panel.add(new JLabel(description[x]));
      height += 50;

      
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