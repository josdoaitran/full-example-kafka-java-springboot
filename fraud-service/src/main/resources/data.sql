--DROP TABLE IF EXISTS `user_fraud_info`;
--
--DROP TABLE IF EXISTS `user`;
--
--CREATE TABLE `user` (
--  `id` int(11) NOT NULL AUTO_INCREMENT,
--  `name` varchar(20) NOT NULL,
--  `phone` varchar(10) NOT NULL,
--  `status` varchar(128) NOT NULL,
--  PRIMARY KEY (id)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE `user_fraud_info` (
--  `id` int(11) NOT NULL AUTO_INCREMENT,
--  `phone` varchar(10) NOT NULL,
--  `credit_info` varchar(128) NOT NULL,
--  `updated_at` varchar(255),
--  PRIMARY KEY (id)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user_fraud_info (id, phone, credit_info, updated_at) VALUES ('1', '0906973152', 'Good Profile', NULL);

INSERT INTO user_fraud_info (id, phone, credit_info, updated_at) VALUES ('2', '0906031925', 'Good Profile', NULL);

INSERT INTO user_fraud_info (id, phone, credit_info, updated_at) VALUES ('3', '0912589740', 'Fraud Profile', '2022-04-08 09:12:32');

INSERT INTO user_fraud_info (id, phone, credit_info, updated_at) VALUES ('4', '0934537836', 'Fraud Profile', '2022-04-08 09:12:32');