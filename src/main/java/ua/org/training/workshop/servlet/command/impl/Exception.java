package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Exception implements Command {
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(Exception.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        logger.debug("Generated exception");
        throw new RuntimeException("Generated exception");
    }
}
