DROP DATABASE IF EXISTS moviefun_dev;
DROP DATABASE IF EXISTS moviefun_test;

CREATE USER IF NOT EXISTS 'moviefun'@'localhost'
  identified by 'moviefun';
GRANT ALL PRIVILEGES ON *.* TO 'moviefun' @'localhost';

CREATE DATABASE moviefun_dev;
CREATE DATABASE moviefun_test;