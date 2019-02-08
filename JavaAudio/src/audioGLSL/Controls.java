package audioGLSL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//import processing.core.PApplet;

public class Controls extends JPanel
                        implements ActionListener,
                                   WindowListener,
                                   ChangeListener {

	private static final long serialVersionUID = 1L;
    static final int BRT_MIN = 0;
    static final int BRT_MAX = 4;
    static final int BRT_INIT = 1;
    public int bright = 1;
    public ParamSlider [] ps;
    private static final String [] labels = {"LayerOneSwitch", "LayerTwoSwitch", "Layer Three", "Layer Four"};
    int [] mmc = {BRT_MIN, BRT_MAX, BRT_INIT};
    AudioProject parent;
    public Controls(AudioProject parent) {
    	this.parent = parent;
    	ps = new ParamSlider [labels.length];
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < labels.length; i++) {
			ps[i] = new ParamSlider(labels[i], mmc, i, parent);
			add(ps[i]);
        }
    }

    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    //React to window events.
    public void windowIconified(WindowEvent e) {
//        stopAnimation();
    }
    public void windowDeiconified(WindowEvent e) {
//        startAnimation();
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        int val = source.getValue();
        System.out.print(val);
    }

    public void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Controls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Controls animator = new Controls(this.parent);
                
        //Add content to the window.
        frame.add(animator, BorderLayout.CENTER);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public int rBrt() {
    	System.out.print(bright);
    	return this.bright;
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}