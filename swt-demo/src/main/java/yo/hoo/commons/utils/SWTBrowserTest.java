package yo.hoo.commons.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTBrowserTest {

	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setText("SWT Browser Test");
		shell.setLayout(new FillLayout());

		final Browser browser = new Browser(shell, SWT.FILL);
		browser.setText("<center><h2>正在启动服务......</h2></center>");
		new Thread() {
			public void run() {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						browser.setUrl("http://www.baidu.com/");
					}
				});
			};
		}.start();
		browser.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent event) {
				if (event.button == 3) {
					browser.execute("document.oncontextmenu = function() {return false;}");
				}
			}

			public void mouseUp(MouseEvent event) {
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}