package ua.org.training.workshop.servlet.command.impl;

public interface Pages {

    String ACCESS_DENIED_PAGE = "/WEB-INF/access-denied.jsp";

    String ADMIN_PAGE = "/WEB-INF/jsp/admin/page.jsp";
    String ADMIN_PAGE_REDIRECT = "redirect:app/admin/page";

    String FIRST_PAGE = "/index.jsp";

    String LOGIN_PAGE = "/WEB-INF/login.jsp";

    String MANAGER_PAGE = "/WEB-INF/jsp/manager/page.jsp";
    String MANAGER_PAGE_REDIRECT = "redirect:app/manager/page";

    String REGISTRATION_FORM_OK = "redirect:app/login";
    String REGISTRATION_FORM_PAGE = "/WEB-INF/registration-form.jsp";

    String USER_INFO_PAGE = "/WEB-INF/jsp/admin/user.jsp";
    String USER_EDIT_PAGE = "/WEB-INF/jsp/admin/edit-user-form.jsp";
    String USER_PAGE = "/WEB-INF/jsp/user/page.jsp";
    String USER_PAGE_REDIRECT = "redirect:app/user/page";
    String USER_PAGE_REDIRECT_NEW_REQUEST_SUCCESSED = "redirect:app/user/page?created=true";

    String WORKMAN_PAGE = "/WEB-INF/jsp/workman/page.jsp";
    String WORKMAN_PAGE_REDIRECT = "redirect:app/workman/page";

}
