package com.myershome.homeapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@Theme(value="homeapp-theme",variant = Lumo.DARK)
@SpringBootApplication
@EnableScheduling
public class HomeappApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(HomeappApplication.class, args);
	}

}
