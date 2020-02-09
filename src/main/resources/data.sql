-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 09, 2020 at 11:02 PM
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

--
-- Dumping data for table `history_request_list`
--

INSERT INTO `history_request_list` (`id`, `ddate_created`, `ddate_updated`, `sdescription`, `stitle`, `nauthor`, `nstatus`, `nuser`, `nprice`, `scause`, `sreview`, `nrating`, `slang`) VALUES
(8, '2020-01-13 17:14:32', '2020-01-13 17:14:32', 'May the Force be with you', 'Fix the Death Star, please', 12, 3, 4, '0', 'he is evil', NULL, -1, 'en_US'),
(14, '2020-01-14 22:44:05', '2020-01-14 22:44:05', 'На улюблені джинси', 'Поставити шкіряну латку', 1, 2, 4, '15', 'така ціна', NULL, -1, 'uk_UA'),
(15, '2020-01-14 22:53:25', '2020-01-14 22:53:25', 'Її зламала войовнича верба, а нову мені, у найближчій час, точно не куплять... машина і все таке...', 'Чарівна паличка', 6, 2, 4, '143', 'робимо нову', NULL, -1, 'uk_UA'),
(19, '2020-01-15 13:09:32', '2020-01-15 13:09:32', 'May the Force be with you', 'Fix the Death Star, please', 12, 3, 4, '0', '', NULL, -1, 'en_US'),
(20, '2020-01-15 13:16:47', '2020-01-15 13:16:47', 'May the Force be with you', 'Fix the Death Star, please', 12, 3, 4, '0', 'This is the third attempt in a week - he is persistent', NULL, -1, 'en_US'),
(21, '2020-01-15 13:29:23', '2020-01-15 13:29:23', 'Наш будинок "Барліг" підірвала Белатриса Лестрандж', 'Полагодити будинок', 6, 2, 4, '20000', '', NULL, -1, 'uk_UA'),
(22, '2020-01-15 13:31:55', '2020-01-15 13:31:55', 'Наш будинок "Барліг" підірвала Белатриса Лестрандж', 'Полагодити будинок', 6, 4, 13, '2500', '', NULL, -1, 'uk_UA'),
(25, '2020-01-15 21:52:31', '2020-01-15 21:52:31', 'Не вмикається люстра, був пшик і щось голосно ляснуло... лячно ((((( пишу з ноута при свічках - боюся вмикати електричні прилади', 'Лампочка', 10, 2, 1, '111', '111', NULL, -1, 'uk_UA');

--
-- Dumping data for table `next_statuses`
--

INSERT INTO `next_statuses` (`nstatus`, `nnextstatus`) VALUES
(1, 2),
(1, 3),
(2, 4);

--
-- Dumping data for table `request_list`
--

