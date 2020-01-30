package ua.org.training.workshop.dao.impl;

public interface MySQLQueries {
    String ACCOUNT_FIND_PAGE_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_USER_LIST (?, ?, ?, ?, ?);";
    String ACCOUNT_DELETE_ROLES_BY_USER_ID_QUERY =
            " delete from user_role where nuser = ? ";
    String ACCOUNT_FIND_ROLES_BY_USER_ID_QUERY =
            " select * from user_role ur" +
                    " inner join role r " +
                    " on ur.nrole = r.id" +
                    " where ur.nuser = ? ";
    String ACCOUNT_FIND_BY_ID_QUERY =
            " select * from user_list u where u.id = ?";
    String ACCOUNT_FIND_BY_USERNAME_QUERY =
            " select * from user_list u where slogin = ?";
    String ACCOUNT_FIND_BY_PHONE_QUERY =
            " select * from user_list u where sphone = ?";
    String ACCOUNT_FIND_BY_EMAIL_QUERY =
            " select * from user_list u where semail = ?";
    String ACCOUNT_INSERT_CALLABLE_STATEMENT =
            "CALL APP_INSERT_USER_LIST (?,?,?,?,?,?,?,?,?, ?);";
    String ACCOUNT_INSERT_ROLE_PREPARE_STATEMENT =
            "INSERT INTO `user_role`" +
                    "(`nuser`,`nrole`) " +
                    "VALUES (?,?)";

    String REQUEST_FIND_PAGE_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST (?, ?, ?, ?, ?);";
    String REQUEST_FIND_PAGE_BY_AUTHOR_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST_BY_AUTHOR (?, ?, ?, ?, ?, ?);";
    String REQUEST_FIND_BY_ID_QUERY =
            " select * from request_list r where r.id = ?";
    String REQUEST_INSERT_PREPARE_STATEMENT =
            "CALL APP_INSERT_REQUEST_LIST (?,?,?,?,?,?,?,?);";

    String ROLE_FIND_BY_CODE_QUERY =
            " select * from role r where scode = ?";
    String ROLE_FIND_ALL_QUERY =
            " select * from role r";

    String STATUS_FIND_NEXT_STATUSES_BY_CURRENT_ID_STATUS_QUERY =
            " select * from next_statuses ns " +
                    " inner join status s " +
                    " on ns.nnextstatus = s.id " +
                    " where ns.nstatus = ? ";
    String STATUS_FIND_BY_CODE_QUERY =
            " select * from status s where scode = ?";
    String STATUS_FIND_BY_ID_QUERY =
            " select * from status s where id = ?";
    String STATUS_FIND_ALL_QUERY =
            " select * from status s";

}
