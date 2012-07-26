import com.blazebit.blazefaces.model.filter.*;
import com.blazebit.blazefaces.component.celleditor.CellEditor;
import javax.faces.component.UIComponent;

    private CellEditor cellEditor = null;


    public CellEditor getCellEditor() {
        if(cellEditor == null) {
            for(UIComponent child : getChildren()) {
                if(child instanceof CellEditor)
                    cellEditor = (CellEditor) child;
            }
        }

        return cellEditor;
    }

    public boolean isDynamic() {
        return false;
    }