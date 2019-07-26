--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: group_resource; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.group_resource (
    group_id character varying(256) NOT NULL,
    resource_id character varying(256) NOT NULL,
    permission character varying(3) NOT NULL
);


ALTER TABLE public.group_resource OWNER TO postgres;

--
-- Name: COLUMN group_resource.group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_resource.group_id IS '群组id';


--
-- Name: COLUMN group_resource.resource_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_resource.resource_id IS '资源id';


--
-- Name: COLUMN group_resource.permission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_resource.permission IS '操作权限';


--
-- Name: group_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.group_user (
    group_id character varying(256) NOT NULL,
    user_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL
);


ALTER TABLE public.group_user OWNER TO postgres;

--
-- Name: COLUMN group_user.group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_user.group_id IS '群组id';


--
-- Name: COLUMN group_user.user_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_user.user_id IS '用户id';


--
-- Name: COLUMN group_user.created_at; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.group_user.created_at IS '用户加入群组时间';


--
-- Name: master_slaves; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.master_slaves (
    master_id character varying(256) NOT NULL,
    slave_id character varying(256) NOT NULL
);


ALTER TABLE public.master_slaves OWNER TO postgres;

--
-- Name: COLUMN master_slaves.master_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.master_slaves.master_id IS '父目录id';


--
-- Name: COLUMN master_slaves.slave_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.master_slaves.slave_id IS '子目录（文档）id';


--
-- Name: spa_dir; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spa_dir (
    id character varying(256) NOT NULL,
    title character varying(256) NOT NULL,
    thumbnail character varying(256) NOT NULL,
    root smallint NOT NULL,
    home smallint NOT NULL,
    modifiable smallint NOT NULL,
    personal smallint NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL
);


ALTER TABLE public.spa_dir OWNER TO postgres;

--
-- Name: COLUMN spa_dir.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.id IS '目录唯一标识';


--
-- Name: COLUMN spa_dir.title; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.title IS '目录名称';


--
-- Name: COLUMN spa_dir.thumbnail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.thumbnail IS '缩略图地址';


--
-- Name: COLUMN spa_dir.root; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.root IS '根目录标志位';


--
-- Name: COLUMN spa_dir.home; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.home IS 'home目录标志位';


--
-- Name: COLUMN spa_dir.modifiable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.modifiable IS '可删改标志位';


--
-- Name: COLUMN spa_dir.personal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.personal IS '用户个人工作区标志位';


--
-- Name: COLUMN spa_dir.creator_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.creator_id IS '目录创建者id';


--
-- Name: COLUMN spa_dir.created_at; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_dir.created_at IS '目录创建时间';


--
-- Name: spa_doc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spa_doc (
    id character varying(256) NOT NULL,
    title character varying(256) NOT NULL,
    thumbnail character varying(256) NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL
);


ALTER TABLE public.spa_doc OWNER TO postgres;

--
-- Name: COLUMN spa_doc.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_doc.id IS '文档唯一标识';


--
-- Name: COLUMN spa_doc.title; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_doc.title IS '文档名称';


--
-- Name: COLUMN spa_doc.thumbnail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_doc.thumbnail IS '缩略图地址';


--
-- Name: COLUMN spa_doc.creator_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_doc.creator_id IS '文档创建者id';


--
-- Name: COLUMN spa_doc.created_at; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_doc.created_at IS '文档创建时间';


