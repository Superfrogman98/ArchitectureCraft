// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Pages for sawbench, etc.
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.shape;

import gcewing.architecture.client.gui.widget.GuiText;

public class ShapePage {

    public final String title;
    public final Shape[] shapes;
    public final String[] shapeNames;

    public ShapePage(String title, Shape... shapes) {
        this.title = title;
        this.shapes = shapes;
        this.shapeNames = new String[shapes.length];
    }

    public int size() {
        return shapes.length;
    }

    public Shape get(int i) {
        if (i >= 0 && i < shapes.length) return shapes[i];
        return null;
    }

    public String getTitle() {
        try {
            return GuiText.valueOf(GuiText.class, title).getLocal();
        } catch (IllegalArgumentException ignore) {}
        return "UNKNOWN";
    }

    public void updateShapeNames() {
        for (int i = 0; i < shapes.length; i++) {
            try {
                shapeNames[i] = GuiText.valueOf(GuiText.class, shapes[i].name()).getLocal();
            } catch (IllegalArgumentException e) {
                shapeNames[i] = "UNKNOWN";
            }
        }
    }

    public String[] getShapeNames() {
        return shapeNames;
    }
}