INSERT INTO `request_list` (`id`, `ddate_created`, `ddate_updated`, `sdescription`, `stitle`, `nauthor`, `bclosed`, `nstatus`, `nuser`, `nprice`, `scause`, `slang`) VALUES
(16, '2020-01-07 00:00:00', '2020-01-15 21:17:39', 'It does not boil water (((', 'Kettle', 1, 0, 1, 1, '0', NULL, 'en_US'),
(17, '2020-01-08 00:00:00', '2020-01-15 00:00:00', 'Не вмикається люстра, був пшик і щось голосно ляснуло... лячно ((((( пишу з ноута при свічках - боюся вмикати електричні прилади', 'Лампочка', 10, 0, 2, 1, '111', '111', 'uk_UA'),
(21, '2020-01-08 00:00:00', '2020-01-14 22:53:25', 'Її зламала войовнича верба, а нову мені, у найближчій час, точно не куплять... машина і все таке...', 'Чарівна паличка', 6, 0, 2, 4, '143', 'робимо нову', 'uk_UA'),
(22, '2020-01-10 00:00:00', '2020-01-13 11:52:26', 'I feel that Force is going out', 'Lightsaber energy leak', 12, 0, 1, 12, '0', NULL, 'en_US'),
(31, '2020-01-07 00:00:00', '2020-01-13 00:00:00', 'На улюблені джинси', 'Поставити шкіряну латку', 1, 0, 2, 4, '15', 'така ціна', 'uk_UA'),
(36, '2020-01-15 11:34:42', '2020-01-15 11:34:42', 'Користуюсь вже місяць хочу наточити ножі, бо мені здається, що їх вже потрібно наточувати', 'Кавомолка', 10, 0, 1, 10, '0', NULL, 'uk_UA'),
(37, '2020-01-29 16:17:36', '2020-01-29 16:17:36', 'Some valuable description', 'Brand New Request', 1, 0, 1, 1, '0', NULL, 'en_US'),
(42, '2020-01-29 17:09:29', '2020-01-29 17:09:29', 'Ще один опис заявки', 'Ще одна заявка', 10, 0, 1, 10, '0', NULL, 'uk_UA'),
(43, '2020-01-29 18:46:56', '2020-01-29 18:46:56', 'Текст', 'Ще одна заявка', 1, 0, 1, 1, '0', NULL, 'uk_UA'),
(44, '2020-01-30 00:18:40', '2020-01-30 00:18:40', 'And some description', 'Brand New Request Again', 1, 0, 1, 1, '0', NULL, 'en_US'),
(45, '2020-01-30 00:19:50', '2020-01-30 00:19:50', 'Було б чудово', 'Закрити усі TODO', 1, 0, 1, 1, '0', NULL, 'uk_UA'),
(47, '2020-01-31 14:02:02', '2020-01-31 14:02:02', 'Description', 'Something', 1, 0, 1, 1, '0', NULL, 'en_US'),
(48, '2020-02-04 16:28:32', '2020-02-04 16:28:32', 'In English!', 'New Request From Barbie', 10, 0, 1, 10, '0', NULL, 'en_US');

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `scode`, `sname`) VALUES
(1, 'ADMIN', 'Administator'),
(2, 'USER', 'User'),
(3, 'MANAGER', 'Manager'),
(4, 'WORKMAN', 'The best of the best workman ever');

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`id`, `scode`, `sname`, `bclose`) VALUES
(1, 'REGISTER', 'Request was created by user', 0),
(2, 'ACCEPT', 'Request was accepted by manager', 0),
(3, 'REJECT', 'Request was rejected by manager', 1),
(4, 'DONE', 'Request was done by workman', 1);

--
-- Dumping data for table `user_list`
--

