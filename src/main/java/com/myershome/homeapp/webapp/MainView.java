package com.myershome.homeapp.webapp;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class) // map view to the root
class MainView extends VerticalLayout {

  // https://open-web-calendar.hosted.quelltext.eu/
  String openWebIFrame = "<iframe id=\"open-web-calendar\" \n" +
      "    style=\"background:url('https://raw.githubusercontent.com/niccokunzmann/open-web-calendar/master/static/img/loaders/circular-loader.gif') center center no-repeat;\"\n"
      +
      "    src=\"https://open-web-calendar.hosted.quelltext.eu/calendar.html?css=.CALENDAR-INDEX-0%20%7B%20background-color%3A%20%23ffffff%3B%20%7D.dhx_cal_navline%2C%20.dhx_scale_bar%2C%20.dhx_cal_container%2C%20.dhx_cal_header%2C%20.dhx_cal_scale_placeholder%2C%20.dhx_scale_holder%2C%20.dhx_scale_hour%20%7Bbackground-color%3A%20%23233349%3B%7D.event%2C%20.dhx_cal_tab.active%2C%20.dhx_cal_tab.active%3Ahover%20%7Bbackground-color%3A%20%2390bdf9%3B%7D%20.dhx_month_head%2C%20.dhx_cal_tab%2C%20.dhx_cal_today_button%20%7Bcolor%3A%20%2390bdf9%3B%7D%20.dhx_cal_tab%2C%20.dhx_cal_tab.active%20%7Bborder-color%3A%20%2390bdf9%3B%7D.dhx_scale_holder_now%2C%20.dhx_now%20.dhx_month_head%2C%20.dhx_now%20.dhx_month_body%20%7B%20background-color%3A%20%232d3d53%3B%7D.dhx_month_body%2C%20.dhx_month_head%2C%20.dhx_cal_container%20%7B%20background-color%3A%20%232d3d53%3B%20%7D.dhx_after%20.dhx_month_body%2C%20.dhx_before%20.dhx_month_body%2C%20.dhx_after%20.dhx_month_head%2C%20.dhx_before%20.dhx_month_head%20%7B%20background-color%3A%20%232d3d53%3B%20%7D&amp;hour_format=%25g%3A%25i%E2%80%AF%25a&amp;title=Home%20Calendar&amp;url=https%3A%2F%2Fcalendar.google.com%2Fcalendar%2Fical%2Fcombatwombat7895%2540gmail.com%2Fpublic%2Fbasic.ics\"\n"
      +
      "    sandbox=\"allow-scripts allow-same-origin allow-top-navigation\"\n" +
      "    allowTransparency=\"true\" scrolling=\"no\" \n" +
      "    frameborder=\"0\" height=\"600px\" width=\"100%\"></iframe>";
  String html = "https://calendar.google.com/calendar/embed?height=1080&wkst=1&ctz=America%2FNew_York&bgcolor=%23ffffff&showTitle=0&showTz=0&showPrint=0&src=Y29tYmF0d29tYmF0Nzg5NUBnbWFpbC5jb20&color=%237986CB\" style=\"border-width:0\" width=\"1920\" height=\"1080\" frameborder=\"0\" scrolling=\"no";
  String html2 = "https://open-web-calendar.hosted.quelltext.eu/calendar.html?css=.CALENDAR-INDEX-0%20%7B%20background-color%3A%20%23ffffff%3B%20%7D.dhx_cal_navline%2C%20.dhx_scale_bar%2C%20.dhx_cal_container%2C%20.dhx_cal_header%2C%20.dhx_cal_scale_placeholder%2C%20.dhx_scale_holder%2C%20.dhx_scale_hour%20%7Bbackground-color%3A%20%23233349%3B%7D.event%2C%20.dhx_cal_tab.active%2C%20.dhx_cal_tab.active%3Ahover%20%7Bbackground-color%3A%20%2390bdf9%3B%7D%20.dhx_month_head%2C%20.dhx_cal_tab%2C%20.dhx_cal_today_button%20%7Bcolor%3A%20%2390bdf9%3B%7D%20.dhx_cal_tab%2C%20.dhx_cal_tab.active%20%7Bborder-color%3A%20%2390bdf9%3B%7D.dhx_scale_holder_now%2C%20.dhx_now%20.dhx_month_head%2C%20.dhx_now%20.dhx_month_body%20%7B%20background-color%3A%20%232d3d53%3B%7D.dhx_month_body%2C%20.dhx_month_head%2C%20.dhx_cal_container%20%7B%20background-color%3A%20%232d3d53%3B%20%7D.dhx_after%20.dhx_month_body%2C%20.dhx_before%20.dhx_month_body%2C%20.dhx_after%20.dhx_month_head%2C%20.dhx_before%20.dhx_month_head%20%7B%20background-color%3A%20%232d3d53%3B%20%7D&amp;hour_format=%25g%3A%25i%E2%80%AF%25a&amp;title=Home%20Calendar&amp;url=https%3A%2F%2Fcalendar.google.com%2Fcalendar%2Fical%2Fcombatwombat7895%2540gmail.com%2Fpublic%2Fbasic.ics";
  String html3 = "https://open-web-calendar.hosted.quelltext.eu/calendar.html?css=.CALENDAR-INDEX-0%20%7B%20background-color%3A%20%23ffffff%3B%20%7D.dhx_cal_navline%2C%20.dhx_scale_bar%2C%20.dhx_cal_container%2C%20.dhx_cal_header%2C%20.dhx_cal_scale_placeholder%2C%20.dhx_scale_holder%2C%20.dhx_scale_hour%20%7Bbackground-color%3A%20%23233349%3B%7D.event%2C%20.dhx_cal_tab.active%2C%20.dhx_cal_tab.active%3Ahover%20%7Bbackground-color%3A%20%2390bdf9%3B%7D%20.dhx_month_head%2C%20.dhx_cal_tab%2C%20.dhx_cal_today_button%20%7Bcolor%3A%20%2390bdf9%3B%7D%20.dhx_cal_tab%2C%20.dhx_cal_tab.active%20%7Bborder-color%3A%20%2390bdf9%3B%7D.dhx_scale_holder_now%2C%20.dhx_now%20.dhx_month_head%2C%20.dhx_now%20.dhx_month_body%20%7B%20background-color%3A%20%232d3d53%3B%7D.dhx_month_body%2C%20.dhx_month_head%2C%20.dhx_cal_container%20%7B%20background-color%3A%20%232d3d53%3B%20%7D.dhx_after%20.dhx_month_body%2C%20.dhx_before%20.dhx_month_body%2C%20.dhx_after%20.dhx_month_head%2C%20.dhx_before%20.dhx_month_head%20%7B%20background-color%3A%20%232d3d53%3B%20%7D&hour_format=%25g%3A%25i%E2%80%AF%25a&skin=dhtmlxscheduler_contrast_black.css&title=Home%20Calendar&url=https%3A%2F%2Fcalendar.google.com%2Fcalendar%2Fical%2Fcombatwombat7895%2540gmail.com%2Fpublic%2Fbasic.ics";

  MainView() {
    setSizeFull();
    IFrame calendar = new IFrame(html3);
    setSizeFull();
    calendar.setSizeFull();
    // calendar.getElement().setAttribute("Theme", Lumo.DARK);
    add(calendar);

  }
}