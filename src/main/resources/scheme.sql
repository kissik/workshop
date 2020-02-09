-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 09, 2020 at 10:59 PM
-- Server version: 5.7.23-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `workshop`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_GET_COUNT_PAGINATON` (IN `stmt` VARCHAR(1000), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN
  SET @qry = stmt;
  PREPARE stm FROM @qry;
  EXECUTE stm;
  SET ncount = @counter;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_INSERT_REQUEST_LIST` (IN `sdescription` VARCHAR(50) CHARSET utf8, IN `stitle` VARCHAR(50) CHARSET utf8, IN `nauthor` BIGINT(20), IN `bclosed` BOOLEAN, IN `nstatus` BIGINT(20), IN `nuser` BIGINT(20), IN `slang` VARCHAR(50) CHARSET utf8, OUT `nid` BIGINT(20) UNSIGNED)  BEGIN

INSERT INTO `request_list`(`sdescription`, `stitle`, `nauthor`, `bclosed`, `nstatus`, `nuser`, `slang`) 
VALUES (`sdescription`, `stitle`, `nauthor`, `bclosed`, `nstatus`, `nuser`, `slang`);

SELECT LAST_INSERT_ID() into `nid`;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_INSERT_USER_LIST` (IN `slogin` VARCHAR(50) CHARSET utf8, IN `sfirst_name` VARCHAR(50) CHARSET utf8, IN `slast_name` VARCHAR(50) CHARSET utf8, IN `semail` VARCHAR(50) CHARSET utf8, IN `sphone` CHAR(13) CHARSET utf8, IN `spassword` VARCHAR(64) CHARSET utf8, IN `benabled` BOOLEAN, IN `sfirst_name_origin` VARCHAR(50) CHARSET utf8, IN `slast_name_origin` VARCHAR(50) CHARSET utf8, OUT `nid` BIGINT(20) UNSIGNED)  BEGIN
INSERT INTO `user_list` (`slogin`, `sfirst_name`, `slast_name`,`semail`, `sphone`, `spassword`, `benabled`,`sfirst_name_origin`, `slast_name_origin`) 
VALUES (`slogin`, `sfirst_name`, `slast_name`,`semail`, `sphone`, `spassword`, `benabled`,`sfirst_name_origin`, `slast_name_origin`);

SELECT LAST_INSERT_ID() into `nid`;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_HISTORY_REQUEST_LIST` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = ' from `history_request_list` h ';
SET stmt = concat(stmt, ' inner join `user_list` u on h.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on h.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on h.nstatus = s.id');
SET stmt = concat(stmt, ' where s.bclose = 1 ');
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' AND ( h.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       h.sdescription like ''%', `ssearch`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_HISTORY_REQUEST_LIST_BY_LANGUAGE_AND_AUTHOR` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `nauthor` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `slang` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = 'from `history_request_list` h ';
SET stmt = concat(stmt, ' inner join `user_list` u on h.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on h.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on h.nstatus = s.id');
SET stmt = concat(stmt, ' where h.nauthor = ', `nauthor`);
SET stmt = concat(stmt, ' AND h.slang = ''', `slang`, '''');
SET stmt = concat(stmt, ' AND s.bclose = 1 ');
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' AND (h.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, ' h.sdescription like ''%', `ssearch`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_REQUEST_LIST` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = ' from `request_list` r ';
SET stmt = concat(stmt, ' inner join `user_list` u on r.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on r.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on r.nstatus = s.id');
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' where r.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       r.sdescription like ''%', `ssearch`, '%'' ');
end if;

SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`%`@`%` PROCEDURE `APP_PAGINATION_REQUEST_LIST_BY_AUTHOR` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `nauthor` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = ' from `request_list` r ';
SET stmt = concat(stmt, ' inner join `user_list` u on r.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on r.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on r.nstatus = s.id');
SET stmt = concat(stmt, ' where r.nauthor = ', `nauthor`);
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' AND (r.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       r.sdescription like ''%', `ssearch`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_REQUEST_LIST_BY_LANGUAGE_AND_AUTHOR` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `nauthor` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `slang` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = 'from `request_list` r ';
SET stmt = concat(stmt, ' inner join `user_list` u on r.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on r.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on r.nstatus = s.id');
SET stmt = concat(stmt, ' where r.nauthor = ', `nauthor`);
SET stmt = concat(stmt, ' AND r.slang = ''', `slang`, '''');
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' AND (r.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, ' r.sdescription like ''%', `ssearch`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_REQUEST_LIST_BY_LANGUAGE_AND_STATUS` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `nstatus` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `slang` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 

DECLARE stmt varchar(1000);

SET stmt = 'from `request_list` r ';
SET stmt = concat(stmt, ' inner join `user_list` u on r.nuser = u.id');
SET stmt = concat(stmt, ' inner join `user_list` a on r.nauthor = a.id');
SET stmt = concat(stmt, ' inner join `status` s on r.nstatus = s.id');
SET stmt = concat(stmt, ' where r.nstatus = ', `nstatus`);
SET stmt = concat(stmt, ' AND r.slang = ''', `slang`, '''');
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' AND (r.stitle like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, ' r.sdescription like ''%', `ssearch`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by stitle ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `APP_PAGINATION_USER_LIST` (IN `nlimit` BIGINT(20) UNSIGNED, IN `noffset` BIGINT(20) UNSIGNED, IN `ssearch` VARCHAR(50), IN `ssorting` VARCHAR(50), OUT `ncount` BIGINT(20) UNSIGNED)  BEGIN 
DECLARE stmt varchar(1000);

SET stmt = 'from `user_list` u ';
if (`ssearch` is not null) and (length(`ssearch`) > 0) THEN
	SET stmt = concat(stmt, ' where u.slogin like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.sfirst_name like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.slast_name like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.sfirst_name_origin like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.slast_name_origin like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.sphone like ''%', `ssearch`, '%'' or ');
    SET stmt = concat(stmt, '       u.semail like ''%', `ssearch`, '%'' ');
end if;
SET stmt = concat(stmt, ' order by slogin ', `ssorting`);
SET stmt = concat(stmt, ' limit ', `nlimit`,' offset ', `noffset`);

CALL APP_GET_COUNT_PAGINATON(concat('select count(*) into @counter ',stmt) , ncount);  

SET @qry = concat('select * ',stmt);

PREPARE stm FROM @qry;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `history_request_list`
--

CREATE TABLE `history_request_list` (
  `id` bigint(20) NOT NULL,
  `ddate_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ddate_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sdescription` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `stitle` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `nauthor` bigint(20) UNSIGNED NOT NULL,
  `nstatus` bigint(20) NOT NULL,
  `nuser` bigint(20) UNSIGNED NOT NULL,
  `nprice` decimal(17,0) NOT NULL DEFAULT '0',
  `scause` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sreview` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nrating` bigint(20) NOT NULL DEFAULT '-1',
  `slang` char(5) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'uk_UA'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `next_statuses`
--

CREATE TABLE `next_statuses` (
  `nstatus` bigint(20) NOT NULL,
  `nnextstatus` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `request_list`
--

CREATE TABLE `request_list` (
  `id` bigint(20) NOT NULL,
  `ddate_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ddate_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sdescription` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `stitle` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `nauthor` bigint(20) UNSIGNED NOT NULL,
  `bclosed` tinyint(1) NOT NULL DEFAULT '0',
  `nstatus` bigint(20) NOT NULL,
  `nuser` bigint(20) UNSIGNED NOT NULL,
  `nprice` decimal(17,0) NOT NULL DEFAULT '0',
  `scause` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `slang` char(5) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'uk_UA'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `request_list`
--
DELIMITER $$
CREATE TRIGGER `REQUEST_LIST_AFTER_UPDATE` AFTER UPDATE ON `request_list` FOR EACH ROW BEGIN
DECLARE n_id bigint(20);

INSERT INTO request_list_history(`sdescription`, `stitle`, `nauthor`, `nstatus`, `nuser`, `nprice`, `scause`, `slang`)
VALUES(NEW.`sdescription`, NEW.`stitle`, NEW.`nauthor`, NEW.`nstatus`, NEW.`nuser`, NEW.`nprice`, NEW.`scause`, NEW.`slang`);

END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `scode` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE `status` (
  `id` bigint(20) NOT NULL,
  `scode` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `sname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bclose` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_list`
--

CREATE TABLE `user_list` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `slogin` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sfirst_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `slast_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `semail` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sphone` char(13) COLLATE utf8_unicode_ci NOT NULL DEFAULT '+380003332211',
  `spassword` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `benabled` tinyint(1) NOT NULL,
  `ddate_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ddate_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sfirst_name_origin` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `slast_name_origin` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `nuser` bigint(20) UNSIGNED NOT NULL,
  `nrole` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `history_request_list`
--
ALTER TABLE `history_request_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_rlh_status_idx` (`nstatus`),
  ADD KEY `fk_rlh_ul_nauthor_idx` (`nauthor`),
  ADD KEY `fk_rlh_ul_user_idx` (`nuser`);

--
-- Indexes for table `next_statuses`
--
ALTER TABLE `next_statuses`
  ADD KEY `FKsuta6axf3migey5h9oj16xj5x` (`nnextstatus`),
  ADD KEY `FKoddjp3l3g4hylg6nlj4j6y2ft` (`nstatus`);

--
-- Indexes for table `request_list`
--
ALTER TABLE `request_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlgwyum1p63t6sj0d73cqjpx5e` (`nstatus`),
  ADD KEY `fk_request_list_user_list_author` (`nauthor`),
  ADD KEY `fk_request_list_user_list_user` (`nuser`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `scode` (`scode`);

--
-- Indexes for table `user_list`
--
ALTER TABLE `user_list`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `slogin` (`slogin`),
  ADD UNIQUE KEY `sphone` (`sphone`),
  ADD UNIQUE KEY `semail` (`semail`) USING BTREE;

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`nuser`,`nrole`),
  ADD KEY `FKgamli03ce2otbpme8w6gmbk91` (`nrole`),
  ADD KEY `fk_user_role_user_list_user_idx` (`nuser`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `history_request_list`
--
ALTER TABLE `history_request_list`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
--
-- AUTO_INCREMENT for table `request_list`
--
ALTER TABLE `request_list`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
--
-- AUTO_INCREMENT for table `user_list`
--
ALTER TABLE `user_list`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `history_request_list`
--
ALTER TABLE `history_request_list`
  ADD CONSTRAINT `fk_rlh_s_status` FOREIGN KEY (`nstatus`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `fk_rlh_ul_author` FOREIGN KEY (`nauthor`) REFERENCES `user_list` (`id`),
  ADD CONSTRAINT `fk_rlh_ul_user` FOREIGN KEY (`nuser`) REFERENCES `user_list` (`id`);

--
-- Constraints for table `next_statuses`
--
ALTER TABLE `next_statuses`
  ADD CONSTRAINT `FKoddjp3l3g4hylg6nlj4j6y2ft` FOREIGN KEY (`nstatus`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `FKsuta6axf3migey5h9oj16xj5x` FOREIGN KEY (`nnextstatus`) REFERENCES `status` (`id`);

--
-- Constraints for table `request_list`
--
ALTER TABLE `request_list`
  ADD CONSTRAINT `FKlgwyum1p63t6sj0d73cqjpx5e` FOREIGN KEY (`nstatus`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `fk_request_list_user_list_author` FOREIGN KEY (`nauthor`) REFERENCES `user_list` (`id`),
  ADD CONSTRAINT `fk_request_list_user_list_user` FOREIGN KEY (`nuser`) REFERENCES `user_list` (`id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FKgamli03ce2otbpme8w6gmbk91` FOREIGN KEY (`nrole`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `fk_user_role_user_list_user` FOREIGN KEY (`nuser`) REFERENCES `user_list` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