--
-- Name: spa_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spa_group (
    group_id character varying(256) NOT NULL,
    group_name character varying(256) NOT NULL,
    group_desc text NOT NULL,
    creator_id character varying(256) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    personal smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.spa_group OWNER TO postgres;

--
-- Name: COLUMN spa_group.group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.group_id IS '群组唯一标识';


--
-- Name: COLUMN spa_group.group_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.group_name IS '群组名称';


--
-- Name: COLUMN spa_group.group_desc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.group_desc IS '群组描述';


--
-- Name: COLUMN spa_group.creator_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.creator_id IS '群主id';


--
-- Name: COLUMN spa_group.created_at; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.created_at IS '群组创建时间';


--
-- Name: COLUMN spa_group.personal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_group.personal IS '个人群组标志位';


--
-- Name: spa_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spa_user (
    user_id character varying(256) NOT NULL,
    username character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    work_no character varying(256) NOT NULL,
    isadmin smallint NOT NULL,
    personal_dir character varying(256) NOT NULL,
    personal_group character varying(256) NOT NULL
);


ALTER TABLE public.spa_user OWNER TO postgres;

--
-- Name: COLUMN spa_user.user_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.user_id IS '用户唯一标识';


--
-- Name: COLUMN spa_user.username; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.username IS '用户名称';


--
-- Name: COLUMN spa_user.password; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.password IS '账户密码';


--
-- Name: COLUMN spa_user.email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.email IS '邮箱地址';


--
-- Name: COLUMN spa_user.work_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.work_no IS '用户工号';


--
-- Name: COLUMN spa_user.isadmin; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.isadmin IS '管理员标志位';


--
-- Name: COLUMN spa_user.personal_dir; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.personal_dir IS '用户个人目录id';


--
-- Name: COLUMN spa_user.personal_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.spa_user.personal_group IS '用户个人群组id';


--
-- Data for Name: group_resource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.group_resource (group_id, resource_id, permission) FROM stdin;
a0b60b00-a25d-4657-b7ca-a302a89ff302	a721dbeb-49fe-4426-9393-496e609723ca	100
a0b60b00-a25d-4657-b7ca-a302a89ff302	d38ac304-0017-4f3f-ae01-8b16bc8763e5	100
a0b60b00-a25d-4657-b7ca-a302a89ff302	aa92a8f0-7895-40f9-a89f-f83abf4ed82d	110
\.


--
-- Data for Name: group_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.group_user (group_id, user_id, created_at) FROM stdin;
a0b60b00-a25d-4657-b7ca-a302a89ff302	b797b3cb-a60b-4e9c-973b-94798dd9dd2a	2019-07-19 21:07:21.356
\.


--
-- Data for Name: master_slaves; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.master_slaves (master_id, slave_id) FROM stdin;
a721dbeb-49fe-4426-9393-496e609723ca	d38ac304-0017-4f3f-ae01-8b16bc8763e5
d38ac304-0017-4f3f-ae01-8b16bc8763e5	aa92a8f0-7895-40f9-a89f-f83abf4ed82d
\.


--
-- Data for Name: spa_dir; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spa_dir (id, title, thumbnail, root, home, modifiable, personal, creator_id, created_at) FROM stdin;
a721dbeb-49fe-4426-9393-496e609723ca	root	./assets/images/docCnt.png	1	0	0	0	b797b3cb-a60b-4e9c-973b-94798dd9dd2a	2019-07-19 21:02:23.342
d38ac304-0017-4f3f-ae01-8b16bc8763e5	home	./assets/images/docCnt.png	0	1	0	0	b797b3cb-a60b-4e9c-973b-94798dd9dd2a	2019-07-19 21:02:23.342
aa92a8f0-7895-40f9-a89f-f83abf4ed82d	admin	./assets/images/docCnt.png	0	0	0	1	b797b3cb-a60b-4e9c-973b-94798dd9dd2a	2019-07-19 21:02:23.342
\.


--
-- Data for Name: spa_doc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spa_doc (id, title, thumbnail, creator_id, created_at) FROM stdin;
\.


--
-- Data for Name: spa_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spa_group (group_id, group_name, group_desc, creator_id, created_at, personal) FROM stdin;
a0b60b00-a25d-4657-b7ca-a302a89ff302	admin	个人群组用以授权	b797b3cb-a60b-4e9c-973b-94798dd9dd2a	2019-07-19 21:00:35.234	1
\.


--
-- Data for Name: spa_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spa_user (user_id, username, password, email, work_no, isadmin, personal_dir, personal_group) FROM stdin;
b797b3cb-a60b-4e9c-973b-94798dd9dd2a	admin	sparrow	happ@gmail.com	0001	1	aa92a8f0-7895-40f9-a89f-f83abf4ed82d	a0b60b00-a25d-4657-b7ca-a302a89ff302
\.


--
-- Name: spa_dir spa_dir_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spa_dir
    ADD CONSTRAINT spa_dir_pkey PRIMARY KEY (id);


--
-- Name: spa_doc spa_doc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spa_doc
    ADD CONSTRAINT spa_doc_pkey PRIMARY KEY (id);


--
-- Name: spa_group spa_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spa_group
    ADD CONSTRAINT spa_group_pkey PRIMARY KEY (group_id);


--
-- Name: spa_user spa_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spa_user
    ADD CONSTRAINT spa_user_pkey PRIMARY KEY (user_id);


--
-- PostgreSQL database dump complete
--

