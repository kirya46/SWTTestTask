package example;

import common.ImageUtil;
import common.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;

/**
 * Created by Kirill Stoianov on 19/08/17.
 */
public class EventListenerTest {

    private static final String TAG = EventListenerTest.class.getSimpleName();
    private static Widget mouseControl = null;

    public static void main(String[] args) {
        Display display = new Display();

    /* Overall, keep track of the Widget the mouse is moving over */
        display.addFilter(SWT.MouseMove, new Listener() {
            @Override
            public void handleEvent(Event e) {
                mouseControl = e.widget;
            }
        });

        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        final Composite composite = new Composite(shell, SWT.BORDER);
        composite.setLayout(new FillLayout());


        final Label label1 = new Label(composite, SWT.BORDER);
        File file = new File("/Users/kirya/Pictures/wallpaper/Aerial_island_Bahamas-Mac_OS_Wallpaper_1366x768.jpg");
        Image image = new Image(display, file.getPath());
        image = ImageUtil.resize(image, 50, 50);
        label1.setImage(image);

        Label label2 = new Label(composite, SWT.BORDER);
        label2.setImage(image);

//
//        composite.addListener(SWT.MouseEnter, new Listener()
//        {
//            @Override
//            public void handleEvent(Event event)
//            {
//            /* Check if the mouse was previously moving over a child (you could
//             * even do recursive search here) */
//                for (Control child : composite.getChildren())
//                {
//                    if (child.equals(mouseControl))
//                        return;
//                }
//                System.out.println("ENTER");
//            }
//        });
//
//        composite.addListener(SWT.MouseExit, new Listener()
//        {
//            @Override
//            public void handleEvent(Event event)
//            {
//            /* Check if the mouse is now located over a child (you could
//             * even do recursive search here) */
//                for (Control child : composite.getChildren())
//                {
//                    if (child.getBounds().contains(new Point(event.x, event.y)))
////                    	label1.setBounds(event.x, event.y, 50,50);
//                        return;
//                }
//                System.out.println("EXIT");
//            }
//        });
//
//        composite.addListener(SWT.MouseHover, new Listener()
//        {
//
//            @Override
//            public void handleEvent(Event event)
//            {
//                System.out.println("HOVER");
//            }
//        });
//
//        composite.addListener(SWT.MouseMove, new Listener()
//        {
//
//            @Override
//            public void handleEvent(Event event)
//            {
//            }
//        });

        MyListener listener = new MyListener(label1);

        label1.addListener(SWT.MouseEnter, listener);
        label1.addListener(SWT.MouseDown, listener);
        label1.addListener(SWT.MouseMove, listener);
        label1.addListener(SWT.MouseExit, listener);
        
        MyListener listener2 = new MyListener(label2);

        label2.addListener(SWT.MouseEnter, listener2);
        label2.addListener(SWT.MouseDown, listener2);
        label2.addListener(SWT.MouseMove,listener2);
        label2.addListener(SWT.MouseExit, listener2);

        shell.setSize(1000,1000);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

    public static class MyListener implements Listener {

        public static boolean selected = false;

        private Label target;

        public MyListener(Label target) {
            this.target = target;
        }

        @Override
        public void handleEvent(Event event) {
            switch (event.type) {
                case SWT.MouseEnter:
                    Log.d(TAG, "MouseEnter");
                    break;
                case SWT.MouseDown:
                    Log.d(TAG, "MouseDown");
                    selected = true;

                    break;
                case SWT.MouseMove:
                    Log.d(TAG, "MouseMove");
                    if (selected) {
                        target.setLocation(event.x, event.y);
                    }

                    break;

                case SWT.MouseUp:
                    Log.d(TAG, "MouseUp");
                    selected = false;

                    break;
                case SWT.MouseExit:
                    Log.d(TAG, "MouseExit");
                    selected = false;

                    break;

            }
        }
    }

    public static String getEventName(int eventType) {
        switch (eventType) {
            case SWT.None:
                return "null";
            case SWT.KeyDown:
                return "key down";
            case SWT.KeyUp:
                return "key up";
            case SWT.MouseDown:
                return "mouse down";
            case SWT.MouseUp:
                return "mouse up";
            case SWT.MouseMove:
                return "mouse move";
            case SWT.MouseEnter:
                return "mouse enter";
            case SWT.MouseExit:
                return "mouse exit";
            case SWT.MouseDoubleClick:
                return "mouse double click";
            case SWT.Paint:
                return "paint";
            case SWT.Move:
                return "move";
            case SWT.Resize:
                return "resize";
            case SWT.Dispose:
                return "dispose";
            case SWT.Selection:
                return "selection";
            case SWT.DefaultSelection:
                return "default selection";
            case SWT.FocusIn:
                return "focus in";
            case SWT.FocusOut:
                return "focus out";
            case SWT.Expand:
                return "expand";
            case SWT.Collapse:
                return "collapse";
            case SWT.Iconify:
                return "iconify";
            case SWT.Deiconify:
                return "deiconify";
            case SWT.Close:
                return "close";
            case SWT.Show:
                return "show";
            case SWT.Hide:
                return "hide";
            case SWT.Modify:
                return "modify";
            case SWT.Verify:
                return "verify";
            case SWT.Activate:
                return "activate";
            case SWT.Deactivate:
                return "deactivate";
            case SWT.Help:
                return "help";
            case SWT.DragDetect:
                return "drag detect";
            case SWT.Arm:
                return "arm";
            case SWT.Traverse:
                return "traverse";
            case SWT.MouseHover:
                return "mouse hover";
            case SWT.HardKeyDown:
                return "hard key down";
            case SWT.HardKeyUp:
                return "hard key up";
            case SWT.MenuDetect:
                return "menu detect";
        }

        return "unkown ???";
    }

}
