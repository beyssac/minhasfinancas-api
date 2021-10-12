--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2021-05-16 18:54:37

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

--
-- TOC entry 6 (class 2615 OID 17351)
-- Name: financas; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA financas;


ALTER SCHEMA financas OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 206 (class 1259 OID 17363)
-- Name: lancamento; Type: TABLE; Schema: financas; Owner: postgres
--

CREATE TABLE financas.lancamento (
    id bigint NOT NULL,
    descricao character varying(100) NOT NULL,
    mes integer NOT NULL,
    ano integer NOT NULL,
    valor numeric(16,2),
    tipo character varying(20),
    status character varying(20),
    id_usuario bigint,
    data_cadastro date DEFAULT now()
);


ALTER TABLE financas.lancamento OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 17361)
-- Name: lancamento_id_seq; Type: SEQUENCE; Schema: financas; Owner: postgres
--

CREATE SEQUENCE financas.lancamento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE financas.lancamento_id_seq OWNER TO postgres;

--
-- TOC entry 2834 (class 0 OID 0)
-- Dependencies: 205
-- Name: lancamento_id_seq; Type: SEQUENCE OWNED BY; Schema: financas; Owner: postgres
--

ALTER SEQUENCE financas.lancamento_id_seq OWNED BY financas.lancamento.id;


--
-- TOC entry 204 (class 1259 OID 17354)
-- Name: usuario; Type: TABLE; Schema: financas; Owner: postgres
--

CREATE TABLE financas.usuario (
    id bigint NOT NULL,
    nome character varying(150),
    email character varying(100),
    senha character varying(20),
    data_cadastro date DEFAULT now()
);


ALTER TABLE financas.usuario OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 17352)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: financas; Owner: postgres
--

CREATE SEQUENCE financas.usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE financas.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 2835 (class 0 OID 0)
-- Dependencies: 203
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: financas; Owner: postgres
--

ALTER SEQUENCE financas.usuario_id_seq OWNED BY financas.usuario.id;


--
-- TOC entry 2696 (class 2604 OID 17366)
-- Name: lancamento id; Type: DEFAULT; Schema: financas; Owner: postgres
--

ALTER TABLE ONLY financas.lancamento ALTER COLUMN id SET DEFAULT nextval('financas.lancamento_id_seq'::regclass);


--
-- TOC entry 2694 (class 2604 OID 17357)
-- Name: usuario id; Type: DEFAULT; Schema: financas; Owner: postgres
--

ALTER TABLE ONLY financas.usuario ALTER COLUMN id SET DEFAULT nextval('financas.usuario_id_seq'::regclass);


--
-- TOC entry 2701 (class 2606 OID 17369)
-- Name: lancamento lancamento_pkey; Type: CONSTRAINT; Schema: financas; Owner: postgres
--

ALTER TABLE ONLY financas.lancamento
    ADD CONSTRAINT lancamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2699 (class 2606 OID 17360)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: financas; Owner: postgres
--

ALTER TABLE ONLY financas.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2702 (class 2606 OID 17370)
-- Name: lancamento lancamento_id_usuario_fkey; Type: FK CONSTRAINT; Schema: financas; Owner: postgres
--

ALTER TABLE ONLY financas.lancamento
    ADD CONSTRAINT lancamento_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES financas.usuario(id);


-- Completed on 2021-05-16 18:54:38

--
-- PostgreSQL database dump complete
--

