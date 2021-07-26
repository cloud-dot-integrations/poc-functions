create table history(requestid varchar primary key , account varchar, ctime bigint, mtime bigint, success boolean, outcome varchar);
grant all on history to PUBLIC;
