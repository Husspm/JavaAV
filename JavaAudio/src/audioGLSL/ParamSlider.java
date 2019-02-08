package audioGLSL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ParamSlider extends JPanel implements ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -28745395100076349L;
	public int refID;
	public int returnValue;
	AudioProject parent;
	ParamSlider(String label, int[] thresh, int id, AudioProject parent) {
		returnValue = 0;
		this.parent = parent;
		if (label == "LayerOneSwitch") {
			thresh[1] = parent.layerOneShader.length - 1;
			System.out.println(" Threshold Changed! ");
		}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// Create the label.
		this.refID = id;
		JLabel sliderLabel = new JLabel(label, JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Create the slider.
		JSlider brightness = new JSlider(JSlider.HORIZONTAL, thresh[0], thresh[1], thresh[2]);
		brightness.addChangeListener(this);
		brightness.setName(label);
		brightness.setBackground(new Color(100, 20, 140));
		brightness.setForeground(Color.white);
		// Turn on labels at major tick marks.
		brightness.setMajorTickSpacing(2);
		brightness.setMinorTickSpacing(1);
		brightness.setPaintTicks(true);
		brightness.setPaintLabels(true);
		brightness.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
		Font font = new Font("Sans-Serif", Font.ITALIC, 10);
		brightness.setFont(font);
		// Put everything together.
		add(sliderLabel);
		add(brightness);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		returnValue = source.getValue();
		parent.setParam(returnValue, source.getName());
//		this.setReturnValue(returnValue);
//		System.out.println(" value " + returnValue + " refID " + refID);
	}
	public void setReturnValue(int val) {
		this.returnValue = val;
	}
	public int getReturnValue() {
		return this.returnValue;
	}
}
