CREATE DATABASE server;
USE server;

DROP TABLE serverconnection;
CREATE TABLE serverconnection(
  source_class VARCHAR(20),
  source_func VARCHAR(20),
  target_class VARCHAR(20),
  target_func VARCHAR(20),
  description VARCHAR(50)
)DEFAULT CHARSET = utf8;

INSERT INTO serverconnection VALUES ("class1","func1","class2","func1","test1");
INSERT INTO serverconnection VALUES ("class1","func2","class2","func2","test2");
INSERT INTO serverconnection VALUES ("class2","func1","class3","func1","test3");
INSERT INTO serverconnection VALUES ("class3","func1","class1","func2","test4");
INSERT INTO serverconnection VALUES ("class2","func2","class3","func2","test5");
INSERT INTO serverconnection VALUES ("class4","func2","class3","func2","test6");
INSERT INTO serverconnection VALUES ("class5","func2","class4","func2","test7");
INSERT INTO serverconnection VALUES ("class4","func2","class6","func2","test8");
INSERT INTO serverconnection VALUES ("class3","func2","class1","func2","test9");
INSERT INTO serverconnection VALUES ("class6","func2","class2","func2","test10");
