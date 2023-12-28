-- 뷰삭제
drop view if exists lrlist;
drop view if exists ptlist;
drop view if exists cholist;
drop view if exists lslist;
drop view if exists dbrlist;
drop view if exists dblist;
drop view if exists minfo;

-- 테이블 삭제
drop table if exists goodsbuy cascade;
drop table if exists goodscart cascade;
drop table if exists goodscompany cascade;
drop table if exists goodsimgfile cascade;
drop table if exists goodslist cascade;
drop table if exists lessonreservationlist cascade;
drop table if exists lessonparttime cascade;
drop table if exists lessonchoreoimgfile cascade;
drop table if exists lessonchoreo cascade;
drop table if exists lessonimgfile cascade;
drop table if exists lessonlist cascade;
drop table if exists danceboardreply cascade;
drop table if exists dancecategory cascade;
drop table if exists dancevideofile cascade;
drop table if exists danceimgfile cascade;
drop table if exists danceboard cascade;
drop table if exists member cascade;



-- 회원정보
create table if not exists member(
	m_id varchar(20) not null primary key,
	m_pwd varchar(200) not null,
    m_name varchar(20) not null,
    m_birth date not null,
    m_phone varchar(20) not null,
    m_sex varchar(20) not null,
    m_email varchar(30) not null,
    m_zipcode varchar(20) not null,
    m_addr varchar (50) not null,
    m_category varchar(20) not null
);
-- 커뮤니티 게시판
create table if not exists danceboard(
	d_num int not null auto_increment primary key,
    d_title varchar(30) not null,
    d_category varchar(20) not null,
    d_contents varchar(200) not null,
    d_mid varchar(20) not null,
    d_date timestamp default current_timestamp,
	d_views int default 0 not null,
    constraint fk_d_mid foreign key(d_mid) references member(m_id)
);

-- 게시글 작성시 생성 된 카테고리
create table if not exists dancecategory(
	dc_num int not null auto_increment primary key,
    dc_category varchar(20)
);
insert into dancecategory value(null, '힙합');
insert into dancecategory value(null, 'K-POP');
insert into dancecategory value(null, '스트릿댄스');
insert into dancecategory value(null, '댄스스포츠');
insert into dancecategory value(null, '유행춤');
insert into dancecategory value(null, '크럼프');
insert into dancecategory value(null, '롹킹');

-- 게시글 섬네일 파일 저장 장소lessonreservationlist
create table if not exists danceimgfile(
	dif_num int not null auto_increment primary key,
    dif_dnum int not null,
    dif_oriname varchar(100) not null,
    dif_sysname varchar(100) not null,
    constraint fk_dif_dnum foreign key (dif_dnum)references danceboard(d_num)
);
-- 게시글 동영상 파일 저장 장소
create table if not exists dancevideofile(
	dvf_num int not null auto_increment primary key,
    dvf_dnum int not null,
    dvf_oriname varchar(100) not null,
    dvf_sysname varchar(100) not null,
    constraint fk_dvf_dnum foreign key (dvf_dnum)references danceboard(d_num)
);

-- 게시글 댓글 테이블
create table if not exists danceboardreply(
    dbr_num int not null auto_increment primary key,
    dbr_dnum int not null,
    dbr_contents varchar(200) not null,
    dbr_mid varchar(20) not null,
    dbr_date datetime not null default current_timestamp,
    constraint fk_dbr_dnum foreign key(dbr_dnum) references danceboard(d_num),
    constraint fk_dbr_mid foreign key(dbr_mid) references member(m_id)
 );

-- 레슨 등록
create table if not exists lessonlist(
	ls_num int not null auto_increment primary key,
    ls_academy varchar(30) not null,
    ls_addr varchar(100) not null,
    ls_title varchar(30) not null,
    ls_date timestamp default current_timestamp,
    ls_total int not null,
    ls_mid varchar(20) not null,
    ls_price int not null,
    constraint fk_ls_mid foreign key (ls_mid)references member(m_id)
);
-- 레슨할 학원 이미지 등록
create table if not exists lessonimgfile(
	lif_num int not null auto_increment primary key,
    lif_lsnum int not null,
    lif_oriname varchar(100) not null,
    lif_sysname varchar(100) not null,
    constraint fk_lif_lsnum foreign key (lif_lsnum)references lessonlist(ls_num)
);

