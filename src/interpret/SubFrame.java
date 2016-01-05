package interpret;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class SubFrame extends JFrame {

	private final DialogListener listener;

	protected SubFrame(DialogListener listener) {
		this.listener = listener;
	}

	protected void onReturn(Object value) {
		setVisible(false);
		if (listener != null) {
			listener.onSubFrameReturn(value);
		}
		dispose();
	}

	protected void onCancel() {
		setVisible(false);
		if (listener != null) {
			listener.onSubFrameCancel();
		}
		dispose();
	}
}
