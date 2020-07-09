
package com.totalcross.knowcode;

import com.totalcross.knowcode.parse.XmlContainerLayout;
import com.totalcross.knowcode.parse.XmlContainerFactory;

import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.Toast;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.event.UpdateListener;

public class HomeApplianceXML extends MainWindow {

	private ImageControl cloudImage;

	public HomeApplianceXML() {
		setUIStyle(Settings.MATERIAL_UI);
	}

	static {
		Settings.applicationId = "TCMT";
		Settings.appVersion = "1.0.0";
		Settings.iosCFBundleIdentifier = "com.totalcross.easytiful";
	}

	public void initUI() {

		XmlContainerLayout xmlCont = (XmlContainerLayout) XmlContainerFactory.create("xml/homeApplianceXML.xml");
		swap(xmlCont);

		Button plus = (Button) xmlCont.getControlByID("@+id/plus");
		Label insideTempLabel = (Label) xmlCont.getControlByID("@+id/insideTempLabel");
		Label externalTempLabel = (Label) xmlCont.getControlByID("@+id/externalTempLabel");
		cloudImage = (ImageControl) xmlCont.getControlByID("@+id/nuvem");

		new Thread() {
			public void run() {

				while (true) {
					blinkCloud();
					ReadSensors rs = new ReadSensors();
					String temp = rs.readTemp();

					Vm.debug("Recebedno info do dht11 " + temp);
					if (temp != null && !temp.isEmpty() && !temp.equalsIgnoreCase("error"))
						externalTempLabel.setText(temp);

					try {
						cloudAnimation = false;
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

		plus.addPressListener(new PressListener() {

			@Override
			public void controlPressed(ControlEvent e) {
				// TODO

				try {

					String tempString = insideTempLabel.getText();
					int temp;
					temp = Convert.toInt(tempString);
					insideTempLabel.setText(Convert.toString(++temp));

				} catch (InvalidNumberException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		Button minus = (Button) xmlCont.getControlByID("@+id/minus");
		minus.addPressListener(new PressListener() {

			@Override
			public void controlPressed(ControlEvent e) {
				// TODO

				try {
					String tempString = insideTempLabel.getText();
					int temp;
					temp = Convert.toInt(tempString);
					insideTempLabel.setText(Convert.toString(--temp));

				} catch (InvalidNumberException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	boolean cloudAnimation = false;

	private void blinkCloud() {
		cloudAnimation = true;

		new Thread() {
			public void run() {
				while (cloudAnimation) {
					cloudImage.setVisible(false);
					Vm.sleep(500);
					cloudImage.setVisible(true);
					Vm.sleep(500);
				}
			}
		}.start();
	}

}