-- 레슨 안무가 등록
create table if not exists lessonchoreo(
	cho_num int not null auto_increment primary key,
    cho_lsnum int not null,
    cho_lsacademy varchar(40) not null,
    cho_name varchar(20) not null,
    constraint fk_cho_lsnum foreign key (cho_lsnum) references lessonlist(ls_num)
);

create table if not exists lessonchoreoimgfile(
	lcif_num int not null auto_increment primary key,
    lcif_chonum int not null,
    lcif_oriname varchar(100) not null,
    lcif_sysname varchar(100) not null,
    constraint fk_lcif_chonum foreign key(lcif_chonum)references lessonchoreo(cho_num)
);

-- 레슨 안무가가 진행할 시간 등록
create table if not exists lessonparttime(
	lpt_num int not null auto_increment primary key,
    lpt_chonum int not null,
    lpt_choname varchar(30) not null,
    lpt_parttime varchar(50) not null,
    lpt_ptimedate varchar(50) not null,
    constraint fk_lpt_chonum foreign key(lpt_chonum)references lessonchoreo(cho_num)
);

-- 고객이 예약한 레슨 정보
create table if not exists lessonreservationlist(
    lrl_num int not null auto_increment primary key,
    lrl_mid varchar(20) not null,
    lrl_lsnum int not null,
    lrl_lsacademy varchar(50) not null,
    lrl_lsaddr varchar(50) not null,
    lrl_lstitle varchar(50) not null,
    lrl_chonum int not null,
    lrl_choname varchar(20) not null,
    lrl_lptparttime varchar(50) not null,
    lrl_lptptimedate date not null,
    lrl_lstotal int not null,
    lrl_lsprice int not null,
    constraint fk_lrl_lsnum foreign key(lrl_lsnum)references lessonlist(ls_num),
	constraint fk_lrl_chonum foreign key(lrl_chonum)references lessonchoreo(cho_num),
    constraint fk_lrl_mid foreign key(lrl_mid)references member(m_id)
);
-- 굿즈 상품 등록
create table if not exists goodslist(
	gs_num int not null auto_increment primary key,
    gs_mid varchar(30) not null,
    gs_pname varchar(30) not null,
    gs_ammount int not null,
    gs_price int not null,
    gs_date date not null,
    constraint fk_gs_mid foreign key(gs_mid)references member(m_id)
);
-- 굿즈 상품 이미지 파일
create table if not exists goodsimgfile(
	gif_num int not null auto_increment primary key,
    gif_gsnum int not null,
    gif_oriname varchar(100) not null,
    gif_sysname varchar(100) not null,
    constraint fk_gif_gsnum foreign key(gif_gsnum) references goodslist(gs_num)
);

-- 굿즈 상품 담당 회사
create table if not exists goodscompany(
	gc_num int not null auto_increment primary key,
    gc_gsnum int not null,
    gc_name varchar(30) not null,
    gc_phone varchar(30) not null,
    gc_addr varchar(50) not null,
    gc_manager varchar(30) not null,
    constraint fk_gc_gsnum foreign key(gc_gsnum) references goodslist(gs_num)
);

-- 굿즈 상품을 담을 장바구니
create table if not exists goodscart(
	gct_gsnum int not null,
    gct_mid varchar(30) not null,
    constraint fk_gct_gsnum foreign key(gct_gsnum) references goodslist(gs_num),
    constraint fk_gct_mid foreign key(gct_mid) references member(m_id)
);

-- 고객 굿즈 구매한 내역
create table if not exists goodsbuy (
	gsb_num int not null auto_increment primary key,
    gsb_gsnum int not null,
    gsb_ammount int not null,
    gsb_price int not null,
    gsb_payment varchar(50) not null,
    gsb_date varchar(30) not null,
    gsb_mid varchar(30) not null,
    constraint fk_gsb_gsnum foreign key(gsb_gsnum) references goodslist(gs_num),
    constraint fk_gsb_mid foreign key(gsb_mid) references member(m_id)
);



