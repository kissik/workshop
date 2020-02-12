package ua.org.training.workshop.web.command.impl;

public interface Pages {

    String ACCESS_DENIED_PAGE = "/WEB-INF/access-denied.jsp";

    String ADMIN_PAGE = "/WEB-INF/jsp/admin/page.jsp";
    String ADMIN_PAGE_REDIRECT = "redirect:app/admin/page";

    String FIRST_PAGE = "redirect:";

    String LOGIN_PAGE = "/WEB-INF/login.jsp";

    String MANAGER_PAGE = "/WEB-INF/jsp/manager/page.jsp";
    String MANAGER_PAGE_REDIRECT = "redirect:app/manager/page";
    String MANAGER_PAGE_REDIRECT_UPDATE_REQUEST_SUCCESSED = "redirect:app/manager/page?updated=true";;

    String REGISTRATION_FORM_OK = "redirect:app/login";
    String REGISTRATION_FORM_PAGE = "/WEB-INF/registration-form.jsp";

    String USER_INFO_PAGE = "/WEB-INF/jsp/admin/user.jsp";
    String USER_EDIT_PAGE = "/WEB-INF/jsp/admin/edit-user-form.jsp";
    String USER_PAGE = "/WEB-INF/jsp/user/page.jsp";
    String USER_PAGE_REDIRECT = "redirect:app/user/page";
    String USER_PAGE_REDIRECT_NEW_REQUEST_SUCCESSED = "redirect:app/user/page?created=true";
    String USER_PAGE_REDIRECT_UPDATE_HISTORY_REQUEST_SUCCESSED = "redirect:app/user/page?updated=true";

    String WORKMAN_PAGE = "/WEB-INF/jsp/workman/page.jsp";
    String WORKMAN_PAGE_REDIRECT = "redirect:app/workman/page";
    String WORKMAN_PAGE_REDIRECT_UPDATE_REQUEST_SUCCESSED = "redirect:app/workman/page?updated=true";
}
