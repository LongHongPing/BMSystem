/*
create database library;

use library;

create table book(
bookId int primary key auto_increment not null,
ISBN varchar(30) not null,
author varchar(30) not null,
booksort varchar(30) not null,
publishname varchar(30) not null,
publishdate datetime not null,
price float not null,
pagenum int not null,
keywords varchar(30) not null,
registerdate datetime not null,
num int not null,
status varchar(5) default "未借出",
remarks varchar(99) not null
)charset=utf8;

create table reader (
readerId int primary key auto_increment not null,
readerName varchar(30) not null,
gender varchar(30) not null,
typeId varchar(30) not null,
department varchar(30) not null,
signInDate varchar(30) not null,
remark varchar(30) not null,
pwd varchar(30) not null,
tel int not null,
email varchar(30) not null,
maximum int not null,
adm varchar(5)
)charset=utf8;
*/
/*
create table borrow(
	borrowid int primary key auto_increment not null,
    bookId int not null,
    readerId int not null,
    borrowDate datetime not null,
    returnTime datetime default now()
)charset=utf8;

create table blacklist(
	readerId int primary key not null,
    overDueCount int default 0
);

-- ******存储过程****** 
-- 注册
delimiter //
create procedure pro_regist(
out p_readerId int,
in p_readerName varchar(30),
in p_gender varchar(30),
in p_typeId varchar(30),
in p_department varchar(30),
in p_signInDate varchar(30),
in p_remark varchar(30),
in p_pwd varchar(30),
in p_tel varchar(30),
in p_email varchar(30)
)
begin
	begin
	insert into reader(readerName,gender,typeId,department,signInDate,remark,pwd,tel,email,maximum,adm) 
				values(p_readername,p_gender,p_typeId,p_department,p_signInDate,p_remark,p_pwd,p_tel,p_email,10,"否");
	end;
    begin
    select readerId into p_readerId from reader where readerName = p_readerName;
    end;
end;//
delimiter ;

-- 借书
delimiter //
create procedure pro_borrow(
	in p_readerid int,
    in p_bookid int,
    in p_sqldate datetime
)
begin
    begin
		insert into borrow(readerId,bookId,borrowDate) 
					values(p_readerid,p_bookid,p_sqldate); 
		update book set status = "借出",num = num-1 where bookid = p_bookid;
        update reader set maximum = maximum-1 where readerId = p_readerid;
    end;
end;//
delimiter ;

-- 还书
delimiter //
create procedure pro_return(
	in p_bookid int,
    in p_readerid int
)
begin
	begin
		update borrow set returnTime = now() where bookid = p_bookid;
        update book set status = "未借出",num = num+1 where bookid = p_bookid;
        update reader set maximum = maximum+1 where readerId = p_readerid;
    end;
end;//
delimiter ;
*/