-- 로그인 후 출력할 회원정보 뷰
-- CREATE OR REPLACE VIEW MINFO AS
-- SELECT M.M_ID,M.M_NAME, M.M_POINT,G.G_NAME 
-- FROM MEMBER M INNER JOIN GRADE G
-- ON M.M_POINT BETWEEN G.G_LOWPOINT AND G.G_HIGHPOINT;
-- SELECT * FROM MINFO;

-- 회원 아이디 이름 조회 뷰
create or replace view minfo as
select 
m.m_id,
m.m_name,
m.m_category,
m.m_email,
m.m_phone
from member m;

-- 게시판 최신글 순으로 정리
create or replace view dblist as
select 
db.d_num, 
db.d_title,
db.d_category, 
db.d_contents,
db.d_mid, 
db.d_date, 
db.d_views,
dif.dif_oriname,
dif.dif_sysname
from danceboard db 
inner join member m on db.d_mid = m.m_id
inner join dancecategory dc on db.d_category = dc.dc_category
inner join danceimgfile dif on db.d_num = dif.dif_dnum
order by db.d_num desc;

create or replace view dbrlist as
select 
dbr.dbr_num, 
dbr.dbr_dnum,
dbr.dbr_contents,
dbr.dbr_mid,
dbr.dbr_date
from danceboardreply dbr
inner join member m on dbr.dbr_mid = m.m_id
inner join danceboard db on dbr.dbr_dnum = db.d_num
order by dbr.dbr_date desc;

-- 레슨 목록에서 보일 목록 뷰
create or replace view lslist as
select 
ls.ls_num,
ls.ls_academy,
ls.ls_addr,
ls.ls_title,
ls.ls_date,
ls.ls_total,
ls.ls_mid,
ls.ls_price,
lif.lif_oriname,
lif.lif_sysname
from lessonlist ls
inner join lessonimgfile lif on ls.ls_num = lif.lif_num
inner join member m on ls.ls_mid = m.m_id
order by ls.ls_num desc;

-- 레슨 목록에서 보일 안무가 뷰
create or replace view cholist as
select 
cho.cho_num,
cho.cho_lsnum,
cho.cho_lsacademy,
cho.cho_name
from lessonchoreo cho
inner join lessonlist ls on cho.cho_lsnum = ls.ls_num and cho.cho_lsacademy = ls.ls_academy
order by cho.cho_num desc;

-- 레슨 목록에서 보일 파트타임 뷰
create or replace view ptlist as
select 
lpt.lpt_num,
lpt.lpt_chonum,
lpt.lpt_choname,
lpt.lpt_parttime,
lpt.lpt_ptimedate
from lessonparttime lpt
inner join lessonchoreo lc on lpt.lpt_chonum = lc.cho_num and lpt.lpt_choname = lc.cho_name
order by lpt.lpt_num desc;

-- 예약 후 목록 뷰
create or replace view lrlist as
select
lrl.lrl_num,
lrl.lrl_mid,
m.m_name,
m.m_phone,
lrl.lrl_lsnum,
lrl.lrl_lsacademy,
lrl.lrl_lsaddr,
lrl.lrl_lstitle,
lrl.lrl_chonum,
lrl.lrl_choname,
lrl.lrl_lptparttime,
lrl.lrl_lptptimedate,
lrl.lrl_lstotal,
lrl.lrl_lsprice,
lif.lif_oriname,
lif.lif_sysname
from lessonreservationlist lrl
inner join member m on lrl.lrl_mid = m.m_id
inner join lessonlist ls on lrl.lrl_lsnum = ls.ls_num and 
							lrl.lrl_lsacademy = ls_academy and 
                            lrl.lrl_lsaddr = ls.ls_addr and
                            lrl.lrl_lstitle = ls.ls_title
inner join lessonimgfile lif on lrl.lrl_lsnum = lif.lif_lsnum
inner join lessonchoreo cho on lrl.lrl_chonum = cho.cho_num and
							  lrl.lrl_choname = cho.cho_name
order by lrl.lrl_num desc;










