package com.myershome.homeapp.webapp.renderers;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;


//@CssImport("./styles/grid-style.css")
public class GridRenderer extends Div {

    public GridRenderer(){
        addClassNames(LumoUtility.Display.GRID,
                LumoUtility.Grid.Column.COLUMNS_2,
                LumoUtility.Grid.Breakpoint.Small.COLUMNS_2,
                LumoUtility.Grid.Breakpoint.Medium.COLUMNS_4,
                LumoUtility.Grid.Breakpoint.Large.COLUMNS_6,
                LumoUtility.Grid.Breakpoint.XLarge.COLUMNS_8,
                LumoUtility.Gap.Row.MEDIUM,
                LumoUtility.Gap.Column.XLARGE);

    }

}
