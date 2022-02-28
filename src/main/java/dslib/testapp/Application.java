package dslib.testapp;

import jexer.*;
import jexer.event.TMenuEvent;
import jexer.event.TResizeEvent;
import jexer.menu.TMenu;

import java.io.UnsupportedEncodingException;


public class Application extends TApplication {

    public static final int MENU_SET_OPERATIONS_1 = 2001;
    public static final int MENU_SET_OPERATIONS_2 = 2002;
    public static final int MENU_SET_OPERATIONS_3 = 2003;
    public static final int MENU_RELATIONS = 2004;

    MyWindow w;

    public static class MyWindow extends TWindow {
        private final TText text;

        public MyWindow(TApplication application) {
            super(application, "Задачи", 60, 20);
            text = addText("", 0, 0, 60, 20);
        }

        @Override
        public void onResize(final TResizeEvent event) {
            if (event.getType() == TResizeEvent.Type.WIDGET) {
                // Resize the text field
                TResizeEvent textSize = new TResizeEvent(event.getBackend(),
                        TResizeEvent.Type.WIDGET, event.getWidth() - 2,
                        event.getHeight() - 2);
                text.onResize(textSize);
                return;
            }

            // Pass to children instead
            for (TWidget widget: getChildren()) {
                widget.onResize(event);
            }
        }

        public void addText(String s) {
            text.addLine(s);
        }

        public void clear() {
            text.setText("");
        }
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        final Application app = this;
        if (menu.getId() == MENU_SET_OPERATIONS_1) {
            w.clear();
            Excercises.setPractice1(w::addText, (a)->new TMessageBox(app, "Съобщение", a));
            return true;
        }

        if (menu.getId() == MENU_SET_OPERATIONS_2) {
            w.clear();
            Excercises.setPractice2(w::addText, (a)->new TMessageBox(app, "Съобщение", a));
            return true;
        }

        if (menu.getId() == MENU_SET_OPERATIONS_3) {
            w.clear();
            Excercises.setPractice1(w::addText, (a)->new TMessageBox(app, "Съобщение", a));
            return true;
        }

        return super.onMenu(menu);

    }

    public Application() throws UnsupportedEncodingException {
        super(BackendType.SWING);

        TMenu opsMenu = addMenu("&Задачи");
        opsMenu.addItem(MENU_SET_OPERATIONS_1, "&Операции с множества 1");
        opsMenu.addItem(MENU_SET_OPERATIONS_2, "&Операции с множества 2");
        opsMenu.addItem(MENU_SET_OPERATIONS_3, "&Операции с множества 3");
        opsMenu.addItem(MENU_RELATIONS, "&Релации");
        opsMenu.addSeparator();
        opsMenu.addItem(TMenu.MID_EXIT, "&Изход");

        w = new MyWindow(this);
        w.maximize();
        w.setCloseBox(false);
        w.show();
    }

}