INSERT INTO `user_list` (`id`, `slogin`, `sfirst_name`, `slast_name`, `semail`, `sphone`, `spassword`, `benabled`, `ddate_created`, `ddate_modified`, `sfirst_name_origin`, `slast_name_origin`) VALUES
(1, 'kissik', 'Iryna', 'Afanasieva', 'iryna.v.afanasieva@gmail.com', '+380003332211', '$2a$11$dp4wMyuqYE3KSwIyQmWZFeUb7jCsHAdk7ZhFc0qGw6i5J124imQBi', 1, '2020-01-07 00:26:43', '2020-01-07 19:07:45', 'Ірина', 'Афанасьєва'),
(4, 'jack_sparrow', 'Jack', 'Sparrow', 'jack.sparrow@yohoho.com', '+380001122333', '$2a$11$bjnTh5K8Cl83eR92260OHe3WBiwXDJbw2m.2BGzgkAaSYs2hPswfG', 1, '2020-01-07 19:15:35', '2020-01-07 20:01:19', 'Джек', 'Горобець'),
(5, 'harry_potter', 'Harry', 'Potter', 'harry.potter@gryffindor.hogwarts', '+380001112233', '$2a$11$S9gLLs9Jz47/4/SOd53Y6.XnpaEj6Uj51qxhYQ4TUbMfsXNo.M2Cm', 1, '2020-01-07 19:32:06', '2020-01-08 19:06:23', 'Гаррі', 'Поттер'),
(6, 'ron_weasley', 'Ron', 'Weasley', 'ron.weasley@gryffindor.hogwarts', '+380001122334', '$2a$11$jzZTI2LsV3Gcnk.BMudNq.r3iZznShJAFUjPpLdAJfMFtqd29.npq', 1, '2020-01-07 19:40:53', '2020-01-08 19:06:23', 'Рон', 'Візлі'),
(7, 'fred_weasley', 'Fred', 'Weasley', 'fred.weasley@gryffindor.hogwards', '+380001122335', '$2a$11$1s/yY8UwaKo457urBjU20ebNg3QiNaS4A1AajiVcWE0EAH1frcwFi', 1, '2020-01-07 19:47:23', '2020-01-08 19:06:23', 'Фред', 'Візлі'),
(8, 'george_weasley', 'George', 'Weasley', 'george.weasley@gryffindor.hogwarts', '+380001122336', '$2a$11$sWTFx1XpH5NZF7a0atYQNOAPHXFPt4ZQXhtKH.5yluTZZBpqN7by2', 1, '2020-01-07 19:53:29', '2020-01-08 19:06:23', 'Джордж', 'Візлі'),
(9, 'ginny_weasley', 'Ginevra', 'Weasley', 'ginevra.weasley@gryffindor.hogwarts', '+380001122337', '$2a$11$fV2QZrapR6vUGnEyhx/iE.FlFSm8JT6kFbkdyvWT/I40ac4IISG3W', 1, '2020-01-07 20:00:20', '2020-01-07 20:00:20', 'Джінні', 'Візлі'),
(10, 'barbie', 'Barbie', 'Smith', 'barbie@theawesome.com', '+380001112238', '$2a$11$8Jl4rSCVd.7TvdNXfjgS9.uWcn1MrUvgQjl0HTpSUig.3bdQkreAS', 1, '2020-01-08 12:27:04', '2020-01-08 12:27:04', 'Барбі', 'Сміт'),
(11, 'snow_white', 'Snow', 'White', 'snow.white@disney.com', '+380002223311', '$2a$11$PAu8DuF07esMnSdiGGvrwuTMgpgOAlVywvy/H9K3Q.shW.8oKfosW', 1, '2020-01-08 12:35:17', '2020-01-08 12:35:17', 'Біло', 'Сніжка'),
(12, 'darth_vader', 'Anakin', 'Skywalker', 'darth.vader@death.star', '+380000000000', '$2a$11$bNtL1wumOhwLWf5Phyuq8O54HBSLqB4Sva9m9Yc8TgqfgCF3FM9.a', 1, '2020-01-08 22:52:22', '2020-01-08 22:52:22', 'Анакін', 'Скайвокер'),
(13, 'papa_karlo', 'Papa', 'Karlo', 'papa.karlo@italy.com', '+380001144555', '$2a$11$M33yvVY0WJheGYB16eDGiOXpsf74Z0E5pUJBZZlgEeQIgxJm9vdrC', 1, '2020-01-09 16:10:14', '2020-01-09 16:10:14', 'Тато', 'Карло'),
(23, 'hermione', 'Hermione', 'Granger', 'hermione.granger@gryffindor.hogwarts', '+380008888888', '$2a$11$3YwvF1WpNEYsmCbf28wUyu88wdB0XOXSLc4SIRoVikebHw8XJplkG', 1, '2020-01-10 11:06:10', '2020-01-10 11:06:10', 'Герміона', 'Гренджер'),
(30, 'test_user', 'Text', 'User', 'test.user@test.com', '+385556664477', '$2a$11$6THrRUCBMt6XYEdCyejcneqP/wW4vCY3by/AIWghzl2L7o9g0Ia6C', 1, '2020-01-10 16:56:57', '2020-01-10 16:56:57', 'Тест', 'Користувач'),
(35, 'albus_dumbledore', 'Albus', 'Dumbledore', 'albus.dumbledore@gryffindor.hogwarts', '+389999999999', '$2a$11$Mv4c5eLEX/xqwaNVLdw52OclgUHDvKygmX7MtdnabyVVodLXDCevG', 1, '2020-01-14 14:56:37', '2020-01-14 14:56:37', 'Албус', 'Дамблдор'),
(37, 'dobby_free_elf', 'Dobby', 'Free Elf', 'dobby@malfoy.home', '+333333333333', '$2a$11$LiolbNO9trVu9Li6Y6CWO.xfHZC/yTS6thh1eNdpomEFr1v9t2llS', 1, '2020-01-15 12:25:26', '2020-01-15 12:25:26', 'Доббі', 'Вільний ельф'),
(38, 'frodo_baggins', 'Frodo', 'Baggins', 'frodo.baggins@mount.doom', '+386547898765', '$2a$11$RKvdRemqN1olq8ifdWzcweuA4C.xj48qlyBOTiupytUzWf9jtmidW', 1, '2020-01-25 17:39:05', '2020-01-25 18:35:38', 'Фродо', 'Беггінс'),
(39, 'gandalf_grey', 'Gandalf', 'The Grey', 'gandalf.grey@istari.wizard', '+380001111111', '$2a$11$.2Ykdh6fwXP5pK6l9GRFw.ru4r0TYr7xcIaPVNSM1rLYEH3t5ENxu', 1, '2020-01-25 18:05:22', '2020-01-25 18:36:00', 'Гендальф', 'Сірий'),
(41, 'new_user', 'new_user', 'new_user', 'new_user@new_user', '+334455667788', '$2a$11$ED0RR3E9/aGykMnEVJLpqO7lmYNl960Phtf8PPLiaK0AnvV7WSoIu', 1, '2020-01-25 18:33:55', '2020-01-25 18:33:55', 'новий користувач', 'Новий користувач'),
(46, 'test22', 'Text', 'Test', 'some.email@gmaillll.com', '+332211455772', '$2a$11$DNcP1DfLWKWbHfpGCQKmqewdDGi1oAbm/lmHM.6KnxfYvtcBT5W9S', 1, '2020-01-25 23:20:24', '2020-01-25 23:20:24', 'Текст', 'Прізвище'),
(47, 'testtttttt', 'Test', 'Test', 'email@email.com', '+333331114477', '$2a$11$kYZor3d6QuURWL8WJuWM7.DEB3ThoDy2b9JYbvO6OafipCay3.uWy', 1, '2020-01-31 16:40:00', '2020-01-31 16:40:00', 'Назва', 'Прізвище'),
(48, 'albus_dumbledore_2', 'Albus', 'Dumbledore', 'some.emailllll@gryffindor.hogwarts', '+389999999990', '$2a$11$3nd3v17ixr5XXIs.jNiRLuBuo8tYWgoklMy4k9qg3mtAaL0D60QZy', 1, '2020-01-31 17:02:31', '2020-01-31 17:02:31', 'Албус', 'Дамблдор'),
(54, 'draco_malfoy', 'Draco', 'Malfoy', 'draco.malfoy@malfoy.corp', '+770007777777', '$2a$11$ic96ZjOofzt7D9vdjf2Ag.YogRQeogN9zAaQyJMJBtuc67Kjnwp/K', 1, '2020-02-06 23:16:35', '2020-02-06 23:16:35', 'Драко', 'Малфой');

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`nuser`, `nrole`) VALUES
(1, 1),
(35, 1),
(37, 1),
(1, 2),
(4, 2),
(5, 2),
(6, 2),
(8, 2),
(9, 2),
(10, 2),
(11, 2),
(12, 2),
(23, 2),
(30, 2),
(37, 2),
(38, 2),
(39, 2),
(41, 2),
(46, 2),
(47, 2),
(48, 2),
(54, 2),
(1, 3),
(4, 3),
(5, 3),
(7, 3),
(37, 3),
(1, 4),
(13, 4),
(37, 4),
(48, 4);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
