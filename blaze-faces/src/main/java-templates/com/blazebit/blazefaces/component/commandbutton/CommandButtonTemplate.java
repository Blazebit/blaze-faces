import com.blazebit.blazefaces.util.HTML;
import java.util.logging.Logger;
        
    private final static Logger logger = Logger.getLogger(CommandButton.class.getName());

    public String resolveStyleClass() {
        String icon = getIcon();
        Object value = getValue();
        String styleClass = ""; 
    
        if(value != null && icon == null) {
            styleClass = HTML.BUTTON_TEXT_ONLY_BUTTON_CLASS;
        }
        else if(value != null && icon != null) {
            styleClass = getIconPos().equals("left") ? HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS : HTML.BUTTON_TEXT_ICON_RIGHT_BUTTON_CLASS;
        }
        else if(value == null && icon != null) {
            styleClass = HTML.BUTTON_ICON_ONLY_BUTTON_CLASS;
        }
    
        if(isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        } 
    
        String userStyleClass = getStyleClass();
        if(userStyleClass != null) {
            styleClass = styleClass + " " + userStyleClass;
        }
    
        return styleClass;
    }
    
    public boolean isPartialSubmitSet() {
        return (getStateHelper().get(PropertyKeys.partialSubmit) != null) || (this.getValueExpression("partialSubmit") != null);
    }