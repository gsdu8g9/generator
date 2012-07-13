package test;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.windom.generator.Generator;
import org.windom.generator.definition.Definition;
import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.PlainInput;
import org.windom.util.Utils;

public class Main implements Runnable {

	private Generator gen;
	private Generator uiGen;
	
	public Main() {
		try {
			Definition def = new PlainInput("hazassag.txt").read();
			Definition uiDef = new PlainInput("ui.txt").read();
			gen = new Generator(def);
			uiGen = new Generator(uiDef);
			uiGen.setFormatSentences(false);
			SwingUtilities.invokeLater(this);
		} catch (InputException e) {
			showException(e);
		}
	}
	
	@Override
	public void run() {
		final Object[] options = { uiGen.generate("next"), uiGen.generate("cancel") };
		final int choice = JOptionPane.showOptionDialog(
			null,
			gen.generate(),
			uiGen.generate("title"),
			JOptionPane.YES_NO_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			options,
			options[0]);
		if (choice == 0) {
			SwingUtilities.invokeLater(this);
		}
	}
	
	private static void showException(final Exception e) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(
					null,
				    e.getMessage(),
				    "Hupsz",
				    JOptionPane.ERROR_MESSAGE);				
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			SwingUtilities.invokeLater(r);
		}
	}
	
	public static void main(String[] args) {
		Utils.initUiLookAndFeel();
		new Main();
	}
	
}
